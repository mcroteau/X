package xyz.ioc.web;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.ioc.common.AuthenticationInfo;
import xyz.ioc.common.CommonUtilities;
import xyz.ioc.model.Account;
import xyz.ioc.service.EmailService;
import xyz.ioc.service.PhoneService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

	@Autowired
	private PhoneService phoneService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CommonUtilities commonUtilities;

	@Autowired
	private AuthenticationInfo authenticationInfo;


	@RequestMapping(value="/", method=RequestMethod.GET)
	public String posts(HttpServletRequest request,
						ModelMap model,
						final RedirectAttributes redirect){

		if(!authenticationInfo.isAuthenticated()){
			//redirect.addFlashAttribute("error", "Start an account.");
			return "redirect:/home";
		}

		return "main";
	}


	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(HttpServletRequest request){
		//phoneService.support("SigmaX ~ Home : " + request.getRemoteHost());
		System.out.println("home");
		return "home";
	}


	@RequestMapping(value="/issues/report", method=RequestMethod.GET)
	public String report(){
		phoneService.support("SigmaX ~ Reporting Issue");
		return "report";
	}


	@RequestMapping(value="/issues/report", method=RequestMethod.POST)
	public String reportIssue(
			@RequestParam(value="email", required = true ) String email,
			@RequestParam(value="issue", required = true ) String issue,
			final RedirectAttributes redirect,
			ModelMap model){


		if (email.equals("")) {
			redirect.addFlashAttribute("error", "Please enter a valid email address");
			return "redirect:/issues/report";
		}

		if (issue.equals("")) {
			redirect.addFlashAttribute("error", "Issue was left black, please tell us what happened.");
			return "redirect:/issues/report";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(email);
		sb.append("<br/>");
		sb.append(issue);
		emailService.send("croteau.mike+apollo@gmail.com", "SigmaX Issue", sb.toString());

		model.addAttribute("message", "Thank you. Issue has been reported.");
		return "success";
	}


	@RequestMapping(value="/invite", method=RequestMethod.GET)
	public String invite(){
		return "invite";
	}


	@RequestMapping(value="/invite", method=RequestMethod.POST)
	public String sendInvite(
			@RequestParam(value="emails", required = true ) String emails,
			final RedirectAttributes redirect,
			ModelMap model){

		if(!authenticationInfo.isAuthenticated()){
			redirect.addFlashAttribute("error", "Please signin to continue...");
			return "redirect:/signin";
		}

		Account account = authenticationInfo.getAuthenticatedAccount();

		if (emails.equals("")) {
			redirect.addFlashAttribute("error", "Please enter valid email addresses");
			return "redirect:/invite";
		}

		String body = "<h1>SigmaX</h1>" +
				"<p>" + account.getName() + " invited you to join SigmaX! " +
				"<a href=\"https://goapollo.co\">https://goapollo.co</a>";

		emailService.send(emails, "You have been invited to join!", body);

		model.addAttribute("message", "Invite(s) have been sent! Thank you!");
		return "success";
	}


	///SOCKETS DEV
	@RequestMapping(value="/gwd", method=RequestMethod.GET)
	public String gwd(){
		return "gwd";
	}

}