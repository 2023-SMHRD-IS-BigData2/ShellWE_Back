package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.roxi.entity.Roxi_Patient;

public interface PatientRepository extends JpaRepository<Roxi_Patient, Integer>{
	public List<Roxi_Patient> findAll();
}
