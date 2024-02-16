package com.smhrd.smart.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.smhrd.smart.controller.CommentController;
import com.smhrd.smart.controller.PatientController;

@RestController //리액트로 데이터만 전달하는 컨트롤러 어노테이션
@CrossOrigin("http://localhost:3000")//해당 url로 요청이 들어왔을 시 작동
public class front {

	@Autowired
	private PatientController patientcontroller;
	
	@Autowired
	private CommentController commentcontroller;
    
	//환자 번호에 해당하는 생체 데이터 JSONArray 형태로 출력
    @RequestMapping("/getList")// 만약 url이 http://localhost:8088/boot/getList 요청이 들어왔을 시 실행
    public JSONArray getList(String patinum) { 
    	JSONArray dataList = new JSONArray();// JSONArray 객체 생성
    	dataList = patientcontroller.getDetailList(patinum); //patientcontroller에 존재하는 getDetailList() 메소드 호출 후 return 결과 변수에 담기
    	return dataList;// JSONArray 데이터 리턴
    }
    
    //환자 기본 정보 출력 함수
    @RequestMapping("/getPatient")
    public JSONObject getPatient() {
    	JSONObject patientList = new JSONObject();
    	patientList = patientcontroller.getPatient();
    	return patientList;
    }
    
    //해당 환자의 모든 vital 수치 출력(환자 나이, 성별 x)
    @RequestMapping("/getVital")
    public JSONArray getVital(String patinum) {
    	JSONArray VitalList = new JSONArray();
    	VitalList = patientcontroller.getvital(patinum);
    	return VitalList;
    }
    
    //해당 날짜에 위험한 컬럼 표현
    @RequestMapping("/dengerColumns")
    public JSONObject dateColumn(String patinum, String date) {
    	JSONObject dengercolumns = new JSONObject();
    	dengercolumns = patientcontroller.getDengerList(Integer.parseInt(patinum), date);
    	return dengercolumns;
    }
    
    //환자별 comment 출력
    @RequestMapping("/getComment")
    public JSONObject getComment(String patinum) {
    	JSONObject commnets = new JSONObject();
    	commnets = commentcontroller.getcomment(Integer.parseInt(patinum));
    	return commnets;
    }
    
    //코멘트 입력
    @RequestMapping("/insertComment")
    public String insertComment(String insertComment, String patinum, String membernum) {
    		String result = commentcontroller.insertcomment(insertComment, patinum, Integer.parseInt(membernum));
    		return result;
    }
    
    //상태 변화시 commnet 입력
    @RequestMapping("/changeStatus")
    public String changeStatus(String sepsisslevel, String patinum) {
    	String result = patientcontroller.changeStatus(sepsisslevel, patinum);
    	return result;
    }
    

}