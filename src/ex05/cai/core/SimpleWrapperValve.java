package ex05.cai.core;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;

public class SimpleWrapperValve implements Valve, Contained {
	// 需要从contaner（wrapper）获取servlet
	private Container container;

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
		System.out.println("Wrapper Pipeline BaseValve");
		SimpleWrapper wrapper = (SimpleWrapper)container;
		ServletRequest sreq =  request.getRequest();
		ServletResponse sresp = response.getResponse();
		HttpServletRequest hsreq = null;
		HttpServletResponse hsresp = null;
		if(sreq instanceof HttpServletRequest){
			hsreq = (HttpServletRequest) sreq;
		}
		if(sresp instanceof HttpServletResponse){
			hsresp = (HttpServletResponse) sresp;
		}
		
		Servlet servlet = wrapper.allocate();
		
		if(hsreq != null && hsresp != null){
			servlet.service(hsreq, hsresp);
		}else {
			servlet.service(sreq, sresp);
		}
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

}
