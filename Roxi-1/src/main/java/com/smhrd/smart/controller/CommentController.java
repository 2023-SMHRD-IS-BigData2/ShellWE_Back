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
	
	@Autowired
	private MemberRepository mrepo;
	
	
	@RequestMapping("/comment")
	public String comment(Model model, String patinum) {
		List<Smart_comment> list = crepo.findBypatinum(Integer.parseInt(patinum));
		System.out.println(list);
		model.addAttribute("list", list);
		model.addAttribute("pNum", Integer.parseInt(patinum));
		return "comment";
	}

	public JSONObject getcomment(int patinum) {
		JSONObject patientcomment = new JSONObject();
		List<Smart_comment> list = crepo.findBypatinum(patinum);
		patientcomment.put("comments", list);
		return patientcomment;
	}
	
	// 코멘트 입력
	public String insertcomment(String insertComment, String patinum, String id) {
		Smart_comment r = new Smart_comment();
		Smart_Member member = mrepo.findByid(id);
        if(member!=null) {
        	r.setMembernum(member.getMembernum()); //세션에 값이 존재할 경우 Membernum 추가
        	r.setMembername(member.getName()); // 세션에 값이 존재할 경우 Membername 추가
        	System.out.println(member);
        }else {
        	r.setMembernum(0);
        	r.setMembername("Dr. test");
        	System.out.println("session is null");
        }
		r.setPatinum(Integer.parseInt(patinum));
		r.setContents(insertComment);
		crepo.save(r);
		return "success";
	}
	
	
	
}
