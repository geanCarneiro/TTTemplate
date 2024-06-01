package com.gean.tttemplate.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import javax.imageio.ImageIO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class FileResourceUtils {
	
	public static final String PRESET_FILE = "presets.json";
	public static final String LISTAS_FILE = "listas.json";
	public static final String USER_INFO_FILE = "USERINFO";
	public static final String OAUTH1_ACCESS_TOKEN = "AUTH1ACCCESSTOKEN";
	public static final String LOGO_FILE = "twitter.png";
	
	
	public static List<TweetPresetNode> loadPreset() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file = new File(PRESET_FILE);
			
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
			File file = new File(LISTAS_FILE);
						
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
                    
                    final List<ListaNomeada> listasFiltrada2 = Arrays.asList();
                                        
                    List<ListaNomeada> listasFiltrada = listas.stream().filter(lista -> lista.getNome().equalsIgnoreCase(nome))
					.collect(Collectors.toList());
			
			if(listasFiltrada.size() > 0) 
				return listasFiltrada.get(0);			 
			
		}
		
		return new ListaNomeada();
	}
	
	public static void saveObjectOnFile(String file, Object obj) {
		File atf = new File(file);
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
		File atf = new File(file);
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
	
	public static void saveObjectToJson(String jsonFile, Object obj) {
		if(!jsonFile.split("[.]")[1].equalsIgnoreCase("json")) {
			System.err.println(jsonFile + " não é um arquivo Json");
			return;
		}
		
		File arquivoJson = new File(jsonFile);
		
		saveObjectToJson(arquivoJson, obj);
		
	}
	
	public static void saveObjectToJson(File jsonFile, Object obj) {
		
		JsonMapper mapper = new JsonMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try {
			mapper.writeValue(jsonFile, obj);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;			
		}
		
	}
	
	
	public static Image getImageFromPath(String path) {		
		File fileImage = new File(path);
		
		try {
                    System.out.println(fileImage.getAbsolutePath());
                    return ImageIO.read(fileImage);
		} catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
		}
	}
	
        public static List<Map<String, String>> xlsToList(File excelFile) throws Exception {
		ArrayList<Map<String, String>> out = new ArrayList<>();
		
		try(FileInputStream fis = new FileInputStream(excelFile)) {
			try(Workbook wb = new HSSFWorkbook(fis)){
				
				Sheet planilha = wb.getSheetAt(0);
				
				for(int i = planilha.getFirstRowNum()+1; i <= planilha.getLastRowNum(); i++) {
					HashMap<String, String> params = new HashMap<>();
					Row row = planilha.getRow(i);
					if(row == null)	continue;
					if(row.getCell(row.getFirstCellNum()).getCellType() == CellType.BLANK) continue;
					
					for(Cell c: row) {
						String label = planilha.getRow(planilha.getFirstRowNum()).getCell(c.getColumnIndex()).getStringCellValue();
						
						HSSFDataFormatter hdf = new HSSFDataFormatter();
						params.put(label, hdf.formatCellValue(c));
						
						
					}
					out.add(params);
				}
				
			}
		}
		
		
		return out;
	}
	
	
}
