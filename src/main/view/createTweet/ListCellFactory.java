package main.view.createTweet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.utils.TweetPreset;
import main.utils.TweetPresetFactory;
import main.view.createTweet.CreateTweetController;
import main.view.createTweet.CreateTweetFactory;

public class ListCellFactory
		implements Callback<ListView<LinkedHashMap>, ListCell<LinkedHashMap>> {

	
	
	@Override
	public ListCell<LinkedHashMap> call(ListView<LinkedHashMap> param) {
		// TODO Auto-generated method stub
		return new ListCell<LinkedHashMap>() {
			protected void updateItem(LinkedHashMap item, boolean empty) {
				super.updateItem(item, empty);
				
				if(empty) {
					setText(null);
				} else if(item == null) {
					setText("[N/D]");
				} else {
					setText((String) item.get(item.get("listName")));
				}
			};
		};
	}

}
