package main.view.telaPrincipal;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TelaPrincipalFactory {

	public static void createView() {
	
		try {
			Parent root = FXMLLoader.load(TelaPrincipalFactory.class.getResource("telaPrincipalView.fxml"));
			
			Stage stage = new Stage();
			stage.setTitle("PresetTweet");
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
