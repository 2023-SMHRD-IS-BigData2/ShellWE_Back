package com.smhrd.smart.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Persistent;

import lombok.Data;

@Entity
@Data
@Persistent
public class Smart_vital {
	
	//바이탈 넘버
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int num;
	
	// 환자번호
	@Column
	private int patientnum;
	
	// 패혈증 수치
	@Column
	private float sepsisscore;
	
	// 검사 일시 default값 지정
	@Column(columnDefinition = "datetime default now()", insertable = false, updatable = false)
	private String sepdate;
	// 산소포화도
	@Column
	private float o2sat;
	
	// 체온
	@Column
	private float temp;
	// 수축기 혈압
	@Column
	private int sbp;
	// 이완기 혈압
	@Column
	private int dbp;
	//호흡수
	@Column
	private int resp;
	// 심장박동수
	@Column
	private int hr;
	// 평균 동맥압 평균 혈압
	@Column
	private float map;
	
	//이산화 탄소 농도
	@Column
	private float etco2;
	
	//과잉 탄산염
	@Column
	private float baseexcess;
	
	//탄산수소이온
	@Column
	private float hco3;
	
	//흡기 손소분율
	@Column
	private float fio2;
	
	//산성 또는 알카리성 정도를 나타내는 척도
	@Column
	private float ph;
	
	//이산화 탄소 분압
	@Column
	private float paco2;
	
	//동맥혈의 산소포화도
	@Column
	private float sao2;
	
	//간 기능을 평가하기위해 사용하는 효소
	@Column
	private float ast;
	
	//신장기능 평가
	@Column
	private float bun;
	
	//알카리성 인산효소
	@Column
	private float alkalinephos;
	
	//칼슘
	@Column
	private float calcium;
	
	//염소이온
	@Column
	private float chloride;
	
	//크레아틴
	@Column
	private float creatinine;
	
	//직접빌리루빈
	@Column
	private float bilirubindirect;
	
	//혈당
	@Column
	private float glucose;
	
	//라틱산
	@Column
	private float lactate;
	
	//마그네슘
	@Column
	private float magnesium;
	
	//인
	@Column
	private float phosphate;
	
	//칼륨
	@Column
	private float potassium;
	
	//총 빌리루빈
	@Column
	private float bilirubintotal;
	
	//트로포닌
	@Column
	private float troponini;
	
	//헤마토크리트
	@Column
	private float hct;
	
	//헤모글로빈
	@Column
	private float hgb;
	
	//부분혈장응고시간
	@Column
	private float ptt;
	
	//백혈구 수
	@Column
	private float wbc;
	
	//혈액응고 역할 단백질
	@Column
	private float fibrinogen;
	
	//혈소판 수
	@Column
	private float platelets;
}
