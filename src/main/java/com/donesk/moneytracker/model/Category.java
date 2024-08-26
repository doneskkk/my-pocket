package com.donesk.moneytracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "category")
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @NonNull
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return "Category{id=" + id +
                ", name='" + type + '\'' +
                '}';
    }

}
