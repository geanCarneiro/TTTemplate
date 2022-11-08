package main.view.createTweet;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.utils.TweetPreset;

public class CreateTweetFactory {
	
	public static void createView(TweetPreset tweet) {
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(CreateTweetFactory.class.getResource("createTweetView.fxml"));
			fxmlLoader.setController(new CreateTweetController(tweet));
			
			Parent root = (Parent)fxmlLoader.load();
			
			
			Stage stage = new Stage();
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
