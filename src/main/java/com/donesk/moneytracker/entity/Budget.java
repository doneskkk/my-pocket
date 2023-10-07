package com.donesk.moneytracker.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

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
    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title", nullable = false)
    private String title;

    @NonNull
    @Column(name = "current_progress")
    private Double currentProgress = 0.0;

    @NonNull
    @Min(value = 0, message = "Goal might be greater than 0")
    @Column(name = "goal", nullable = false)
    private Double goal;

    @NonNull
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Transaction> transactionList = new ArrayList<>();

    @NonNull
    @Column(name = "status", nullable = false)
    private boolean status = false;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull( message = "The user field cannot be empty")
    private User user;

    public void updateCurrentProgress(double amount) {
        this.currentProgress += amount;
    }
}
