package com.eep.stocker.services;

import com.eep.stocker.domain.ReferenceGenerator;
import com.eep.stocker.repository.IReferenceGeneratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReferenceGeneratorService {
    private static final Logger log = LoggerFactory.getLogger(ReferenceGeneratorService.class);
    private IReferenceGeneratorRepository referenceGeneratorRepository;

    public ReferenceGeneratorService(IReferenceGeneratorRepository referenceGeneratorRepository) {
        this.referenceGeneratorRepository = referenceGeneratorRepository;
    }

    /***
     *
     * @param name e.g. "PurchaseOrder"
     * @param prefix e.g. prefix of the generated reference number e.g. "PO"
     * @param startingNumber the starting number of the number part of the reference e.g. 1L
     * @return true if created, false if already exists
     */
    public boolean createReferenceGenerator(String name, String prefix, Long startingNumber) {
        Optional<ReferenceGenerator> referenceGeneratorOptional = referenceGeneratorRepository.findFirstByReferenceName(name);
        if(referenceGeneratorOptional.isPresent())
            return false;

        ReferenceGenerator referenceGenerator = new ReferenceGenerator();
        referenceGenerator.setReferenceName(name);
        referenceGenerator.setPrefix(prefix);
        referenceGenerator.setNumber(startingNumber);
        referenceGeneratorRepository.save(referenceGenerator);
        return true;
    }

    /***
     *
     * @param name e.g. "PURCHASE"
     * @return reference String e.g. "PO0000017"
     */
    public Optional<String> getNextReference(String name) {
        log.info("get next reference for generator " + name + " called");
        Optional<ReferenceGenerator> referenceGeneratorOptional = referenceGeneratorRepository.findFirstByReferenceName(name);
        if(!referenceGeneratorOptional.isPresent()) {
            log.info("reference generator " + name + " does not exist");
            return Optional.empty();
        }

        ReferenceGenerator referenceGenerator = referenceGeneratorOptional.get();
        String nextReference = referenceGenerator.generate();
        log.info("reference " + nextReference + " generated");
        referenceGeneratorRepository.save(referenceGenerator);
        return Optional.of(nextReference);
    }
}
