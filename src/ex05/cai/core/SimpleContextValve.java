package ex05.cai.core;

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
	private Container container;
	
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
		// 判断servlet 类型
		if(!(request instanceof HttpServletRequest) ||
				!(response instanceof HttpServletResponse)){
			return;
		}
		
		// 获取应用名和请求的servlet名
		HttpServletRequest hsreq = (HttpServletRequest)request.getRequest();
		// context名，web application name
		String contextPath = hsreq.getContextPath();
		String requestURI = hsreq.getRequestURI();
		String relateURI = requestURI.substring(contextPath.length());
		
		Context context = (Context)getContainer();
		Wrapper wrapper = null;
		try{
			wrapper = (Wrapper)context.map(request, true);
		} catch (IllegalArgumentException e) {
			badRequest(requestURI, (HttpServletResponse)response.getResponse());
			return;
		}
		
		if(wrapper == null){
			notFound(requestURI, (HttpServletResponse)response.getResponse());
			return;
		}
		response.setContext(context);
		wrapper.invoke(request, response);
		
	}

	private void badRequest(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
		} catch (IllegalStateException e) {
		      ;
	    }
	    catch (IOException e) {
	      ;
	    }
	}

	private void notFound(String requestURI, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
		} catch (IllegalStateException e) {
		      ;
	    }
	    catch (IOException e) {
	      ;
	    }
	}

}
