package org.brijframework.resources.factory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.asm.factories.FileFactory;
import org.brijframework.factories.Factory;
import org.brijframework.resources.Resource;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.util.resouces.ResourcesUtil;

public interface ResourceFactory extends Factory, FileFactory {
	
	public String getResourceType();
	
	public Collection<? extends Resource> getResources();

	void load(Resource metaResource);
	
	default ResourceFactory loadFactory() {
		System.err.println(this.getClass().getSimpleName()+ " loading... ");
		this.clear();
		try {
			for (File file : ResourcesUtil.getResources(All_INF)) {
				if(file.getName().endsWith(getResourceType()) && !isIgnoreFile(file)) {
					Resource resource=build(file);
					if(resource==null) {
						continue;
					}
					System.err.println("Resource     : "+resource.getId());
					load(resource);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println(this.getClass().getSimpleName()+ " done... ");
		return this;
	}

	ConcurrentHashMap<Object, ? extends Resource> getCache();
	
	@Override
	default ResourceFactory clear() {
		this.getCache().clear();
		return this;
	}

	public Resource build(File file);
	
	default boolean isIgnoreFile(File file) {
		if (MANIFEST_MF.equalsIgnoreCase(file.getName()) || POM_PROPERTIES.equalsIgnoreCase(file.getName())
				|| POM_XML.equalsIgnoreCase(file.getName())) {
			return true;
		}
		return false;
	}

	Collection<JsonResource> getResources(String dir);
	
	
}
