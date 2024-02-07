package com.smhrd.smart.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smhrd.smart.entity.Smart_vital;
import com.smhrd.smart.entity.smart_vital1;

public interface SepsissRepository1 extends JpaRepository<smart_vital1, Integer> {
	List<smart_vital1> findBypatientnum(int patientnum);
	
	@Query("SELECT r FROM smart_vital1 r WHERE DATE(r.sepdate) = :date AND r.patientnum = :patientnum")
	List<smart_vital1> findBypatientnumAndSepdate(@Param("patientnum") int patientnum, @Param("date") Date date);

	public void deleteBypatientnum(int parseInt);
}
