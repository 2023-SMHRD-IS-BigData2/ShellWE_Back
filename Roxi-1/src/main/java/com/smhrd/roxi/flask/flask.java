package com.smhrd.roxi.flask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

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

@Controller
public class flask {
	final String URL = "http://localhost:7000/sepsis";
	final String POST = "POST";
	final String USER_AGENT = "Mozilla/5.0";
	@RequestMapping("/flask")
	public String flask(Model model) throws IOException {
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) model.getAttribute("list");
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
            System.out.println("Response from Flask server: " + responseBody);
        } else {
            System.err.println("Failed to send data. Status code: " + statusCode);
        }
        
		return "redirect:/";
	}
}
