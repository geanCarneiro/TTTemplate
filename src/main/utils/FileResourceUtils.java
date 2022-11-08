package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.Main;

public class FileResourceUtils {
	
	public static final String PRESET_FILE = "presets.json";
	public static final String LISTAS_FILE = "listas.json";
	public static final String ACCESS_TOKEN_FILE = "ACCESSTOKEN";
	
	
	public static List<TweetPresetNode> loadPreset() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file = getFileFromPath(PRESET_FILE);
			
			List<TweetPresetNode> presets = mapper.readValue(file, new TypeReference<List<TweetPresetNode>>() {});
			
			return presets;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		return new ArrayList<>();
	}
	
	public static List<ListaNomeada> getListas() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file = getFileFromPath(LISTAS_FILE);
						
			List<ListaNomeada> listas = mapper.readValue(file, new TypeReference<List<ListaNomeada>>() {});
			
			return listas;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		return new ArrayList<>();
	}
	
	public static ListaNomeada getLista(String nome) {
		
		List<ListaNomeada> listas = getListas();
		
		if(listas.size() > 0) {
			
			return listas.stream()
					.filter(lista -> lista.getNome().equalsIgnoreCase(nome))
					.collect(Collectors.toList()).get(0);
			
		}
		
		return new ListaNomeada();
	}
	
	public static void saveObjectOnFile(String file, Object obj) {
		File atf = FileResourceUtils.getFileFromPath(file);
		try {
			atf.createNewFile();
						
			try (FileOutputStream fos = new FileOutputStream(atf)) {
				try(ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(obj);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static <T> T getObjectFromFile(String file) {
		File atf = FileResourceUtils.getFileFromPath(file);
		try (FileInputStream fis = new FileInputStream(atf)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				return (T) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public static File getFileFromPath(String path) {		
		if(new File("src/").exists()) {
			return new File("src/" + path);
		} else {
			return new File(path);
		}
	}
	
	
	
}
