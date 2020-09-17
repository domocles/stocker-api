package com.eep.stocker.services;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IAssemblyLineRepository;
import com.eep.stocker.repository.IAssemblyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssemblyService {
    private static final Logger log = LoggerFactory.getLogger(AssemblyService.class);
    private final IAssemblyRepository assemblyRepository;
    private final IAssemblyLineRepository assemblyLineRepository;

    public AssemblyService(IAssemblyRepository assemblyRepository, IAssemblyLineRepository assemblyLineRepository) {
        this.assemblyRepository = assemblyRepository;
        this.assemblyLineRepository = assemblyLineRepository;
    }

    public Optional<Assembly> getAssemblyById(long id) {
        log.info("retrieving assembly with id of: {}", id);
        return assemblyRepository.findById(id);
    }

    public Optional<Assembly> saveAssembly(Assembly assembly) {
        if(assembly == null) return Optional.empty();
        log.info("saving assembly with name of: {}", assembly.getName());
        return Optional.of(assemblyRepository.save(assembly));
    }

    public Optional<Assembly> deleteAssembly(Assembly assembly) {
        if(assembly == null) return Optional.empty();
        log.info("delete assembly with name of: {}", assembly.getName());
        assemblyRepository.delete(assembly);
        return Optional.of(assembly);
    }

    public Optional<Assembly> deleteAssemblyById(long id) {
        log.info("delete assembly with id of: {}", id);
        Optional<Assembly> assy = assemblyRepository.findById(id);
        assy.ifPresent(a ->assemblyRepository.deleteById(id));
        return assy;
    }

    public List<Assembly> getAllAssemblies() {
        log.info("get all assemblies");
        return assemblyRepository.findAll();
    }

    public Set<Assembly> getAllAssembliesByComponent(StockableProduct component) {
        if(component==null) return Collections.emptySet();

        log.info("get all assemblies by stockable product: {}", component.getName());
        List<AssemblyLine> assemblyLines = assemblyLineRepository.getAssemblyLineByStockableProduct(component);
        Set<Assembly> assemblies = assemblyLines.stream().map(AssemblyLine::getAssembly).collect(Collectors.toSet());
        log.info("{} assemblies found containing {}", assemblies.size(), component.getName());
        return assemblies;
    }

    public List<AssemblyLine> getAssemblyLinesForAssembly(Assembly assembly) {
        if(assembly==null) return Collections.emptyList();

        log.info("get all assembly lines for assembly: {}", assembly.getName());
        return assemblyLineRepository.getAssemblyLineByAssembly(assembly);
    }
}
