package egov.main.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egov.main.service.MainService;

@Controller
public class MainController {

	private static final Logger logger= LoggerFactory.getLogger(MainController.class);
	
	@Resource(name="MainService") MainService mainService;
	
	@RequestMapping(value = "/main.do")
	public String main(HttpServletRequest request,ModelMap model)
	{
		return "main/main";
	}
	
	@RequestMapping(value = "/main2.do")
	public String main2(HttpServletRequest request,ModelMap model)
	{
		return "main/main2";
	}

	@RequestMapping(value = "/main3.do")
	public String main3(@RequestParam("userNo")String userN,HttpServletRequest request,ModelMap model)
	{
		int userNo = Integer.parseInt(request.getParameter("userNo").toString());
		String userId = request.getParameter("id").toString();
		String userPw = request.getParameter("pw").toString();
		
		if(userId.equals("asdf123"))
		{
			model.addAttribute("serverId","asdf123");
		}
		else
		{
			model.addAttribute("serverId","로그인실패");
		}
		
		userNo =userNo+5; 
		model.addAttribute("userNo",userNo);
		model.addAttribute("userPw",userPw);
		
		
		return "main/main3";
	}
	
	@RequestMapping(value = "/main4/{userVal}.do")
	public String main4(@PathVariable("userVal")String userVal,HttpServletRequest request,ModelMap model)
	{
		model.addAttribute("userNo",userVal);
		
		return "main/main3";
	}

	/*
	@RequestMapping(value = "/main5.do")
	public String main5(HttpServletRequest request,ModelMap model) throws Exception
	{
		String userid = "";
		HashMap<String,Object> paramMap = new HashMap<String,Object>(); 
		HashMap<String,Object> resultMap = new HashMap<String,Object>(); 
		resultMap = mainService.selectMain(paramMap);
		userid = resultMap.get("USER_ID").toString();
		model.addAttribute("userid",userid);
		
		return "main/main3";
	}
	*/

	//여기서는 login도 메인에 작성을 하도록 하겠습니다.
	@RequestMapping(value = "/login.do")
	public String login(HttpServletRequest request,ModelMap model)
	{
		return "login/login";
	}
	
	@RequestMapping(value = "/loginSubmission.do")
	public String main5(HttpServletRequest request,ModelMap model) throws Exception
	{
		String userId =request.getParameter("id").toString();
		//1차체크.javascript로 유효성 체크 2차체크
		if(userId.length()>10)
		{
			return "redirect:/login.do";
		}
		
		HashMap<String,Object> paramMap = new HashMap<String,Object>(); 
		HashMap<String,Object> resultMap = new HashMap<String,Object>(); 
		
		paramMap.put("userId", userId);
		
		resultMap = mainService.selectLogin(paramMap);
		if(null==resultMap)
		{

			return "redirect:/login.do";
		}
		request.getSession().setAttribute("USER_ID", userId);
		
		logger.info("ST접속정보 기록============");
		logger.info("유저아이디:"+userId+", 접속날짜:");
		logger.info("ED접속정보 기록============");
		
		return "main/main4";
	}
	
	@RequestMapping(value = "/exception.do")
	public String exception(HttpServletRequest request,ModelMap model) throws Exception 
	// 예뢰를 발생시킬 것이기 때문에 throws Exception 필요
	{
		throw new Exception("사용자 임의의 에러발생");
	}
	
	
}
