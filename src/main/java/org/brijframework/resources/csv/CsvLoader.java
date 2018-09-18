package org.brijframework.resources.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class CsvLoader {

	public List<RowObject> insertArray = new ArrayList<>();

	public List<RowObject> updateArray = new ArrayList<>();

	public List<RowObject> extraArray = new ArrayList<>();

	public List<RowObject> failedArray = new ArrayList<>();

	private CsvModel csvModel;

	public void setCsvModel(CsvModel csvModel) {
		this.csvModel = csvModel;
	}

	public CsvModel getCsvModel() {
		return csvModel;
	}

	public abstract CsvModel getKeysMapper();

	public abstract Collection<?> dataArray();

	@SuppressWarnings("unchecked")
	public boolean isContainRecord(Map<String, Object> contains)  {
		for (Object oldObject : dataArray()) {
			if (oldObject instanceof Map) {
				Map<Object, Object> mapObject = (Map<Object, Object>) oldObject;
				Set<Entry<String, Object>> entries = contains.entrySet();
				for (Entry<String, Object> entry : entries) {
					Object oldValue = mapObject.get(entry.getKey());
					Object newValue = contains.get(entry.getKey());
					if((oldValue!=null || newValue!=null) && oldValue.toString().equals(newValue.toString()) ){
						return true;
					}
				}
			}else if (oldObject instanceof Collection<?>) {
				System.err.println("collection is not allowed");
			}else{
				Set<Entry<String, Object>> entries = contains.entrySet();
				for (Entry<String, Object> entry : entries) {
					try {
						Field field = oldObject.getClass().getDeclaredField(entry.getKey().toString());
						field.setAccessible(true);
						Object oldValue = field.get(oldObject);
						Object newValue = contains.get(entry.getKey());
						if((oldValue!=null || newValue!=null) && oldValue.toString().equals(newValue.toString()) ){
							return true;
						}
					} catch (NoSuchFieldException | SecurityException|IllegalArgumentException |IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public void loadFromCsvFile(File file) throws IOException {
		StringBuilder buffer = new StringBuilder();
		Reader fileReader = new FileReader(file);
		int data = fileReader.read();
		while (data != -1) {
			buffer.append((char) data);
			data = fileReader.read();
		}
		fileReader.close();
		loadFromCsvBuffer(buffer.toString());
	}

	public void loadFromCsvBuffer(String csvData) {
		if (csvData == null || csvData.isEmpty()) {
			return;
		}
		String[] csvDataArray = csvData.trim().split("\n");
		if (csvDataArray.length == 0) {
			System.err.println("Invalid csv file ");
		}
		String[] headerKeys = csvDataArray[0].split(",");
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 1; i < csvDataArray.length; i++) {
			if (csvDataArray[i].trim().isEmpty()) {
				continue;
			}
			String[] dataValues = csvDataArray[i].trim().split(",");
			Map<String, Object> map = new HashMap<>();
			for (int j = 0; j < headerKeys.length; j++) {
				String key = headerKeys[j];
				Object value = "";
				if (j < dataValues.length) {
					value = dataValues[j];
				}
				map.put(key, value);
			}
			list.add(map);
		}
		loadFromCsvArray(list);
	}

	public void loadFromCsvArray(List<Map<String, Object>> csvArray) {
		for (Map<String, Object> csvHash : csvArray) {
			RowObject rowObject = new RowObject();
			rowObject.csvObject = csvHash;
			rowObject.rowObject = mappedHash(csvHash);
			if (!isValidRecord(csvHash)) {
				rowObject.isError = true;
				rowObject.message = errorMsg(csvHash);
				failedArray.add(rowObject);
				continue;
			}
			if (isExtraRecord(csvHash)) {
				rowObject.isError = true;
				rowObject.message = extraMsg(csvHash);
				extraArray.add(rowObject);
				continue;
			}
			boolean isUpdate = this.isContainRecord(rowObject.rowObject);
			if (isUpdate) {
				this.updateArray.add(rowObject);
			} else {
				this.insertArray.add(rowObject);
			}
		}
	}

	public boolean isExtraRecord(Map<String, Object> csvHash) {
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> csvfieldHash = csvModel.csvFieldHash();
		for (String mappedkey : csvHash.keySet()) {
			CsvMapper field = csvfieldHash.get(mappedkey);
			if (field == null) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidRecord(Map<String, Object> csvHash) {
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> csvfieldHash = csvModel.csvFieldHash();
		for (String mappedkey : csvfieldHash.keySet()) {
			CsvMapper field = csvfieldHash.get(mappedkey);
			if (field.isRequied && mappedKey(csvHash, field.headerkey) == null) {
				return false;
			}
		}
		return true;
	}

	public boolean isExistsRecord(Map<String, Object> csvHash) {
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> mdlfieldHash = csvModel.mdlFieldHash();
		Map<String, Object> contains = new HashMap<>();
		for (String key : csvModel.uniqueKeys.split("~")) {
			contains.put(key, mappedValue(csvHash, mdlfieldHash.get(key).headerkey));
		}
		return this.isContainRecord(contains);
	}

	public String extraMsg(Map<String, Object> csvHash) {
		StringBuilder builder = new StringBuilder();
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> csvfieldHash = csvModel.csvFieldHash();
		int count = 0;
		int size = csvHash.keySet().size();
		for (String key : csvHash.keySet()) {
			CsvMapper mappedKey = csvfieldHash.get(key);
			if (mappedKey == null) {
				builder.append("\"" + key + "\" key is remove from extra keys");
			}
			count++;
			if (count < size) {
				builder.append(" , ");
			}
		}
		return builder.toString();
	}

	public String errorMsg(Map<String, Object> csvHash) {
		StringBuilder builder = new StringBuilder();
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> csvfieldHash = csvModel.csvFieldHash();
		int count = 0;
		int size = csvfieldHash.keySet().size();
		for (String key : csvfieldHash.keySet()) {
			String mappedKey = mappedKey(csvHash, key);
			if (mappedKey == null) {
				builder.append("\"" + key + "\"");
			}
			count++;
			if (count < size) {
				builder.append(" , ");
			}
			if (count == size) {
				builder.append(" key is missing");
			}
		}
		return builder.toString();
	}

	protected String mappedKey(Map<String, Object> csvHash, String key) {
		for (String cKey : csvHash.keySet()) {
			if (cKey.equalsIgnoreCase(key)) {
				return cKey;
			}
		}
		return null;
	}

	protected Object mappedValue(Map<String, Object> csvHash, String key) {
		String mappedKey = mappedKey(csvHash, key);
		return mappedKey != null ? csvHash.get(mappedKey) : null;
	}

	public Map<String, Object> mappedHash(Map<String, Object> csvHash) {
		Map<String, Object> mappedHash = new HashMap<>();
		CsvModel csvModel = getKeysMapper();
		Map<String, CsvMapper> csvfieldHash = csvModel.csvFieldHash();
		for (String mappedkey : csvfieldHash.keySet()) {
			CsvMapper validation = csvfieldHash.get(mappedkey);
			mappedHash.put(validation.mappedkey, csvHash.get(mappedKey(csvHash, validation.headerkey)));
		}
		return mappedHash;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\n");
		buffer.append("\ninsertArray=" + insertArray);
		buffer.append("\nupdateArray=" + updateArray);
		buffer.append("\nextraArray =" + extraArray);
		buffer.append("\nfailedArray=" + failedArray);
		buffer.append("\n}");
		return buffer.toString();
	}
}
