package com.ksoft.validator.api.services;

import com.ksoft.validation.core.algorithm.DocumentValidator;
import com.ksoft.validation.core.model.Country;
import com.ksoft.validation.core.model.DocumentType;
import com.ksoft.validation.core.service.impl.IdValidationServiceImpl;
import com.ksoft.validator.api.dtos.ValidationResponse;
import com.ksoft.validator.api.validators.ValidatorFactory;

import org.springframework.stereotype.Service;

@Service
public class DocumentValidationService {

    private final IdValidationServiceImpl idValidationService;

    public DocumentValidationService(IdValidationServiceImpl idValidationService) {
        this.idValidationService = idValidationService;
    }

    public ValidationResponse validateDocument(String countryCode, String documentType, String documentNumber) {
        // Delegar la validación principal al servicio existente
        boolean isValid = idValidationService.validate(Country.valueOf(countryCode), DocumentType.valueOf(documentType), documentNumber);

        DocumentValidator validator = ValidatorFactory.getValidator(countryCode, documentType);
        
        ValidationResponse response = new ValidationResponse();
        response.setValid(isValid);
        
        if (isValid) {
            response.setFormattedDocument(validator.format(documentNumber));
            response.setDocumentType(validator.getDocumentType());
            response.setAdditionalInfo(isValid?"Documento válido":"Documento inválido");
        }
        
        return response;
    }
}