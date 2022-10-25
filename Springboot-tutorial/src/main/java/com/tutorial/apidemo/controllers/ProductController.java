package com.tutorial.apidemo.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.JpaRepositoryNameSpaceHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.models.ResponObject;
import com.tutorial.apidemo.repositories.ProductRepository;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProducts() {
	return repository.findAll();
    }

    // Get detail product
    @GetMapping("/{id}")
    // Đối tượng trả vể phải có các trường như: data,message,status
    ResponseEntity<ResponObject> findById(@PathVariable Long id) {
	Optional<Product> foundProduct = repository.findById(id);
	return foundProduct.isPresent()
		? ResponseEntity.status(HttpStatus.OK)
			.body(new ResponObject("ok", "Query product successfully", foundProduct))
		: ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ResponObject("False", "Cannot find product with id = " + id, ""));

//	if (foundProduct.isPresent()) {
//	    return ResponseEntity.status(HttpStatus.OK)
//		    .body(new ResponObject("ok", "Query product successfully", foundProduct));
//	} else {
//	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
//		    .body(new ResponObject("False", "Cannot find product with id = " + id, ""));
//	}
    }

    // insert new Product with POST method
    // Postman, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponObject> insertProduct(@RequestBody Product newProduct) {
	List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
	if (foundProducts.size() > 0) {
	    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
		    .body(new ResponObject("fail", "Product name already taken", ""));

	}
	return ResponseEntity.status(HttpStatus.OK)
		.body(new ResponObject("ok", "Insert Product successfully", repository.save(newProduct)));
    }

    // update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
	Product updateProduct = repository.findById(id).map(product -> {
	    product.setProductName(newProduct.getProductName());
	    product.setYear(newProduct.getYear());
	    product.setPrice(newProduct.getPrice());
	    return repository.save(product);
	}).orElseGet(() -> {
	    newProduct.setId(id);
	    return repository.save(newProduct);
	});
	return ResponseEntity.status(HttpStatus.OK)
		.body(new ResponObject("ok", "Update Product successfully", updateProduct));

    }

    // Delete a Product => Delete method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponObject> deleteProduct(@PathVariable Long id) {
	boolean exists = repository.existsById(id);
	if (exists) {
	    repository.deleteById(id);
	    return ResponseEntity.status(HttpStatus.OK)
		    .body(new ResponObject("ok", "Delete product successfully", "")
		);
	}
	return ResponseEntity.status(HttpStatus.NOT_FOUND)
		.body(new ResponObject("failed", "Cannot find product to delete", "")
	);
    }
}
