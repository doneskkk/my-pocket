package com.donesk.moneytracker.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Budgets")
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "current_progress")
    private Double currentProgress;

    @NonNull
    @Column(name = "goal", nullable = false)
    private Double goal;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Transaction> transactionList;

    public Budget(String title, Double currentProgress, Double goal, List<Transaction> transactionList) {
        this.title = title;
        this.currentProgress = currentProgress;
        this.goal = goal;
        this.transactionList = transactionList;
    }

    public Budget() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Double currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
