package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.AssemblyDoesNotExistException;
import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
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

    /***
     * Gets an assembly by its database id.  Deprecated, use getAssemblyByUid
     * @param id = the unique database id of the assembly
     * @return an {@code Optional} containing the assembly if it exists, else Optional.empty
     */
    @Deprecated
    public Optional<Assembly> getAssemblyById(long id) {
        log.info("retrieving assembly with id of: {}", id);
        return assemblyRepository.findById(id);
    }

    /***
     * Gets an assembly by its unique identifier.
     * @param uid - the unique identifier of the assembly
     * @return - an {@code Optional} containing the assembly if it exists, else Optional.empty
     */
    public Optional<Assembly> getAssemblyByUid(String uid) {
        log.info("retrieving assembly by uid");
        var uuid = UUID.fromString(uid);
        return assemblyRepository.findByUid(uuid);
    }

    public Optional<Assembly> saveAssembly(Assembly assembly) {
        if(assembly == null) return Optional.empty();
        Optional<Assembly> assemblyExists = assemblyRepository.findAssemblyByMpn(assembly.getMpn());
        if(assemblyExists.isPresent()) throw new MpnNotUniqueException(String.format("Assembly with mpn of %s already exists", assembly.getMpn()));
        log.info("saving assembly with name of: {}", assembly.getName());
        return Optional.of(assemblyRepository.save(assembly));
    }

    public Optional<Assembly> updateAssembly(Assembly assembly) {
        if(assembly == null) return Optional.empty();
        log.info("saving assembly with name of: {}", assembly.getName());
        return Optional.of(assemblyRepository.save(assembly));
    }

    public Optional<Assembly> addSubAssemblyToAssemblyById(long assemblyId, long subAssemblyId) {
        log.info("adding subassembly to {}", assemblyId);
        Optional<Assembly> assembly = getAssemblyById(assemblyId);
        Optional<Assembly> subassembly = getAssemblyById(subAssemblyId);

        if(!assembly.isPresent() || !subassembly.isPresent())
            return Optional.empty();

        Assembly assy = assembly.get();
        Assembly subassy = subassembly.get();
        assy.getSubAssemblies().add(subassy);

        return Optional.of(assemblyRepository.save(assy));

    }

    public Optional<Assembly> deleteAssembly(Assembly assembly) {
        if(assembly == null) return Optional.empty();
        log.info("delete assembly with name of: {}", assembly.getName());
        assemblyRepository.delete(assembly);
        return Optional.of(assembly);
    }

    @Deprecated
    public Optional<Assembly> deleteAssemblyById(long id) {
        log.info("delete assembly with id of: {}", id);
        Optional<Assembly> assy = assemblyRepository.findById(id);
        assy.ifPresent(a ->assemblyRepository.deleteById(id));
        return assy;
    }

    public Optional<Assembly> deleteAssemblyByUid(String uid) {
        log.info("delete assembly with id of: {}", uid);
        var assy = assemblyRepository.findByUid(UUID.fromString(uid)).orElseThrow(() -> new AssemblyDoesNotExistException("Assembly Does Not Exist"));
        assemblyRepository.deleteById(assy.getId());
        return Optional.of(assy);
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

    public Optional<Assembly> getAssemblyByMpn(String mpn) {
        log.info("get assembly by mpn: {} called", mpn);
        return assemblyRepository.findAssemblyByMpn(mpn);
    }

    public List<Assembly> getAssembliesByCategory(String category) {
        log.info("get assemblies by category: {} called", category);
        return assemblyRepository.findAssemblyByCategory(category);
    }

    public List<String> getAllCategories() {
        log.info("get categories for assemblies");
        return assemblyRepository.findDistinctCategories();
    }
}
