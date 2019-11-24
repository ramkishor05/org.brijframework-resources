package org.brijframework.resources.factory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.factories.impl.FileFactory;
import org.brijframework.resources.Resource;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.resources.locator.ResourceLocator;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.resouces.ResourcesUtil;

public interface ResourceFactory<K,T> extends FileFactory<K,T> {
	
	public String getResourceType();
	
	public Collection<? extends Resource> getResources();

	void load(Resource metaResource);
	
	default ResourceFactory<K,T> loadFactory() {
		ConsolePrint.screen("ResourceFactory -> "+this.getClass().getSimpleName(),"Lunching factory to load resource");
		this.clear();
		try {
			for (File file : ResourcesUtil.getResources(All_INF)) {
				if(file.getName().endsWith(getResourceType()) && !isIgnoreFile(file)) {
					String id=null;
					if(file.getAbsolutePath().contains("classes")) {
						id=ResourceLocator.CLASSPATH.getLocator()+file.getAbsolutePath().split("classes")[1];
					}else {
						id=ResourceLocator.FILEPATH.getLocator()+file.getAbsolutePath();
					}
					Resource resource=build(id,file);
					if(resource==null) {
						continue;
					}
					ConsolePrint.screen("Resource ","Load resource with id : "+resource.getId());
					load(resource);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConsolePrint.screen("ResourceFactory -> "+this.getClass().getSimpleName(),"Lunched factory to load resource");
		return this;
	}

	ConcurrentHashMap<K,T> getCache();
	
	public Resource build(String id, File file);
	
	default boolean isIgnoreFile(File file) {
		if (MANIFEST_MF.equalsIgnoreCase(file.getName()) || POM_PROPERTIES.equalsIgnoreCase(file.getName())
				|| POM_XML.equalsIgnoreCase(file.getName())) {
			return true;
		}
		return false;
	}

	Collection<JsonResource> getResources(String dir);

	
}
