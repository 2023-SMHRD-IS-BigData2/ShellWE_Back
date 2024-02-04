package com.smhrd.roxi.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smhrd.roxi.entity.Roxi_Patient;
import com.smhrd.roxi.entity.Roxi_Sepsiss;
import com.smhrd.roxi.entity.Roxi_comment;
import com.smhrd.roxi.repository.CommentRepository;
import com.smhrd.roxi.repository.PatientRepository;
import com.smhrd.roxi.repository.SepsissRepository;

import aj.org.objectweb.asm.Type;

@Controller
public class PatientController {
	
	
	@Autowired
	private PatientRepository repo;
	
	@Autowired
	private SepsissRepository srepo;
	
	@Autowired
	private CommentRepository crepo;
	
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
	
	@RequestMapping("/dengerList")
	public String dengerList(Model model) {
		List<Roxi_Patient> list = repo.findBysepsisslevel("Screening");
		model.addAttribute("list",list);
		return "main";
	}
	
	
	/*
	 * Detail 페이지에서 특정 날짜 선택해서 정보 추출 함수
	 * input : 날짜, 환자 번호
	 * 특정 날짜와 환자 번호로 데이터베이스에서 값 받아와 List 형태로 저장
	 * */
	@RequestMapping("/selectDate")
	public String selectDate(Model model, String patinum,String date) {
		System.out.println(date);
		LocalDate localDate = LocalDate.parse(date);
		Date d = Date.valueOf(localDate);
		int pNum = Integer.parseInt(patinum);
		List<Roxi_Sepsiss> list = srepo.findBypatientnumAndSepdate(pNum, d);
		Optional<Roxi_Patient> patient = repo.findById(pNum);
		model.addAttribute("patient",patient.get());
		model.addAttribute("list", list);
		return "detail";
		
	}
	@RequestMapping("/searchPatient")
	public String searchPatient(Model model, String search) {
		List<Roxi_Patient> list = repo.findByname(search);
		model.addAttribute("list", list);
		return "main";
	}
	
	@RequestMapping("/searchWard")
	public String searchWard(Model model, String searchWard) {
		List<Roxi_Patient> list = repo.findByward(searchWard);
		model.addAttribute("list",list);
		return "main";
	}
	
	//상태 변화 실행함수
	//상태 변화 시 환자 테이블 status 수정
	//수정 된 정보 comment 테이블에 입력
    @Transactional
    @RequestMapping("/changeSepsisslevel")
    public String changeSepsisslevel(String sepsisslevel, String patinum, String pastStatus) {
    	System.out.println("change");
        Roxi_Patient patient = repo.findById(Integer.parseInt(patinum)).orElse(null);
        if (patient != null) {
            patient.setSepsisslevel(sepsisslevel);
            repo.save(patient);
    		Roxi_comment r = new Roxi_comment();
    		r.setPatinum(Integer.parseInt(patinum));
    		r.setMembernum(1);
    		r.setContents(pastStatus + " -> " + sepsisslevel);
    		crepo.save(r);
        }
        return "redirect:/"; // 변경 후 리다이렉트할 URL 반환
    }
    
    @RequestMapping("/delPatient")
    public String delPatient(String patinum) {
    	repo.deleteById(Integer.parseInt(patinum));
    	
    	return "redirect:/";
    }
	
}