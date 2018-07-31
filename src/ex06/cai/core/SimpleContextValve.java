package ex06.cai.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;
import org.apache.catalina.Wrapper;

public class SimpleContextValve implements Valve, Contained {
	Container container = null;

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
		if(!(request instanceof HttpServletRequest)||!(response instanceof HttpServletResponse)){
			return;
		}
		
		// 类型转换是为了在异常处理中打印request uri
		HttpServletRequest hreq = (HttpServletRequest) request;
//		HttpServletResponse hresp = (HttpServletResponse) response;
//		String contextPath = hreq.getContextPath();
		String requestURI = hreq.getRequestURI();
//		String relativeURI  = requestURI.substring(contextPath.length());
		
		Context context = (Context) container;
		Wrapper wrapper = null;
		try{
			// 什么时候update为false呢？还是wrapper之后保存在request中？
			wrapper = (Wrapper) context.map(request, true);
		}catch (IllegalArgumentException e) {
			badRequest(requestURI,(HttpServletResponse)response.getResponse());
			return;
		}
		
		if(wrapper == null){
			noFound(requestURI, (HttpServletResponse)response.getResponse());
			return;
		}
		// 为什么在这里设置
		response.setContext(context);
		wrapper.invoke(request, response);
	}

	private void noFound(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
		} catch (IOException e) {
		}
	}

	private void badRequest(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
		} catch (IOException e) {
		}
	}

}
