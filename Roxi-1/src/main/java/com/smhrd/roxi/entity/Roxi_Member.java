package com.smhrd.roxi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Roxi_Member {
	// 사원번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto_increment
	private int membernum;
	
	// 이름
	@Column
	private String name;
	
	// 비밀번호
	@Column
	private String pw;
	
	// 직급
	@Column
	private String rank;
	
	// 로그인 시간
	@Column(columnDefinition = "datetime default now()", insertable = false, updatable = false)
	private String date;
	
	// 연락처
	@Column
	private int tell;
	
	
	
	

}
