package org.brijframework.resources.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.brijframework.asm.resources.DefaultResource;


public class ClassResource implements DefaultResource{

	public static final String CLASS_INF = "/classes";
	
	private String path;
	private Class<?> model;
	private String id;

	public ClassResource(Class<?> _class) {
		this.setPath(_class.getResource(_class.getSimpleName() + ".class").getFile());
		this.setModel(_class);
	}

	public ClassResource(String _path) {
		this(getClass(_path));
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private static Class<?> getClass(String _path) {
		try {
			return Class.forName(_path);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.getFile());
	}

	public File getFile() {
		return null;
	}

	public URI getURI() {
		return null;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Class<?> getModel() {
		return model;
	}

	public void setModel(Class<?> model) {
		this.model = model;
	}

}
