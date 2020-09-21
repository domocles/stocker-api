package com.eep.stocker.services;

import com.eep.stocker.domain.Assembly;
import com.eep.stocker.domain.AssemblyLine;
import com.eep.stocker.domain.StockableProduct;
import com.eep.stocker.repository.IAssemblyLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class AssemblyLineServiceTest {
    @Mock
    private static IAssemblyLineRepository assemblyLineRepository;

    private static AssemblyLineService assemblyLineService;

    private StockableProduct MF220;
    private StockableProduct MF286;

    private Assembly assembly1;
    private Assembly assembly1unsaved;
    private Assembly assembly2;

    private AssemblyLine assemblyLine1;
    private AssemblyLine assemblyLine2;
    private AssemblyLine assemblyLine3;

    public AssemblyLineServiceTest() {
        MockitoAnnotations.initMocks(this);
        assemblyLineService = new AssemblyLineService(assemblyLineRepository);
    }

    @BeforeEach
    void setUp() {
        MF220 = new StockableProduct(1L, "MF220",
                "EEP200919001",
                "Mild Steel Flange",
                "Flange",
                new HashSet<>(),
                "Flanges",
                1.72D,
                25.0D);

        MF286 = new StockableProduct(2L, "MF286",
                "EEP200919002",
                "Mild Steel Flange",
                "Flange",
                new HashSet<>(),
                "Flanges",
                1.45D,
                75.D);

        assembly1unsaved = new Assembly(null, "Golf Decat", "EEP101");
        assembly1 = new Assembly(1L, "Golf Decat", "EEP102");
        assembly2 = new Assembly(2L, "ST170 Mk2 Decat", "EEP103");

        assemblyLine1 = new AssemblyLine(1L, MF220, assembly1, 3);
        assemblyLine2 = new AssemblyLine(2L, MF220, assembly1, 3);
        assemblyLine3 = new AssemblyLine(3L, MF220, assembly2, 3);
    }

    @Test
    void getAllAssemblyLinesTest() {
        given(assemblyLineRepository.findAll()).willReturn(Arrays.asList(assemblyLine1, assemblyLine2, assemblyLine3));

        List<AssemblyLine> assemblyLines = assemblyLineService.getAllAssemblyLines();

        assertThat(assemblyLines.size()).isEqualTo(3);
        assertThat(assemblyLines).contains(assemblyLine1, assemblyLine2, assemblyLine3);
    }

    @Test
    void getAssemblyLineByIdTest() {
        given(assemblyLineRepository.findById(anyLong())).willReturn(Optional.of(assemblyLine2));

        Optional<AssemblyLine> assemblyLine = assemblyLineService.getAssemblyLineById(4);

        assertThat(assemblyLine).isPresent().contains(assemblyLine2);
    }

    @Test
    void getAssemblyLineByNonExistantIdTest() {
        given(assemblyLineRepository.findById(anyLong())).willReturn(Optional.empty());

        Optional<AssemblyLine> assemblyLine = assemblyLineService.getAssemblyLineById(4);

        assertThat(assemblyLine).isNotPresent();
    }

    @Test
    void getAssemblyLinesForAssemblyTest() {
        given(assemblyLineRepository.getAssemblyLineByAssembly(any(Assembly.class)))
                .willReturn(Arrays.asList(assemblyLine1, assemblyLine2));

        List<AssemblyLine> assemblyLines = assemblyLineService.getAllAssemblyLinesForAssembly(assembly1);

        assertThat(assemblyLines.size()).isEqualTo(2);
        assertThat(assemblyLines).contains(assemblyLine1, assemblyLine2);
    }

    @Test
    void deleteAssemblyLineTest() {
        Optional<AssemblyLine> assemblyLine = assemblyLineService.deleteAssemblyLine(assemblyLine3);

        assertThat(assemblyLine).isPresent().contains(assemblyLine3);
    }

    @Test
    void deleteAssemblyLineWithNullTest() {
        Optional<AssemblyLine> assemblyLine = assemblyLineService.deleteAssemblyLine(null);

        assertThat(assemblyLine).isNotPresent();
    }

    @Test
    void deleteAssemblyLineByIdTest() {
        given(assemblyLineRepository.findById(anyLong())).willReturn(Optional.of(assemblyLine1));

        Optional<AssemblyLine> assemblyLine = assemblyLineService.deleteAssemblyLineById(4);

        assertThat(assemblyLine).isPresent().contains(assemblyLine1);
    }

    @Test
    void deleteAssemblyLineByNonExistantIdTest() {
        given(assemblyLineRepository.findById(anyLong())).willReturn(Optional.empty());

        Optional<AssemblyLine> assemblyLine = assemblyLineService.deleteAssemblyLineById(4);

        assertThat(assemblyLine).isNotPresent();
    }

    @Test
    void saveAssemblyLineTest() {
        given(assemblyLineRepository.save(any(AssemblyLine.class))).willReturn(assemblyLine1);

        Optional<AssemblyLine> assemblyLine = assemblyLineService.saveAssemblyLine(assemblyLine1);

        assertThat(assemblyLine).contains(assemblyLine1);
    }
}