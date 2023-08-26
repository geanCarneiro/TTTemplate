package main.view.createTweet;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import main.Main;
import main.utils.AlertFactory;
import main.utils.Param;
import main.utils.TweetPreset;
import main.utils.TweetPresetFactory;
import main.utils.TwitterUtils;

public class CreateTweetController {
	
	private TweetPreset preset;
	
	private int charCount;
	
	private Text txtQtMidia;
	
	private HashMap<String, Node> campos = new HashMap<>();
	
	public CreateTweetController(TweetPreset preset) {
		this.preset = preset;
	}
	
	private List<File> midias = null;
	
	@FXML 
	private GridPane createTweetForm;
	
	@FXML
	private CheckBox checkUpper;
	
	@FXML
	private Text counter;
	
	@FXML
	private Text txtTweet;
	
	@FXML
	private Button btnGerarTweet;
    
	@FXML
	public void initialize() {
		
		List<Param> params = TweetPresetFactory.getParams(this.preset.getPreset());
		int rowIndex = 0;
		for(Param p : params) {
			
			Node input = new Text("NAN[input]");
			String rawLabel = "NAN";
			
			switch (p.getType()) {
				case STRING:
					rawLabel = p.getLabel();				
					input = new TextField();			
					((TextField) input).textProperty().addListener( (obs, ov, nv) -> updateCountText() );
					break;
				case LIST:
					rawLabel = p.getLabel();
					input = new ComboBox<String>();
					((ComboBox<String>) input).setItems(FXCollections.observableArrayList( ( List<String> ) p.getData() ) );
					List<String> lista = ( List<String> ) p.getData();
					if(lista.size() > 0) ((ComboBox<String>) input).setValue( lista.get(0) );
					((ComboBox<String>) input).setOnAction(e -> updateCountText() );
					break;
				case NUMBER:
					rawLabel = p.getLabel();
					input = new Spinner<Integer>(0, Integer.MAX_VALUE, 0, 1);
					((Spinner)input).setMaxWidth(100);
					((Spinner)input).setEditable(true);
					final Spinner finalSpinner = ((Spinner)input);
					finalSpinner.focusedProperty().addListener((s, ov, nv) -> {
					    if (nv) return;
					    //intuitive method on textField, has no effect, though
					    //spinner.getEditor().commitValue(); 
					    commitEditorText(finalSpinner);
					    updateCountText();
					});
					finalSpinner.getEditor().textProperty().addListener((obs, ov, nv) -> updateCountText());
					break;
				case TEXT:
					rawLabel = p.getLabel();
					input = new TextArea();
					((TextArea)input).setPrefColumnCount(20);
					((TextArea)input).setPrefRowCount(3);
					((TextArea)input).setWrapText(true);
					((TextArea)input).textProperty().addListener((obs, ov, nv) -> updateCountText());
					break;
				case OBJECT:
					rawLabel = p.getLabel();
					input = new ComboBox<LinkedHashMap>();
					((ComboBox<LinkedHashMap>) input).setItems(FXCollections.observableArrayList( ( List<LinkedHashMap> ) p.getData() ) );
					((ComboBox<LinkedHashMap>) input).setValue( (( List<LinkedHashMap> ) p.getData()).get(0) );
					((ComboBox<LinkedHashMap>) input).setOnAction(e -> updateCountText() );
					((ComboBox<LinkedHashMap>) input).setCellFactory(new ListCellFactory());
					((ComboBox<LinkedHashMap>) input).setButtonCell(new ListCellFactory().call(null));
					
					break;
				case DATE:
					rawLabel = p.getLabel();
					input = new DatePicker(LocalDate.now());
					if (p.getData() != null && !p.getData().toString().isEmpty()) {
						((DatePicker) input).setConverter(new StringConverter<LocalDate>() {
							
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(p.getData().toString());
							
							@Override
							public String toString(LocalDate object) {
								return formatter.format((LocalDate) object);
							}
	
							@Override
							public LocalDate fromString(String string) {
								return LocalDate.parse(string, formatter);
							}
						});
					}
					break;
			}
			
			String label = rawLabel.split("\\.")[0].replace("_", " ") + ": ";
			Text txtLabel = new Text(label);
			GridPane.setValignment(txtLabel, VPos.CENTER);
			if(!existeCampo(rawLabel)) createTweetForm.addRow(rowIndex, txtLabel, input);
			
			if(!existeCampo(rawLabel)) campos.put(rawLabel, input);
			rowIndex++;
			
		}
		if(this.preset.getMidia()) {
			Text txtLabel = new Text("Midia: ");
			GridPane.setValignment(txtLabel, VPos.CENTER);
			txtQtMidia = new Text("Nenhum arquivo carregado...");
			GridPane.setValignment(txtQtMidia, VPos.CENTER);
			Button btnCarregar = new Button("Carregar...");			
			btnCarregar.setOnAction(value ->  {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Carregar Midia");
				List<File> midias = fileChooser.showOpenMultipleDialog(null);
				if(midias != null) {
					this.midias = midias;
					txtQtMidia.setText(midias.size() + " midia(s) selecionado(s)...");
				}
				
			});
			Button btnLimpar = new Button("Limpar");
			btnLimpar.setOnAction(value ->  {
				limparMidia();
				
			});
			createTweetForm.addRow(rowIndex, txtLabel, txtQtMidia, btnCarregar, btnLimpar);
			
		}
		
		this.txtTweet.setText(this.preset.getPath());
		this.btnGerarTweet.setDisable(!Main.isConectado());
		updateCountText();
	}
	
