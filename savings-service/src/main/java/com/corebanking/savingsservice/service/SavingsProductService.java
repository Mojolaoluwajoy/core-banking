
package com.corebanking.savingsservice.service;

import com.corebanking.savingsservice.dto.CreateSavingsProductRequest;
import com.corebanking.savingsservice.dto.SavingsProductResponse;
import com.corebanking.savingsservice.entity.SavingsProduct;
import com.corebanking.savingsservice.exception.DuplicateProductException;
import com.corebanking.savingsservice.exception.ProductNotFoundException;
import com.corebanking.savingsservice.repository.SavingsProductRepository;
import com.corebanking.savingsservice.util.SavingsProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsProductService {

    private final SavingsProductRepository savingsProductRepository;


    public SavingsProductResponse createProduct(CreateSavingsProductRequest request) {
        log.info("Creating savings product with short name: {}", request.getShortName());

        if (savingsProductRepository.existsByShortName(request.getShortName())) {
            throw new DuplicateProductException("Savings product with short name "
                    + request.getShortName() + " already exists");
        }

        SavingsProduct savedProduct = savingsProductRepository
                .save(SavingsProductMapper.toEntity(request));

        log.info("Savings product created successfully with ID: {}", savedProduct.getId());
        return SavingsProductMapper.toResponse(savedProduct);
    }


    public SavingsProductResponse getProductById(Long id) {
        log.info("Fetching savings product with ID: {}", id);

        SavingsProduct product = savingsProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Savings product not found with ID: " + id));

        return SavingsProductMapper.toResponse(product);
    }


    public List<SavingsProductResponse> getAllProducts() {
        log.info("Fetching all savings products");

        return savingsProductRepository.findAll()
                .stream()
                .map(SavingsProductMapper::toResponse)
                .collect(Collectors.toList());
    }
}