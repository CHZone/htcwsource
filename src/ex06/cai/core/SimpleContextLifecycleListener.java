package ex06.cai.core;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

public class SimpleContextLifecycleListener implements LifecycleListener {

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
//		Lifecycle lifecycle = event.getLifecycle();
		System.out.println("SimpleContextLifecycleEvents's event " + event.getType().toString());
		if(Lifecycle.START_EVENT.equals(event.getType())){
			System.out.println("starting context.");
		}else if(Lifecycle.START_EVENT.equals(event.getType())){
			System.out.println("stop context.");
		}
	}

}
