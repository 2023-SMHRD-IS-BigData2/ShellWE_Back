package com.smhrd.smart.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.smart.entity.Smart_Member;
import com.smhrd.smart.repository.MemberRepository;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping()
public class MemberController {

	@Autowired
	private MemberRepository repo;

	// 로그인 페이지
	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	// 로그인 시스템
	@PostMapping("/login")
	public ResponseEntity<JSONObject> loginSystem(String ID, String PW, HttpSession session, Model model) {
		/*
		 * 로그인 하는 의사 번호와 pw확인하고 로그인 성공시 정보를 session에 담아 view에 전달하고, 로그인 실패시 model에 '로그인
		 * 실패'를 담아서 전달하는 메서드 try catch문을 이용하여 예외처리로 입력정보의 id값이 int값이 아니더라도 model에
		 * loginError로 담아 로그인 실패를 전달하도록 하였음.
		 * 
		 */
		JSONObject json = new JSONObject();
		System.out.println("id : " + ID);
		try {
			String admin = "admin";
			Smart_Member loginMember = repo.findByidAndPw(ID, PW);
			// 로그인 성공시
			if (admin.equals(ID)) {
				repo.save(loginMember);
				session.setAttribute("LoginMember", loginMember);
				json.put("login", "admin");
				return new ResponseEntity<>(json, HttpStatus.OK);
			} else {

				if (loginMember != null) {

					loginMember.setLogintime(LocalDateTime.now());
					repo.save(loginMember);

					session.setAttribute("LoginMember", loginMember);
					System.out.println("로그인 성공");
					json.put("login", "main");
					return new ResponseEntity<>(json, HttpStatus.OK);
					// 로그인 실패시
				} else {
					System.out.println("로그인 실패");
					json.put("login", "login");
					return new ResponseEntity<>(json, HttpStatus.OK);
				}
			}
			// 입력값이 잘못되었을 경우
		} catch (Exception e) {
			json.put("login", "login");
			return new ResponseEntity<>(json, HttpStatus.OK);
		}
	}

	// 로그아웃, 로그아웃 시간을 입력하는 메소드
	@Configuration
	public class CorsConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("http://localhost:3000")
					.allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		/*
		 * 세션에서 로그인정보를 가져와 loginMember에 저장 loginMember가 비어있지 않다면 로그아웃 시간을 업데이트하고 저장
		 * session정보를 삭제하여 로그아웃함.
		 */
		Smart_Member loginMember = (Smart_Member) session.getAttribute("LoginMember");
		if (loginMember != null) {
			loginMember.setLogouttime(LocalDateTime.now());
			repo.save(loginMember);
			session.removeAttribute("LoginMember");
		}
		return "redirect:/login";
	}

	// 관리자가 의료진을 등록하는 메소드
	@PostMapping("/insertMember")
	public ResponseEntity<JSONObject> insertMember(@RequestBody Smart_Member member, RedirectAttributes redirect,
			Model model) {
		JSONObject responseJson = new JSONObject();
		/*
		 * 관리자가 의료진을 등록버튼을 누르면 jpa를 이용하여 DB에 의료진의 정보를 등록 Optional클래스의 isPresent()메소드
		 * 이용하여 아이디 중복확인 insertError에 등록 성공, 실패 여부를 담아 front에 전달
		 */
		try {
			Optional<Smart_Member> isEqualsID = repo.findById(member.getId());
			if (isEqualsID.isPresent()) {
				// 중복된 아이디
				responseJson.put("insert", "중복된 아이디");
				return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
			} else {
				// 등록 성공
				responseJson.put("insert", "등록 성공");
				repo.save(member);
				return new ResponseEntity<>(responseJson, HttpStatus.OK);
			}
		} catch (Exception e) {
			// 등록 실패
			responseJson.put("insert", "등록 실패");
			return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 의료진 정보를 불러와서 view에 전달하는 메소드
	// front와 연동시 변경 예정 login.jsp를 테스트 페이지로 사용
	@RequestMapping("/admin")
	public ResponseEntity<JSONObject> memberList() {
		JSONObject responseJson = new JSONObject();

		try {
			// 모든 의료진의 정보를 불러와 JSONArray에 담기
			List<Smart_Member> memberList = repo.findAll();
			JSONArray memberArray = new JSONArray();
			for (Smart_Member member : memberList) {
				JSONObject memberJson = new JSONObject();
				memberJson.put("membernum", member.getMembernum());
				memberJson.put("id", member.getId());
				memberJson.put("name", member.getName());
				memberJson.put("memberrank", member.getMemberrank());
				memberJson.put("tell", member.getTell());
				memberJson.put("date", member.getDate());
				memberJson.put("loginTime", member.getLogintime());
				memberJson.put("logoutTime", member.getLogouttime());

				memberArray.add(memberJson);
			}

			responseJson.put("members", memberArray);
			return new ResponseEntity<>(responseJson, HttpStatus.OK);
		} catch (Exception e) {
			// 에러 발생 시
			responseJson.put("error", "Failed to retrieve member list");
			return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateMember")
	public ResponseEntity<JSONObject> updateMember(@RequestBody Smart_Member updatedMember, HttpSession session) {
		JSONObject responseJson = new JSONObject();

		try {
			// 세션에서 현재 로그인된 멤버 정보 가져오기
			Smart_Member loginMember = (Smart_Member) session.getAttribute("LoginMember");

			// 업데이트된 정보로 로그인된 멤버 정보 업데이트
			loginMember.setName(updatedMember.getName());
			loginMember.setPw(updatedMember.getPw());
			loginMember.setMemberrank(updatedMember.getMemberrank());
			loginMember.setTell(updatedMember.getTell());

			// 업데이트된 정보를 저장
			repo.save(loginMember);

			// 업데이트 성공 응답
			responseJson.put("update", "업데이트 성공");
			return new ResponseEntity<>(responseJson, HttpStatus.OK);
		} catch (Exception e) {
			// 업데이트 실패 응답
			responseJson.put("update", "업데이트 실패");
			return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 의료진 정보를 삭제하는 메소드
	@DeleteMapping("/deleteMember")
	public ResponseEntity<JSONObject> deleteMember(@RequestParam("membernum") int membernum) {
		JSONObject responseJson = new JSONObject();
		String admin = "admin";
		try {
			// 삭제 로직 수행 (membernum을 사용하여 삭제)
			if(repo.findById(membernum).get().getId().equals(admin)) {
				responseJson.put("delete", "관리자 계정입니다.");
				return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				repo.deleteById(membernum);
				responseJson.put("delete", "삭제 성공");
			return new ResponseEntity<>(responseJson, HttpStatus.OK);			
			}
		} catch (Exception e) {
			// 삭제 실패
			responseJson.put("delete", "삭제 실패");
			return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 의료진 선택시 상세 정보를 가져오는 메서드ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 필요한가...? 생각해보니 필요 없을지도?
	public String memberDetail() {
		return "";
	}
}