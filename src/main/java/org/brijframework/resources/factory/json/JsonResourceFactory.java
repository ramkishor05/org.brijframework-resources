package org.brijframework.resources.factory.json;

import java.io.File;

import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.asm.AbstractResourceFactory;
import org.brijframework.resources.factory.file.FileResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.config.SingletonFactory;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.reflect.InstanceUtil;

public class JsonResourceFactory extends AbstractResourceFactory<String,JsonResource> implements FileResourceFactory<String,JsonResource>{
	
	private static JsonResourceFactory factory;

	@SingletonFactory
	public static JsonResourceFactory factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(JsonResourceFactory.class);
		}
		return factory;
	}
	
	@Override
	public void load(Resource metaResource){
		getCache().put(metaResource.getId(), (JsonResource)metaResource);
		getContainer().load(getResourceType().toString()).add(metaResource.getId(), (JsonResource)metaResource);
	}

	@Override
	public JsonResource build(String id, File file) {
		JsonResource resource=new JsonResource(file);
		resource.setId(id);
		return resource;
	}

	@Override
	public String getResourceType() {
		return ResourceType.JSON;
	}

	@Override
	protected void preregister(String key, JsonResource value) {
		
	}

	@Override
	protected void postregister(String key, JsonResource value) {
		
	}

	
}
