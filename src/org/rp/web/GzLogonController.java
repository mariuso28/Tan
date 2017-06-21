package org.rp.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/logon")

public class GzLogonController {

	private static Logger log = Logger.getLogger(GzLogonController.class);	
	private GzServices gzServices;
	
	
	@RequestMapping(value = "/access_denied", method = RequestMethod.GET)
	public String accessDenied(ModelMap model) {
		
		log.info("Received request to accessDenied");
			
		model.addAttribute("errMsg","Access Denied");
		
		return "GzError";
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signin(ModelMap model) {
		
		log.info("Received request to signin XXX");
			
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", "");
		logon.put("infoMsg", "");
		logon.put("email", "brian@test.com");
		
		return new ModelAndView("logon","logon", logon);
	}
	
	public ModelAndView signin(ModelMap model,String email,String infoMsg,String errMsg) {
		
		log.info("Received request to signin");
			
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", errMsg);
		logon.put("infoMsg", infoMsg);
		logon.put("email", "");
		
		return new ModelAndView("logon","logon", logon);
	}
	
	// gz/logon/signin?error&message="+errMsg;
	@RequestMapping(value = "/signin", params="error", method = RequestMethod.GET)
	public ModelAndView signinFail(String message,HttpServletRequest req) {
		log.info("Received request to signin - error");
		return goSignin(message,req);
	}
	
	@RequestMapping(value = "/password", params="user", method = RequestMethod.GET)
	public ModelAndView password(String email,ModelMap model) {
		
		String errMsg = gzServices.tryResetPassword(email);
		if (!errMsg.isEmpty())
		{
			log.error("error trying reset password : " + errMsg);
			return signin(model,email,"",errMsg);
		}
		
		String infoMsg = "Your password has been reset, please check your inbox and login to change.";
		return signin(model,email,infoMsg,"");
	}
	
	private ModelAndView goSignin(String errMsg,HttpServletRequest req){
		String defaultUser = "";
		if (req!=null)
		{
			if (req.getParameter("email")!=null)
				defaultUser = req.getParameter("email");
			else
			if (req.getUserPrincipal()!=null && req.getUserPrincipal().getName()!=null)
				defaultUser = req.getUserPrincipal().getName();
		}
		HashMap<String,String> logon = new HashMap<String,String>();
		logon.put("errMsg", errMsg);
		logon.put("infoMsg", "");
		logon.put("email", defaultUser);
		return new ModelAndView("logon","logon", logon);
	}
	
	@RequestMapping(value = "/errStackDump", method = RequestMethod.GET)
	public Object errStackDump() {
		log.info("Received request to errStackDump");
		return "GzError";
	}
	
	@ResponseBody
	@RequestMapping(value = "/storeImage", method = RequestMethod.POST)
	public Object storeImage(@RequestParam("userId") String uid, @RequestParam("image") MultipartFile image) {
		
		log.info("Received request to storeImage");
		
		UUID userId = UUID.fromString(uid);
		
		String email;
		try {
			email = gzServices.getGzHome().getEmailForId(userId);
			gzServices.getGzHome().storeImage(email,image,image.getContentType());
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			log.info("Could not store image for : " + uid);
			return "Failed";
		}
		
		return "Success";
	}
	
	@RequestMapping(value = "/getImage", method = RequestMethod.GET)
	public void getImage(@RequestParam("userId") String uid,HttpServletResponse response) 
	{	
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(response.getOutputStream(), 1024);
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Could not get image for : " + uid);
			return;
		}
		try {
			byte[] byteToWrite = gzServices.getGzHome().getImage(uid);
			if (byteToWrite==null)
			{
				log.info("Image for : " + uid + " Not available. Getting default.");
				byteToWrite = gzServices.getGzHome().getImage("noprofile@zzzz");
			}
			if (byteToWrite!=null)
			{
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				out.write(byteToWrite);
				out.flush();
			}
			else
				log.error("Image for : " + uid + " or default could not be retrieved.");
		} catch (GzPersistenceException | IOException e) {
			log.info("Could not get image for : " + uid);
			return;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			log.info("Could not get image for : " + uid);
			return;
		}
	}
	
	@Autowired
	public void setServices(GzServices gzServices)
	{
		this.setGzServices(gzServices);
	}

	public GzServices getGzServices() {
		return gzServices;
	}

	public void setGzServices(GzServices gzServices) {
		this.gzServices = gzServices;
	}
}
