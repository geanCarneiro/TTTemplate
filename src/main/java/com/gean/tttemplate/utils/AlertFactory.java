package com.gean.tttemplate.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javax.swing.JOptionPane;

public class AlertFactory {

	private static Alert createDefaultAlert(AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(null);
		return alert;
	}
	
	public static void createInformationAlert(String content) {
            JOptionPane.showMessageDialog(null, content, "Atenção", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void createErrorAlert(String content) {
            JOptionPane.showMessageDialog(null, content, "ERRO", JOptionPane.ERROR_MESSAGE);
	}	
	

}
