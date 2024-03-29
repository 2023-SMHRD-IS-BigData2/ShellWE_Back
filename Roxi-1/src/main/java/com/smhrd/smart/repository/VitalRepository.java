package com.smhrd.smart.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.smart.entity.smart_vital1;

public interface VitalRepository extends JpaRepository<smart_vital1, Long>{
	List<smart_vital1> findByPatientnumAndSepdateBetween(int patientnum,LocalDate startDate, LocalDate endDate);
	smart_vital1 findByVitalnum(int vitalnum);
	List<smart_vital1> findByPatientnum(int patientnum);
	smart_vital1 findFirstByOrderByVitalnumDesc(); // smartnum을 기준으로 내림차순으로 정렬하여 엔티티 리스트를 가져오는 메소드
}
