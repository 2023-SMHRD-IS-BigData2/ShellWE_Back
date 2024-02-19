package com.smhrd.smart.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.smart.entity.smart_vital1;
import com.smhrd.smart.repository.VitalRepository;

@Controller
public class VitalController {
	
	@Autowired
	private VitalRepository vrepo;
	
	@GetMapping("/selectDate")
	public String getVitalDate(@RequestParam("patinum") int patinum,
			@RequestParam("startdate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("enddate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			Model model){
		List<smart_vital1> dateVital = vrepo.findByPatientnumAndSepdateBetween(patinum, startDate, endDate);
		System.out.println("환자" +patinum+ "시작일"+startDate+"끝날짜"+endDate+"hr정보"+dateVital.get(1).getHr());
		model.addAttribute(dateVital);
		return "/detail?patinum="+patinum;		
	}
	
	public String setVitalDate(int sepsisscore, int vitalnum) {
		smart_vital1 vital = vrepo.findByVitalnum(vitalnum);
		vital.setSepsisscore((float) sepsisscore);
		vrepo.save(vital);
		return "success";
	}
	
	public List<smart_vital1> patientAllVital(int patinum){
		List<smart_vital1> list = vrepo.findByPatientnum(patinum);
		return list;
	}
}
