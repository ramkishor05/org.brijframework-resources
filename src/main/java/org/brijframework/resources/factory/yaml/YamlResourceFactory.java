package org.brijframework.resources.factory.yaml;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.asm.AbstractResourceFactory;
import org.brijframework.resources.factory.env.EnvResourceFactory;
import org.brijframework.resources.files.yaml.YamlResource;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class YamlResourceFactory extends AbstractResourceFactory<String,YamlResource>  implements EnvResourceFactory<String,YamlResource> {
	
	private ConcurrentHashMap<String,YamlResource>cache = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static YamlResourceFactory factory;

	@SingletonFactory
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
	public YamlResource build(String id, File file) {
		YamlResource resource = new YamlResource(file);
		resource.setId(id);
		return resource;
	}
	
	@Override
	public ConcurrentHashMap<String,YamlResource> getCache() {
		return cache;
	}

	@Override
	public String getResourceType() {
		return ResourceType.YML;
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
	protected void preregister(String key, YamlResource value) {
	}

	@Override
	protected void postregister(String key, YamlResource value) {
	}
	
}