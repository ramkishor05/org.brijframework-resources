package org.brijframework.resources.context;

import org.brijframework.context.module.impl.AbstractModuleContext;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class ResourceContext extends AbstractModuleContext{
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		System.err.println("ResourceContext startup processing...");
		super.start();
		System.err.println("ResourceContext startup completed....");
	}


	@Override
	public void stop() {
		System.err.println("ResourceContext destory processing...");
		super.stop();
		System.err.println("ResourceContext destory completed....");
	}

}
