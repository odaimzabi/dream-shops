package com.odaimzabi.dream_shops.services.product;

import com.odaimzabi.dream_shops.dtos.ImageDTO;
import com.odaimzabi.dream_shops.dtos.ProductDTO;
import com.odaimzabi.dream_shops.models.Category;
import com.odaimzabi.dream_shops.models.Image;
import com.odaimzabi.dream_shops.models.Product;
import com.odaimzabi.dream_shops.repositories.CategoryRepository;
import com.odaimzabi.dream_shops.repositories.ImageRepository;
import com.odaimzabi.dream_shops.repositories.ProductRepository;
import com.odaimzabi.dream_shops.exceptions.ProductNotFoundException;
import com.odaimzabi.dream_shops.requests.AddProductRequest;
import com.odaimzabi.dream_shops.requests.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public Product addProduct(AddProductRequest request) {
    Category category =
        Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
            .orElseGet(
                () -> {
                  Category newCategory = new Category(request.getCategory().getName());
                  return categoryRepository.save(newCategory);
                });
    return productRepository.save(new Product(request, category));
  }

  @Override
  public Product updateProduct(UpdateProductRequest request, Long id) {
    return productRepository
        .findById(id)
        .map(product -> this.updateExistingProduct(product, request))
        .map(productRepository::save)
        .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
  }

  private Product updateExistingProduct(Product product, UpdateProductRequest request) {
    product.setBrand(request.getBrand());
    product.setCategory(request.getCategory());
    product.setName(request.getName());
    product.setInventory(request.getInventory());
    product.setPrice(request.getPrice());
    product.setDescription(request.getDescription());

    Category category = categoryRepository.findByName(request.getName());
    product.setCategory(category);

    return product;
  }

  private Product createProduct(AddProductRequest request, Category category) {
    return new Product(request, category);
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product Not found"));
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository
        .findById(id)
        .ifPresentOrElse(
            productRepository::delete, () -> new ProductNotFoundException("Product Not Found"));
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryName(category);
  }

  @Override
  public List<Product> getProductsByBrand(String brand) {
    return productRepository.findByBrand(brand);
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand,name);
  }

  public List<ProductDTO> getConvertedProducts(List<Product> products){
    return  products.stream().map(this::convertToDto).toList();
  }

  public ProductDTO convertToDto(Product product){
    ProductDTO productDto = modelMapper.map(product, ProductDTO.class);
    List<ImageDTO> imageDtos = product.getImages().stream()
            .map(image -> modelMapper.map(image, ImageDTO.class))
            .toList();
    productDto.setImages(imageDtos);
    return productDto;
  }
}
