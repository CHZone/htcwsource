package ex05.cai.valves;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;

public class HeaderLoggerValve implements Valve, Contained {
	private Container container;

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

	@Override
	public String getInfo() {
		return null;
	}

	@Override
	public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
		context.invokeNext(request, response);;
		System.out.println("Header Logger Valve");
		ServletRequest sreq = request.getRequest();
		if(sreq instanceof HttpServletRequest){
			HttpServletRequest hsreq = (HttpServletRequest)sreq;
			Enumeration<String> headerNames = hsreq.getHeaderNames();
			while(headerNames.hasMoreElements()){
				String headName = headerNames.nextElement().toString();
				String headValue = hsreq.getHeader(headName);
				System.out.println(headName+":"+headValue);
			}
		}else{
			System.out.println("Not a HTTP Request");
		}
		
		System.out.println("-------------Header Logger Valve end--------------");
	}

}
