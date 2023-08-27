package main.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertFactory {

	private static Alert createDefaultAlert(AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		return alert;
	}
	
	public static Optional<ButtonType> createInformationAlert(String content) {
		Alert alert = createDefaultAlert(AlertType.INFORMATION);
		alert.setContentText(content);
		return alert.showAndWait();
	}

	public static Optional<ButtonType> createErrorAlert(String content) {
		Alert alert = createDefaultAlert(AlertType.ERROR);
		alert.setContentText(content);
		return alert.showAndWait();
	}	
	

}
