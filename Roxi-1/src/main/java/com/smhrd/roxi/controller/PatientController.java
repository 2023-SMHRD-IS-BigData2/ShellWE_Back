package com.smhrd.roxi.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.roxi.entity.Smart_Patient;
import com.smhrd.roxi.entity.Smart_vital;
import com.smhrd.roxi.entity.Smart_comment;
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
		List<Smart_Patient> list = repo.findAll();
		model.addAttribute("list",list);
		return "main";
	}
	
	
	@PostMapping("/insert")
	public String insert(Smart_Patient patient) {
		System.out.println(patient.getName());
		repo.save(patient);
		return "redirect:/";
	}
	
	@GetMapping("/detail")
	public String detail(Model model, String patinum) {
		Optional<Smart_Patient> patient = repo.findById(Integer.parseInt(patinum));
		List<Smart_vital> list = srepo.findBypatientnum(Integer.parseInt(patinum));
		model.addAttribute("patient",patient.get());
		model.addAttribute("list", list);
		return "detail";
	}
	
	@GetMapping("/detailInsert")
	public String detailInsert(Smart_vital sepsiss) {
		srepo.save(sepsiss);
		return "redirect:/detail?patinum="+sepsiss.getPatientnum();
	}
	
	@RequestMapping("/dengerList")
	public String dengerList(Model model) {
		List<Smart_Patient> list = repo.findBysepsisslevel("Screening");
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
		List<Smart_vital> list = srepo.findBypatientnumAndSepdate(pNum, d);
		Optional<Smart_Patient> patient = repo.findById(pNum);
		model.addAttribute("patient",patient.get());
		model.addAttribute("list", list);
		return "detail";
		
	}
	@RequestMapping("/searchPatient")
	public String searchPatient(Model model, String search) {
		List<Smart_Patient> list = repo.findByname(search);
		model.addAttribute("list", list);
		return "main";
	}
	
	@RequestMapping("/searchWard")
	public String searchWard(Model model, String searchWard) {
		List<Smart_Patient> list = repo.findByward(searchWard);
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
        Smart_Patient patient = repo.findById(Integer.parseInt(patinum)).orElse(null);
        if (patient != null) {
            patient.setSepsisslevel(sepsisslevel);
            repo.save(patient);
    		Smart_comment r = new Smart_comment();
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
	
    //flask 서버에 값 넘기기
    @RequestMapping("/SendAllData")
    public String SendAllData(RedirectAttributes redirect, String patinum) {
    
    	List<HashMap<String, Object>> list = new ArrayList<>();
    	List<Smart_vital> plist = srepo.findBypatientnum(Integer.parseInt(patinum));
    	Smart_Patient patient = repo.findById(Integer.parseInt(patinum)).get();
    	for(int i=0; i<plist.size();i++) {
    		HashMap<String, Object> hash = new HashMap<>();
    		hash.put("age", patient.getAge());
    		hash.put("gender", patient.getGender());
    		hash.put("o2sat", plist.get(i).getO2sat());
    		hash.put("temp", plist.get(i).getTemp());
    		hash.put("sbp", plist.get(i).getSbp());
    		hash.put("dbp", plist.get(i).getDbp());
    		hash.put("resp", plist.get(i).getResp());
    		hash.put("hr", plist.get(i).getHr());
    		hash.put("map", plist.get(i).getMap());
    		hash.put("etco2", plist.get(i).getEtco2());
    		hash.put("baseexcess", plist.get(i).getBaseexcess());
    		hash.put("hco3", plist.get(i).getHco3());
    		hash.put("fio2", plist.get(i).getFio2());
    		hash.put("ph", plist.get(i).getPh());
    		hash.put("paco2", plist.get(i).getPaco2());
    		hash.put("sao2", plist.get(i).getSao2());
    		hash.put("ast", plist.get(i).getAst());
    		hash.put("bun", plist.get(i).getBun());
    		hash.put("alkalinephos", plist.get(i).getAlkalinephos());
    		hash.put("calcium", plist.get(i).getCalcium());
    		hash.put("chloride", plist.get(i).getChloride());
    		hash.put("creatinine", plist.get(i).getCreatinine());
    		hash.put("bilirubin_direct", plist.get(i).getBilirubindirect());
    		hash.put("glucose", plist.get(i).getGlucose());
    		hash.put("lactate", plist.get(i).getLactate());
    		hash.put("magnesium", plist.get(i).getMagnesium());
    		hash.put("phosphate", plist.get(i).getPhosphate());
    		hash.put("potassium", plist.get(i).getPotassium());
    		hash.put("biliubin_total", plist.get(i).getBilirubintotal());
    		hash.put("troponini", plist.get(i).getTroponini());
    		hash.put("hct", plist.get(i).getHct());
    		hash.put("hgb", plist.get(i).getHgb());
    		hash.put("ptt", plist.get(i).getPtt());
    		hash.put("wbc", plist.get(i).getWbc());
    		hash.put("fibrinogen", plist.get(i).getFibrinogen());
    		hash.put("platelets", plist.get(i).getPlatelets());
    		list.add(hash);
    	}
    	redirect.addFlashAttribute("list", list);
    	return "redirect:/flask";
    }
    
}