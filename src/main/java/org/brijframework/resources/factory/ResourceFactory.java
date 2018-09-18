package org.brijframework.resources.factory;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.factories.Factory;
import org.brijframework.group.Group;
import org.brijframework.resources.Resource;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.resouces.JSONUtil;

public interface ResourceFactory extends Factory {
    
	public ResourceType getResourceType();
	
	public Collection<? extends Resource> getResources();

	void load(Resource metaResource);
	
	@SuppressWarnings("unchecked")
	default ResourceFactory loadFactory() {
		this.clear();
		Group group =ResourceContainer.getContainer().get(getResourceType());
		Set<Entry<String, Resource>> entriesgroup = group.getCache().entrySet();
		for (Entry<String, Resource> resource : entriesgroup) {
			Resource metaResource = resource.getValue();
			if (metaResource.getExtension().equals(getResourceType().toString())) {
				load(metaResource);
			}
		}
		return this;
	}

	ConcurrentHashMap<Object, ? extends Resource> getCache();
	
	@Override
	default ResourceFactory clear() {
		this.getCache().clear();
		return this;
	}
	
	@Override
	default String getJson() {
		return JSONUtil.getJsonStringFromObject(this.getCache());
	}
	
}
