package main.view.telaPrincipal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import main.Main;
import main.utils.AlertFactory;
import main.utils.FileResourceUtils;
import main.utils.TweetPreset;
import main.utils.TweetPresetFactory;
import main.utils.TweetPresetNode;
import main.utils.TwitterUtils;
import main.view.telaPrincipal.gerenciarPreset.GerenciarPresetFactory;
import main.view.tweetEmMassaImport.TweetEmMassaImportFactory;
import twitter4j.JSONObject;

public class TelaPrincipalController {
		
	private ObservableList<TweetPresetNode> presetListData = FXCollections.observableArrayList();
	
    private boolean isConnected;
	
	public TelaPrincipalController() {
		
	}
	
	@FXML 
	private TreeView<TweetPreset> presetList;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private Button btnConnectar;
	
	@FXML
	private Button btnDesconectar;
	
	@FXML
	private Text txtStatusConectado;
    
	@FXML
	public void initialize() {
		
		

		presetList.setCellFactory(new PresetTreeCellFactory());
		
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
	
	public void update() {
		
		this.btnConnectar.setVisible(!Main.isConectado());
		this.btnDesconectar.setVisible(Main.isConectado());
		
		this.rootPane.lookupAll(".verifyConnection").forEach(node -> {
			node.setDisable(!Main.isConectado());
		});
		
		if(Main.isConectado()) {
			this.txtStatusConectado.setText("Connectado como " + TwitterUtils.getConnectedUser().getName() + " (@" +  TwitterUtils.getConnectedUser().getScreenName() + ")");
		} else {
			this.txtStatusConectado.setText("Não Connectado");
		}
		
		this.presetList.getRoot().getChildren().clear();
		this.presetList.getRoot().getChildren().addAll(Main.presets.stream().map(item -> item.toTreeItem()).collect(Collectors.toList()));
	}
	
	@FXML
	public void connectar() {
		Main.accessToken = TwitterUtils.userOAuth();
		
		if(Main.accessToken != null)
			FileResourceUtils.saveObjectOnFile(FileResourceUtils.ACCESS_TOKEN_FILE, Main.accessToken);
				
		update();
	}
	
	@FXML
	public void desconectar() {
		Main.accessToken = null;
		
		File file = FileResourceUtils.getFileFromPath(FileResourceUtils.ACCESS_TOKEN_FILE);
		file.delete();
		update();
	}
	
	@FXML
	public void doGerenciarPreset() {
		if(Main.presets == null) Main.presets = Arrays.asList();
		
		GerenciarPresetFactory.createView(Main.presets, this);
	}
	
	private static boolean tweetEmMassa_debounce = false;
	
	@FXML
	public void tweetEmMassa() {
		if(!tweetEmMassa_debounce) {
			tweetEmMassa_debounce = true;
			Platform.runLater(() -> {
				try {
					TreeItem<TweetPreset> presetItem = presetList.getSelectionModel().getSelectedItem();
					
					if(presetItem == null || !presetItem.isLeaf()) {
						Platform.runLater(() -> AlertFactory.createInformationAlert("Selecione o tweet que deseja fazer!"));
						return;
					}
	
					TweetPreset preset = presetItem == null ? null : presetItem.getValue();
					
					// importar xls
					File excelFile = TweetEmMassaImportFactory.createView(preset);
					
					// ETL - xls(x) para List<List<Map>>
					if(excelFile != null) {
						List<Map<String, String>> allParams = xlsToList(excelFile);
						
						
					
						if(!allParams.isEmpty()) {
							
							// validar e corrige todas as midias
							if(preset.getMidia()) {
								for(Map<String, String> params : allParams) {
									String midiaFileName = params.get(TweetPresetFactory.DEFAULT_MIDIA_LABEL);
									
									if(!midiaFileName.endsWith(".mp4")) midiaFileName = midiaFileName + ".mp4";
									
									final String midiaFileNameFinal = midiaFileName;
									
									if(!new File(excelFile.getParent() + File.separator + midiaFileName).exists()) {
										Platform.runLater(() -> AlertFactory.createErrorAlert("O ARQUIVO " + midiaFileNameFinal.toUpperCase() + " NÃO EXISTE NA PASTA " + excelFile.getParent().toUpperCase()));
										return;
									}
									
									params.put(TweetPresetFactory.DEFAULT_MIDIA_LABEL, midiaFileName);
										
								}
							}
					
							// para cada registro
							for(Map<String, String> params : allParams) {
								
								// prepara os dados
								String tweet = TweetPresetFactory.toTweetUpperCase(TweetPresetFactory.encode(preset.getPreset(), params));
								
								File midia = new File(excelFile.getParent() + File.separator + params.get(TweetPresetFactory.DEFAULT_MIDIA_LABEL));
								
								System.out.println("Postando tweet do registro:");
								System.out.println(new JSONObject(params).toString(2));
								
								if(preset.getMidia())
									TwitterUtils.twittarComVideo(tweet, midia);
								else
									TwitterUtils.twittar(tweet);
								
							}
						
							Platform.runLater(() -> AlertFactory.createInformationAlert("Tweets concluidos com sucesso!"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					AlertFactory.createErrorAlert("Erro ao tweetar: " + e.getMessage());
				} finally {
					tweetEmMassa_debounce = false;
				}
				
	
			});
		}
				
	}
	
	@FXML
	public void recarregarDados() {
		
		Executors.newSingleThreadExecutor().execute(() -> {
			Main.listas = FileResourceUtils.getListas();
			Main.presets = FileResourceUtils.loadPreset();
			update();
		});
		
				
	}

	private List<Map<String, String>> xlsToList(File excelFile) throws Exception {
		ArrayList<Map<String, String>> out = new ArrayList<>();
		
		try(FileInputStream fis = new FileInputStream(excelFile)) {
			try(Workbook wb = new HSSFWorkbook(fis)){
				
				Sheet planilha = wb.getSheetAt(0);
				
				for(int i = planilha.getFirstRowNum()+1; i <= planilha.getLastRowNum(); i++) {
					HashMap<String, String> params = new HashMap<>();
					Row row = planilha.getRow(i);
					if(row == null)	continue;
					if(row.getCell(row.getFirstCellNum()).getCellType() == CellType.BLANK) continue;
					
					for(Cell c: row) {
						String label = planilha.getRow(planilha.getFirstRowNum()).getCell(c.getColumnIndex()).getStringCellValue();
						
						HSSFDataFormatter hdf = new HSSFDataFormatter();
						params.put(label, hdf.formatCellValue(c));
						
						
					}
					out.add(params);
				}
				
			}
		}
		
		
		return out;
	}

}