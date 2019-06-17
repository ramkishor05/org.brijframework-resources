package org.brijframework.resources.files.yaml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.brijframework.resources.files.MapResource;
import org.brijframework.resources.files.MetaResource;
import org.brijframework.util.objects.PropertiesUtil;
import org.brijframework.util.resouces.YamlUtil;

public class YamlResource extends MetaResource implements MapResource{

	public YamlResource(String path) {
		super(path);
	}
	
	public YamlResource(File path) {
		super(path);
	}

	public YamlResource() {
	}
	
	@Override
	public Properties getProperties(){
		return PropertiesUtil.getProperties(getHashMap());
	}
	
	@Override
	public HashMap<String, Object> getHashMap(){
		try( InputStream in = getInputStream()) {
            return YamlUtil.getHashMap(in);
        }catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
