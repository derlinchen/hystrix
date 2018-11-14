package com.climber.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class PermisFilter extends ZuulFilter {

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String login = request.getParameter("login");
		if (login == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.addZuulResponseHeader("content-type", "text/html;charset=utf-8");
			ctx.setResponseBody("非法访问");
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		// post、error、route和static per表示 在路由器之前执行
		return "pre";
	}

}
