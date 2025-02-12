package com.one.project.users.controller;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.one.project.users.dto.UsersDto;
import com.one.project.users.service.UsersService;

@Controller
public class UsersController {
	
	@Autowired
	private UsersService service;
	
	//로그아웃 요청 처리
	@RequestMapping("/users/logout")
	public String logout(HttpSession session) {
		//세션에서 id 라는 키값으로 저장된 값 삭제 
		session.removeAttribute("id");
		return "users/logout";
	}
	
	//회원 가입 요청 처리
	@RequestMapping(value = "/users/signup_form", method = RequestMethod.GET)
	public String signupForm() {
		return "users/signup_form";
	}
	
	//아이디 중복 확인을 해서 json 문자열을 리턴해주는 메소드
	@RequestMapping("/users/checkid")
	@ResponseBody
	public Map<String, Object> checkid(@RequestParam String inputId){
		//UsersService 가 리턴해주는 Map 을 리턴해서 json 문자열을 응답한다. 
		return service.isExistId(inputId);
	}	
	
	//회원 가입 요청 처리 ( post 방식 요청은 요청 method 를 명시하는것이 좋다.
	@RequestMapping(value = "/users/sign", method = RequestMethod.POST)
	public ModelAndView signup(ModelAndView mView, UsersDto dto) {
		
		service.addUser(dto);
		
		mView.setViewName("users/sign");
		return mView;
	}
	
	//회원 정보 페이지 처리
	
	@RequestMapping("/users/info")
	public ModelAndView authInfo(HttpSession session, ModelAndView mView, 
			HttpServletRequest request) {
		
		service.getInfo(session, mView);
		
		mView.setViewName("users/info");
		return mView;
	}
	
	//회원 탈퇴 요청 처리

	//회원 정보 수정 폼 요청 처리
	
	//회원 정보 수정 폼 처리
	
	//로그인 요청 처리
	@RequestMapping("/users/login")
	public ModelAndView login(ModelAndView mView, UsersDto dto,
			@RequestParam String url, HttpSession session) {
		/*
		 *  서비스에서 비즈니스 로직을 처리할때 필요로  하는 객체를 컨트롤러에서 직접 전달을 해 주어야 한다.
		 *  주로, HttpServletRequest, HttpServletResponse, HttpSession, ModelAndView
		 *  등등의 객체 이다. 
		 */
		service.loginProcess(dto, session);
		
		String encodedUrl=URLEncoder.encode(url);
		mView.addObject("url", url);
		mView.addObject("encodedUrl", encodedUrl);
		mView.setViewName("users/login");
		return mView;
	}
	//비밀번호(newPwd)폼 요청 처리
	   @RequestMapping("/users/pwd_updateform")
	   public ModelAndView pwdUpdateForm(ModelAndView mView, HttpServletRequest request) {
	      
	      mView.setViewName("users/pwd_updateform");
	      return mView;
	}
	   //비밀번호(newPwd) 요청 처리
	   @RequestMapping("/users/pwd_update")
	   public ModelAndView pwdUpdate(UsersDto dto, 
	         ModelAndView mView, HttpSession session, HttpServletRequest request) {
	   
	      service.updateUserPwd(session, dto, mView);
	      
	      mView.setViewName("users/pwd_update");
	      return mView;
	}
	
	   //회원 탈퇴 요청 처리
	      @RequestMapping("/users/delete")
	      public ModelAndView authDelete(HttpSession session, ModelAndView mView,
	             HttpServletRequest request) {
	         
	         service.deleteUser(session, mView);
	         
	         mView.setViewName("users/delete");
	         return mView;
	      }

}
