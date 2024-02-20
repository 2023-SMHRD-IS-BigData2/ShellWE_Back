 package com.smhrd.smart.flask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.smart.controller.CriteriaSepsissController;
import com.smhrd.smart.controller.PatientController;
import com.smhrd.smart.controller.VitalController;
import com.smhrd.smart.entity.Smart_Patient;
import com.smhrd.smart.repository.PatientRepository;

@Controller
public class flask {
	final String URL = "http://localhost:7000/sepsis";
	final String POST = "POST";
	final String USER_AGENT = "Mozilla/5.0";
	
//	@Autowired
//	private PatientController patientcontroller;
	
	@Autowired
	private VitalController vitalcontroller;
	
	@Autowired
	private CriteriaSepsissController criteriaseosiss;

	@Autowired
	private PatientRepository patientrepository;
	
	
	//환자 대표 sepsisscore 예측 함수
	public String flask(List<HashMap<String, Object>> list, int patinum) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
        HttpEntity<List<HashMap<String, Object>>> requestEntity = new HttpEntity<>(list, headers);
		
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);//flask 연동 메소드 호출
        
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Response from Flask server: " + responseBody);
           
        } else {
            System.err.println("Failed to send data. Status code: " + statusCode);
        }
        return responseEntity.getBody();//예측 값 리턴
	}
	
	//세부 바이탈 예측 함수
	public int flask_1(List<HashMap<String, Object>> list, int patinum, int vitalnum) throws IOException {
		Smart_Patient smartpatient = patientrepository.findById(patinum).get();
		System.out.println("vitalnum : "+vitalnum);
		int cnt = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
        HttpEntity<List<HashMap<String, Object>>> requestEntity = new HttpEntity<>(list, headers);
		
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            
            String result = vitalcontroller.setVitalDate(Integer.parseInt(responseBody),vitalnum+1); //환자 패혈증 수치 업데이트 메소드, 그냥 vitalnum 넣으면 새로 생성된 행이 아니라 이전 vital에 저장됨
            System.out.println("이름 : "+smartpatient.getName());
            System.out.println("새로운 패혈증 수치 : "+ responseBody);
            String seplevel = smartpatient.getSepsisslevel();
            if(seplevel.equals("None")) {
            	smartpatient.setSepsisslevel(criteriaseosiss.sepsissscoer(Integer.parseInt(responseBody))); // db에 저장할 sepsisslevel확인후 저장
            	if(!smartpatient.getSepsisslevel().equals(seplevel)) {
            		cnt+=1;
            	}
            }
            smartpatient.setSepsisscore(Integer.parseInt(responseBody)); //환자 테이블에 sepsisscore 저장
            patientrepository.save(smartpatient); // jpa를 이용하여 db에 업데이트 하도록 수정
            System.out.println("Response from Flask server: " + responseBody);
        } else {
            System.err.println("Failed to send data. Status code: " + statusCode);
        }
        
        return cnt;
        
	}
	
	
}
