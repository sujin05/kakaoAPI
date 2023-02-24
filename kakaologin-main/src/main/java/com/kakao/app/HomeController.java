package com.kakao.app;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

	KakaoAPI kakaoApi = new KakaoAPI();
	
	@RequestMapping(value="/login")
	public ModelAndView login(@RequestParam("code") String code, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 1번 인증코드 요청 전달
		String accessToken = kakaoApi.getAccessToken(code);
		// 2번 인증코드로 토큰 전달
		HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

		System.out.println("login info : " + userInfo.toString());
		
		if(userInfo.get("email") != null) {
			session.setAttribute("userId", userInfo.get("email"));
			session.setAttribute("accessToken", accessToken);
		}
		mav.addObject("userId", userInfo.get("email"));
		mav.setViewName("index");
		return mav;
	}
	// 주소로 들어온 id 유저의 알림 목록들을 DB에서 조회해서 반환하는 API
	@GetMapping(value ="/notice/{usetId}")
	public void getNotice(@PathVariable String userId){
		// 1. userId를 뽑아낸다.
		// 2. DB에서 해당 user의 알림 목록을 뽑아낸다. -> 서비스 코드로 분리가 됨
		// 3. 알림 목록을 반환 형식에 맞게 파싱해서 반환한다.
		return;
	}
	@RequestMapping(value="/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//삭제
		kakaoApi.kakaoLogout((String)session.getAttribute("accessToken"));
		session.removeAttribute("accessToken");
		session.removeAttribute("userId");
		mav.setViewName("index");
		return mav;
	}
	
	
	
}
