package com.smhrd.roxi.front;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.smhrd.roxi.controller.PatientController;

@RestController //리액트로 데이터만 전달하는 컨트롤러 어노테이션
@CrossOrigin("http://localhost:3000")//해당 url로 요청이 들어왔을 시 작동
public class front {

	@Autowired
	private PatientController patientcontroller;
    
	//환자 번호에 해당하는 생체 데이터 JSONArray 형태로 출력
    @RequestMapping("/getList")// 만약 url이 http://localhost:8088/boot/getList 요청이 들어왔을 시 실행
    public JSONArray getList(String patinum) { 
    	JSONArray dataList = new JSONArray();// JSONArray 객체 생성
    	dataList = patientcontroller.getDetailList(patinum); //patientcontroller에 존재하는 getDetailList() 메소드 호출 후 return 결과 변수에 담기
    	return dataList;// JSONArray 데이터 리턴
    }
}