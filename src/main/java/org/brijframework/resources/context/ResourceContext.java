package org.brijframework.resources.context;

import org.brijframework.asm.context.DefaultContainerContext;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.support.util.SupportUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class ResourceContext extends DefaultContainerContext{
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(ResourceContainer.class.isAssignableFrom(cls)) {
					register((Class<? extends ResourceContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startup() {
		System.err.println("ResourceContext loading start.");
		SupportUtil.getDepandOnSortedClassList(getClassList()).forEach((container) -> {
			loadContainer(container);
		});
		System.err.println("ResourceContext loading done.");
	}


	@Override
	public void destory() {
	}

}
