package main.view.tweetEmMassaImport;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.utils.TweetPreset;

public class TweetEmMassaImportFactory {
	
	public static File createView(TweetPreset tweet) {
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(TweetEmMassaImportFactory.class.getResource("TweetEmMassaImport.fxml"));
			fxmlLoader.setController(new TweetEmMassaImportController(tweet));
			
			Parent root = (Parent)fxmlLoader.load();
			
			
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UTILITY);
			stage.setScene(new Scene(root));
			stage.setTitle("Tweet Em Massa");
			stage.sizeToScene();
			stage.showAndWait();
			return TweetEmMassaImportController.response;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
