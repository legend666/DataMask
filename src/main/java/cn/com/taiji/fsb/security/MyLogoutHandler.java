package cn.com.taiji.fsb.security;

import cn.com.taiji.fsb.annotation.HandleLog;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyLogoutHandler implements LogoutHandler {

    @HandleLog(types = "登录模块",describe = "登出操作")
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        request.getSession().invalidate();//注销用户，使session失效。
    }
}
