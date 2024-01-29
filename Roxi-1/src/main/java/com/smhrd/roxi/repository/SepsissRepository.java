package com.smhrd.roxi.repository;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_Sepsiss;

public interface SepsissRepository extends JpaRepository<Roxi_Sepsiss, Integer> {
	List<Roxi_Sepsiss> findBypatientnum(int patientnum);
	
	@Query("SELECT r FROM Roxi_Sepsiss r WHERE DATE(r.sepdate) = :date AND r.patientnum = :patientnum")
	List<Roxi_Sepsiss> findBypatientnumAndSepdate(@Param("patientnum") int patientnum, @Param("date") Date date);

}
