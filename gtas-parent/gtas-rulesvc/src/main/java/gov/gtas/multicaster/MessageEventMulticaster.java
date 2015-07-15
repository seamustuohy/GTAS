package gov.gtas.multicaster;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

public class MessageEventMulticaster implements ApplicationEventMulticaster {

	private ApplicationEventMulticaster asyncEventMulticaster;

	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		if (listener.getClass().getAnnotation(AsyncListener.class) != null) {
			asyncEventMulticaster.addApplicationListener(listener);
		}
	}

	@Override
	public void addApplicationListenerBean(String listenerBeanName) {
		// do nothing
	}

	@Override
	public void removeApplicationListener(ApplicationListener<?> listener) {
		asyncEventMulticaster.removeApplicationListener(listener);
	}

	@Override
	public void removeApplicationListenerBean(String listenerBeanName) {
		// do nothing
	}

	@Override
	public void removeAllListeners() {
		asyncEventMulticaster.removeAllListeners();
	}

	@Override
	public void multicastEvent(ApplicationEvent event) {
		asyncEventMulticaster.multicastEvent(event);
	}

	public void setAsyncEventMulticaster(
			ApplicationEventMulticaster asyncEventMulticaster) {
		this.asyncEventMulticaster = asyncEventMulticaster;
	}
}
