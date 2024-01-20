package com.smhrd.roxi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Roxi_Sepsiss {
	
	// 환자번호
	@ManyToOne
	@JoinColumn(name = "sepsnum", referencedColumnName = "patinum")
	private Roxi_Patient sepsnum;
	// 패혈증 수치
	@Column
	private String sepsisscore;
	// 검사 일시 default값 지정
	@Column(columnDefinition = "datetime default now()", insertable = false, updatable = false)
	private String sepdate;
	// 산소포화도
	@Column
	private String spo2;
	// 체온
	@Column
	private String temp;
	// 수축기 혈압
	@Column
	private String sbp;
	// 이완기 혈압
	@Column
	private String dbp;
	//호흡수
	@Column
	private String resp;
	// 심장박동수
	@Column
	private String hr;
	
	
}
