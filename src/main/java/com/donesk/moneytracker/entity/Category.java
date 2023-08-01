package com.donesk.moneytracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NonNull
    @Column(name = "description")
    private String description;
    @NonNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions = new ArrayList<>();

    public Category() {
    }

    public Category(String description, CategoryType type, List<Transaction> transactions) {
        this.description = description;
        this.type = type;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
