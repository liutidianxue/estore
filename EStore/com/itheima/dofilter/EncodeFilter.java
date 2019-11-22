package com.itheima.dofilter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodeFilter implements Filter {
	FilterConfig config = null;
	ServletContext context = null;
	String encode = null;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// ÏìÓ¦ÂÒÂë½â¾ö
		response.setCharacterEncoding(encode);
		response.setContentType("text/html;charset=" + encode);
		chain.doFilter(new MyHttpServletRequest((HttpServletRequest) request), response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		this.context = config.getServletContext();
		this.encode = context.getInitParameter("encode");

	}

	private class MyHttpServletRequest extends HttpServletRequestWrapper {
		private HttpServletRequest request = null;
		private boolean isNotEncode = true;

		public MyHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			try {
				if (request.getMethod().equalsIgnoreCase("post")) {
					request.setCharacterEncoding(encode);
					return request.getParameterMap();
				} else if (request.getMethod().equalsIgnoreCase("get")) {
					Map<String, String[]> map = request.getParameterMap();
					if (isNotEncode) {
						for (Map.Entry<String, String[]> entry : map.entrySet()) {
							String[] vs = entry.getValue();
							for (int i = 0; i < vs.length; i++) {
								vs[i] = new String(vs[i].getBytes("iso8859-1"),
										encode);
							}
						}
						isNotEncode = false;
					}
					return map;
				} else {
					return request.getParameterMap();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public String[] getParameterValues(String name) {
			return getParameterMap().get(name);
		}
		
		@Override
		public String getParameter(String name) {
			return getParameterValues(name) == null?null:getParameterValues(name)[0];
		}
	}
}
