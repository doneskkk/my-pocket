package com.donesk.moneytracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Budgets")
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "current_progress")
    private Double currentProgress = 0.0;

    @NonNull
    @Column(name = "goal", nullable = false)
    private Double goal;

    @NonNull
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN) // Eager fetch
    private List<Transaction> transactionList = new ArrayList<>();

    @NonNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull( message = "The user field cannot be empty")
    @JsonIgnore
    private User user;

    public void updateCurrentProgress(double amount, Category category) {
        if(String.valueOf(category.getType()).equals("INCOME")) {
            this.currentProgress += amount;
        } else {
            this.currentProgress -=amount;
        }
    }
}
