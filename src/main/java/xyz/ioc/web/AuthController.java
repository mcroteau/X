package xyz.ioc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;


import java.util.Map;
import java.util.HashMap;

import xyz.ioc.common.AuthenticationInfo;
import xyz.ioc.model.Account;
import xyz.ioc.dao.AccountDao;

@Controller
public class AuthController {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AuthenticationInfo authenticationInfo;


	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String signin(){
	   	return "authentication/signin";
	}



	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public String authenticate(ModelMap model,	
							   HttpServletRequest request, 
							   final RedirectAttributes redirect, 
							   @ModelAttribute("signon") Account account){

		try{
			
			UsernamePasswordToken token = new UsernamePasswordToken(account.getUsername(), account.getPassword());
			
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			token.setRememberMe(true);

			if(!authenticationInfo.isAuthenticated()){
				redirect.addFlashAttribute("error", "Wrong username and password...");
				return "redirect:/signin";
			}

			Account sessionAccount = accountDao.findByUsername(account.getUsername());

			request.getSession().setAttribute("account", sessionAccount);
			request.getSession().setAttribute("imageUri", sessionAccount.getImageUri());

			return "redirect:/";
			
			
		} catch ( Exception e ) { 
			e.printStackTrace();
		} 

		redirect.addFlashAttribute("error", "Wrong username and password");
		return "redirect:/signin";
	
	}	


	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String signout(ModelMap model,	
							   HttpServletRequest request, 
							   final RedirectAttributes redirect){
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();		
		
		Session session = currentUser.getSession();
		session.setAttribute("account", null);

		model.addAttribute("message", "Successfully signed out");
		request.getSession().setAttribute("account", "");
		request.getSession().setAttribute("imageUri", "");

		return "redirect:/";
	} 

	
}