package com.smhrd.smart.scheduled;
import java.util.concurrent.ThreadLocalRandom;

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
	
//	@org.springframework.scheduling.annotation.Scheduled(fixedRate = 30000)
//	public void fixedRate() throws NumberFormatException, IOException {
//		System.out.println("30초마다 실행됨");
//		List<Smart_Patient> list = repo.findAll();
//		for(int i=0; i<list.size();i++) {
//			System.out.println("patinum : "+list.get(i).getPatinum());
//			setvital(list.get(i).getPatinum());
//		}
//		
//	}
	
	//각 컬럼별로 랜덤 값 or null 값 저장되도록 설정
	//null 값 들어갈 확률 조정 가능
	public void setvital(int patinum) throws NumberFormatException, IOException {
		System.out.println(patinum+"번 환자 데이터 새롭게 입력되는 중");
		smart_vital1 big = vrepo.findFirstByOrderByVitalnumDesc();
		int lastIndex = big.getVitalnum();
		smart_vital1 vital = new smart_vital1();
        Float o2sat = randomNullableFloat(95f, 100f, 0);
        Float temp = randomNullableFloat(36.1f, 37.5f, 0);
        Integer sbp = randomNullableInteger(90, 130, 0);
        Integer dbp = randomNullableInteger(60, 80, 80);
        Integer resp = randomNullableInteger(5, 56, 80);
        Integer hr = randomNullableInteger(60, 110, 0);
        Float map = randomNullableFloat(70f, 100f, 0);
        Float etco2 = randomNullableFloat(35f, 45f, 70);
        Float baseexcess = randomNullableFloat(-2f, 2f, 80);
        Float hco3 = randomNullableFloat(22f, 36f, 80);
        Float fio2 = randomNullableFloat(0.02f, 1f, 80);
        Float ph = randomNullableFloat(7.35f, 7.5f, 80);
        Float paco2 = randomNullableFloat(35f, 45f, 80);
        Float sao2 = randomNullableFloat(95f, 100f, 80);
        Float ast = randomNullableFloat(10f, 40f, 80);
        Float bun = randomNullableFloat(7f, 20f, 80);
        Float alkalinephos = randomNullableFloat(20f, 140f, 80);
        Float calcium = randomNullableFloat(8f, 10f, 80);
        Float chloride = randomNullableFloat(96f, 107f, 80);
        Float creatinine = randomNullableFloat(0.6f, 1.3f, 80);
        Float bilirubindirect = randomNullableFloat(0f, 0.3f, 80);
        Float glucose = randomNullableFloat(70f, 100f, 80);
        Float lactate = randomNullableFloat(0.5f, 3f, 80);
        Float magnesium = randomNullableFloat(1.7f, 2.3f, 80);
        Float phosphate = randomNullableFloat(2f, 5f, 80);
        Float potassium = randomNullableFloat(3.5f, 5f, 80);
        Float bilirubintotal = randomNullableFloat(0.2f, 1.2f, 80);
        Float troponini = randomNullableFloat(0f, 0.04f, 80);
        Float hct = randomNullableFloat(40f, 50f, 80);
        Float hgb = randomNullableFloat(15f, 17f, 80);
        Float ptt = randomNullableFloat(20f, 40f, 80);
        Float wbc = randomNullableFloat(10f, 23f, 80);
        Float fibrinogen = randomNullableFloat(200f, 400f, 80);
        Float platelets = randomNullableFloat(200f, 500f, 80);
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
        patientcontroller.getScore(patinum, lastIndex);
        
	}
    private static Float randomNullableFloat(float min, float max, int nullProbability) {
        if (ThreadLocalRandom.current().nextInt(100) < nullProbability) {
            return null;
        }
        return Math.round((ThreadLocalRandom.current().nextFloat() * (max - min) + min) * 10.0f) / 10.0f;
    }

    private static Integer randomNullableInteger(int min, int max, int nullProbability) {
        if (ThreadLocalRandom.current().nextInt(100) < nullProbability) {
            return null;
        }
        return ThreadLocalRandom.current().nextInt(min, max);
    }
	
	
}
