package com.test.test.spring.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.test.spring.service.CertificateService;

@Controller
class CertificateController {
	@Autowired
	CertificateService certificateService;
    

	
	@RequestMapping(value = "/certification", method = RequestMethod.POST)
	@ResponseBody
	public String certification(@RequestBody Map<String, Object> params, HttpServletRequest request)	{
		String msg = certificateService.certificateLogin(request, params);
		return msg;
	}
	
	@RequestMapping(value = "/updateCertPass", method = RequestMethod.POST)
	@ResponseBody
	public String updateCertPass(@RequestBody Map<String, Object> params, HttpServletRequest request)	{
		String msg = certificateService.updateCertPass(request, params);
		return msg;
	}
	
}
