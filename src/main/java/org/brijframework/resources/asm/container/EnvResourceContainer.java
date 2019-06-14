package org.brijframework.resources.asm.container;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.group.Group;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.resources.group.EnvResourceGroup;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=FileResourceContainer.class)
public class EnvResourceContainer implements ResourceContainer {

	private ConcurrentHashMap<Object, Group> cache = new ConcurrentHashMap<>();
	private static EnvResourceContainer container;

	@Assignable
	public static EnvResourceContainer getContainer() {
		if (container == null) {
			container = InstanceUtil.getSingletonInstance(EnvResourceContainer.class);
			container.loadContainer();
		}
		return container;
	}

	@Override
	public Container loadContainer() {
		
		return this;
	}

	@Override
	public ConcurrentHashMap<Object, Group> getCache() {
		return cache;
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
