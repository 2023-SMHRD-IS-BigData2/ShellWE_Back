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
	
	@RequestMapping("/sepsissscoer") // sepsiss 정보를 업데이트하는 메소드 
	public String CriteriaSepsiss(@RequestBody Smart_sepsiss sepsiss) {
		csrepo.save(sepsiss);
		return ""; 
	}
	
	@RequestMapping("/smartsepsiss") // sepsiss 정보를 전달하는 메소드
	public ResponseEntity<JSONObject> sepsiss() {
	    JSONObject responseJson = new JSONObject();
	    try {
	        Smart_sepsiss smartsepsiss = (Smart_sepsiss) csrepo.findAll();
	        
	        if (smartsepsiss != null) {
	            responseJson.put("smartsepsiss", smartsepsiss);
	            return new ResponseEntity<>(responseJson, HttpStatus.OK);
	        } else {
	            responseJson.put("error", "Smart sepsiss not found");
	            return new ResponseEntity<>(responseJson, HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        responseJson.put("error", "Error while fetching Smart sepsiss");
	        return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
