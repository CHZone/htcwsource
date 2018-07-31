package ex06.cai.core;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;

public class SimplePipeline implements Pipeline, Lifecycle {
	
	private Container container = null;
	private Valve basic;
	private Valve[] valves = new Valve[0];
	
	public  SimplePipeline(Container container) {
		this.setContainer(container);
	}
	
	public void setContainer(Container container){
		this.container = container;
	}
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public Valve getBasic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBasic(Valve valve) {
		this.basic = valve;
		((Contained) valve).setContainer(this.container);
	}

	@Override
	public void addValve(Valve valve) {
		if(valve instanceof Contained){
			((Contained) valve).setContainer(this.container);
		}
		synchronized (valves) {
			Valve[] result = new Valve[valves.length+1];
			System.arraycopy(valves, 0, result, 0, valves.length);
			result[valves.length] = valve;
			valves = result;
		}
	}

	@Override
	public Valve[] getValves() {
		return valves;
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		(new StandradPipelineValveContext()).invokeNext(request, response);
	}

	@Override
	public void removeValve(Valve valve) {
		// TODO Auto-generated method stub

	}
	protected class StandradPipelineValveContext implements ValveContext{
		private int stag = 0;

		@Override
		public String getInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void invokeNext(Request request, Response response) throws IOException, ServletException {
			int subscript = stag;
			stag = stag +1;
			if(subscript < valves.length){
				valves[subscript].invoke(request, response, this);
			}else if(subscript == valves.length){
				basic.invoke(request, response, this);
			}else{
				throw new ServletException("no valve !!!");
			}
		}
	}

}
