package com.saver;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Saver {

	private static final String FILE_NAME = "saver.db";
	private static File DATA_FILE = new File(FILE_NAME);

	private LinkedHashSet dataList;

	public static void dataFileLocation(String dir) {
		String delim = "/";
		if (dir.contains("\\"))
			delim = "\\";
		if (dir.endsWith(delim))
			dir += FILE_NAME;
		else
			dir = dir + delim + FILE_NAME;
		DATA_FILE = new File(dir);
	}

	public void save(Object data) {
		readData();
		dataList.add(data);
		writeData();
	}

	public void save(Collection cl) {
		if (isNotEmpty(cl)) {
			cl.stream().forEach(d -> {
				save(d);
			});
		}
	}

	public void delete(Collection cl) {
		if (isNotEmpty(cl)) {
			cl.forEach(d -> {
				delete(d);
			});
		}
	}

	public Set getAllData() {
		readData();
		return dataList;
	}

	public void deleteAllData() {
		if (DATA_FILE.exists())
			DATA_FILE.delete();
	}

	public void delete(Object o) {
		readData();
		if (isNotEmpty(dataList)) {
			if (dataList.contains(o)) {
				dataList.remove(o);
				writeData();
			}
		}
	}

	public boolean exists(Object data) {
		readData();
		if (isNotEmpty(dataList))
			return dataList.contains(data);
		else
			return false;
	}

	private void readData() {
		if (DATA_FILE.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(DATA_FILE);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				dataList = (LinkedHashSet) in.readObject();
				in.close();
				fileIn.close();
			} catch (Exception i) {
				i.printStackTrace();
			}
		} else
			dataList = new LinkedHashSet();
	}

	private void writeData() {
		try {
			FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dataList);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			i.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return dataList.toString();
	}

}
