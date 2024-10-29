package com.odaimzabi.dream_shops.services.product;

import com.odaimzabi.dream_shops.models.Product;
import com.odaimzabi.dream_shops.requests.AddProductRequest;
import com.odaimzabi.dream_shops.requests.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductRequest product, Long id);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByBrandAndName(String brand,String name);

    Long countProductsByBrandAndName(String brand,String name);
}
