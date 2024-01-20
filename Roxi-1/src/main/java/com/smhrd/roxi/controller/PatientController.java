package com.smhrd.roxi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.repository.PatientRepository;

@Controller
public class PatientController {
	
	@Autowired
	private PatientRepository repo;
	
	@RequestMapping("/")
	public String Main(Model model) {
		List<Roxi_Patient> list = repo.findAll();
		System.out.println(list.get(0).getName());
		model.addAttribute("list",list);
		return "main";
	}
	@PostMapping("/insert")
	public String insert(Roxi_Patient patient) {
		System.out.println(patient.getName());
		repo.save(patient);
		
		return "redirect:/";
	}
	
}