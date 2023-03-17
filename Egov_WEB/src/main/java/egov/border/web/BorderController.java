package egov.border.web;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.border.service.BorderService;
import egov.lib.pagination.PaginationInfo; 								// 이건 전자정부에서 제공이 아니라 따로 만든 내용
// import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;  // 전자정부 프레임워크에서 제공

@Controller
public class BorderController {
	
	private static final Logger logger= LoggerFactory.getLogger(BorderController.class);
	
	@Resource(name="BorderService") BorderService borderService;
	
	@RequestMapping(value="/borderWrite.do")
	public String borderWrite(HttpServletRequest request, ModelMap model)
	{
		String userId="";
		
		//글쓰기권한 검사도 가능
		if (request.getSession().getAttribute("USER_ID")==null)
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
		
		
		model.addAttribute("userId",userId); // Key 값, Value 값(입력한 갑)
		return "border/borderwrite";
	}
	
	
	
	@RequestMapping(value="/borderInsert.do")
	public String borderInsert(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		
		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "" ;
		
//		System.out.println(title);
//		System.out.println(mytextarea);
//		System.out.println(userId);
		
		// Java Script 유효성 알아서 진행
		// 서버단에서 재 검증 필요
		if(title.length()>15)
		{
			return "redirect:/borderWrite.do";
		}
		//2000자 잇상이면
		else if(mytextarea.length()>2000)
		{
			return "redirect:/borderWrite.do";
		}
		// Session에 User ID 가 저장이 안되어 있다면?
		else if(request.getSession().getAttribute("USER_ID")==null) 
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else // 사용자가 로그인을 했을 때,
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}
//		try {

		borderService.insertBorder(paramMap);
//		} catch(Exception e)
//		{
//			System.out.println(e);
//		}
		return "redirect:/borderList.do";
	}
	
	@RequestMapping(value="/borderList.do")
	public String borderList(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		paramMap.put("ref_cursor", null);	
		
		
		/* START 페이징 처리
		 * 사용자의 요청 페이지번호*/
		String pageNo = request.getParameter("pageNo");
		if(pageNo==null || pageNo.equals(""))
		{
			pageNo="1";
		}
		else
		{
			pageNo = request.getParameter("pageNo").toString();
		}
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(pageNo));
		paginationInfo.setRecordCountPerPage(10); //한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); //페이징 리스트의 사이즈ex:10입력<1>~<10>
		
		paramMap.put("currentPageNo", paginationInfo.getCurrentPageNo());
		paramMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		
		borderService.selectBorder(paramMap);
		int listcount = Integer.parseInt(paramMap.get("list_count").toString());
		paginationInfo.setTotalRecordCount(listcount);
	    //전체 게시물 건 수
	    //End페이징처리		

		list = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		model.addAttribute("borderList",list);
		model.addAttribute("paginationInfo",paginationInfo);
		model.addAttribute("pageNo",pageNo);
		System.out.println("========3==========");

		return "border/borderlist";
	}
	
	@RequestMapping(value="/borderView.do")
	public String borderView(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		ArrayList<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		
		String no = request.getParameter("no").toString();
		
		paramMap.put("borderId", no);	
		paramMap.put("ref_cursor", null);
		
		borderService.selectBorderView(paramMap);
		
		resultList = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		//  -------------------------------------------------------------------------------------
		// 글쓴이가 아니면 수정 삭제버튼 안보이게 하기
		if(request.getSession().getAttribute("USER_ID")==null) 
		{
			model.addAttribute("userId","");
		}
		else // 사용자가 로그인을 했을 때,
		{
			model.addAttribute("userId",request.getSession().getAttribute("USER_ID").toString());
		}

		model.addAttribute("resultList",resultList);
		// 글쓴이가 아니면 수정 삭제버튼 안보이게 하기
		//  -------------------------------------------------------------------------------------

		
		return "border/borderview";
	}
	
	@RequestMapping(value="/borderReply.do")
	public String borderReply(HttpServletRequest request, ModelMap model)
	{
		String userId="";
		String no="";
				
		//글쓰기권한 검사도 가능
		if (request.getSession().getAttribute("USER_ID")==null)
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
		
		
		no = request.getParameter("no").toString();
		model.addAttribute("userId",userId); // Key 값, Value 값(입력한 갑)
		model.addAttribute("no",no); // Key 값, Value 값(입력한 갑)
		return "border/borderreply";
	}
	
	@RequestMapping(value="/borderReplyReq.do")
	public String borderReplyReq(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		
		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "" ;
		String no = request.getParameter("no").toString();

		
		if(title.length()>15)
		{
			return "redirect:/borderList.do";
		}
		//2000자 잇상이면
		else if(mytextarea.length()>2000)
		{
			return "redirect:/borderList.do";
		}
		// Session에 User ID 가 저장이 안되어 있다면?
		else if(request.getSession().getAttribute("USER_ID")==null) 
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else // 사용자가 로그인을 했을 때,
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderId", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}
		borderService.insertBorderReply(paramMap);
		System.out.println(paramMap);

		return "redirect:/borderList.do";
	}
	
	@RequestMapping(value="/borderEdit.do")
	public String borderEdit(HttpServletRequest request, ModelMap model)
	{
		String userId="";
		String no="";
				
		//글쓰기권한 검사도 가능 
		if (request.getSession().getAttribute("USER_ID")==null)
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
		
		/* 2가지 방법. JSP로 부터 받거나, 서버에서 재 조회 */
		
		
		no = request.getParameter("no").toString();
		model.addAttribute("userId",userId); // Key 값, Value 값(입력한 갑)
		model.addAttribute("no",no); // Key 값, Value 값(입력한 갑)
		return "border/borderedit";
	}
	
	@RequestMapping(value="/borderEditReq.do")
	public String borderEditReq(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		
		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "" ;
		String no = request.getParameter("no").toString();

		
		if(title.length()>15)
		{
			return "redirect:/borderList.do";
		}
		//2000자 잇상이면
		else if(mytextarea.length()>2000)
		{
			return "redirect:/borderList.do";
		}
		// Session에 User ID 가 저장이 안되어 있다면?
		else if(request.getSession().getAttribute("USER_ID")==null) 
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else // 사용자가 로그인을 했을 때,
		{
			
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderId", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}
		borderService.updateBorderEdit(paramMap);
		System.out.println(paramMap);

		// return "redirect:/borderView.do?"+no; // 상세보기 페이지로 넘어갈 수 있음
		return "redirect:/borderList.do";
	}
	
	
	@RequestMapping(value="/borderRemove.do")
	public String borderRemove(HttpServletRequest request, ModelMap model) throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		String userId = "" ;
		String no = request.getParameter("no").toString();
		if(request.getSession().getAttribute("USER_ID")==null) 
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else // 사용자가 로그인을 했을 때,
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderId", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());

		}
		borderService.updateBorderRemove(paramMap);
		System.out.println(paramMap);
		// return "redirect:/borderView.do?"+no; // 상세보기 페이지로 넘어갈 수 있음
		return "redirect:/borderList.do";
	}
	
}
