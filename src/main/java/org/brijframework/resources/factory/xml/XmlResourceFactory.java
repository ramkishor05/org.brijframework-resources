package org.brijframework.resources.factory.xml;

import java.io.File;

import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.asm.AbstractResourceFactory;
import org.brijframework.resources.factory.file.FileResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.files.xml.XmlResource;
import org.brijframework.support.constants.ResourceType;
import org.brijframework.support.factories.SingletonFactory;
import org.brijframework.util.reflect.InstanceUtil;

public class XmlResourceFactory extends AbstractResourceFactory<String,XmlResource> implements FileResourceFactory<String,XmlResource> {
	
	private static XmlResourceFactory factory;

	@SingletonFactory
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
	public String getResourceType() {
		return ResourceType.XML;
	}

	@Override
	protected void preregister(String key, XmlResource value) {
		
	}

	@Override
	protected void postregister(String key, XmlResource value) {
		
	}

}
