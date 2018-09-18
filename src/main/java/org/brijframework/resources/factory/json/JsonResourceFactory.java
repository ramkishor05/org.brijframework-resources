package org.brijframework.resources.factory.json;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class JsonResourceFactory implements ResourceFactory{
	
	private ConcurrentHashMap<Object, JsonResource> cache = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static JsonResourceFactory factory;

	public static JsonResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(JsonResourceFactory.class);
			factory.loadFactory();
		}
		return factory;
	}
	
	@Override
	public void load(Resource metaResource){
		getCache().put(metaResource.getId(), new JsonResource(metaResource));
	}

	@Override
	public ConcurrentHashMap<Object, JsonResource> getCache() {
		return cache;
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.JSON;
	}

	@Override
	public Collection<JsonResource> getResources() {
		return getCache().values();
	}

	public JsonResource getResource(String key) {
		return getCache().get(key);
	}

	@Override
	public Container getContainer() {
		if(container==null) {
			this.setContainer(ResourceContainer.getContainer());
		}
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}
}
