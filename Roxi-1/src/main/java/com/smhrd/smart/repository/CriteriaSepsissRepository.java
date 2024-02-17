package com.smhrd.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.smart.entity.Smart_sepsiss;

@Repository
public interface CriteriaSepsissRepository extends JpaRepository<Smart_sepsiss, Integer>{
	
}