	@FXML
	private void gerarTweet(ActionEvent event) {
				
		Executors.newSingleThreadExecutor().execute(() -> {
		String encodedTweet = doEncode();
		String tweet = checkUpper.isSelected() ? TweetPresetFactory.toTweetUpperCase(encodedTweet) : encodedTweet;
			try {
				if(this.midias == null || this.midias.size() == 0) {
					TwitterUtils.twittar(tweet);
				} else { 
					
					if (this.midias.size() == 1) {
						if(isImage(this.midias.get(0)))
							TwitterUtils.twittarComImagem(tweet, this.midias.get(0));
						else if(isVideo(this.midias.get(0)))
							TwitterUtils.twittarComVideo(tweet, this.midias.get(0));
						else {
							Platform.runLater(() -> AlertFactory.createInformationAlert("ATENÇÃO!! O ARQUIVO QUE VAI POSTAR DEVE SER UMA IMAGEM OU UM VIDEO!!")) ;
							return;
						}
					} else {
						boolean isVideo = true;
						for(File f: this.midias) {
							if(!isVideo(f)) isVideo = false;
						}
						if(isVideo)
							TwitterUtils.twittarComVariosVideos(tweet, this.midias.toArray(new File[0]));
						else {
							Platform.runLater(() -> AlertFactory.createInformationAlert("ATENÇÃO!! TODOS OS ARQUIVOS A POSTAR DEVEM SER VIDEOS!!"));
							return;
						}
					}
				}
				Platform.runLater(() -> AlertFactory.createInformationAlert("Tweet feito com sucesso!!"));
			} catch (Exception e) {
				e.printStackTrace();
				Platform.runLater(() -> AlertFactory.createErrorAlert("Erro ao postar Tweet!!"));
			}
			
			Platform.runLater(() -> limparCampos()) ;
		});
		
	}
	
	@FXML
	private void CopiarTweet(ActionEvent event) {
				
		Executors.newSingleThreadExecutor().execute(() -> {
			String encodedTweet = doEncode();
			String tweet = checkUpper.isSelected() ? TweetPresetFactory.toTweetUpperCase(encodedTweet) : encodedTweet;
			StringSelection selection = new StringSelection(tweet);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
			Platform.runLater(() -> AlertFactory.createInformationAlert("Tweet copiado com sucesso!!"));
			
			
//			Platform.runLater(() -> limparCampos()) ;
		});
		
	}
	
