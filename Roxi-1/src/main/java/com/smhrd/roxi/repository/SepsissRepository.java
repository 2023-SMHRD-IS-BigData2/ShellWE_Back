package com.smhrd.roxi.repository;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.roxi.entity.Smart_Patient;
import com.smhrd.roxi.entity.Smart_vital;

@Repository
public interface SepsissRepository extends JpaRepository<Smart_vital, Integer> {
	List<Smart_vital> findBypatientnum(int patientnum);
	
	@Query("SELECT r FROM Smart_vital r WHERE DATE(r.sepdate) = :date AND r.patientnum = :patientnum")
	List<Smart_vital> findBypatientnumAndSepdate(@Param("patientnum") int patientnum, @Param("date") Date date);

	public void deleteBypatientnum(int parseInt);

}
