package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.Students;
import java.util.List;
import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Students, Long> {
    
    // Find a student by name
    Students findByName(String name);

    // Find a student by seat number
    Optional<Students> findBySeatNumber(String seatNumber);

    // Custom query to find students by keyword in name, address, phone number, or status
    @Query("SELECT s FROM Students s WHERE s.name LIKE %:keyword% OR s.address LIKE %:keyword% OR s.phoneNumber LIKE %:keyword% OR s.status LIKE %:keyword%")
    List<Students> findAllByKeyword(@Param("keyword") String keyword);
}
