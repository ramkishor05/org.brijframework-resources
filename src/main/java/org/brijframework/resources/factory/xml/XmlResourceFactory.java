package org.brijframework.resources.factory.xml;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.FileResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.files.xml.XmlResource;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class XmlResourceFactory implements FileResourceFactory {
	
	private ConcurrentHashMap<Object, XmlResource> cache = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static XmlResourceFactory factory;

	@Assignable
	public static XmlResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(XmlResourceFactory.class);
		}
		return factory;
	}
	
	@Override
	public void load(Resource metaResource) {
		getCache().put(metaResource.getId(), (XmlResource)metaResource);
		getContainer().load(getResourceType()).add(metaResource.getId(), (JsonResource)metaResource);
	}
	
	@Override
	public XmlResource build(String id, File file) {
		XmlResource resource = new XmlResource(file);
		resource.setId(id);
		return resource;
	}
	
	@Override
	public ConcurrentHashMap<Object, XmlResource> getCache() {
		return cache;
	}

	@Override
	public String getResourceType() {
		return ResourceType.XML;
	}

	@Override
	public Collection<XmlResource> getResources() {
		return cache.values();
	}

	public XmlResource getResource(String key) {
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
