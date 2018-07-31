package ex05.cai.startup;

import java.io.IOException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Loader;
import org.apache.catalina.Mapper;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;

import ex05.cai.core.SimpleContext;
import ex05.cai.core.SimpleContextMapper;
import ex05.cai.core.SimpleLoader;
import ex05.cai.core.SimpleWrapper;
import ex05.cai.valves.ClientIPLoggerValve;
import ex05.cai.valves.HeaderLoggerValve;

public class Bootstrap2 {
	public static void main(String[] args){
		HttpConnector connector = new HttpConnector();
		
		Wrapper wrapper1 = new SimpleWrapper();
		wrapper1.setName("Primitive");
		wrapper1.setServletClass("PrimitiveServlet");
		
		Wrapper wrapper2 = new SimpleWrapper();
		wrapper2.setName("Modern");
		wrapper2.setServletClass("ModernServlet");
		
		//Context 和 Pipeline 同时创建
		SimpleContext context = new SimpleContext();
		// 添加 wrapper 到children
		context.addChild(wrapper1);
		context.addChild(wrapper2);
		
		Valve valve1 = new HeaderLoggerValve();
		Valve valve2 = new ClientIPLoggerValve();
		((Pipeline)context).addValve(valve1);
		((Pipeline)context).addValve(valve2);
		
		Mapper mapper = new SimpleContextMapper();
		mapper.setProtocol("http");
		context.addMapper(mapper);
		
		//loader
		Loader loader = new SimpleLoader();
		context.setLoader(loader);
		
		// patter -> servlet name
		context.addServletMapping("/Primitive", "Primitive");
		context.addServletMapping("/Modern", "ModernServlet");
		
		connector.setContainer(context);
		
		try {
			connector.initialize();
			connector.start();
			
			System.in.read();
		} catch (LifecycleException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
