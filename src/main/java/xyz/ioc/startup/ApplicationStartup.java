package xyz.ioc.startup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import xyz.ioc.common.ApplicationConstants;
import xyz.ioc.common.CommonUtilities;
import xyz.ioc.dao.*;
import xyz.ioc.model.*;


public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	public AccountDao accountDao;

	@Autowired
	public RoleDao roleDao;

	@Autowired
	public CommonUtilities commonUtilities;

	private static String MOCK_PASSWORD = "password";

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshEvent) {
		createRoles();
		createAdministrator();
	}


	private void createAdministrator(){
		Account existing = accountDao.findByUsername(ApplicationConstants.ADMIN_USERNAME);
		String pass = commonUtilities.hash(MOCK_PASSWORD);

		if(existing == null){
			Account admin = new Account();
			admin.setName("Administrator");
			admin.setUsername(ApplicationConstants.ADMIN_USERNAME);
			admin.setPassword(pass);
			admin.setImageUri(ApplicationConstants.HERO);
			Account saved = accountDao.saveAdministrator(admin);
		}
		System.out.println("Accounts : " + accountDao.count());
	}


	private void createRoles(){
		Role adminRole = roleDao.find(ApplicationConstants.ROLE_ADMIN);
		Role accountRole = roleDao.find(ApplicationConstants.ROLE_ACCOUNT);

		if(adminRole == null){
			adminRole = new Role();
			adminRole.setName(ApplicationConstants.ROLE_ADMIN);
			roleDao.save(adminRole);
		}

		if(accountRole == null){
			accountRole = new Role();
			accountRole.setName(ApplicationConstants.ROLE_ACCOUNT);
			roleDao.save(accountRole);
		}

		System.out.println("Roles : " + roleDao.count());
	}

	

	private void generateAccounts(){
		long count = accountDao.count();
		if(count == 1){
			for(int m = 0; m < ApplicationConstants.NUMBER_MOCK_ACCOUNTS; m++){
				Account account = new Account();
				String name = "Christian" + " " + commonUtilities.generateRandomString(9);
				account.setName(name);
				account.setUsername("croteau.mike+"+ m + "@gmail.com");
				account.setLocation(commonUtilities.generateRandomString(7));
				account.setAge("33");
				String password = commonUtilities.hash(MOCK_PASSWORD);
				account.setPassword(password);
				account.setImageUri(ApplicationConstants.PROFILE_IMAGE_DIRECTORY + "jacques-fresco.png");
				accountDao.save(account);
				Account savedAccount = accountDao.findByUsername(account.getUsername());
				accountDao.saveAccountPermission(savedAccount.getId(), "account:maintenance:" + savedAccount.getId());
			}
		}

		System.out.println("Accounts : " + accountDao.count());
	}

}