package com.smhrd.smart.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.Persistent;

import lombok.Data;

@Entity
@Data
@Persistent
public class smart_vital1 {
	//바이탈 넘버
		@Id
		@Column
		private int vitalnum;

		// 환자번호
		@Column
		private int patientnum;

		// 패혈증 수치
		@Column
		private Float sepsisscore;

		// 검사 일시 default값 지정
		@Column(columnDefinition = "datetime")
		private LocalDate sepdate;
		// 산소포화도
		@Column(nullable = true)
		private Float o2sat;

		// 체온
		@Column(nullable = true)
		private Float temp;
		// 수축기 혈압
		@Column(nullable = true)
		private Integer sbp;
		// 이완기 혈압
		@Column(nullable = true)
		private Integer dbp;
		//호흡수
		@Column(nullable = true)
		private Integer resp;
		// 심장박동수
		@Column(nullable = true)
		private Integer hr;
		// 평균 동맥압 평균 혈압
		@Column(nullable = true)
		private Float map;

		//이산화 탄소 농도
		@Column(nullable = true)
		private Float etco2;

		//과잉 탄산염
		@Column(nullable = true)
		private Float baseexcess;

		//탄산수소이온
		@Column(nullable = true)
		private Float hco3;

		//흡기 손소분율
		@Column(nullable = true)
		private Float fio2;

		//산성 또는 알카리성 정도를 나타내는 척도
		@Column(nullable = true)
		private Float ph;

		//이산화 탄소 분압
		@Column(nullable = true)
		private Float paco2;

		//동맥혈의 산소포화도
		@Column(nullable = true)
		private Float sao2;

		//간 기능을 평가하기위해 사용하는 효소
		@Column(nullable = true)
		private Float ast;

		//신장기능 평가
		@Column(nullable = true)
		private Float bun;

		//알카리성 인산효소
		@Column(nullable = true)
		private Float alkalinephos;

		//칼슘
		@Column(nullable = true)
		private Float calcium;

		//염소이온
		@Column(nullable = true)
		private Float chloride;

		//크레아틴
		@Column(nullable = true)
		private Float creatinine;

		//직접빌리루빈
		@Column(nullable = true)
		private Float bilirubindirect;

		//혈당
		@Column(nullable = true)
		private Float glucose;

		//라틱산
		@Column(nullable = true)
		private Float lactate;

		//마그네슘
		@Column(nullable = true)
		private Float magnesium;

		//인
		@Column(nullable = true)
		private Float phosphate;

		//칼륨
		@Column(nullable = true)
		private Float potassium;

		//총 빌리루빈
		@Column(nullable = true)
		private Float bilirubintotal;

		//트로포닌
		@Column(nullable = true)
		private Float troponini;

		//헤마토크리트
		@Column(nullable = true)
		private Float hct;

		//헤모글로빈
		@Column(nullable = true)
		private Float hgb;

		//부분혈장응고시간
		@Column(nullable = true)
		private Float ptt;

		//백혈구 수
		@Column(nullable = true)
		private Float wbc;

		//혈액응고 역할 단백질
		@Column(nullable = true)
		private Float fibrinogen;

		//혈소판 수
		@Column(nullable = true)
		private Float platelets;
}