package main.view.createTweet;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.utils.FileResourceUtils;
import main.utils.TweetPreset;

public class CreateTweetFactory {
	
	public static void createView(TweetPreset tweet) {
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(CreateTweetFactory.class.getResource("createTweetView.fxml"));
			fxmlLoader.setController(new CreateTweetController(tweet));
			
			Parent root = (Parent)fxmlLoader.load();
			
			
			Stage stage = new Stage();
			stage.getIcons().add(FileResourceUtils.getImageFromPath(FileResourceUtils.LOGO_FILE));
			stage.setScene(new Scene(root));
			stage.setTitle("Criar Tweet");
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

}
