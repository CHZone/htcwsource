package ex06.cai.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Container;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;
import org.apache.catalina.Wrapper;

public class SimpleContextMapper implements Mapper {
	
	private SimpleContext context = null;
	private String protocol;

	@Override
	public Container getContainer() {
		return context;
	}

	@Override
	public void setContainer(Container container) {
		if(container instanceof SimpleContext)
			this.context = (SimpleContext) container;
		else
			throw new IllegalArgumentException("illegal type of container");
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public Container map(Request request, boolean update) {
		if(update && request.getWrapper() != null){
			return request.getWrapper();
		}
		
		String contextPath = ((HttpServletRequest)request.getRequest()).getContextPath();
		String requestURI = ((HttpRequest)request).getDecodedRequestURI();
		String relativeURI = requestURI.substring(contextPath.length());
		
		String servletName = context.findServletMapping(relativeURI);
		Wrapper wrapper = null;
		String pathInfo = null;
		if(servletName != null)
			wrapper = (Wrapper) context.findChild(servletName);
		
		if(update){
			request.setWrapper(wrapper);
			((HttpRequest)request).setServletPath(relativeURI);
			((HttpRequest)request).setPathInfo(pathInfo);;
		}
		return wrapper;
	}

}
