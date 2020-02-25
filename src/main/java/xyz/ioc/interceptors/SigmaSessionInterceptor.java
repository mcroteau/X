package xyz.ioc.interceptors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.ioc.common.SigmaSessionManager;
import xyz.ioc.dao.AccountDao;
import xyz.ioc.model.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class SigmaSessionInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SigmaSessionManager sigmaSessionManager;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        long currentTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        //System.out.println("Request URL::" + request.getRequestURL().toString());
        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated()){
            String username = subject.getPrincipal().toString();
            sigmaSessionManager.sessions.put(username, System.currentTimeMillis());
        }

        for (Map.Entry<String, Long> entry : sigmaSessionManager.sessions.entrySet()){
            long currentTime = System.currentTimeMillis();
            long sessionTime = entry.getValue();

            if(currentTime - sessionTime >= 60000){
                sigmaSessionManager.sessions.remove(entry.getKey());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {


        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated()){
            Account sessionAaccount = accountDao.findByUsername(subject.getPrincipal().toString());
            request.getSession().setAttribute("account", sessionAaccount);
            request.getSession().setAttribute("imageUri", sessionAaccount.getImageUri());
        }

    }

}
