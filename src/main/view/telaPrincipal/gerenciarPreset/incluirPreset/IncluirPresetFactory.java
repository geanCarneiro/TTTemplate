package main.view.telaPrincipal.gerenciarPreset.incluirPreset;

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
import main.view.telaPrincipal.gerenciarPreset.GerenciarPresetController;

public class IncluirPresetFactory {

	public static TweetPreset createView(List<TweetPresetNode> presets, GerenciarPresetController pai) {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(
					IncluirPresetFactory.class.getResource("incluirPresetView.fxml"));
			fxmlLoader.setController(new IncluirPresetController(presets, pai));

			Parent root = (Parent) fxmlLoader.load();

			Stage stage = new Stage();
			stage.getIcons().add(FileResourceUtils.getImageFromPath(FileResourceUtils.LOGO_FILE));
			stage.setScene(new Scene(root));
			stage.setTitle("Incluir Preset");
			stage.setResizable(false);
			stage.showAndWait();
			return IncluirPresetController.response;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
