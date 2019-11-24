package org.brijframework.resources.container.impl;

import org.brijframework.container.impl.module.AbstractModuleContainer;
import org.brijframework.group.Group;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.resources.factory.EnvResourceFactory;
import org.brijframework.resources.group.EnvResourceGroup;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=FileResourceContainer.class)
public class EnvResourceContainer extends AbstractModuleContainer implements ResourceContainer{

	private static EnvResourceContainer container;

	@Assignable
	public static EnvResourceContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(EnvResourceContainer.class);
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (EnvResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends EnvResourceFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (EnvResourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends EnvResourceFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Group load(Object groupKey) {
		Group group=get(groupKey);
		if(group==null) {
			group=new EnvResourceGroup(groupKey);
			this.add(groupKey, group);
		}
		return group;
	}



}
