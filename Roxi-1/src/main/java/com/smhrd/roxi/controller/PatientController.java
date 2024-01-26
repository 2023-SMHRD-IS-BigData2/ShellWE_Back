package com.smhrd.roxi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_Sepsiss;
import com.smhrd.roxi.repository.PatientRepository;
import com.smhrd.roxi.repository.SepsissRepository;

@Controller
public class PatientController {
	
	@Autowired
	private PatientRepository repo;
	
	@Autowired
	private SepsissRepository srepo;
	
	@RequestMapping("/")
	public String Main(Model model) {
		List<Roxi_Patient> list = repo.findAll();
		model.addAttribute("list",list);
		return "main";
	}
	@PostMapping("/insert")
	public String insert(Roxi_Patient patient) {
		System.out.println(patient.getName());
		repo.save(patient);
		return "redirect:/";
	}
	
	@GetMapping("/detail")
	public String detail(Model model, String patinum) {
		System.out.println(patinum);
		Optional<Roxi_Patient> patient = repo.findById(Integer.parseInt(patinum));
		List<Roxi_Sepsiss> list = srepo.findBypatientnum(Integer.parseInt(patinum));
		model.addAttribute("patient",patient.get());
		model.addAttribute("list", list);
		return "detail";
	}
	
	@GetMapping("/detailInsert")
	public String detailInsert(Roxi_Sepsiss sepsiss) {
		srepo.save(sepsiss);
		return "redirect:/detail?patinum="+sepsiss.getPatientnum();
	}
	
}