package com.eep.stocker.services;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.repository.IAssemblyLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssemblyLineService {
    private static final Logger log = LoggerFactory.getLogger(AssemblyLineService.class);

    private final IAssemblyLineRepository assemblyLineRepository;

    public AssemblyLineService(IAssemblyLineRepository assemblyLineRepository) {
        this.assemblyLineRepository = assemblyLineRepository;
    }

    public List<AssemblyLine> getAllAssemblyLines() {
        log.info("get all assembly lines");
        return assemblyLineRepository.findAll();
    }

    @Deprecated
    public Optional<AssemblyLine> getAssemblyLineById(long id) {
        log.info("get assembly line with id: {}", id);
        return assemblyLineRepository.findById(id);
    }

    public Optional<AssemblyLine> getAssemblyLineByUid(String uid) {
        log.info("get assembly line with uid: {}", uid);
        var uuid = UUID.fromString(uid);
        return assemblyLineRepository.findByUid(uuid);
    }

    public List<AssemblyLine> getAllAssemblyLinesForAssembly(Assembly assembly) {
        List<AssemblyLine> assemblyLines = new ArrayList<>();
        if(assembly != null) {
            log.info("get assembly lines for assembly: {}", assembly.getName());
            assemblyLines = assemblyLineRepository.getAssemblyLineByAssembly(assembly);
        } else
            log.error("get assembly lines for assembly called with null assembly");

        return assemblyLines;
    }

    public Optional<AssemblyLine> saveAssemblyLine(AssemblyLine assemblyLine) {
        if(assemblyLine == null) return Optional.empty();
        log.info("save assembly line called: {}", assemblyLine.toString());
        return Optional.of(assemblyLineRepository.save(assemblyLine));
    }

    public Optional<AssemblyLine> deleteAssemblyLine(AssemblyLine assemblyLine) {
        if(assemblyLine != null) {
            log.info("delete assembly line: {}", assemblyLine);
            assemblyLineRepository.delete(assemblyLine);
        }
        else
            log.error("delete assembly line called with a null assembly line");

        return Optional.ofNullable(assemblyLine);
    }

    public Optional<AssemblyLine> deleteAssemblyLineById(long id) {
        log.info("delete assembly line with id of: {}", id);
        Optional<AssemblyLine> assemblyLine = assemblyLineRepository.findById(id);
        assemblyLine.ifPresent(assemblyLineRepository::delete);
        return assemblyLine;
    }
}
