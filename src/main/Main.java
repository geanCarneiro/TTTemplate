package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ProgressMonitor;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import javafx.application.Application;
import javafx.stage.Stage;
import main.utils.FileResourceUtils;
import main.utils.ListaNomeada;
import main.utils.TweetPreset;
import main.utils.TweetPresetNode;
import main.view.telaPrincipal.TelaPrincipalFactory;
import main.view.telaPrincipal.TelaPrincipalFactory;
import twitter4j.auth.AccessToken;

public class Main extends Application{
	
	public static List<TweetPresetNode> presets = Arrays.asList();
	
	public static List<ListaNomeada> listas = Arrays.asList();
	
	public static AccessToken accessToken = null;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		TelaPrincipalFactory.createView();
	}
	
	public static void main(String[] args) throws Exception{
		Main.accessToken = FileResourceUtils.getObjectFromFile(FileResourceUtils.ACCESS_TOKEN_FILE);
		
		File listaFile = FileResourceUtils.getFileFromPath(FileResourceUtils.LISTAS_FILE);
		File templateFile = FileResourceUtils.getFileFromPath(FileResourceUtils.PRESET_FILE);
		
		if(!listaFile.exists()) criarListaPadrao(listaFile);
		
		if(!templateFile.exists()) criarTemplatePadrao(templateFile);
		
		Main.listas = FileResourceUtils.getListas();
		Main.presets = FileResourceUtils.loadPreset();
		Application.launch(Main.class);
//		launch();
		
	}
	
	private static void criarTemplatePadrao(File file) {
		
		List<TweetPresetNode> templatePadrao =  Arrays.asList(
				new TweetPresetNode("Exemplos de Preset", Arrays.asList(
						new TweetPresetNode("Aniversario", "{nome:lnome} fez {idade:n} no dia {aniversario:ddd/MM} e comeu {o_que_ele_comeu}", true),
						new TweetPresetNode("dia do feriado", "{dia:d} será {feriado}", false)
					))
		);
		
		FileResourceUtils.saveObjectToJson(file, templatePadrao);
		
	}

	private static void criarListaPadrao(File file) {
		
		List<ListaNomeada> listaPadrao = Arrays.asList(
				new ListaNomeada("nome", Arrays.asList("Gean", "Felipe", "André"))
			);
		
		FileResourceUtils.saveObjectToJson(file, listaPadrao);
		
	}

	public static boolean isConectado() {
		return accessToken != null;
	}
	
}
