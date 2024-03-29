package com.smhrd.smart.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.parser.diagnose.RangeUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.smart.entity.Smart_Patient;
import com.smhrd.smart.entity.Smart_comment;
import com.smhrd.smart.entity.Smart_sepsiss;
import com.smhrd.smart.entity.smart_vital1;
import com.smhrd.smart.flask.flask;
import com.smhrd.smart.repository.CommentRepository;
import com.smhrd.smart.repository.CriteriaSepsissRepository;
import com.smhrd.smart.repository.PatientRepository;
import com.smhrd.smart.repository.SepsissRepository;
import com.smhrd.smart.repository.VitalRepository;

import aj.org.objectweb.asm.Type;

@Controller
public class PatientController {

	
	
	@Autowired
	private PatientRepository repo;

	@Autowired
	private SepsissRepository srepo;

	@Autowired
	private CommentRepository crepo;
	
	@Autowired
	private VitalRepository vrepo;
	
	@Autowired
	private CriteriaSepsissRepository csrepo;
	
	@Autowired
	private flask fsk;

	@RequestMapping("/")
	public String Main(Model model) {
		List<Smart_Patient> list = repo.findAll();
		model.addAttribute("list", list);
		return "main";
	}

	// 환자 정보 리턴 메소드
	// 전체 환자, 위험 환자, 오늘 발생환자 수 출력
	public JSONObject getPatient() {
		JSONObject num = new JSONObject();
		List<Smart_Patient> list = repo.findAll();
		List<Smart_Patient> listScreening = repo.findBysepsisslevel("Screening");
		JSONObject patientList = new JSONObject();
		List<Smart_sepsiss> li = csrepo.findAll();
		int cnt = 0;
		System.out.println(LocalDate.now());
		for(int i=0; i<listScreening.size(); i++) {
			if(list.get(i).getSepstartdate()!=null&&list.get(i).getSepstartdate().equals(LocalDate.now())) {
				cnt++;
			}
		}
		
		patientList.put("patientList",list);
		patientList.put("Allpatient", list.size());
		patientList.put("Screening", listScreening.size());
		patientList.put("todayScreening", cnt);
		patientList.put("CriteriaSep", li.get(0).getSepsiss());
		return patientList;

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
		List<smart_vital1> list = srepo.findBypatientnum(Integer.parseInt(patinum));
		model.addAttribute("patient", patient.get());
		model.addAttribute("list", list);
		return "detail";
	}

	@GetMapping("/detailInsert")
	public String detailInsert(smart_vital1 sepsiss) {
		srepo.save(sepsiss);
		return "redirect:/detail?patinum=" + sepsiss.getPatientnum();
	}

	@RequestMapping("/dengerList")
	public String dengerList(Model model) {
		List<Smart_Patient> list = repo.findBysepsisslevel("Screening");
		model.addAttribute("list", list);
		return "main";
	}

	/*
	 * Detail 페이지에서 특정 날짜 선택해서 정보 추출 함수 input : 날짜, 환자 번호 특정 날짜와 환자 번호로 데이터베이스에서 값
	 * 받아와 List 형태로 저장
	 */
	@RequestMapping("/selectDate")
	public String selectDate(Model model, String patinum, String date) {
		System.out.println(date);
		LocalDate localDate = LocalDate.parse(date);
		Date d = Date.valueOf(localDate);
		int pNum = Integer.parseInt(patinum);
		List<smart_vital1> list = srepo.findBypatientnumAndSepdate(pNum, d);
		Optional<Smart_Patient> patient = repo.findById(pNum);
		model.addAttribute("patient", patient.get());
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
		model.addAttribute("list", list);
		return "main";
	}

	// 상태 변화 실행함수
	// 상태 변화 시 환자 테이블 status 수정
	// 수정 된 정보 comment 테이블에 입력
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
	
	
	//환자 상태 변화
	//상태 변화 시 comment에 추가
	public String changeStatus(String sepsisslevel, String patinum) {
		System.out.println(sepsisslevel);
		System.out.println(patinum);
		Smart_Patient patient = repo.findById(Integer.parseInt(patinum)).get();
		System.out.println(patient);
		String pastStatus = patient.getSepsisslevel();
		
		if (patient != null) {
			patient.setSepsisslevel(sepsisslevel);
			repo.save(patient);
			Smart_comment r = new Smart_comment();
			r.setPatinum(Integer.parseInt(patinum));
			r.setMembernum(1);
			r.setContents(pastStatus + " -> " + sepsisslevel);
			System.out.println(pastStatus + " -> " + sepsisslevel);
			crepo.save(r);
		}
		return "change!";
	}

