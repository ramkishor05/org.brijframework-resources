package org.brijframework.resources.factory.yaml;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.EnvResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.files.yaml.YamlResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;

public class YamlResourceFactory implements EnvResourceFactory {
	
	private ConcurrentHashMap<Object, YamlResource> cache = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static YamlResourceFactory factory;

	@Assignable
	public static YamlResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(YamlResourceFactory.class);
		}
		return factory;
	}
	
	@Override
	public void load(Resource metaResource) {
		cache.put(metaResource.getId(), (YamlResource)metaResource);
		getContainer().load(getResourceType()).add(metaResource.getId(), (YamlResource)metaResource);
	}
	
	@Override
	public YamlResource build(File file) {
		YamlResource resource = new YamlResource(file);
		String id=file.getAbsolutePath().contains("classes")? file.getAbsolutePath().split("classes")[1]: file.getAbsolutePath();
		resource.setId(id);
		return resource;
	}
	
	@Override
	public ConcurrentHashMap<Object, YamlResource> getCache() {
		return cache;
	}

	@Override
	public String getResourceType() {
		return ResourceType.YML;
	}

	@Override
	public Collection<YamlResource> getResources() {
		return getCache().values();
	}

	public YamlResource getResource(String key) {
		return getCache().get(key);
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