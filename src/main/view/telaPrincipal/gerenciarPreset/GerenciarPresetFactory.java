package main.view.telaPrincipal.gerenciarPreset;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.utils.FileResourceUtils;
import main.utils.TweetPreset;
import main.utils.TweetPresetNode;
import main.view.telaPrincipal.TelaPrincipalController;

public class GerenciarPresetFactory {
	
	public static void createView(List<TweetPresetNode> presets, TelaPrincipalController pai) {
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(GerenciarPresetFactory.class.getResource("gerenciarPresetView.fxml"));
			fxmlLoader.setController(new GerenciarPresetController(presets, pai));
			
			Parent root = (Parent)fxmlLoader.load();
			
			
			Stage stage = new Stage();
			stage.getIcons().add(FileResourceUtils.getImageFromPath(FileResourceUtils.LOGO_FILE));
			stage.setScene(new Scene(root));
			stage.setTitle("Gerenciar Preset");
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

}
