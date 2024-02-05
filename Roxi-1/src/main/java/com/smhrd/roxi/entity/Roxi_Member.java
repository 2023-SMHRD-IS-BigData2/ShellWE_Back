package com.smhrd.roxi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;

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
	
	// 의료진 ID
	@NotNull
	@Column(unique = true)
	private String id;
	
	// 비밀번호
	@NotNull
	private String pw;
	
	// 직급
	@Column
	private String rank;
	
	// 회원 등록일
	@Column(columnDefinition = "datetime default now()", insertable = false, updatable = false)
	private String date;
	
	// 로그인 시간
	@Column(columnDefinition = "datetime")
	private LocalDateTime logintime;
	
	// 로그아웃 시간
	@Column(columnDefinition = "datetime")
	private LocalDateTime logouttime;
	
	// 연락처
	@Column
	private String tell;

	

}