	@RequestMapping("/delPatient")
	public String delPatient(String patinum) {
		repo.deleteById(Integer.parseInt(patinum));

		return "redirect:/";
	}

	// flask 서버에 값 넘기기
	// 환자 대표 Sepsisscore 값 설정 함수
	public void SendAllData(int patinum) throws NumberFormatException, IOException {
		List<HashMap<String, Object>> list = new ArrayList<>(); //모델에 넘겨줄 리스트 생성
		List<smart_vital1> plist = srepo.findBypatientnum(patinum);//해당 환자의 모든 vital 값 리스트 가져오기
		Smart_Patient patient = repo.findById(patinum).get(); // 환자 age, gender 가져오기 위한 객체 생성
		for (int i = 0; i < plist.size(); i++) {// 모델에 넘겨줄 리스트에 값 저장하는 반복문
			HashMap<String, Object> hash = new HashMap<>(); // list 내부에 hash 형태로 저장
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
			list.add(hash); //해쉬에 값 저장했으면 list에 하나 추가
		}
		String sepscore = fsk.flask(list, patinum); //flask 연동 메소드 호출 후 받아온 값 String 형태로 저장
		setSepsisScore(Integer.parseInt(sepscore), patinum);  //sepsisscore 저장 하는 함수 호출
	}

	// JSONArrya 형태로 리턴하는 함수(react와 연동시 사용)
	public JSONArray getDetailList(String patinum) {
		JSONArray dataList = new JSONArray(); // JSONArray 객체
		List<HashMap<String, Object>> list = new ArrayList<>();// hashmap 데이터 타입의 list
		List<smart_vital1> plist = srepo.findBypatientnum(Integer.parseInt(patinum));
		Smart_Patient patient = repo.findById(Integer.parseInt(patinum)).get();
		// list에 hashmap 형태로 넣기
		for (int i = 0; i < plist.size(); i++) {
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
			hash.put("time", plist.get(i).getSepdate());
			hash.put("smart", plist.get(i).getSepsisscore());
			list.add(hash);// 저장된 hashmap을 list에 저장
		}
		dataList.add(list);// JSONArray 객체에 list 저장
		return dataList; // 객체 리턴
	}

	public JSONArray getvital(String patinum) {
		JSONArray vitalList = new JSONArray();
		List<smart_vital1> list = srepo.findBypatientnum(Integer.parseInt(patinum));
		vitalList.add(list);
		return vitalList;
	}

