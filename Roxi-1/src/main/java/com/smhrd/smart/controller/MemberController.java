package com.smhrd.smart.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.smart.entity.Smart_Member;
import com.smhrd.smart.repository.MemberRepository;

@Controller
public class MemberController {

	@Autowired
	private MemberRepository repo;
	
	// 로그인 페이지
	@GetMapping("/loginMember")
	public String loginForm() {
		return "login";
	}

	// 로그인 시스템
	@PostMapping("/loginMember")
	public String loginSystem(@RequestParam("id") String id, @RequestParam("pw") String pw,
			HttpSession session, Model model) {
		/* 로그인 하는 의사 번호와 pw확인하고 로그인 성공시 정보를 session에 담아 view에 전달하고, 
		 * 로그인 실패시 model에 '로그인 실패'를 담아서 전달하는 메서드
		 * try catch문을 이용하여 예외처리로 입력정보의 id값이 int값이 아니더라도 model에 loginError로 담아 로그인 실패를 전달하도록 하였음.
		 * 
		*/
		try {
			Smart_Member loginMember = repo.findByidAndPw(id, pw);
			// 로그인 성공시
			if (loginMember != null) {
				
				loginMember.setLogintime(LocalDateTime.now());
				repo.save(loginMember);
				
				session.setAttribute("LoginMember", loginMember);
				System.out.println("로그인 성공");
				return "redirect:/";
			// 로그인 실패시 
			} else {
				System.out.println("로그인 실패");
				model.addAttribute("loginError", "로그인 실패");
				return "login";
			}
			// 입력값이 잘못되었을 경우
		} catch (Exception e) {
			model.addAttribute("loginError", "로그인 실패");
			return "login";
		}
	}

	// 로그아웃, 로그아웃 시간을 입력하는 메소드
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		/* 세션에서 로그인정보를 가져와 loginMember에 저장
		   loginMember가 비어있지 않다면 로그아웃 시간을 업데이트하고 저장
		   session정보를 삭제하여 로그아웃함.
		*/
		Smart_Member loginMember = (Smart_Member) session.getAttribute("LoginMember");
		if(loginMember != null) {
			loginMember.setLogouttime(LocalDateTime.now());
			repo.save(loginMember);
			session.removeAttribute("LoginMember");
		}
		return "redirect:/login";
	}
	
	
	// 관리자가 의료진을 등록하는 메소드
	@PostMapping("/insertMember")
	public String insertMember(Smart_Member member, RedirectAttributes redirect, Model model) {
		/*
		  관리자가 의료진을 등록버튼을 누르면 jpa를 이용하여
		  DB에 의료진의 정보를 등록
		  Optional클래스의 isPresent()메소드 이용하여 아이디 중복확인 insertError에 
		  등록 성공, 실패 여부를 담아 front에 전달 
		*/
		Optional<Smart_Member> isEqualsID = repo.findById(member.getId());
		try {
			if(isEqualsID.isPresent()) {
				redirect.addFlashAttribute("insertError","중복된 아이디입니다");
				model.addAttribute("additionalData");
				return "redirect:login";
			}else {
				redirect.addFlashAttribute("insertError","등록 성공");
				model.addAttribute("additionalData");
				repo.save(member);
				return "redirect:login";							
			}
		} catch (Exception e) {
			redirect.addFlashAttribute("insertError", "등록실패");
			model.addAttribute("additionalData");
			return"redirect:login"; 
		}
	}
	
	// 의료진 정보를 불러와서 view에 전달하는 메소드
	// front와 연동시 변경 예정 login.jsp를 테스트 페이지로 사용 
	@RequestMapping("/login") 
	public String memberList(Model model) {
		/* memberList페이지를 요청하면 모든 의료진의 정보를
		   jpa를 이하여 불러와 model에 담아 view에 전달하는 메소드
		*/
		List<Smart_Member> memberList = repo.findAll();
		model.addAttribute("allMember",memberList);
		return "login";
	}
	
	@RequestMapping("/updataMember")
	public String updateMember(Smart_Member member, HttpSession session) {
		Smart_Member loginMember = (Smart_Member)session.getAttribute("LoginMember");
		loginMember.setName(member.getName());
		loginMember.setPw(member.getPw());
		loginMember.setMemberrank(member.getMemberrank());
		loginMember.setTell(member.getTell());
		repo.save(loginMember);
		
		return "redirect:login";
	}
	
	// 의료진 정보를 삭제하는 메소드
	@GetMapping("/deleteMember")
	public String deleteMember(@RequestParam("membernum") int membernum) {
		/*
		  view에서 삭제버튼을 누른 의료진의 번호를 전달받아 
		  jpa를 이용하여 의료진번호가 같은 의료진의 정보를 삭제하는 메소드 
		 */ 
		repo.deleteById(membernum);
		return "redirect:login";
	}
	
}
