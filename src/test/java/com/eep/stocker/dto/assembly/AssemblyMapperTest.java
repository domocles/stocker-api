package com.eep.stocker.dto.assembly;

import com.eep.stocker.domain.Assembly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssemblyMapperTest {
    @Autowired
    AssemblyMapper mapper;

    private Assembly vo132;
    private Assembly vo132APipe;
    private Assembly vo132BPipe;
    private Assembly vo132CPipe;

    @BeforeEach
    private void setup() {
        vo132APipe = Assembly.builder()
                .name("VO132 A Pipe")
                .mpn("EEPVO132A")
                .description("A pipe for the VO132 assembly, 54 x 1.5 x 455mm Aluminised")
                .category("Cut Tube")
                .tag("VO132")
                .build();

        vo132BPipe = Assembly.builder()
                .name("VO132 B Pipe")
                .mpn("EEPVO132B")
                .description("B pipe for the VO132 assembly, 44.45 x 1.5 x 580mm Aluminised")
                .category("Cut Tube")
                .tag("VO132")
                .build();

        vo132CPipe = Assembly.builder()
                .name("VO132 C Pipe")
                .mpn("EEPVO132C")
                .description("C pipe for the VO132 assembly, 44.45 x 1.5 x 590mm Aluminised")
                .category("Cut Tube")
                .tag("VO132")
                .build();

        vo132 = Assembly.builder()
                .name("VO132")
                .mpn("EEPVO132")
                .description("VO132 assembly")
                .category("Finished Product")
                .tag("VO132")
                .subAssembly(vo132APipe).subAssembly(vo132BPipe).subAssembly(vo132CPipe)
                .build();
    }

    @Test
    void canMapToGetResponseTest() {
        var response = mapper.mapToGetResponse(vo132);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("VO132"),
                () -> assertThat(response.getSubAssemblies().size()).isEqualTo(3)
        );
    }

    @Test
    void canMapToLowDetailResponseTest() {
        var response = mapper.mapToGetLowDetailResponse(vo132);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("VO132"),
                () -> assertThat(response.getSubAssemblyIds()).contains(
                        vo132APipe.getUid().toString(),
                        vo132BPipe.getUid().toString(),
                        vo132CPipe.getUid().toString()
                )
        );
    }
}
