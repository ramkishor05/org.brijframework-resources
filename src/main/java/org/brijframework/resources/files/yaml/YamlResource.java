package org.brijframework.resources.files.yaml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.brijframework.resources.files.MetaResource;
import org.yaml.snakeyaml.Yaml;

public class YamlResource extends MetaResource {

	public YamlResource(String path) {
		super(path);
	}
	
	public YamlResource(File path) {
		super(path);
	}

	public YamlResource() {
	}
	
	public Properties getProperties(){
		Properties properties=new Properties();
		HashMap<String, Object> map= getHashMap();
		String parentKey="";
		fillMap(properties, parentKey, map);
		return properties;
	}
	
	@SuppressWarnings("unchecked")
	private void fillMap(Properties properties,String parentKey,Map<String, Object> map) {
		for(Entry<String, Object> entry:map.entrySet()) {
			if(entry.getValue() instanceof Map) {
				fillMap(properties,parentKey.isEmpty()?entry.getKey(): parentKey+"."+entry.getKey(), (Map<String, Object>)entry.getValue()); 
			}else if(entry.getValue() instanceof List<?>){
				fillList(properties,parentKey.isEmpty()?entry.getKey(): parentKey+"."+entry.getKey(), (List<Object>)entry.getValue()); 
			}else {
				properties.put(parentKey.isEmpty()?entry.getKey():parentKey+"."+entry.getKey(), entry.getValue());
			}
		}
		if(!parentKey.isEmpty())
		properties.put(parentKey, map);
	}
	
	@SuppressWarnings("unchecked")
	private void fillList(Properties properties,String parentKey,List<Object> list) {
		int index=0;
		for(Object object:list) {
			if(object instanceof Map) {
				fillMap(properties, parentKey+"["+index+"]", (Map<String, Object>)object); 
			}else if(object instanceof List<?>){
				fillList(properties, parentKey+"["+index+"]", (List<Object>)object); 
			}else {
				properties.put(parentKey+"["+index+"]", object);
			}
			index++;
		}
		if(!parentKey.isEmpty())
		properties.put(parentKey, list);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getHashMap(){
		Yaml yaml = new Yaml();  
		try( InputStream in = getInputStream()) {
            return yaml.loadAs( in, HashMap.class );
        }catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
