package ex06.cai.startup;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;
import org.apache.catalina.Mapper;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;

import ex06.cai.core.SimpleContext;
import ex06.cai.core.SimpleContextLifecycleListener;
import ex06.cai.core.SimpleContextMapper;
import ex06.cai.core.SimpleLoader;
import ex06.cai.core.SimpleWrapper;

public class Bootstrap {
	public static void main(String[] args){
		HttpConnector connector = new HttpConnector();
		
		SimpleContext context = new SimpleContext();
		
		Loader loader = new SimpleLoader();
		context.setLoader(loader);
		
		Wrapper wrapper = new SimpleWrapper();
		wrapper.setName("Primitive");
		wrapper.setServletClass("PrimitiveServlet");
		Wrapper wrapper2 = new SimpleWrapper();
		wrapper2.setName("Modern");
		wrapper2.setServletClass("ModernServlet");
		
		context.addChild(wrapper);
		context.addServletMapping("/Primitive", "Primitive");
		context.addChild(wrapper2);
		context.addServletMapping("/Modern", "Modern");
		
		Mapper mapper = new SimpleContextMapper();
		mapper.setProtocol("http");
		context.addMapper(mapper);
		
		LifecycleListener listener = new SimpleContextLifecycleListener();
		context.addLifecycleListener(listener);
		
		connector.setContainer(context);
		
		try {
			connector.initialize();
			connector.start();
			context.start();
			System.in.read();
			connector.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
