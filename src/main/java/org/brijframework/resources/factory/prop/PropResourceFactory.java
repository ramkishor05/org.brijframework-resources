package org.brijframework.resources.factory.prop;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.files.prop.PropResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;

public class PropResourceFactory implements ResourceFactory {
	
	private ConcurrentHashMap<Object, PropResource> containers = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static PropResourceFactory factory;

	@Assignable
	public static PropResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(PropResourceFactory.class);
		}
		return factory;
	}
	
	@Override
	public void load(Resource metaResource) {
		containers.put(metaResource.getId(), (PropResource)metaResource);
		getContainer().load(getResourceType()).add(metaResource.getId(), (PropResource)metaResource);
		System.err.println("Resource : "+metaResource.getFile());
	}
	
	@Override
	public PropResource build(File file) {
		PropResource resource = new PropResource(file);
		resource.setId(file.getName());
		return resource;
	}
	
	@Override
	public ConcurrentHashMap<Object, PropResource> getCache() {
		return containers;
	}

	@Override
	public String getResourceType() {
		return ResourceType.PROP;
	}

	@Override
	public Collection<PropResource> getResources() {
		return containers.values();
	}

	public PropResource getResource(String key) {
		return containers.get(key);
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public Collection<JsonResource> getResources(String dir) {
		return null;
	}
	
}
