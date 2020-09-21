package com.eep.stocker.services;

import com.eep.stocker.controllers.error.exceptions.MpnNotUniqueException;
import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IAssemblyLineRepository;
import com.eep.stocker.repository.IAssemblyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class AssemblyServiceTest {
    @Mock
    private IAssemblyRepository assemblyRepository;

    @Mock
    private IAssemblyLineRepository assemblyLineRepository;

    private AssemblyService assemblyService;
    private StockableProduct MF220;
    private StockableProduct MF286;
    private Assembly assembly1;
    private Assembly assembly1unsaved;
    private Assembly assembly2;

    private AssemblyLine assemblyLine1;
    private AssemblyLine assemblyLine2;
    private AssemblyLine assemblyLine3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.assemblyService = new AssemblyService(assemblyRepository, assemblyLineRepository);

        MF220 = new StockableProduct(1L, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.72D,
                25.0D);

        MF286 = new StockableProduct(2L, "MF286",
                "EEP200919002",
                "Mild Steel Flange",
                "Flange",
                new HashSet<String>(),
                "Flanges",
                1.45D,
                75.D);

        assembly1unsaved = new Assembly(null, "Golf Decat", "EEP101");
        assembly1 = new Assembly(Long.valueOf(1), "Golf Decat", "EEP102");
        assembly2 = new Assembly(Long.valueOf(2), "ST170 Mk2 Decat", "EEP103");

        assemblyLine1 = new AssemblyLine(Long.valueOf(1), MF220, assembly1, 3);
        assemblyLine2 = new AssemblyLine(Long.valueOf(2), MF220, assembly1, 3);
        assemblyLine3 = new AssemblyLine(Long.valueOf(3), MF220, assembly2, 3);
    }

    @Test
    void getAssemblyById() {
        given(assemblyRepository.findById(any(Long.class))).willReturn(Optional.of(assembly1));

        Optional<Assembly> assembly = assemblyService.getAssemblyById(1);

        assertThat(assembly.isPresent());
        assertThat(assembly.get()).isEqualTo(assembly1);
    }

    @Test
    void saveAssemblyTest() {
        given(assemblyRepository.findAssemblyByMpn(any(String.class))).willReturn(Optional.empty());
        given(assemblyRepository.save(any(Assembly.class))).willReturn(assembly1);

        Optional<Assembly> assembly = assemblyService.saveAssembly(assembly1unsaved);

        assertThat(assembly).isPresent();
        assertThat(assembly.get()).isEqualTo(assembly1);
    }

    @Test
    void saveAssemblyWithNonUniqueMpnTest() {
        given(assemblyRepository.findAssemblyByMpn(any(String.class))).willReturn(Optional.of(assembly1));

        assertThrows(MpnNotUniqueException.class, () -> assemblyService.saveAssembly(assembly1unsaved));
    }

    @Test
    void updateAssemblyTest() {
        given(assemblyRepository.findAssemblyByMpn(any(String.class))).willReturn(Optional.of(assembly1));
        given(assemblyRepository.save(any(Assembly.class))).willReturn(assembly2);

        Optional<Assembly> assembly = assemblyService.updateAssembly(assembly1);

        assertThat(assembly).isPresent();
        assertThat(assembly.get()).isEqualTo(assembly2);
    }

    @Test
    void deleteAssemblyTest() {
        Optional<Assembly> assembly = assemblyService.deleteAssembly(assembly1);

        assertThat(assembly).isPresent();
        assertThat(assembly.get()).isEqualTo(assembly1);
    }

    @Test
    void deleteAssemblyByIdTest() {
        given(assemblyRepository.findById(anyLong())).willReturn(Optional.of(assembly1));

        Optional<Assembly> assembly = assemblyService.deleteAssemblyById(1);

        assertThat(assembly).isPresent();
        assertThat(assembly.get()).isEqualTo(assembly1);
    }

    @Test
    void deleteAssemblyByIdDoesNotExistTest() {
        given(assemblyRepository.findById(anyLong())).willReturn(Optional.empty());

        Optional<Assembly> assembly = assemblyService.deleteAssemblyById(1);

        assertThat(assembly).isNotPresent();
    }

    @Test
    void getAllAssembliesTest() {
        given(assemblyRepository.findAll()).willReturn(Arrays.asList(assembly1, assembly2));

        List<Assembly> allAssemblies = assemblyService.getAllAssemblies();

        assertThat(allAssemblies.isEmpty()).isFalse();

    }

    @Test
    void getAllAssembliesByComponentTest() {
        given(assemblyLineRepository.getAssemblyLineByStockableProduct(any(StockableProduct.class)))
                .willReturn(Arrays.asList(assemblyLine1, assemblyLine2, assemblyLine3));

        Set<Assembly> assemblies = assemblyService.getAllAssembliesByComponent(MF220);

        assertThat(assemblies).contains(assembly1, assembly2);
        assertThat(assemblies.size()).isEqualTo(2);
    }

    @Test
    void getAssemblyLinesForAssemblyTest() {
        given(assemblyLineRepository.getAssemblyLineByAssembly(any(Assembly.class)))
                .willReturn(Arrays.asList(assemblyLine1, assemblyLine2));

        List<AssemblyLine> assemblyLines = assemblyService.getAssemblyLinesForAssembly(assembly1);

        assertThat(assemblyLines.size()).isEqualTo(2);
        assertThat(assemblyLines).contains(assemblyLine1, assemblyLine2);
    }

    @Test
    void getAssemblyByMpnTest() {
        given(assemblyRepository.findAssemblyByMpn(any(String.class))).willReturn(Optional.of(assembly1));

        Optional<Assembly> assembly = assemblyService.getAssemblyByMpn("EEP101");

        assertThat(assembly).isPresent().contains(assembly1);
    }
}