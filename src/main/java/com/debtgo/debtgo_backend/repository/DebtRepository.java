package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.debt.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    long countByStatus(String status);

    // Número de clientes activos
    @Query("SELECT COUNT(DISTINCT d.clientName) FROM Debt d WHERE d.status = 'ACTIVE'")
    long countActiveClients();

    // Porcentaje de cumplimiento de pago
    @Query("SELECT ROUND(CAST(SUM(CASE WHEN d.status = 'PAID' THEN 1 ELSE 0 END) * 100.0 / COUNT(d) AS double), 2) FROM Debt d")
    Double calculateComplianceRate();
}