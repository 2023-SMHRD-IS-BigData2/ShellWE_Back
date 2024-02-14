package com.smhrd.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Smart_comment {

	// 코멘트 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto_increment
	private int commentnum;
	// 환자 번호

	//@JoinColumn(name="patinum", referencedColumnName = "patinum")
	//@ManyToOne
	@Column
	private int patinum;
	// 의사사번
	//@JoinColumn(name="membernum", referencedColumnName = "membernum")
	//@ManyToOne
	@Column
	private int membernum;
	// 입력시간
	@Column(columnDefinition = "datetime default now()", insertable = false, updatable = false)
	private String inputdate;
	// 내용
	@Column
	private String contents;
	
	@Column
	private String membername;
}
