package org.brijframework.resources.factory.prop;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.EnvResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.files.prop.PropResource;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class PropResourceFactory implements EnvResourceFactory {
	
	private ConcurrentHashMap<Object, PropResource> cache = new ConcurrentHashMap<>();
	
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
		getCache().put(metaResource.getId(), (PropResource)metaResource);
		getContainer().load(getResourceType()).add(metaResource.getId(), (PropResource)metaResource);
	}
	
	@Override
	public PropResource build(File file) {
		PropResource resource = new PropResource(file);
		String id=file.getAbsolutePath().contains("classes")? file.getAbsolutePath().split("classes")[1]: file.getAbsolutePath();
		resource.setId(id);
		return resource;
	}
	
	@Override
	public ConcurrentHashMap<Object, PropResource> getCache() {
		return cache;
	}

	@Override
	public String getResourceType() {
		return ResourceType.PROP;
	}

	@Override
	public Collection<PropResource> getResources() {
		return cache.values();
	}

	public PropResource getResource(String key) {
		return cache.get(key);
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
