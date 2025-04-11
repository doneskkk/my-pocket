package com.donesk.moneytracker.repository;

import com.donesk.moneytracker.model.Budget;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId")
    List<Budget> findByUserId(@Param("userId") Long userId);

}
