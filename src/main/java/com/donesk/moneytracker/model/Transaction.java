package com.donesk.moneytracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    @JsonIgnore
    private Budget budget;

    // Override toString to avoid recursive calls
    @Override
    public String toString() {
        return "Transaction{id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

}