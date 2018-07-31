package ex05.cai.startup;

import org.apache.catalina.Loader;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;

import ex05.cai.core.SimpleWrapper;
import ex05.cai.valves.ClientIPLoggerValve;
import ex05.cai.valves.HeaderLoggerValve;
import ex05.pyrmont.core.SimpleLoader;

public class Bootstrap1 {
	public static void main(String[] args){
		HttpConnector connector = new HttpConnector();
		Wrapper wrapper = new SimpleWrapper();
		Loader loader = new SimpleLoader();
		Valve valve1 = new HeaderLoggerValve();
		Valve valve2 = new ClientIPLoggerValve();
		
		wrapper.setServletClass("ModernServlet");
		wrapper.setLoader(loader);
		// 只有SimpleWrapper和Pipeline有addValve。
		((Pipeline)wrapper).addValve(valve1);
		((Pipeline)wrapper).addValve(valve2);
		
		connector.setContainer(wrapper);
		
		try {
			connector.initialize();
			connector.start();
			
			//make the applicatin wait until we press a key
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
