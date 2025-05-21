package com.sales_management_system.Repository;

import com.sales_management_system.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
}
