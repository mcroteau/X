package xyz.ioc.common;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ioc.dao.AccountDao;
import xyz.ioc.model.Account;

@Component
public class AuthenticationInfo {

    @Autowired
    public AccountDao accountDao;


    public boolean isAdministrator(){

        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole(ApplicationConstants.ROLE_ADMIN)){
            return true;
        }

        return false;
    }

    public boolean isAuthenticated(){
        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated()){
            return true;
        }

        return false;
    }

    public boolean hasPermission(String str){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted(str)){
            return true;
        }
        return false;
    }

    public Account getAuthenticatedAccount(){
        Subject subject = SecurityUtils.getSubject();
        Account account = accountDao.findByUsername(subject.getPrincipal().toString());
        return account;
    }

}
