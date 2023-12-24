package com.github.parkeer.repositories;

import com.github.parkeer.entities.admin.AdminEntity;
import com.github.parkeer.entities.admin.AdminId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, AdminId> {

    Optional<AdminEntity> findOneByUsername(final String username);

    @Query(value = "SELECT NEXTVAL('public.admin_admin_id_seq')", nativeQuery = true)
    Long getNextId();
}
