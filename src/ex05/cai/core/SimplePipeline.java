package ex05.cai.core;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Contained;
import org.apache.catalina.Container;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Valve;
import org.apache.catalina.ValveContext;

public class SimplePipeline implements Pipeline{
	
	// 为毛用protected，private不行吗？
	protected Valve basic = null;
	// 有setter，没有getter , 为什么要container，需要把container，赋值给Valve
	protected Container container = null;
	protected Valve[] valves = new Valve[0];
	

	public SimplePipeline(Container contrainer) {
		setContainer(contrainer);
	}
	
	public void setContainer(Container container) {
		this.container = container;
	}
	
	@Override
	public Valve getBasic() {
		return basic;
	}

	@Override
	public void setBasic(Valve valve) {
		basic = valve;
		((Contained)basic).setContainer(container);
	}

	@Override
	public void addValve(Valve valve) {
		if(valve instanceof Contained){
			((Contained)valve).setContainer(container);
		}
		synchronized (valves) {
			Valve[] results = new Valve[valves.length+1];
			System.arraycopy(valves, 0, results, 0, valves.length);
			results[valves.length] = valve;
			valves = results;
		}
		
	}

	@Override
	public Valve[] getValves() {
		return valves;
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		// 使用新创建的SimplePiplineValveContext,重复使用需要初始化状态。
		(new SimplePipelineValveContext()).invokeNext(request, response);
	}

	@Override
	public void removeValve(Valve valve) {
		
	}
	// 内部类作用： 
	// 1. 可以访问外部类
	// 2. 创建内部类，来保证每次调用的初始化状态一致，是否有性能上的优势？
	protected class SimplePipelineValveContext implements ValveContext{
		protected int stag = 0;

		@Override
		public String getInfo() {
			return null;
		}

		@Override
		public void invokeNext(Request request, Response response) throws IOException, ServletException {
			int subscript = stag;
			stag = stag + 1;
			if(subscript < valves.length){
				valves[subscript].invoke(request, response, this);
			}
			else if(subscript == valves.length){
				basic.invoke(request, response, this);
			}else{
				throw new ServletException("No valve");
			}
		}
		
	}

}
