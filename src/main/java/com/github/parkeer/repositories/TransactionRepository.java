package com.github.parkeer.repositories;

import com.github.parkeer.entities.transaction.TransactionEntity;
import com.github.parkeer.entities.transaction.TransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, TransactionId> {

    @Query(value = "SELECT NEXTVAL('public.transaction_transaction_id_seq')", nativeQuery = true)
    Long getNextId();
}
