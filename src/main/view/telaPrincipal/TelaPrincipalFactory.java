package main.view.telaPrincipal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.utils.FileResourceUtils;

public class TelaPrincipalFactory {

	public static void createView() {
	
		try {
			Parent root = FXMLLoader.load(TelaPrincipalFactory.class.getResource("telaPrincipalView.fxml"));
			
			Stage stage = new Stage();
			stage.setTitle("PresetTweet");
			stage.getIcons().add(FileResourceUtils.getImageFromPath(FileResourceUtils.LOGO_FILE));
			stage.setScene(new Scene(root, 600, 500));
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					
					Platform.exit();
					System.exit(0);
					
				}
			});
			stage.show();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
	}

}