	// 일일 전체 데이터 에서 이상치 존재 유무 리턴 함수
	// 날짜와 환자 데이터가 입력되면 해당 환자의 해당 날짜 데이터 가져와 정상 범위 확인
	public JSONObject getDengerList(int patinum, String date) {
		JSONObject dengerlist = new JSONObject();
		LocalDate localDate = LocalDate.parse(date);
		Date d = Date.valueOf(localDate);
		List<smart_vital1> list = srepo.findBypatientnumAndSepdate(patinum, d);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		HashMap<String, Boolean> dengercolumn = new HashMap<>();
		HashMap<String, String> updown = new HashMap<>();
		Integer v1;
		Float v2;
		String column;
		for (int i = 0; i < list.size(); i++) {

			v1 = list.get(i).getHr();
			if (v1 == null) {
				dengercolumn.put("hr", true);
			} else {
				if (!dengercolumn.containsKey("hr")) {
					if (v1 < 100 && v1 > 60) {
						dengercolumn.put("hr", true);
					} else {
						dengercolumn.put("hr", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("hr") == true) && !(v1 < 100 && v1 > 60)) {// 만약 hr 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("hr", false);
					}
				}
			}

			v2 = list.get(i).getO2sat();
			if (v2 == null) {
				dengercolumn.put("o2sat", true);
			} else {
				if (!dengercolumn.containsKey("o2sat")) {
					if (v2 <= 100 && v2 >= 95) {
						dengercolumn.put("o2sat", true);
					} else {
						dengercolumn.put("o2sat", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("o2sat") == true) && !(v2 <= 100 && v2 >= 95)) {// 해당컬럼 값이 true이고, 정상 범위를
																							// 벗어난다면
						dengercolumn.put("o2sat", false);
					}
				}
			}

			v2 = list.get(i).getTemp();
			if (v2 == null) {
				dengercolumn.put("temp", true);
			} else {
				if (!dengercolumn.containsKey("temp")) {
					if (v2 <= 37.2 && v2 >= 36.1) {
						dengercolumn.put("temp", true);
					} else {
						dengercolumn.put("temp", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("temp") == true) && !(v2 <= 37.2 && v2 >= 36.1)) {// 해당컬럼 값이 true이고, 정상 범위를
																							// 벗어난다면
						dengercolumn.put("temp", false);
					}
				}
			}

			v1 = list.get(i).getSbp();
			if (v1 == null) {
				dengercolumn.put("sbp", true);
			} else {
				if (!dengercolumn.containsKey("sbp")) {
					if (v1 <= 120 && v1 >= 90) {
						dengercolumn.put("sbp", true);
					} else {
						dengercolumn.put("sbp", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("sbp") == true) && !(v1 <= 120 && v1 >= 90)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("sbp", false);
					}
				}
			}

			v1 = list.get(i).getDbp();
			if (v1 == null) {
				dengercolumn.put("dbp", true);
			} else {
				if (!dengercolumn.containsKey("dbp")) {
					if (v1 <= 80 && v1 >= 60) {
						dengercolumn.put("dbp", true);
					} else {
						dengercolumn.put("dbp", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("dbp") == true) && !(v1 <= 100 && v1 >= 70)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("dbp", false);
					}
				}
			}

			v1 = list.get(i).getDbp();
			if (v1 == null) {
				dengercolumn.put("resp", true);
			} else {
				if (!dengercolumn.containsKey("resp")) {
					if (v1 <= 20 && v1 >= 12) {
						dengercolumn.put("resp", true);
					} else {
						dengercolumn.put("resp", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("resp") == true) && !(v1 <= 20 && v1 >= 12)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("resp", false);
					}
				}
			}

			v2 = list.get(i).getMap();
			System.out.println("v2 : "+v2);
			if (v2 == null) {
				dengercolumn.put("map", false);
			} else {
				if (!dengercolumn.containsKey("map")) {
					if (v2 <= 100 && v2 >= 70) {
						dengercolumn.put("map", true);
					} else {
						dengercolumn.put("map", false);
					}
				} else {// 값이 비어있지 않고
					System.out.println("dengermap : "+dengercolumn.get("map"));
					if ((dengercolumn.get("map") == true) && !(v2 <= 100 && v2 >= 70)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("map", false);
					}
				}
			}

			v2 = list.get(i).getEtco2();
			if (v2 == null) {
				dengercolumn.put("etco2", true);
			} else {
				if (!dengercolumn.containsKey("etco2")) {
					if (v2 <= 45 && v2 >= 35) {
						dengercolumn.put("etco2", true);
					} else {
						dengercolumn.put("etco2", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("etco2") == true) && !(v2 <= 45 && v2 >= 35)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("etco2", false);
					}
				}
			}

			v2 = list.get(i).getBaseexcess();
			if (v2 == null) {
				dengercolumn.put("baseexcess", true);
			} else {
				if (!dengercolumn.containsKey("baseexcess")) {
					if (v2 <= 2 && v2 >= -2) {
						dengercolumn.put("baseexcess", true);
					} else {
						dengercolumn.put("baseexcess", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("baseexcess") == true) && !(v2 <= 2 && v2 >= -2)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("baseexcess", false);
					}
				}
			}

			v2 = list.get(i).getHco3();
			if (v2 == null) {
				dengercolumn.put("hco3", true);
			} else {
				if (!dengercolumn.containsKey("hco3")) {
					if (v2 <= 26 && v2 >= 22) {
						dengercolumn.put("hco3", true);
					} else {
						dengercolumn.put("hco3", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("hco3") == true) && !(v2 <= 26 && v2 >= 22)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("hco3", false);
					}
				}
			}

			v2 = list.get(i).getFio2();
			if (v2 == null) {
				dengercolumn.put("fio2", true);
			} else {
				if (!dengercolumn.containsKey("fio2")) {
					if (v2 <= 100 && v2 >= 21) {
						dengercolumn.put("fio2", true);
					} else {
						dengercolumn.put("fio2", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("fio2") == true) && !(v2 <= 100 && v2 >= 21)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("fio2", false);
					}
				}
			}

			v2 = list.get(i).getPh();
			if (v2 == null) {
				dengercolumn.put("ph", true);
			} else {
				if (!dengercolumn.containsKey("ph")) {
					if (v2 <= 7.35 && v2 >= 7.45) {
						dengercolumn.put("ph", true);
					} else {
						dengercolumn.put("ph", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("ph") == true) && !(v2 <= 7.35 && v2 >= 7.45)) {// 해당컬럼 값이 true이고, 정상 범위를
																							// 벗어난다면
						dengercolumn.put("ph", false);
					}
				}
			}

			v2 = list.get(i).getPaco2();
			if (v2 == null) {
				dengercolumn.put("paco2", true);
			} else {
				if (!dengercolumn.containsKey("paco2")) {
					if (v2 <= 45 && v2 >= 35) {
						dengercolumn.put("paco2", true);
					} else {
						dengercolumn.put("paco2", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("paco2") == true) && !(v2 <= 45 && v2 >= 35)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("paco2", false);
					}
				}
			}

			v2 = list.get(i).getSao2();
			if (v2 == null) {
				dengercolumn.put("sao2", true);
			} else {
				if (!dengercolumn.containsKey("sao2")) {
					if (v2 <= 95 && v2 >= 100) {
						dengercolumn.put("sao2", true);
					} else {
						dengercolumn.put("sao2", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("sao2") == true) && !(v2 <= 95 && v2 >= 100)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("sao2", false);
					}
				}
			}

			v2 = list.get(i).getAst();
			if (v2 == null) {
				dengercolumn.put("ast", true);
			} else {
				if (!dengercolumn.containsKey("ast")) {
					if (v2 <= 40 && v2 >= 10) {
						dengercolumn.put("ast", true);
					} else {
						dengercolumn.put("ast", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("ast") == true) && !(v2 <= 40 && v2 >= 10)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("ast", false);
					}
				}
			}

			v2 = list.get(i).getBun();
			if (v2 == null) {
				dengercolumn.put("bun", true);
			} else {
				if (!dengercolumn.containsKey("bun")) {
					if (v2 <= 70 && v2 >= 7) {
						dengercolumn.put("bun", true);
					} else {
						dengercolumn.put("bun", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("bun") == true) && !(v2 <= 70 && v2 >= 7)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("bun", false);
					}
				}
			}

			v2 = list.get(i).getAlkalinephos();
			if (v2 == null) {
				dengercolumn.put("alkalinephos", true);
			} else {
				if (!dengercolumn.containsKey("alkalinephos")) {
					if (v2 <= 140 && v2 >= 20) {
						dengercolumn.put("alkalinephos", true);
					} else {
						dengercolumn.put("alkalinephos", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("alkalinephos") == true) && !(v2 <= 140 && v2 >= 20)) {// 해당컬럼 값이 true이고, 정상
																									// 범위를 벗어난다면
						dengercolumn.put("alkalinephos", false);
					}
				}
			}

			v2 = list.get(i).getCalcium();
			if (v2 == null) {
				dengercolumn.put("calcium", true);
			} else {
				if (!dengercolumn.containsKey("calcium")) {
					if (v2 <= 10.5 && v2 >= 8.5) {
						dengercolumn.put("calcium", true);
					} else {
						dengercolumn.put("calcium", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("calcium") == true) && !(v2 <= 10.5 && v2 >= 8.5)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("calcium", false);
					}
				}
			}

			v2 = list.get(i).getChloride();
			if (v2 == null) {
				dengercolumn.put("chloride", true);
			} else {
				if (!dengercolumn.containsKey("chloride")) {
					if (v2 <= 106 && v2 >= 96) {
						dengercolumn.put("chloride", true);
					} else {
						dengercolumn.put("chloride", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("chloride") == true) && !(v2 <= 106 && v2 >= 96)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("chloride", false);
					}
				}
			}

			v2 = list.get(i).getCreatinine();
			if (v2 == null) {
				dengercolumn.put("creatinine", true);
			} else {
				if (!dengercolumn.containsKey("creatinine")) {
					if (v2 <= 1.3 && v2 >= 0.6) {
						dengercolumn.put("creatinine", true);
					} else {
						dengercolumn.put("creatinine", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("creatinine") == true) && !(v2 <= 1.3 && v2 >= 0.6)) {// 해당컬럼 값이 true이고, 정상
																								// 범위를 벗어난다면
						dengercolumn.put("creatinine", false);
					}
				}
			}

			v2 = list.get(i).getBilirubindirect();
			if (v2 == null) {
				dengercolumn.put("bilirubindirect", true);
			} else {
				if (!dengercolumn.containsKey("bilirubindirect")) {
					if (v2 <= 0.3 && v2 >= 0) {
						dengercolumn.put("bilirubindirect", true);
					} else {
						dengercolumn.put("bilirubindirect", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("bilirubindirect") == true) && !(v2 <= 0.3 && v2 >= 0)) {// 해당컬럼 값이 true이고, 정상
																									// 범위를 벗어난다면
						dengercolumn.put("bilirubindirect", false);
					}
				}
			}

			v2 = list.get(i).getGlucose();
			if (v2 == null) {
				dengercolumn.put("glucose", true);
			} else {
				if (!dengercolumn.containsKey("glucose")) {
					if (v2 <= 100 && v2 >= 70) {
						dengercolumn.put("glucose", true);
					} else {
						dengercolumn.put("glucose", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("glucose") == true) && !(v2 <= 100 && v2 >= 70)) {// 해당컬럼 값이 true이고, 정상 범위를
																							// 벗어난다면
						dengercolumn.put("glucose", false);
					}
				}
			}

			v2 = list.get(i).getLactate();
			if (v2 == null) {
				dengercolumn.put("lactate", true);
			} else {
				if (!dengercolumn.containsKey("lactate")) {
					if (v2 <= 2.2 && v2 >= 0.5) {
						dengercolumn.put("lactate", true);
					} else {
						dengercolumn.put("lactate", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("lactate") == true) && !(v2 <= 2.2 && v2 >= 0.5)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("lactate", false);
					}
				}
			}

			v2 = list.get(i).getMagnesium();
			if (v2 == null) {
				dengercolumn.put("magnesium", true);
			} else {
				if (!dengercolumn.containsKey("magnesium")) {
					if (v2 <= 2.3 && v2 >= 1.7) {
						dengercolumn.put("magnesium", true);
					} else {
						dengercolumn.put("magnesium", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("magnesium") == true) && !(v2 <= 2.3 && v2 >= 1.7)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("magnesium", false);
					}
				}
			}

			v2 = list.get(i).getPhosphate();
			if (v2 == null) {
				dengercolumn.put("phosphate", true);
			} else {
				if (!dengercolumn.containsKey("phosphate")) {
					if (v2 <= 4.5 && v2 >= 2.5) {
						dengercolumn.put("phosphate", true);
					} else {
						dengercolumn.put("phosphate", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("phosphate") == true) && !(v2 <= 4.5 && v2 >= 2.5)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("phosphate", false);
					}
				}
			}

			v2 = list.get(i).getPotassium();
			if (v2 == null) {
				dengercolumn.put("potassium", true);
			} else {
				if (!dengercolumn.containsKey("potassium")) {
					if (v2 <= 5 && v2 >= 3.5) {
						dengercolumn.put("potassium", true);
					} else {
						dengercolumn.put("potassium", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("potassium") == true) && !(v2 <= 5 && v2 >= 3.5)) {// 해당컬럼 값이 true이고, 정상 범위를
																								// 벗어난다면
						dengercolumn.put("potassium", false);
					}
				}
			}

			v2 = list.get(i).getBilirubintotal();
			if (v2 == null) {
				dengercolumn.put("bilirubintotal", true);
			} else {
				if (!dengercolumn.containsKey("bilirubintotal")) {
					if (v2 <= 1.2 && v2 >= 0.2) {
						dengercolumn.put("bilirubintotal", true);
					} else {
						dengercolumn.put("bilirubintotal", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("bilirubintotal") == true) && !(v2 <= 5 && v2 >= 3.5)) {// 해당컬럼 값이 true이고, 정상
																									// 범위를 벗어난다면
						dengercolumn.put("bilirubintotal", false);
					}
				}
			}

			v2 = list.get(i).getTroponini();
			if (v2 == null) {
				dengercolumn.put("troponini", true);
			} else {
				if (!dengercolumn.containsKey("troponini")) {
					if (v2 <= 0.04) {
						dengercolumn.put("troponini", true);
					} else {
						dengercolumn.put("troponini", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("troponini") == true) && !(v2 <= 0.04)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("troponini", false);
					}
				}
			}

			v2 = list.get(i).getHct();
			if (v2 == null) {
				dengercolumn.put("hct", true);
			} else {
				if (!dengercolumn.containsKey("hct")) {
					if (v2 <= 53 && v2 >= 36) {
						dengercolumn.put("hct", true);
					} else {
						dengercolumn.put("hct", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("hct") == true) && !(v2 <= 53 && v2 >= 36)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("hct", false);
					}
				}
			}

			v2 = list.get(i).getHgb();
			if (v2 == null) {
				dengercolumn.put("hgb", true);
			} else {
				if (!dengercolumn.containsKey("hgb")) {
					if (v2 <= 18 && v2 >= 12) {
						dengercolumn.put("hgb", true);
					} else {
						dengercolumn.put("hgb", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("hgb") == true) && !(v2 <= 18 && v2 >= 12)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("hgb", false);
					}
				}
			}

			v2 = list.get(i).getPtt();
			if (v2 == null) {
				dengercolumn.put("ptt", true);
			} else {
				if (!dengercolumn.containsKey("ptt")) {
					if (v2 <= 35 && v2 >= 25) {
						dengercolumn.put("ptt", true);
					} else {
						dengercolumn.put("ptt", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("ptt") == true) && !(v2 <= 35 && v2 >= 25)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("ptt", false);
					}
				}
			}

			v2 = list.get(i).getWbc();
			if (v2 == null) {
				dengercolumn.put("wbc", true);
			} else {
				if (!dengercolumn.containsKey("wbc")) {
					if (v2 <= 4000 && v2 >= 11000) {
						dengercolumn.put("wbc", true);
					} else {
						dengercolumn.put("wbc", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("wbc") == true) && !(v2 <= 35 && v2 >= 25)) {// 해당컬럼 값이 true이고, 정상 범위를 벗어난다면
						dengercolumn.put("wbc", false);
					}
				}
			}

			v2 = list.get(i).getFibrinogen();
			if (v2 == null) {
				dengercolumn.put("fibrinogen", true);
			} else {
				if (!dengercolumn.containsKey("fibrinogen")) {
					if (v2 <= 400 && v2 >= 200) {
						dengercolumn.put("fibrinogen", true);
					} else {
						dengercolumn.put("fibrinogen", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("fibrinogen") == true) && !(v2 <= 400 && v2 >= 200)) {// 해당컬럼 값이 true이고, 정상
																								// 범위를 벗어난다면
						dengercolumn.put("fibrinogen", false);
					}
				}
			}

			v2 = list.get(i).getPlatelets();
			if (v2 == null) {
				dengercolumn.put("platelets", true);
			} else {
				if (!dengercolumn.containsKey("platelets")) {
					if (v2 <= 150000 && v2 >= 450000) {
						dengercolumn.put("platelets", true);
					} else {
						dengercolumn.put("platelets", false);
					}
				} else {// 값이 비어있지 않고
					if ((dengercolumn.get("platelets") == true) && !(v2 <= 150000 && v2 >= 450000)) {// 해당컬럼 값이 true이고,
																										// 정상 범위를 벗어난다면
						dengercolumn.put("platelets", false);
					}
				}
			}

		}
		updown.put("hr", "60-100");
		updown.put("o2sat", "95-100");
		updown.put("temp", "36.1-37.2");
		updown.put("sbp", "90-120");
		updown.put("map", "70-100");
		updown.put("dbp", "60-80");
		updown.put("resp", "12-20");
		updown.put("etco2", "35-45");
		updown.put("baseexcess", "-2-+2");
		updown.put("hco3", "22-26");
		updown.put("fio2", "21-100");
		updown.put("ph", "7.35-7.45");
		updown.put("paco2", "35-45");
		updown.put("sao2", "95-100");
		updown.put("ast", "10-40");
		updown.put("bun", "7-20");
		updown.put("alkalinephos", "20-120");
		updown.put("calcium", "8.5-10.5");
		updown.put("chloride", "96-106");
		updown.put("creatinine", "0.6-1.3");
		updown.put("bilirubin_direct", "0-0.3");
		updown.put("glucose", "70-100");
		updown.put("lactate", "0.5-2.2");
		updown.put("magnesium", "1.7-2.3");
		updown.put("phosphate", "2.5-4.5");
		updown.put("pilirubin_total", "0.2-1.2");
		updown.put("troponinl", "0-0.004");
		updown.put("hct", "36-53");
		updown.put("hgb", "13-18");
		updown.put("ptt", "25-35");
		updown.put("wbc", "13-20");
		updown.put("fibrinogen", "220-350");
		updown.put("platelets", "220-480");
		dengerlist.put("dengercolumn", dengercolumn);
		dengerlist.put("minmax", updown);
		return dengerlist;
	}
	
	
	//환자 병동 변경
	public void updateWard(int patinum, String ward) {
		Smart_Patient patient = repo.findById(patinum).get();
		patient.setWard(ward);
		repo.save(patient);
	}
	
