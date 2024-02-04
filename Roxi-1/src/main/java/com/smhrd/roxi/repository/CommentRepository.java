package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_comment;

@Repository
public interface CommentRepository extends JpaRepository<Roxi_comment, Integer> {
	public List<Roxi_comment> findBypatinum(int patinum);
	public void deleteBypatinum(int patinum);
	
}
