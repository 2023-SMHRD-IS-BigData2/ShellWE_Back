package com.smhrd.smart.controller;

import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.smart.entity.Smart_sepsiss;
import com.smhrd.smart.repository.CriteriaSepsissRepository;

@RestController // 리액트로 데이터만 전달하는 컨트롤러 어노테이션
public class CriteriaSepsissController {

   @Autowired
   private CriteriaSepsissRepository csrepo;

   // front로 데이터 보내는 메소드
   @RequestMapping("/getSep")
   public JSONObject getSep() {
      JSONObject json = new JSONObject();
      List<Smart_sepsiss> list = csrepo.findAll();
      json.put("sepscore", list.get(0).getSepsissnum());
      return json;
   }
   
   // DB로 정보를 업데이트
   @RequestMapping("/sepsissscoer") // sepsiss 정보를 업데이트하는 메소드
   public String CriteriaSepsiss(String sepsiss) {
      List<Smart_sepsiss> list = csrepo.findAll();
      System.out.println(list);
      Smart_sepsiss sep = list.get(0); // 업데이트 전
      sep.setSepsissnum(Integer.parseInt(sepsiss));
      csrepo.save(sep);
      return "";
   }

   //** 패혈증 기준에 의해 스크리닝으로 변환
   public String sepsissscoer(int sepsissscoer) {
      String sepsissString = "";
      List<Smart_sepsiss> sepsiss = csrepo.findAll();
      if (sepsiss.get(0).getSepsiss() >= sepsissscoer) {
         sepsissString = "None";
         System.out.println(sepsissString);
         return sepsissString;
      } else {
         sepsissString = "Screening";
         System.out.println(sepsissString);
         return sepsissString;
      }
   }

}