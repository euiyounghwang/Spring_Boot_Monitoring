package com.test.test.spring.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.test.test.spring.util.ESUtil;

@Controller
class MainController {
	
	@Value("${spring.task.name}")
	private String taskName;
	
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request)	{
		HttpSession session = request.getSession();
		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
			return "common/devLogin_certification";
		}
		
//		return "main";
//		  Model model
//		model.addAttribute("monitoring", "test");
		return "main";
		
	}
	
	@RequestMapping(value = "/applicationTask")
	@ResponseBody
	public String applicationTask(HttpServletRequest request)	{
		String msg = taskName;
		return msg;
	}
	
	@RequestMapping(value = "/getStringToHex")
	@ResponseBody
	public String getStringToHex(HttpServletRequest request) throws UnsupportedEncodingException {
		String paramStr =  ESUtil.getString(request, "folder");
		return ESUtil.getStringToHex(paramStr);
	}
	
	@RequestMapping(value = "/getHexToString")
	@ResponseBody
	public String getHexToString(HttpServletRequest request) throws UnsupportedEncodingException, DecoderException {
		String paramStr =  ESUtil.getString(request, "folder");
		return ESUtil.getHexToString(paramStr);
	}
	
	@RequestMapping(value = "/loadPage/{page}")
	public String loadCenterPage(@PathVariable String page, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
			return "common/devLogin_certification";
		}
		return page;
	}
	
	@RequestMapping(value = "/loadPage/{page1}/{page2}")
	public String loadCenterPage2(@PathVariable String page1,@PathVariable String page2, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("certification")==null || "N".equals(session.getAttribute("certification"))){
			return "common/devLogin_certification";
		}
		return page1+"/"+page2;
	}
	
	@RequestMapping(value = "/loadPage/{page1}/{page2}" , method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView loadCenterPage(@PathVariable String page1,@PathVariable String page2, @RequestBody Map<String, Object> params, Model model, HttpServletRequest request){
		
		if(params.containsKey("CD_TP")) { //code
			model.addAttribute("company_code", params.get("COMPANY_CODE"));
			model.addAttribute("cd_tp", params.get("CD_TP"));
			model.addAttribute("cd_tp_meaning", params.get("CD_TP_MEANING"));
		} else if(params.containsKey("OBJECT_ID")) { //sql
			model.addAttribute("OBJECT_ID", params.get("OBJECT_ID"));
		} else if(params.containsKey("HOST_NAME")) { //code
			model.addAttribute("host_name", params.get("HOST_NAME"));
			model.addAttribute("real_ip", params.get("REAL_IP"));
			model.addAttribute("user_id", params.get("USER_ID"));
			model.addAttribute("user_pw", params.get("USER_PW"));
		}
		return new ModelAndView(page1+"/"+page2);
	}
}
