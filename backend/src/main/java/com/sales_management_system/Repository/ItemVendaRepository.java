package com.sales_management_system.Repository;

import com.sales_management_system.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, UUID> {
}
