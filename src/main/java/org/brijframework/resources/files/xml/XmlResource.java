package org.brijframework.resources.files.xml;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.brijframework.resources.Resource;
import org.brijframework.resources.files.json.JsonResource;
import org.json.JSONException;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlResource extends JsonResource{

	private String xmlresource;
	
	public XmlResource(String path) {
		super(path);
	}
	
	public XmlResource(File path) {
		super(path);
	}
	
	public XmlResource(Resource metaResource) {
		super(metaResource);
	}

	public XmlResource(String resource, boolean isPath) {
		if(isPath)
			setPath(resource);
		else
			setXmlResource(resource);
	}

	public XmlResource() {
	}

	@Override
	public String getJsonResource() {
		try {
			return keyReplace(XML.toJSONObject(getXmlResource()), "content", "value").toString();
		} catch (JSONException e) {
			System.err.println(getXmlResource());
		}
		return null;
	}
	
	public String getXmlResource() {
		if(xmlresource==null){
			xmlresource=getResource();
		}
		return xmlresource;
	}
	
	public void setXmlResource(String xmlresource){
		this.xmlresource=xmlresource;
	}

	public boolean isValidXml() {
		return getXmlResource() != null && getXmlResource() != "" && (getXmlResource().trim().startsWith("<") && getXmlResource().trim().endsWith(">"));
	}
	
	public Document document() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(getXmlResource())));
	}
	
	public String source() throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document());
		StreamResult result =  new StreamResult(new StringWriter());
		transformer.transform(source, result);
		return result.getWriter().toString();
	}
}
