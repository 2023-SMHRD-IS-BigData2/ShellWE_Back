package com.smhrd.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.smart.entity.Smart_Patient;
import com.smhrd.smart.entity.Smart_comment;

@Repository
public interface CommentRepository extends JpaRepository<Smart_comment, Integer> {
	public List<Smart_comment> findBypatinum(int patinum);
	public void deleteBypatinum(int patinum);
	
}
