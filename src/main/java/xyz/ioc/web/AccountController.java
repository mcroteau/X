package xyz.ioc.web;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;

import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Map;

import xyz.ioc.common.ApplicationConstants;

import xyz.ioc.common.AuthenticationInfo;
import xyz.ioc.model.*;
import xyz.ioc.dao.AccountDao;
import xyz.ioc.dao.RoleDao;

import xyz.ioc.common.CommonUtilities;
import xyz.ioc.service.EmailService;
import xyz.ioc.service.PhoneService;


@Controller
public class AccountController {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PhoneService phoneService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CommonUtilities commonUtilities;

	@Autowired
	private AuthenticationInfo authenticationInfo;


	@RequiresAuthentication
	@RequestMapping(value="/accounts", method=RequestMethod.GET)
	public String accounts(ModelMap model, 
					final RedirectAttributes redirect, 
				    @RequestParam(value="admin", required = false ) String admin,
				    @RequestParam(value="offset", required = false ) String offset,
				    @RequestParam(value="max", required = false ) String max,
				    @RequestParam(value="page", required = false ) String page){

		if(!authenticationInfo.isAdministrator()){
			redirect.addFlashAttribute("error", "You are not administrator.");
			return "redirect:/signin";
		}

		if(page == null){
			page = "1";
		}						


		List<Account> accounts = new ArrayList<Account>();
		
		if(offset != null) {
			int m = ApplicationConstants.RESULTS_PER_PAGE;
			if(max != null){
				m = Integer.parseInt(max);
			}
			int o = Integer.parseInt(offset);
			accounts = accountDao.findAllOffset(m, o);	
		}else{
			accounts = accountDao.findAll();	
		} 
		
		long count = accountDao.count();
		
		model.addAttribute("accounts", accounts);
		model.addAttribute("total", count);
		
		model.addAttribute("resultsPerPage", ApplicationConstants.RESULTS_PER_PAGE);
		model.addAttribute("activePage", page);

		model.addAttribute("accountsHrefActive", "active");
		
    	return "account/index";

	}





