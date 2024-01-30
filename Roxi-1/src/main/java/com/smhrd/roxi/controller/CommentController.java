package com.smhrd.roxi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smhrd.roxi.entity.Roxi_comment;
import com.smhrd.roxi.repository.CommentRepository;

@Controller
public class CommentController {
	
	@Autowired
	private CommentRepository crepo;
	
	@RequestMapping("/comment")
	public String comment(Model model, String patinum) {
		List<Roxi_comment> list = crepo.findBypatinum(Integer.parseInt(patinum));
		System.out.println(list);
		model.addAttribute("list", list);
		model.addAttribute("pNum", Integer.parseInt(patinum));
		return "comment";
	}
	
	@RequestMapping("/insertComment")
	public String insertComment(String insertComment, String patinum) {
		Roxi_comment r = new Roxi_comment();
		r.setPatinum(Integer.parseInt(patinum));
		r.setMembernum(1);
		r.setContents(insertComment);
		crepo.save(r);
		return "redirect:/comment?patinum="+patinum;
	}
	
}
