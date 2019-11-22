package com.itheima.dofilter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;
import com.itheima.service.UserService;

public class AutologinFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//ֻ��δ��¼���û����Զ���¼
		if(req.getSession(false) == null ||req.getSession().getAttribute("user") == null){
			Cookie[] cs = req.getCookies();
			Cookie findC = null;
			if(cs!=null){
				//ֻ�д����Զ���¼cookie���û����Զ���¼
				for (Cookie c : cs) {
					if(c.getName().equals("autologin")){
						findC = c;
						break;
					}
				}
			}
			//ֻ���Զ���¼cookie�е��û������붼��ȷ�����Զ���¼
			if(findC != null){
				String v = URLDecoder.decode(findC.getValue(), "utf-8");
				UserDao dao = BasicFactory.getFactory().getDao(UserDao.class);
				User user =dao.findUserByUsernameAndPassword(v.split(":")[0], v.split(":")[1]);
				if(user != null){
					req.getSession().setAttribute("user", user);
				}
			}
		}
		//�����Ƿ��Զ���¼��Ҫ����
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
