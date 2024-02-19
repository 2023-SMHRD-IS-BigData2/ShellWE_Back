package com.smhrd.smart.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.smart.entity.Smart_sepsiss;
import com.smhrd.smart.repository.CriteriaSepsissRepository;

@Controller
public class CriteriaSepsissController {
	
	@Autowired
	private CriteriaSepsissRepository csrepo;
	
	@RequestMapping("/sepsissscoer")
	public ResponseEntity<JSONObject> CriteriaSepsiss(@RequestBody Smart_sepsiss sepsiss) {
		JSONObject responseJson = new JSONObject();
		Smart_sepsiss smartsepsiss = csrepo.findById(sepsiss.getSepsiss()).orElse(null); // .orElse(null) 해당 값을 못찾으면 null값으로 지정함
		if(smartsepsiss != null) {
			responseJson.put("change", smartsepsiss.getSepsiss());
		}
		responseJson.put("change", "위험수치가 변경되었습니다");
		csrepo.save(sepsiss);
		return new ResponseEntity<>(responseJson,HttpStatus.OK);
	}
	public String sepsissscoer(int sepsissscoer) {
		String sepsissString ="";
		List<Smart_sepsiss> sepsiss = csrepo.findAll();
		if(sepsiss.get(0).getSepsiss() >= sepsissscoer) {
			sepsissString="None";
			System.out.println(sepsissString);
			return sepsissString;
		}
		else {
			sepsissString="Screening";
			System.out.println(sepsissString);
			return sepsissString;
		}
	}
}
