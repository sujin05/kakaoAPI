package com.kakao.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

	KakaoAPI kakaoApi = new KakaoAPI();


	@RequestMapping(value="/login")
	public String login(@RequestParam("code") String code, HttpSession session) {
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
		//return mav;
		return (userInfo.toString());
	}

	@RequestMapping(value="/logout")
	public String access(HttpSession session) throws IOException {

		String access_token = (String)session.getAttribute("access_token");
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer "+ access_token);

		String result = ("https://kapi.kakao.com/v1/user/logout").toString();
		System.out.println(result);

		return result;
	}

}
