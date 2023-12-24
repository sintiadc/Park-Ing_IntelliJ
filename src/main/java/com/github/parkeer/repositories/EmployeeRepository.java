package com.github.parkeer.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.parkeer.entities.employee.EmployeeEntity;
import com.github.parkeer.entities.employee.EmployeeId;
import com.github.parkeer.enums.AccountRoleType;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, EmployeeId> {

    Optional<EmployeeEntity> findOneByUsername(final String username);

    @Query(value = "SELECT NEXTVAL('public.employee_employee_id_seq')", nativeQuery = true)
    Long getNextId();


}
