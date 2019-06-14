package org.brijframework.resources.factory.json;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.resources.Resource;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;

public class JsonResourceFactory implements ResourceFactory{
	
	private ConcurrentHashMap<Object, JsonResource> cache = new ConcurrentHashMap<>();
	
	private Container container;
	
	private static JsonResourceFactory factory;

	@Assignable
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
		System.err.println("Resource     : "+metaResource.getFile());
	}

	@Override
	public JsonResource build(File file) {
		JsonResource resource=new JsonResource(file);
		resource.setId(file.toString());
		return resource;
	}

	@Override
	public ConcurrentHashMap<Object, JsonResource> getCache() {
		return cache;
	}

	@Override
	public String getResourceType() {
		return ResourceType.JSON;
	}
	
	@Override
	public Collection<JsonResource> getResources(String dir) {
		String dirPath=dir.startsWith("classpath:")? dir.split("classpath:")[1]: dir;
		Collection<JsonResource> list=new ArrayList<>();
		for(JsonResource resource:getCache().values()) {
			String absolutePath=resource.getFile().getAbsolutePath();
			String paths[]=absolutePath.split("classes");
			if(paths.length<1 ) {
				continue;
			}
			if(paths[1].startsWith(dirPath.replace('/', '\\'))) {
				list.add(resource);
			}
		}
		return list;
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
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

}
