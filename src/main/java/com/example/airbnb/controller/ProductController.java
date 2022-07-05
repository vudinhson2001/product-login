package com.example.airbnb.controller;


import com.example.airbnb.model.Product;
import com.example.airbnb.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ProductController {
    @Autowired
    IProductService productService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @PostMapping("/users/products")
    public ResponseEntity<Iterable<Product>> add(@Valid @RequestBody Product product) {
        productService.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/products")
    public ResponseEntity<Iterable<Product>> findAll() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<Product>> findById(@PathVariable long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/users/products/{id}")
    public ResponseEntity<Product> edit(@PathVariable long id, @RequestBody Product product) {
        Optional<Product> product1 = productService.findById(id);
        if (!product1.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        product.setId(product1.get().getId());
        productService.save(product);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> update(@PathVariable long id){
        productService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/products/search-by-name")
    public ResponseEntity<Iterable<Product>> findByName(@RequestParam String name, @PageableDefault(value = 3) Pageable pageable){
        return new ResponseEntity<>(productService.findAllByNameContaining( name,pageable),HttpStatus.OK);
    }
}
