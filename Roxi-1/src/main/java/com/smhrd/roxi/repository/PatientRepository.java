package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.roxi.entity.Roxi_Patient;

public interface PatientRepository extends JpaRepository<Roxi_Patient, Integer>{
	public List<Roxi_Patient> findAll();
	public List<Roxi_Patient> findBySepsisscoreGreaterThanEqual(int value);
	public List<Roxi_Patient> findByname(String name);
	public List<Roxi_Patient> findByward(String word);
}
