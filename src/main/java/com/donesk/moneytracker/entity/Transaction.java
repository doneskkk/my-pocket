package com.donesk.moneytracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;


@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transaction")
public class Transaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Min(value = 1,  message = "Amount might be greater than 0")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    @JsonIgnore
    private Budget budget;

}