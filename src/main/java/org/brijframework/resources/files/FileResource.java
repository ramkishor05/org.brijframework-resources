package org.brijframework.resources.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.asm.resources.DefaultResource;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.Access;

public class FileResource implements DefaultResource {

	private String path;
	private File file;
	private String id;
	
	public FileResource(File file) {
		this.setFile(file);
	}
	
	public FileResource(String path) {
		this.setPath(path);
	}
	
	public FileResource() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id=id;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public URI getURI() {
		if(this.getPath()==null){
			System.err.println("Path does not defined");
			return null;
		}
		return PathUtil.locateURIConfig(this.getPath());
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(getFile());
	}
	
	
	@Override
	public String toString() {
		AtomicInteger count=new AtomicInteger(0);
		StringBuilder builder=new StringBuilder();
		builder.append("{");
		List<Field> fields=FieldUtil.getAllField(this.getClass(), Access.PRIVATE_NO_STATIC_FINAL);
		fields.forEach(field->{
			try {
				builder.append(field.getName()+" = "+PropertyAccessorUtil.getProperty(this, field));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(count.getAndIncrement()<fields.size()) {
				builder.append(",");
			}
		});
		builder.append("}");
		return builder.toString();
	}
}
