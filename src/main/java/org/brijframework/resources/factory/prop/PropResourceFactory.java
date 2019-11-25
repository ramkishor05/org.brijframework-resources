package org.brijframework.resources.factory.prop;

import java.io.File;

import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.asm.AbstractResourceFactory;
import org.brijframework.resources.factory.env.EnvResourceFactory;
import org.brijframework.resources.files.prop.PropResource;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class PropResourceFactory extends AbstractResourceFactory<String,PropResource> implements EnvResourceFactory<String,PropResource> {
	
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
	public PropResource build(String id, File file) {
		PropResource resource = new PropResource(file);
		resource.setId(id);
		return resource;
	}
	
	@Override
	public String getResourceType() {
		return ResourceType.PROP;
	}

	@Override
	protected void preregister(String key, PropResource value) {
	}

	@Override
	protected void postregister(String key, PropResource value) {
	}

}