	@RequestMapping(value="/account/search", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody String search(ModelMap model,
				  HttpServletRequest request,
				  final RedirectAttributes redirect,
				  @RequestParam(value="q", required = false ) String query,
				  @RequestParam(value="o", required = false ) String offset,
				  @RequestParam(value="page", required = false ) String page){

		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();

		if(!authenticationInfo.isAuthenticated()){
			data.put("error", "Authentication required");
			return gson.toJson(data);
		}

		if(query != null){

			if(page == null){
				page = "1";
			}

			long count;
			List<Account> accounts;

			if(offset != null) {
				int o = Integer.parseInt(offset);
				count = accountDao.count();
				accounts = accountDao.search(query, o);
			}else{
				count = accountDao.countQuery(query);
				accounts = accountDao.search(query, 0);
			}


			Map<String, Object> d = new HashMap<String, Object>();
			d.put("count", count);
			d.put("accounts", accounts);
			return gson.toJson(d);
		} else {
			return gson.toJson(data);
		}

	}


	@RequestMapping(value="/account/edit/{id}", method=RequestMethod.GET)
	public String edit(ModelMap model, 
	                     HttpServletRequest request,
						 final RedirectAttributes redirect,
					     @PathVariable String id){

		if(authenticationInfo.isAdministrator() ||
				authenticationInfo.hasPermission(ApplicationConstants.ACCOUNT_MAINTENANCE + id)){

			Account account = accountDao.get(Long.parseLong(id));
			model.addAttribute("account", account);

			return "account/edit";

		}else{
			redirect.addFlashAttribute("error", "You do not have permission to edit this account.");
			return "redirect:/";
		}
		
	}


	@RequestMapping(value="/account/update/{id}", method=RequestMethod.GET)
	public String uget(ModelMap model,
					  	 	 final RedirectAttributes redirect,
					     	@PathVariable String id){


		if(authenticationInfo.isAdministrator() ||
				authenticationInfo.hasPermission(ApplicationConstants.ACCOUNT_MAINTENANCE + id)){


			Account account = accountDao.get(Long.parseLong(id));

			model.addAttribute("account", account);
			return "account/edit";

		}else{
			redirect.addFlashAttribute("error", "You don't hava permissionsa...");
			return "redirect:/";
		}

	}


	@RequestMapping(value="/account/update/{id}", method=RequestMethod.POST)
	public String update(@ModelAttribute("account")
							 Account account, 
							 ModelMap model,
					   		 HttpServletRequest request,
					  	 	 final RedirectAttributes redirect, 
					  	 	 @RequestParam("image") CommonsMultipartFile uploadedProfileImage){
		
		long id = account.getId();
		Account storedAccount = accountDao.get(id);

		String imageFileUri = "";

		if(authenticationInfo.isAdministrator() ||
				authenticationInfo.hasPermission(ApplicationConstants.ACCOUNT_MAINTENANCE + id)){


			if(uploadedProfileImage != null &&
					!uploadedProfileImage.isEmpty()) {

				imageFileUri = commonUtilities.write(uploadedProfileImage, ApplicationConstants.PROFILE_IMAGE_DIRECTORY);
				if(imageFileUri.equals("")){
					commonUtilities.deleteUploadedFile(imageFileUri);
					redirect.addFlashAttribute("account", account);
					redirect.addFlashAttribute("error", "Something went wrong while processing image. PNG, JPG or GIF only.");
					return "redirect:/account/edit/" + id;
				}
				account.setImageUri(imageFileUri);

			}

			if(!account.getImageUri().equals("")) {
				if(!storedAccount.getImageUri().equals(ApplicationConstants.DEFAULT_IMAGE_URI) &&
						!storedAccount.getImageUri().equals(ApplicationConstants.HERO)) {
					System.out.println(">>> deleting existing profile : " + storedAccount.getImageUri());
					String path = commonUtilities.getApplicationPath();
					String fileUri = storedAccount.getImageUri();

					String fullUri = path + fileUri;

					File currentImage = new File(fullUri);
					if (currentImage != null) currentImage.delete();

					account.setImageUri(imageFileUri);
				}

				accountDao.update(account);
				Account savedAccount = accountDao.get(id);

				//TODO: update session account

				redirect.addFlashAttribute("message", "account successfully updated");
				model.addAttribute("account", savedAccount);

				return "redirect:/account/edit/" + id;

			}
			else{

				redirect.addFlashAttribute("account", account);
				redirect.addFlashAttribute("error", "Please include your profile image");
				return "redirect:/account/edit/" + id;
			}

		}else{
			redirect.addFlashAttribute("error", "You don't hava permissionsa...");
			return "redirect:/";
		}

	}


	@RequestMapping(value="/account/edit_password/{id}", method=RequestMethod.GET)
	public String editPassword(ModelMap model, 
	                     HttpServletRequest request,
						 final RedirectAttributes redirect,
					     @PathVariable String id){

		if(authenticationInfo.isAdministrator() ||
				authenticationInfo.hasPermission(ApplicationConstants.ACCOUNT_MAINTENANCE + id)){

			Account account = accountDao.get(Long.parseLong(id));
			model.addAttribute("account", account);
			return "account/edit_password";

		}else {
			redirect.addFlashAttribute("error", "You do not have permission to edit this account. What are you up to?");
			return "redirect:/";
		}
	}


	@RequestMapping(value="/account/update_password/{id}", method=RequestMethod.POST)
	public String updatePassword(@ModelAttribute("account")
							 Account account, 
							 ModelMap model,
					   		 HttpServletRequest request,
					  	 	 final RedirectAttributes redirect // @RequestParam("image") CommonsMultipartFile file
			   				 ){
		
		if(account.getPassword().length() < 7){
		 	redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Passwords must be at least 7 characters long.");
			return "redirect:/signup";
		}

		if(authenticationInfo.isAdministrator() ||
				authenticationInfo.hasPermission(ApplicationConstants.ACCOUNT_MAINTENANCE + account.getId())){
			
			if(!account.getPassword().equals("")){
				String password = commonUtilities.hash(account.getPassword());
				account.setPassword(password);
				accountDao.updatePassword(account);
			}

			redirect.addFlashAttribute("message", "password successfully updated");	
			return "redirect:/signout";
			
		}else{
			redirect.addFlashAttribute("error", "You don't hava permissionsa...");
			return "redirect:/";
		}

	}


	@RequestMapping(value="/registration", method=RequestMethod.GET)
	public String registration(HttpServletRequest request, @ModelAttribute("account") Account account, final RedirectAttributes redirect){
		System.out.println("account name : " + account.getName());
		phoneService.support("SigmaX ~ " + request.getRemoteHost());
		return "account/registration";
	}

	
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signup(HttpServletRequest request, @ModelAttribute("account") Account account, final RedirectAttributes redirect){
		phoneService.support("SigmaX ~ " + request.getRemoteHost());
		return "account/registration";
	}
	

	@RequestMapping(value="/register", method=RequestMethod.POST)
	protected String register(@ModelAttribute("account") Account account, 
								final RedirectAttributes redirect
							  	){

		if(!commonUtilities.validEmail(account.getUsername())){
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Username must be a valid email.");
			return "redirect:/signup";
		}

		if(account.getUsername().contains(" ")){
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Username contains spaces, no spaces are allowed");
			return "redirect:/signup";
		}

		if(account.getName().equals("")){
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Name cannot be blank.");
			return "redirect:/signup";
		}
		
		if(account.getPassword().equals("")) {
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Passwords cannot be blank and must match.");
			return "redirect:/signup";
		}

		if(account.getPassword().length() < 7){
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Passwords must be at least 7 characters long.");
			return "redirect:/signup";
		}

		String passwordHashed = commonUtilities.hash(account.getPassword());

        try{

        	System.out.println("name" + account.getName());

			account.setPassword(passwordHashed.toString());
			account.setImageUri(ApplicationConstants.DEFAULT_IMAGE_URI);
			accountDao.save(account);	
			
			Account savedAccount = accountDao.findByUsername(account.getUsername());

			Role defaultRole = roleDao.find(ApplicationConstants.ROLE_ACCOUNT);

			accountDao.saveAccountRole(savedAccount.getId(), defaultRole.getId());
			accountDao.saveAccountPermission(savedAccount.getId(), ApplicationConstants.ACCOUNT_MAINTENANCE + savedAccount.getId());

			String body = "<h1>SigmaX</h1>"+
					"<p>Thank you for registering! Enjoy!</p>";

			emailService.send(savedAccount.getUsername(), "Successfully Registered", body);
			phoneService.support("SigmaX : Registration " + account.getName() + " " + account.getUsername());

        }catch(Exception e){
			e.printStackTrace();
			redirect.addFlashAttribute("account", account);
        	redirect.addFlashAttribute("error", "Username already exists.");
        	return("redirect:/signup");
        }


		redirect.addFlashAttribute("message", "Thank you for registering. Enjoy");
		return "redirect:/signin";
	}


	@RequestMapping(value="/profile/{id}", method=RequestMethod.GET, produces="application/json")
	public String profile(ModelMap model,
						  final RedirectAttributes redirect,
						  @PathVariable String id){

		Map<String, Object> data = new HashMap<String, Object>();
		Gson gson = new Gson();

		if(!authenticationInfo.isAuthenticated()){
			data.put("error", "Authentication required");
			return gson.toJson(data);
		}

		Account account = accountDao.get(Long.parseLong(id));
		model.addAttribute("account", account);

		return "account/profile";
	}


	@RequestMapping(value="/account/reset", method=RequestMethod.GET)
	public String reset(){
		return "account/reset";
	}


	@RequestMapping(value="/account/send_reset", method=RequestMethod.POST)
	public String sendReset(HttpServletRequest request,
							final RedirectAttributes redirect,
				    			@RequestParam(value="username", required = true ) String username){

		try {
			Account account = accountDao.findByUsername(username);

			if (account == null) {
				redirect.addFlashAttribute("error", "Unable to find account.");
				return ("redirect:/account/reset");
			}

			String resetUuid = commonUtilities.generateRandomString(13);
			account.setUuid(resetUuid);
			accountDao.update(account);

			StringBuffer url = request.getRequestURL();

			String[] split = url.toString().split("/o/");
			String httpSection = split[0];

			String resetUrl = httpSection + "/o/account/confirm_reset?";

			String params = "username=" + URLEncoder.encode(account.getUsername(), "utf-8") + "&uuid=" + resetUuid;
			resetUrl += params;


			String body = "<h1>SigmaX</h1>" +
					"<p>Reset Password :" +
					"<a href=\"" + resetUrl + "\">" + resetUrl + "</a></p>";


			System.out.println("reset : " + resetUrl);

			emailService.send(account.getUsername(), "Reset Password", body);

		}catch(Exception e){
			e.printStackTrace();
		}

		return "account/send_reset";
	}


	@RequestMapping(value="/account/confirm_reset", method=RequestMethod.GET)
	public String reset(ModelMap model,
						final RedirectAttributes redirect,
						@RequestParam(value="username", required = true ) String username,
						@RequestParam(value="uuid", required = true ) String uuid){

		Account account = accountDao.findByUsernameAndUuid(username, uuid);

		if (account == null) {
			redirect.addFlashAttribute("error", "Unable to find account.");
			return ("redirect:/account/reset");
		}
		model.addAttribute("account", account);

		return "account/confirm";
	}




	@RequestMapping(value="/account/reset/{id}", method=RequestMethod.POST)
	public String resetPassword(@ModelAttribute("account") Account account,
								 ModelMap model,
								 HttpServletRequest request,
								 final RedirectAttributes redirect){

		System.out.println(">>> reset password");

		if(account.getPassword().length() < 7){
			redirect.addFlashAttribute("account", account);
			redirect.addFlashAttribute("error", "Passwords must be at least 7 characters long.");
			return "redirect:/account/confirm?username=" + account.getUsername() + "&uuid=" + account.getUuid();
		}

		if(!account.getPassword().equals("")){
			String password = commonUtilities.hash(account.getPassword());
			account.setPassword(password);
			accountDao.updatePassword(account);
		}

		redirect.addFlashAttribute("message", "Password successfully updated");
		return "account/success";

	}

}