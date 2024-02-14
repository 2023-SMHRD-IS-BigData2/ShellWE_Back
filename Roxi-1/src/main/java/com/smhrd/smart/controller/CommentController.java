package com.smhrd.smart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smhrd.smart.entity.Smart_Member;
import com.smhrd.smart.entity.Smart_comment;
import com.smhrd.smart.repository.CommentRepository;
import com.smhrd.smart.repository.MemberRepository;

@Controller
public class CommentController {
	
	
	
	@Autowired
	private CommentRepository crepo;
	
	
	@RequestMapping("/comment")
	public String comment(Model model, String patinum) {
		List<Smart_comment> list = crepo.findBypatinum(Integer.parseInt(patinum));
		System.out.println(list);
		model.addAttribute("list", list);
		model.addAttribute("pNum", Integer.parseInt(patinum));
		return "comment";
	}
	
//	@RequestMapping("/insertComment")
//	public String insertComment(String insertComment, String patinum) {
//		Smart_comment r = new Smart_comment();
//		r.setPatinum(Integer.parseInt(patinum));
//		r.setMembernum(1);
//		r.setContents(insertComment);
//		crepo.save(r);
//		return "redirect:/comment?patinum="+patinum;
//	}

	public JSONObject getcomment(int patinum) {
		JSONObject patientcomment = new JSONObject();
		List<Smart_comment> list = crepo.findBypatinum(patinum);
		patientcomment.put("comments", list);
		return patientcomment;
	}
	
	public String insertcomment(HttpServletRequest request, String insertComment, String patinum) {
		Smart_comment r = new Smart_comment();
        HttpSession session = request.getSession();
        
        // 세션에서 원하는 값을 가져옵니다.
        Smart_Member member = (Smart_Member) session.getAttribute("LoginMember");
        if(member!=null) {
        	r.setMembernum(member.getMembernum()); //세션에 값이 존재할 경우 Membernum 추가
        	r.setMembername(member.getName()); // 세션에 값이 존재할 경우 Membername 추가
        	System.out.println(member);
        }else {
        	r.setMembernum(0);
        	System.out.println("session is null");
        }
		r.setPatinum(Integer.parseInt(patinum));
		r.setContents(insertComment);
		crepo.save(r);
		return "success";
	}
	
}
