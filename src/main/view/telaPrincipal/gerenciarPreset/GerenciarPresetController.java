package main.view.telaPrincipal.gerenciarPreset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Main;
import main.utils.TweetPreset;
import main.utils.TweetPresetNode;
import main.view.telaPrincipal.TelaPrincipalController;

public class GerenciarPresetController {
	
	
	private List<TweetPresetNode> presets;
	private TelaPrincipalController pai;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML 
	private TreeView<TweetPreset> presetList;
	
	public GerenciarPresetController(List<TweetPresetNode> presets, TelaPrincipalController pai) {
		this.presets = (presets == null) ? Arrays.asList() : new ArrayList<>(presets);
		this.pai = pai;
	}
    
	@FXML
	public void initialize() {
		presetList.setCellFactory(new GerenciarPresetTreeCellFactory());
				
		presetList.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
			if (nv != null && !nv.isLeaf()) {
				Platform.runLater(() -> presetList.getSelectionModel().clearSelection());
			}
		});
		
		TreeItem<TweetPreset> root = new TreeItem<TweetPreset>();
		root.setExpanded(true);
		presetList.setRoot(root);
		presetList.setShowRoot(false);
		
		update();
	}
	
	@FXML
	public void doSalvar(ActionEvent event) {
		
		Main.presets = this.presets;
		pai.update();
		
		Button btnSalvar = (Button) event.getSource();
		btnSalvar.getScene().getWindow().hide();
	}
	
	@FXML
	public void doCancelar(ActionEvent event) {
		
		Button btnCancelar = (Button) event.getSource();
		btnCancelar.getScene().getWindow().hide();
		
		
	}
	
	public void update() {
		
		this.presetList.getRoot().getChildren().clear();
		this.presetList.getRoot().getChildren().addAll(this.presets.stream().map(item -> item.toTreeItem()).collect(Collectors.toList()));
	}
	
	

}