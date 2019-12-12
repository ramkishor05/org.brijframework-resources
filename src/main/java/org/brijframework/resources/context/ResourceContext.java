package org.brijframework.resources.context;

import org.brijframework.context.impl.module.AbstractModuleContext;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.util.factories.ReflectionFactory;
import org.brijframework.util.reflect.InstanceUtil;


public class ResourceContext extends AbstractModuleContext{
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionFactory.getFactory().getClassListFromExternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionFactory.getFactory().getClassListFromInternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
