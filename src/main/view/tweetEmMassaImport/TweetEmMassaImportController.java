package main.view.tweetEmMassaImport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.utils.AlertFactory;
import main.utils.Param;
import main.utils.TweetPreset;
import main.utils.TweetPresetFactory;

public class TweetEmMassaImportController {
	
	public static File response = null;
	
	private TweetPreset preset;
	
	private File file = null;
	
	
	@FXML
	private Text txtPresetName;
	
	@FXML
	private TextField txtFieldAquivo;
	
	public TweetEmMassaImportController(TweetPreset preset) {
		this.preset = preset;
	}
	
    
	@FXML
	public void initialize() {
		
		this.txtPresetName.setText(preset.getNome());
		
	}
	
	@FXML
	private void btnImportarAction(ActionEvent event) {
		TweetEmMassaImportController.response = this.file;
		
		Button btnImportar= (Button) event.getSource();
		btnImportar.getScene().getWindow().hide();
	}
	
	@FXML
	private void btnNavegarAction(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Carregar Modelo");
		chooser.getExtensionFilters().add(new ExtensionFilter("Arquivo xls", "*.xls"));
		
		this.file = chooser.showOpenDialog(null);
		this.txtFieldAquivo.setText(this.file == null ? "" : this.file.getAbsolutePath());
	}
	
	@FXML
	private void btnGerarModeloAction(ActionEvent event) {
		List<Param> params = TweetPresetFactory.getParams(this.preset.getPreset());
		
		try(Workbook wb = new HSSFWorkbook()){
		
			Sheet planilha = wb.createSheet(preset.getNome());
			
			Row row = planilha.createRow(0);
			row.setHeight((short) 300);
			int colIndex = 0;
			for(Param p: params) {
				row.createCell(colIndex).setCellValue(p.getLabel());
				planilha.autoSizeColumn(colIndex);
				colIndex++;
			}
			
			if(this.preset.getMidia()) {
				row.createCell(colIndex).setCellValue(TweetPresetFactory.DEFAULT_MIDIA_LABEL);
				planilha.autoSizeColumn(colIndex);
			}
			
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Salvar Modelo Gerado");
			chooser.setInitialFileName("modelo_preset.xls");
			chooser.getExtensionFilters().add(new ExtensionFilter("Arquivo xls", "*.xls"));
			File outputFile = chooser.showSaveDialog(null);
	
			if(outputFile != null) {
				if(!outputFile.getName().endsWith(".xls"))
					outputFile = new File(outputFile.getAbsolutePath() + ".xls");
				
				try {
					outputFile.createNewFile();
					
					try(FileOutputStream fos = new FileOutputStream(outputFile)){
						wb.write(fos);
					}
										
					AlertFactory.createInformationAlert("ARQUIVO " + outputFile.getName().toUpperCase() + " CRIADO COM SUCESSO!!");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					AlertFactory.createErrorAlert("ERRO AO CRIAR O ARQUIVO " + outputFile.getName());
					return;
				}
				
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			AlertFactory.createErrorAlert("ERRO AO GERAR MODELO");
			return;
		}
	}
	
	@FXML
	private void btnCancelarAction(ActionEvent event) {
		
		Button btnCancelar = (Button) event.getSource();
		btnCancelar.getScene().getWindow().hide();
		
		
	}

}