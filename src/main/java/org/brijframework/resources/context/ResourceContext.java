package org.brijframework.resources.context;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.context.Context;

public class ResourceContext implements Context{
	
	private ConcurrentHashMap<Object, Container> cache=new ConcurrentHashMap<Object, Container>();

	private Context context;

	@Override
	public void initialize(Context context) {
		this.context=context;
	}

	@Override
	public void startup() {
		
	}

	@Override
	public void destory() {
	}

	@Override
	public Context getParent() {
		return context;
	}

	@Override
	public ConcurrentHashMap<Object, Container> getContainers() {
		return cache;
	}

}