	@FXML
	private void limparCampos() {
		
		for(Node n : this.campos.values()) {
			if (n instanceof TextField) {
				((TextField) n).setText("");
			} else if (n instanceof ComboBox) {
				if(((ComboBox) n).getItems().size() > 0) ((ComboBox) n).setValue( ((ComboBox) n).getItems().get(0) );
			} else if (n instanceof Spinner) {
				((Spinner) n).getValueFactory().setValue(0);
			} else if (n instanceof TextArea) {
				((TextArea) n).setText("");
			} else if (n instanceof DatePicker) {
				((DatePicker) n).setValue(LocalDate.now());
			}
		}
		
		if(this.preset.getMidia()) limparMidia();
		
		
	}
	
	private void limparMidia() {
		this.midias = null;
		txtQtMidia.setText("Nenhum arquivo carregado...");
	}
	
	private boolean isImage(File f) {
		try {
			String mimetype = Files.probeContentType(f.toPath());
			return mimetype.split("/")[0].equalsIgnoreCase("image");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isVideo(File f) {
		try {
			String mimetype = Files.probeContentType(f.toPath());
			return mimetype.split("/")[0].equalsIgnoreCase("video");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	private String doEncode() {
		HashMap<String, String> params = new HashMap<>();
		
		for(Entry<String, Node> entry : campos.entrySet()) {
			
			String value = "NAN";
			String paramName = entry.getKey();
			
			if (entry.getValue() instanceof TextField) {
				value = ((TextField)entry.getValue()).getText();
			} else if (entry.getValue() instanceof ComboBox<?>) {
				Object comboValue = ((ComboBox<?>) entry.getValue()).getValue();
				if(comboValue == null) {
					value = "";
				} else {
					String[] infoPartes = paramName.split("\\.");
					if(infoPartes.length > 1) {
						for(Object o : ((LinkedHashMap) comboValue).keySet()) {
							params.put(infoPartes[0] + "." + o.toString(), ((LinkedHashMap) comboValue).get(o).toString());
						}

						continue;
					} else {
						value = (String) comboValue;
					}
				}
			} else if (entry.getValue() instanceof Spinner<?>) {
				value = String.valueOf(((Spinner<?>) entry.getValue()).getValue());
			} else if (entry.getValue() instanceof TextArea) {
				value = ((TextArea) entry.getValue()).getText();
			} else if (entry.getValue() instanceof DatePicker) {
				DatePicker picker = ((DatePicker) entry.getValue());
				value = picker.getConverter().toString(picker.getValue());
			}
			
			params.put(paramName, value);
		}
		
		return TweetPresetFactory.encode(preset.getPreset(), params);
	}
	
	private <T> void commitEditorText(Spinner<T> spinner) {
	    if (!spinner.isEditable()) return;
	    String text = spinner.getEditor().getText();
	    SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
	    if (valueFactory != null) {
	        StringConverter<T> converter = valueFactory.getConverter();
	        if (converter != null) {
	            T value = converter.fromString(text);
	            valueFactory.setValue(value);
	        }
	    }
	}
	
	private void updateCountText() {
		charCountUpdate();
		counter.textProperty().bind(Bindings.format("%d/%d", charCount, TweetPreset.MAX_TWEET_SIZE));
		counter.setFill((charCount > TweetPreset.MAX_TWEET_SIZE)? Color.RED : Color.BLACK);
	}
	
	private void charCountUpdate() {
		this.charCount = doEncode().length();
	}
	
	private boolean existeCampo(String rawCampo) {
		final boolean[] encontrou = new boolean[]{false};
		campos.forEach((nome, campo) -> {
			if(nome.split("\\.")[0].equals(rawCampo.split("\\.")[0])) encontrou[0] = true;
		});
		
		return encontrou[0];
	}
	
	

}