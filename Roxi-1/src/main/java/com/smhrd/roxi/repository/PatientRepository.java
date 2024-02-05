package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.roxi.entity.Smart_Patient;

@Repository
public interface PatientRepository extends JpaRepository<Smart_Patient, Integer>{
	public List<Smart_Patient> findAll();
	public List<Smart_Patient> findBySepsisscoreGreaterThanEqual(int value);
	public List<Smart_Patient> findByname(String name);
	public List<Smart_Patient> findByward(String word);
	public List<Smart_Patient> findBysepsisslevel(String sepsisslevel);	
}
