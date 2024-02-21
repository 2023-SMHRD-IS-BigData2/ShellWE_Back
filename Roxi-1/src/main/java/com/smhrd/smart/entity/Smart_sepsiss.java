package com.smhrd.smart.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Smart_sepsiss {
	// 패혈증 기준치를 나타내는 컬럼
	@Id
	private int sepsissnum;
	private int sepsiss;
}
