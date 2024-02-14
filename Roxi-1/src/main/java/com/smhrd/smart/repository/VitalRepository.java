package com.smhrd.smart.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.smart.entity.smart_vital1;

public interface VitalRepository extends JpaRepository<smart_vital1, Long>{
	List<smart_vital1> findByPatientnumAndSepdateBetween(int patientnum,LocalDate startDate, LocalDate endDate);
}
