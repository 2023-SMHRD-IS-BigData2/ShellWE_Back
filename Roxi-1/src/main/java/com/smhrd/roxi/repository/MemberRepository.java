package com.smhrd.roxi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.roxi.entity.Smart_Member;

@Repository
public interface MemberRepository extends JpaRepository<Smart_Member, Integer>{

	Smart_Member findByidAndPw(String id, String pw);

	Optional<Smart_Member> findById(String id);

	
}
