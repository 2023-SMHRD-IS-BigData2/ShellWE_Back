package com.smhrd.roxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.roxi.entity.Roxi_Member;

@Repository
public interface MemberRepository extends JpaRepository<Roxi_Member, Integer>{

	Roxi_Member findByMembernumAndPw(int i, String pw);

	
}
