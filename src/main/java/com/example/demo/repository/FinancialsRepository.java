package com.example.demo.repository;

import com.example.demo.model.Financials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialsRepository extends JpaRepository<Financials, Long> {

    @Query("SELECT SUM(f.amount) FROM Financials f WHERE f.type = 'Income'")
    Double sumIncome();

    @Query("SELECT SUM(f.amount) FROM Financials f WHERE f.type = 'Expenditure'")
    Double sumExpenditure();
}
