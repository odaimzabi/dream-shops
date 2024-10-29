package com.odaimzabi.dream_shops.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.odaimzabi.dream_shops.requests.AddProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;
    @OneToMany(mappedBy = "product",cascade = CascadeType.MERGE,orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Image> images;

    public Product(AddProductRequest request,Category category){
        this.name=request.getName();
        this.brand=request.getBrand();
        this.price=request.getPrice();
        this.inventory=request.getInventory();

        this.category= category;
    }

}
