package com.smhrd.smart.scheduled;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smhrd.smart.controller.PatientController;
import com.smhrd.smart.entity.Smart_Patient;
import com.smhrd.smart.entity.smart_vital1;
import com.smhrd.smart.flask.flask;
import com.smhrd.smart.repository.PatientRepository;
import com.smhrd.smart.repository.VitalRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

@Slf4j     // 로그를 위해서
@Component
public class Scheduled {
	
	@Autowired
	private PatientController patientcontroller;
	
	@Autowired
    private PatientRepository repo;
	
	@Autowired
	private VitalRepository vrepo;
	
	@Autowired
	private flask flsk;
	
//	@org.springframework.scheduling.annotation.Scheduled(fixedRate = 600000)
//	public void fixedRate() throws NumberFormatException, IOException {
//		System.out.println("600초마다 실행됨");
//		List<Smart_Patient> list = repo.findAll();
//		for(int i=0; i<list.size();i++) {
//			setvital(list.get(i).getPatinum());
//		}
//		
//	}
	
	public void setvital(int patinum) throws NumberFormatException, IOException {
		System.out.println(patinum+"번 환자 데이터 새롭게 입력되는 중");
		smart_vital1 big = vrepo.findFirstByOrderByVitalnumDesc();
		int lastIndex = big.getVitalnum();
		smart_vital1 vital = new smart_vital1();
        Float o2sat = Math.round((ThreadLocalRandom.current().nextFloat() * (100f - 90f) + 90f) * 10.0f) / 10.0f;
        Float temp = Math.round((ThreadLocalRandom.current().nextFloat() * (40f - 36.1f) + 36.1f) * 10.0f) / 10.0f;
        Integer sbp = ThreadLocalRandom.current().nextInt(80, 200);
        Integer dbp = ThreadLocalRandom.current().nextInt(34, 120);
        Integer resp = ThreadLocalRandom.current().nextInt(5,56);
        Integer hr = ThreadLocalRandom.current().nextInt(54,140);
        Float map = Math.round((ThreadLocalRandom.current().nextFloat() * (160f - 30f) + 30) * 10.0f) / 10.0f;
        Float etco2 = Math.round((ThreadLocalRandom.current().nextFloat() * (64.9f - 45.1f) + 45.1f) * 10.0f) / 10.0f;
        Float baseexcess = Math.round((ThreadLocalRandom.current().nextFloat() * (24f - -2f) + -2f) * 10.0f) / 10.0f;
        Float hco3 = Math.round((ThreadLocalRandom.current().nextFloat() * (40f - 12f) + 12f) * 10.0f) / 10.0f;
        Float fio2 = Math.round((ThreadLocalRandom.current().nextFloat() * (1f - 0.02f) + 0.02f) * 10.0f) / 10.0f;
        Float ph = Math.round((ThreadLocalRandom.current().nextFloat() * (7.5f - 7f) + 7f) * 10.0f) / 10.0f;
        Float paco2 = Math.round((ThreadLocalRandom.current().nextFloat() * (100f - 22f) + 22f) * 10.0f) / 10.0f;
        Float sao2 = Math.round((ThreadLocalRandom.current().nextFloat() * (110f - 60f) + 60f) * 10.0f) / 10.0f;
        Float ast = Math.round((ThreadLocalRandom.current().nextFloat() * (200f - 10f) + 10f) * 10.0f) / 10.0f;
        Float bun = Math.round((ThreadLocalRandom.current().nextFloat() * (100f - 1f) + 1f) * 10.0f) / 10.0f;
        Float alkalinephos = Math.round((ThreadLocalRandom.current().nextFloat() * (360f - 20f) + 20f) * 10.0f) / 10.0f;
        Float calcium = Math.round((ThreadLocalRandom.current().nextFloat() * (10f - 7.0f) + 7.0f) * 10.0f) / 10.0f;
        Float chloride = Math.round((ThreadLocalRandom.current().nextFloat() * (123f - 80f) + 80f) * 10.0f) / 10.0f;
        Float creatinine = Math.round((ThreadLocalRandom.current().nextFloat() * (8f - 0.3f) + 0.3f) * 10.0f) / 10.0f;
        Float bilirubindirect = Math.round((ThreadLocalRandom.current().nextFloat() * (0.3f - 0f) + 0f) * 10.0f) / 10.0f;
        Float glucose = Math.round((ThreadLocalRandom.current().nextFloat() * (400f - 48f) + 48f) * 10.0f) / 10.0f;
        Float lactate = Math.round((ThreadLocalRandom.current().nextFloat() * (5f - 0.5f) + 0.5f) * 10.0f) / 10.0f;
        Float magnesium = Math.round((ThreadLocalRandom.current().nextFloat() * (3.4f - 1.1f) + 1.1f) * 10.0f) / 10.0f;
        Float phosphate = Math.round((ThreadLocalRandom.current().nextFloat() * (8f - 1f) + 1f) * 10.0f) / 10.0f;
        Float potassium = Math.round((ThreadLocalRandom.current().nextFloat() * (10f - 2.5f) + 2.5f) * 10.0f) / 10.0f;
        Float bilirubintotal = Math.round((ThreadLocalRandom.current().nextFloat() * (1.5f - 0.2f) + 0.2f) * 10.0f) / 10.0f;
        Float troponini = Math.round((ThreadLocalRandom.current().nextFloat() * (5f - 0f) + 0f) * 10.0f) / 10.0f;
        Float hct = Math.round((ThreadLocalRandom.current().nextFloat() * (50f - 20f) + 20f) * 10.0f) / 10.0f;
        Float hgb = Math.round((ThreadLocalRandom.current().nextFloat() * (17f - 5f) + 5f) * 10.0f) / 10.0f;
        Float ptt = Math.round((ThreadLocalRandom.current().nextFloat() * (80f - 20f) + 20f) * 10.0f) / 10.0f;
        Float wbc = Math.round((ThreadLocalRandom.current().nextFloat() * (27f - 2f) + 2f) * 10.0f) / 10.0f;
        Float fibrinogen = Math.round((ThreadLocalRandom.current().nextFloat() * (650f - 100f) + 100f) * 10.0f) / 10.0f;
        Float platelets = Math.round((ThreadLocalRandom.current().nextFloat() * (700f - 20f) + 20f) * 10.0f) / 10.0f;
        vital.setPatientnum(patinum);
        vital.setO2sat(o2sat);
        vital.setTemp(temp);
        vital.setSbp(sbp);
        vital.setDbp(dbp);
        vital.setResp(resp);
        vital.setHr(hr);
        vital.setMap(map);
        vital.setEtco2(etco2);
        vital.setBaseexcess(baseexcess);
        vital.setHco3(hco3);
        vital.setFio2(fio2);
        vital.setPh(ph);
        vital.setPaco2(paco2);
        vital.setSao2(sao2);
        vital.setAst(ast);
        vital.setBun(bun);
        vital.setAlkalinephos(alkalinephos);
        vital.setCalcium(calcium);
        vital.setChloride(chloride);
        vital.setCreatinine(creatinine);
        vital.setBilirubindirect(bilirubindirect);
        vital.setGlucose(glucose);
        vital.setLactate(lactate);
        vital.setMagnesium(magnesium);
        vital.setPhosphate(phosphate);
        vital.setPotassium(potassium);
        vital.setBilirubintotal(bilirubintotal);
        vital.setTroponini(troponini);
        vital.setHct(hct);
        vital.setHgb(hgb);
        vital.setPtt(ptt);
        vital.setWbc(wbc);
        vital.setFibrinogen(fibrinogen);
        vital.setPlatelets(platelets);
        vital.setSepsisscore(0f);
        vital.setVitalnum(lastIndex+1);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        vital.setSepdate(formattedTime);
        vrepo.save(vital);
        System.out.println(vital);
        patientcontroller.getScore(patinum, lastIndex);
        
	}
	
	
}
