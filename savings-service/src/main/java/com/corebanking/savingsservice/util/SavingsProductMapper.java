
package com.corebanking.savingsservice.util;

import com.corebanking.savingsservice.dto.CreateSavingsProductRequest;
import com.corebanking.savingsservice.dto.SavingsProductResponse;
import com.corebanking.savingsservice.entity.SavingsProduct;

public class SavingsProductMapper {


    public static SavingsProduct toEntity(CreateSavingsProductRequest request) {
        SavingsProduct product = new SavingsProduct();
        product.setName(request.getName());
        product.setShortName(request.getShortName());
        product.setDescription(request.getDescription());
        product.setNominalAnnualInterestRate(request.getNominalAnnualInterestRate());
        product.setMinimumOpeningBalance(request.getMinimumOpeningBalance());
        product.setMinimumBalance(request.getMinimumBalance());
        product.setEnforceMinimumBalance(request.isEnforceMinimumBalance());
        return product;
    }


    public static SavingsProductResponse toResponse(SavingsProduct product) {
        return SavingsProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .shortName(product.getShortName())
                .description(product.getDescription())
                .nominalAnnualInterestRate(product.getNominalAnnualInterestRate())
                .minimumOpeningBalance(product.getMinimumOpeningBalance())
                .minimumBalance(product.getMinimumBalance())
                .enforceMinimumBalance(product.isEnforceMinimumBalance())
                .createdAt(product.getCreatedAt())
                .build();
    }

      private SavingsProductMapper() {}
}