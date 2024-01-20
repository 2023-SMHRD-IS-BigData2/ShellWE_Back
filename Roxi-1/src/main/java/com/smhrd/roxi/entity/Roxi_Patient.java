package com.smhrd.roxi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Roxi_Patient {

	// 환자번호
	@Id
	@OneToMany
	@GeneratedValue(strategy = GenerationType.IDENTITY)// Auto_increment
	private int patinum;
	// 환자이름병동
	@Column
	private String name;
	// 환자성별
	@Column
	private String gender;
	// 환자 나이
	@Column
	private int age;
	// 병동
	@Column
	private String ward;
	// 입원일
	@Column
	private String hpdate;
	// 혈액행
	@Column
	private String bloodtype;
	// 패혈증 상태
	@Column
	private String sepsisslevel;
	
	// 패혈증 수치
	private int sepsisscore;
	
	// 환자의 주치의
	@ManyToOne
	@JoinColumn(referencedColumnName = "membernum")
	private Roxi_Member physician;
	
	// FK를 사용하는 경우 toString 메소드를 직접 Override 할것.
	// JPA를 사용할시 롬복으로 toString을 사용할경우 버그 발생함.
	@Override
	public String toString() {
		return "Roxy_patient";
	}
}
