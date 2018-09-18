package org.brijframework.resources.files.json;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.brijframework.resources.Resource;
import org.brijframework.resources.files.MetaResource;
import org.brijframework.support.constants.Constants;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.resouces.JSONUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonResource extends MetaResource {
	
	private String jsonresource;
	
	public JsonResource() {
	}

	public JsonResource(String resource) {
		setJsonResource(resource);
	}

	public JsonResource(File path) {
		this.setFile(path);
	}
	
	public JsonResource(Resource metaResource) {
		this.setFile(metaResource.getFile());
		this.setPath(metaResource.getPath());
	}

	public void setJsonResource(String jsonresource){
		this.jsonresource=jsonresource;
	}
	
	public String getJsonResource() {
		if(jsonresource==null){
			if(this.getPath()!=null)
				setJsonResource(getFileSource());
			if(this.getFile()!=null)
				setJsonResource(getFileSource());
		}
		return jsonresource;
	}

	public boolean isValidJson() {
		return getJsonResource() != null && getJsonResource() != ""
				&& ((getJsonResource().trim().startsWith("{") && getJsonResource().trim().endsWith("}"))
						|| (getJsonResource().trim().startsWith("[") && getJsonResource().trim().endsWith("]")));
	}

	public boolean isJsonObject() {
		if (this.isValidJson()) {
			return getJsonResource().trim().startsWith("{") && getJsonResource().trim().endsWith("}");
		}
		return false;
	}

	public boolean isJsonList() {
		if (this.isValidJson()) {
			return getJsonResource().trim().startsWith("[") && getJsonResource().trim().endsWith("]");
		}
		return false;
	}

	public JSONObject toJsonObject() {
		if (this.isJsonObject()) {
			return JSONUtil.getJSONObjectFromJSONString(getJsonResource());
		}
		return null;
	}

	public JSONArray toJsonList() {
		if (this.isJsonList()) {
			return JSONUtil.getJSONArrayFromJSONString(getJsonResource());
		}
		if (this.isJsonObject()) {
			return new JSONArray().put(JSONUtil.getJSONObjectFromJSONString(getJsonResource()));
		}
		return null;
	}

	public boolean containsKey(String keys) {
		try {
			if (this.isJsonObject()) {
				JSONObject jsonObject = toJsonObject();
				return containsKey(jsonObject, keys);
			}
			if (this.isJsonList()) {
				JSONArray array = toJsonList();
				for (int i = 0; i < array.length(); i++) {
					boolean isContain = containsKey(array.getJSONObject(i), keys);
					if (!isContain) {
						return false;
					}
				}
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean containsKey(JSONObject jsonObject, String keys) throws JSONException {
		Assertion.notNull(jsonObject, AssertMessage.arg_null_or_empty_message);
		String[] keySet = keys.split(Constants.SPLIT_DOT);
		JSONObject current = jsonObject;
		int i = 0;
		for (; i < keySet.length - 1; i++) {
			if (!jsonObject.has(keySet[i])) {
				return false;
			}
			current = jsonObject.getJSONObject(keySet[i]);
		}
		return current.has(keySet[i]);
	}

	@SuppressWarnings("unchecked")
	public JSONObject keyReplace(JSONObject contant, String oldKey, String newKey) throws JSONException {
		Assertion.notNull(contant, AssertMessage.arg_null_or_empty_message);
		JSONObject json = new JSONObject();
		Iterator<String> keys = contant.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = contant.get(key);
			if (value instanceof JSONObject) {
				value = keyReplace((JSONObject) value, oldKey, newKey);
			} else if (value instanceof JSONArray) {
				JSONArray array = (JSONArray) value;
				value = keyReplace(array, oldKey, newKey);
			}
			if (key.equals(oldKey)) {
				json.put(newKey, value);
			} else {
				json.put(key, value);
			}
		}
		return json;
	}

	private JSONArray keyReplace(JSONArray contant, String oldKey, String newKey) throws JSONException {
		Assertion.notNull(contant, AssertMessage.arg_null_or_empty_message);
		JSONArray json = new JSONArray();
		for (int i = 0; i < contant.length(); i++) {
			if (contant.get(i) instanceof JSONObject) {
				json.put(keyReplace(contant.getJSONObject(i), oldKey, newKey));
			} else {
				json.put(contant.get(i));
			}
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	public <T> T toJson(){
		if (this.isJsonObject()) {
			return (T) toJsonObject();
		}
		if (this.isJsonList()) {
			return (T) toJsonList();
		}
		return null;
	}
	
	public <T> T getProperty(String keys) {
		if (this.isJsonObject()) {
			return getPropertyFromJsonObject(keys);
		}
		if (this.isJsonList()) {
			return getPropertyFromJsonList(keys);
		}
		return null;
	}
	
	
	public JSONArray getPropertyList(String keys) {
		Object _json=null;
		if (this.isJsonObject()) {
			_json= getPropertyFromJsonObject(keys);
		}
		if (this.isJsonList()) {
			_json= getPropertyFromJsonList(keys);
		}
		if(_json instanceof JSONObject ){
			return new JSONArray().put(_json);
		}
		return _json!=null?(JSONArray) _json:new JSONArray();
	}

	@SuppressWarnings("unchecked")
	private <T> T getPropertyFromJsonObject(String keys) {
		try {
			return (T) getProperty(toJsonObject(), keys);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPropertyFromJsonList(String keys) {
		try {
			return (T) getProperty(toJsonList(), keys);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray getProperty(JSONArray jsonArray, String keys) throws JSONException {
		JSONArray current = new JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			if (!(jsonArray.get(i) instanceof JSONObject)) {
				break;
			}
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Object object = getProperty(jsonObject, keys);
			if (object != null) {
				current.put(object);
			}
		}
		return current;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(JSONObject jsonObject, String keys) throws JSONException {
		String[] keySet = keys.split(Constants.SPLIT_DOT);
		JSONObject current = jsonObject;
		int i = 0;
		for (; i < keySet.length - 1; i++) {
			if (!(jsonObject.get(keySet[i]) instanceof JSONObject)) {
				return null;
			}
			current = jsonObject.getJSONObject(keySet[i]);
		}
		return (T) current.get(keySet[i]);
	}
	/*
	 *  for object ---------------------------------------------------------------------------------------------------------------
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> toMapList(){
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			for (Object object : toObjectList()) {
				if (object instanceof Map) {
					list.add((Map<String, Object>) object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Object> toObjectList() throws JSONException {
		List<Object> list = new ArrayList<>();
		if (this.isJsonObject()) {
			list.add(JSONUtil.getMapFromJSONString(getJsonResource()));
		}
		if (this.isJsonList()) {
			return JSONUtil.getListFromJSONArray(toJsonList());
		}
		return list;
	}

	public Map<String, Object> toObjectMap() throws JSONException {
		if (this.isJsonObject()) {
			return JSONUtil.getMapFromJSONObject(toJsonObject());
		}
		return null;
	}
	
	public List<Map<String, Object>>  getMapListForKey(String keys){
		List<Map<String, Object>> list=new ArrayList<>();
		JSONArray array=this.getPropertyList(keys);
		for(int i=0;i<array.length();i++){
			try {
				if(array.get(i) instanceof JSONObject){
					list.add(JSONUtil.getMap(array.getJSONObject(i)));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
