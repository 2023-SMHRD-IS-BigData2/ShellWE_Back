package com.smhrd.roxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.roxi.entity.Roxi_Member;
import com.smhrd.roxi.repository.MemberRepository;

@Controller
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
	public String loginSystem(@RequestParam("membernum") String membernum, @RequestParam("pw") String pw,
			RedirectAttributes redirectAttributes, Model model) {
		/* 로그인 하는 의사 번호와 pw확인하고 로그인 성공시 정보를 redirectAttributes에 담아 view에 전달하고, 
		 * 로그인 실패시 model에 '로그인 실패'를 담아서 전달하는 메서드
		 * try catch문을 이용하여 예외처리로 입력정보의 id값이 int값이 아니더라도 model에 loginError로 담아 로그인 실패를 전달하도록 하였음.
		 * 
		*/
		try {
			Roxi_Member loginMember = repo.findByMembernumAndPw(Integer.parseInt(membernum), pw);
			// 로그인 성공시
			if (loginMember != null) {
				redirectAttributes.addFlashAttribute("LoginMember", loginMember);
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
			System.out.println("An error occurred: " + e.getMessage());
			model.addAttribute("loginError", "로그인 실패");
			return "login";
		}
	}

}
