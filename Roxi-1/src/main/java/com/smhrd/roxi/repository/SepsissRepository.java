package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_Sepsiss;

public interface SepsissRepository extends JpaRepository<Roxi_Sepsiss, Integer> {
	List<Roxi_Sepsiss> findBypatientnum(int patientnum);
}
