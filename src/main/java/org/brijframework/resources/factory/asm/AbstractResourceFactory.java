package org.brijframework.resources.factory.asm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.brijframework.factories.impl.AbstractFactory;
import org.brijframework.resources.Resource;
import org.brijframework.resources.config.impl.ResourceConfigration;
import org.brijframework.resources.constants.ResourceConstants;
import org.brijframework.resources.factory.ResourceFactory;
import org.brijframework.resources.locator.ResourceLocator;
import org.brijframework.util.printer.ConsolePrint;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.resouces.ResourcesUtil;

public abstract class AbstractResourceFactory<K, T extends Resource> extends AbstractFactory<K, T> implements ResourceFactory<K, T>{

	public ResourceFactory<K,T> loadFactory() {
		ConsolePrint.screen("ResourceFactory -> "+this.getClass().getSimpleName(),"Lunching factory to load resource");
		this.clear();
		List<ResourceConfigration> configs=getConfigration();
		if(configs==null) {
			ConsolePrint.screen("ResourceConfigration","Invalid Resource configration : "+configs);
			return this;
		}
		for(ResourceConfigration modelConfig:configs) {
			if(!modelConfig.isEnable()) {
				ConsolePrint.screen("ResourceConfigration","Resource configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			try {
				String dirPath=modelConfig.getLocation().startsWith("classpath:")? modelConfig.getLocation().split("classpath:")[1]: modelConfig.getLocation();
				for (File file : ResourcesUtil.getResources(dirPath)) {
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
		}
		ConsolePrint.screen("ResourceFactory -> "+this.getClass().getSimpleName(),"Lunched factory to load resource");
		return this;
	}
	
	@Override
	public Collection<T> getResources() {
		return getCache().values();
	}
	
	@Override
	public Collection<T> getResources(K dir) {
		String dirPath=dir.toString().startsWith("classpath:")? dir.toString().split("classpath:")[1]: dir.toString();
		Collection<T> list=new ArrayList<>();
		for(T resource:getCache().values()) {
			String absolutePath=resource.getFile().getAbsolutePath();
			String paths[]=absolutePath.split("classes");
			if(paths.length<1 ) {
				continue;
			}
			if(paths[1].startsWith(dirPath.replace('/', '\\').replace('\\', '\\'))) {
				list.add(resource);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceConfigration> getConfigration() {
		List<ResourceConfigration> configrations=new ArrayList<>();
		Object resources=getContainer().getContext().getEnvironment().get(ResourceConstants.APPLICATION_RESOURCE_CONFIG);
		if (resources==null) {
			ConsolePrint.screen("ResourceConfigration", "Resource configration not found :"+ResourceConstants.APPLICATION_RESOURCE_CONFIG);
		}else {
			ConsolePrint.screen("ResourceConfigration", "Resource configration found :"+ResourceConstants.APPLICATION_RESOURCE_CONFIG+" | "+resources);
			if(resources instanceof List) {
				configrations= build((List<Map<String, Object>>)resources);
			}else if(resources instanceof Map) {
				configrations= build((Map<String, Object>)resources);
			}
		}
		if(configrations.isEmpty()) {
			configrations.add(new ResourceConfigration(All_INF,true));
		}
		return configrations;
	}
	
	private List<ResourceConfigration> build(Map<String, Object> resource) {
		List<ResourceConfigration> configs=new ArrayList<ResourceConfigration>();
		configs.add(InstanceUtil.getInstance(ResourceConfigration.class, resource));
		return configs;
	}

	private List<ResourceConfigration> build(List<Map<String, Object>> resources) {
		List<ResourceConfigration> configs=new ArrayList<ResourceConfigration>();
		for(Map<String, Object> resource:resources) {
			configs.add(InstanceUtil.getInstance(ResourceConfigration.class, resource));
		}
		return configs;
	}
}
