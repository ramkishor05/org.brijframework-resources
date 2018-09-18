package org.brijframework.resources.factory.xml;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.container.ResourceContainer;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.files.xml.XmlResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class XmlResourceFactory implements ResourceFactory {
	
	private ConcurrentHashMap<Object, XmlResource> containers = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static XmlResourceFactory factory;

	public static XmlResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(XmlResourceFactory.class);
			factory.loadFactory();
		}
		return factory;
	}

	@Override
	public void load(Resource metaResource) {
		containers.put(metaResource.getId(), new XmlResource(metaResource));
	}
	
	@Override
	public ConcurrentHashMap<Object, XmlResource> getCache() {
		return containers;
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.XML;
	}

	@Override
	public Collection<XmlResource> getResources() {
		return containers.values();
	}

	public XmlResource getResource(String key) {
		return containers.get(key);
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