	//환자 전공의 변경
	public void updatePhysician(int patinum, String physician) {
		Smart_Patient patient = repo.findById(patinum).get();
		patient.setPhysician(physician);
		repo.save(patient);
	}
	
	public void setSepsisScore(int sepsisscore, int patinum) {
		Smart_Patient patient = repo.findById(patinum).get();
		patient.setSepsisscore(sepsisscore); //값 업데이트
		repo.save(patient);
		System.out.println(patient.getSepsisscore());
	}
	
	//환자 세부 vital 별 sepsisscore 설정 함수
	public int getScore(int patinum, int vitalnum) throws IOException {
		List<HashMap<String, Object>> list = new ArrayList<>();//모델에 넘겨줄 리스트 생성
		List<smart_vital1> plist = srepo.findByPatientnumAndVitalnumLessThan(patinum, vitalnum);// 해당 환자의 해당 vitalnum 보다 작은 row 에 해당하는 list 받아오기
		Smart_Patient patient = repo.findById(patinum).get();
		for (int i = 0; i < plist.size(); i++) {
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
		int result = fsk.flask_1(list, patinum, vitalnum);//모델 연동 함수 호출
		return result;
	}

	// 전체 환자 대표 sepsisscore 점수 변경
	@RequestMapping("/setAllSepsisscore")
	public String setAllSepsisscore() throws NumberFormatException, IOException {
		List<Smart_Patient> list = repo.findAll(); //전체 환자 리스트 가져오기
		for(int i=0; i<list.size(); i++) {
			SendAllData(list.get(i).getPatinum()); //전체 환자 하나씩 SendAllData 함수에 집어넣기
		}
		return "redirect:/";
	}
	
	//전체환자 세부 sepsisscore 점수 변경
	@RequestMapping("/setAllVitalSepsisscore")
	public String setAllVitalSepsisscore() throws IOException {
		List<Smart_Patient> list = repo.findAll(); //전체 환자 리스트 가져오기
		for(int i=0; i<list.size(); i++) { //전체 환자만큼 반복
			List<smart_vital1> li = vrepo.findByPatientnum(list.get(i).getPatinum()); //매 반복만큼의 환자 하나 당 전체 vital 리스트 가져오기
			for(int j=1; j<li.size(); j++) {//받아온 vital 만큼 반복
				int vitalnum = li.get(j).getVitalnum();
				getScore(list.get(i).getPatinum(), vitalnum);//세부 vital 하나당 sepsisscore 예측 함수 호출
			}
		}
		
		return "redirect:/";
	}

	public int getRandom() {
		Random rd = new Random();
		int rand = rd.nextInt(10)+1;
		return rand;
	}
}