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
	public void flask_1(List<HashMap<String, Object>> list, int patinum, int vitalnum) throws IOException {
		if (list!=null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
		} else {
			System.out.println("list가 비어있습니다.");
		}
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
        HttpEntity<List<HashMap<String, Object>>> requestEntity = new HttpEntity<>(list, headers);
		
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
        
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            
            String result = vitalcontroller.setVitalDate(Integer.parseInt(responseBody),vitalnum); //환자 패혈증 수치 업데이트 메소드
            criteriaseosiss.sepsissscoer(Integer.parseInt(responseBody));
            System.out.println("Response from Flask server: " + responseBody);
            System.out.println("result : "+result);
        } else {
            System.err.println("Failed to send data. Status code: " + statusCode);
        }
        
	}
	
	
}
