package org.rflores.controller;

import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.rflores.service.ProductLineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ProductLineController {

    ProductLineService productLineService;

    @GetMapping("/product-lines/{productLineId}")
    public Map<Descriptors.FieldDescriptor, Object> getProductLines(@PathVariable String productLineId) {
        return productLineService.getProductLine(productLineId);
    }

    @GetMapping("/products")
    public List<Map<Descriptors.FieldDescriptor, Object>> getProducts() throws InterruptedException {
        return productLineService.getProducts();
    }

    @GetMapping("/products/expensive")
    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveProduct() throws InterruptedException {
        return productLineService.getExpensiveProduct();
    }

    @GetMapping("/products/years/{years}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getProductsByYear(@PathVariable Integer[] years) throws InterruptedException {
        return productLineService.getProductsByYear(years);
    }
}
