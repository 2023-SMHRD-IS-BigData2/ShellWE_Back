package com.smhrd.roxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_comment;

public interface CommentRepository extends JpaRepository<Roxi_comment, Integer> {
	public List<Roxi_comment> findBypatinum(int patinum);
}
