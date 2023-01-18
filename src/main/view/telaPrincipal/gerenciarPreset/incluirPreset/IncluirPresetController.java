package main.view.telaPrincipal.gerenciarPreset.incluirPreset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Main;
import main.utils.FileResourceUtils;
import main.utils.TweetPreset;
import main.utils.TweetPresetNode;
import main.view.telaPrincipal.TelaPrincipalController;
import main.view.telaPrincipal.gerenciarPreset.GerenciarPresetController;

public class IncluirPresetController {
	
	public static TweetPreset response = null;
	
	private List<TweetPresetNode> presets;
	private GerenciarPresetController pai;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML 
	private TreeView<TweetPreset> presetList;
	
	public IncluirPresetController(List<TweetPresetNode> presets, GerenciarPresetController pai) {
		this.presets = (presets == null) ? Arrays.asList() : new ArrayList<>(presets);
		this.pai = pai;
	}
    
	@FXML
	public void initialize() {
		update();
	}
	
	@FXML
	public void doIncluirPasta(ActionEvent event) {
		
		TreeItem<TweetPreset> tiTweetPreset = this.presetList.getSelectionModel().getSelectedItem();
		TweetPresetNode node = getNodeByTreeItem(tiTweetPreset);
		
		TextInputDialog tid = new TextInputDialog();
		tid.setHeaderText(null);
		tid.setGraphic(null);
		tid.setContentText("Nome da nova pasta: ");
		
		Button btnOk = (Button) tid.getDialogPane().lookupButton(ButtonType.OK);
		TextField txtField = tid.getEditor();
		BooleanBinding invalido = Bindings.createBooleanBinding(() -> txtField.getText().isEmpty(), txtField.textProperty());
		btnOk.disableProperty().bind(invalido);
		
		Optional<String> retornoDialog = tid.showAndWait();
		
		if(retornoDialog.isPresent()) {
			
			String nomeNovaPasta = retornoDialog.get();
			
			TweetPresetNode novoNode = new TweetPresetNode(nomeNovaPasta, null);
			
			if(node == null) {
				this.presets.add(novoNode);
				this.presetList.getRoot().getChildren().add(novoNode.toTreeItem());
			} else {
				node.addPreset(novoNode);
				tiTweetPreset.getChildren().add(novoNode.toTreeItem());
				tiTweetPreset.setExpanded(true);
			}
			
		}
		
	}
	
	@FXML
	public void doIncluirPreset(ActionEvent event) {
		
		IncluirPresetFactory.createView(null, pai);
		
//		TreeItem<TweetPreset> tiTweetPreset = this.presetList.getSelectionModel().getSelectedItem();
//		TweetPresetNode node = getNodeByTreeItem(tiTweetPreset);
//		
//		TextInputDialog tid = new TextInputDialog();
//		tid.setHeaderText(null);
//		tid.setGraphic(null);
//		tid.setContentText("Nome da nova pasta: ");
//		
//		Button btnOk = (Button) tid.getDialogPane().lookupButton(ButtonType.OK);
//		TextField txtField = tid.getEditor();
//		BooleanBinding invalido = Bindings.createBooleanBinding(() -> txtField.getText().isEmpty(), txtField.textProperty());
//		btnOk.disableProperty().bind(invalido);
//		
//		Optional<String> retornoDialog = tid.showAndWait();
//		
//		if(retornoDialog.isPresent()) {
//			
//			String nomeNovaPasta = retornoDialog.get();
//			
//			TweetPresetNode novoNode = new TweetPresetNode(nomeNovaPasta, null);
//			
//			if(node == null) {
//				this.presets.add(novoNode);
//				this.presetList.getRoot().getChildren().add(novoNode.toTreeItem());
//			} else {
//				node.addPreset(novoNode);
//				tiTweetPreset.getChildren().add(novoNode.toTreeItem());
//				tiTweetPreset.setExpanded(true);
//			}
//			
//		}
		
	}
	
	@FXML
	public void doSalvar(ActionEvent event) {
		
		Main.presets = this.presets;
		FileResourceUtils.saveObjectToJson(FileResourceUtils.PRESET_FILE, this.presets);
		pai.update();
		
		fecharJanela(event);
	}
	
	@FXML
	public void doCancelar(ActionEvent event) {
		
		fecharJanela(event);
	}
	
	private void fecharJanela(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	private TweetPresetNode getNodeByTreeItem(TreeItem<TweetPreset> treeItem) {
		if(treeItem == null) return null;
		
		for(TweetPresetNode node : this.presets) {
			if(node.toTweetPreset().getPath().equalsIgnoreCase(treeItem.getValue().getPath())) {
				return node;
			} else if(node.getPresets() != null) {
				for(TweetPresetNode sub : node.getPresets()) {
					TweetPresetNode retorno = getNodeByTreeItem(sub, treeItem);
					if(retorno != null) return retorno;
				}
			}
		}
		
		return null;
		
	}
	
	private TweetPresetNode getNodeByTreeItem(TweetPresetNode node, TreeItem<TweetPreset> target) {
		
		if(target == null || node == null) return null;
		
		if(node.toTweetPreset().getPath().equalsIgnoreCase(target.getValue().getPath())) {
			return node;
		} else if(node.getPresets() != null) {
			for(TweetPresetNode sub : node.getPresets()) {
				TweetPresetNode retorno = getNodeByTreeItem(sub, target);
				if(retorno != null) return retorno;
			}
		}
		
		return null;
	}
	
	public void update() {
		
	}
	
	

}