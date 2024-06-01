package com.gean.tttemplate;

import com.gean.tttemplate.model.UserInfo;
import com.gean.tttemplate.utils.FileResourceUtils;
import com.gean.tttemplate.utils.ListaNomeada;
import com.gean.tttemplate.utils.TweetPresetNode;
import com.gean.tttemplate.view.telaPrincipal.TelaPrincipalView;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import twitter4j.AccessToken;

public class Main{
	
	public static List<TweetPresetNode> presets = Arrays.asList();
	
	public static List<ListaNomeada> listas = Arrays.asList();
	
	public static UserInfo userInfo;
        
        public static AccessToken OAuth1AccesToken;
        	
	
	
	public static void main(String[] args) throws Exception{
            
            Logger.getGlobal().addHandler(new ConsoleHandler());
                        
            Main.userInfo = FileResourceUtils.getObjectFromFile(FileResourceUtils.USER_INFO_FILE);
            Main.OAuth1AccesToken = FileResourceUtils.getObjectFromFile(FileResourceUtils.OAUTH1_ACCESS_TOKEN);
            
            File listaFile = new File(FileResourceUtils.LISTAS_FILE);
            File templateFile = new File(FileResourceUtils.PRESET_FILE);

            if(!listaFile.exists()) criarListaPadrao(listaFile);

            if(!templateFile.exists()) criarTemplatePadrao(templateFile);

            Main.listas = FileResourceUtils.getListas();
            Main.presets = FileResourceUtils.loadPreset();
            
            SwingUtilities.invokeLater(() -> new TelaPrincipalView().setVisible(true));
		
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
		return userInfo != null;
	}
	
}
