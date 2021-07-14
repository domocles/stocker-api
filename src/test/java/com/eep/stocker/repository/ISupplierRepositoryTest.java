package com.eep.stocker.repository;

import com.eep.stocker.domain.Supplier;
import com.eep.stocker.testdata.SupplierTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ISupplierRepositoryTest extends SupplierTestData {
    @Autowired
    private ISupplierRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Supplier persistedSupplier;
    private Supplier persistedShelleys;
    private Supplier persistedUkf;

    @BeforeEach
    public void setup() {
        persistedSupplier = entityManager.persistFlushFind(supplier);
        persistedShelleys = entityManager.persistFlushFind(shelleys);
        persistedUkf = entityManager.persistFlushFind(ukf);
    }

    @Test
    public void findByUidTest() {
        Optional<Supplier> testSupplier = repository.findByUid(supplier.getUid());
        assertThat(testSupplier).isPresent();
    }
}
