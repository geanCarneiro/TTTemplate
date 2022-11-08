package main.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;
import main.Main;
import twitter4j.Poll;
import twitter4j.PollOption;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtils {
	
	public static final String MULTI_PARTE_MSG = "PARTE %d/%d";
	
	public static void twittar(String tweet) throws Exception {
		
		
        Twitter twitter = getTwitterByAccessToken(Main.accessToken);
        
        twitter.updateStatus(tweet);

		
	}
	
	public static void twittarComImagem(String tweet, File media) throws Exception {
		
		StatusUpdate status = new StatusUpdate(tweet);
		status.setMedia(media);
		
		
        Twitter twitter = getTwitterByAccessToken(Main.accessToken);
        
        twitter.updateStatus(status);

		
	}
	
	public static void twittarComVideo(String tweet, File media) throws Exception {

        Twitter twitter = getTwitterByAccessToken(Main.accessToken);
        
        try(FileInputStream fis = new FileInputStream(media)) {       
        	UploadedMedia uploadedMedia = twitter.uploadMediaChunked(media.getName(), fis);
        
        
			StatusUpdate status = new StatusUpdate(tweet);		
			status.setMediaIds(uploadedMedia.getMediaId());
        
        
			twitter.updateStatus(status);
        }

	}
	
	public static void twittarComVariosVideos(String tweet, File... medias) {

        Twitter twitter = getTwitterByAccessToken(Main.accessToken);
		Status statusAnterior = null;
		long[] mediaIds = new long[medias.length];
		
		// preparar midias
		try {
			
			
			for(int i = 0; i < medias.length; i++) {
				
				System.out.println("Preparando arquivo " + (i+1) + "/" + medias.length);
				
				try (FileInputStream fis = new FileInputStream(medias[i])) {
					UploadedMedia uploadedMedia = twitter.uploadMediaChunked(medias[i].getName(), fis);
			        mediaIds[i] = uploadedMedia.getMediaId();
				}
								
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// postar medias
		try {
			for(int i = 0; i < medias.length; i++) {
				
				StringBuilder sb = new StringBuilder();
				
				if(i == 0) {
					sb.append(tweet + "\n");
				}
				
				sb.append(String.format(MULTI_PARTE_MSG, i+1, medias.length));
				
				System.out.println("Postando media " + (i+1) + "/" + medias.length);
		        
				StatusUpdate status = new StatusUpdate(sb.toString());		
				status.setMediaIds(mediaIds[i]);
				if(statusAnterior != null) {
					status.inReplyToStatusId(statusAnterior.getId());
				}
				
				statusAnterior = twitter.updateStatus(status);

				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
        
        
	}
	
	public static AccessToken userOAuth() {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthAccessToken(null)
				.setOAuthAccessTokenSecret(null)
				.setDebugEnabled(false)
				.setPrettyDebugEnabled(false);
			
			Twitter twitter = new TwitterFactory(cb.build()).getInstance();
			RequestToken requestToken = twitter.getOAuthRequestToken("oob");
			AccessToken accessToken = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    while (null == accessToken) {
		    	
		      Desktop desktop = Desktop.getDesktop();
		      desktop.browse(new URI(requestToken.getAuthorizationURL()));
		      
		      TextInputDialog in = new TextInputDialog();
		      in.setHeaderText(null);
		      in.setTitle("Autorização do Twitter");
		      in.setContentText("Insira o pin gerado pelo Twitter: ");
		      
		      Optional<String> result = in.showAndWait();
		      if (result.isPresent()) {
		    	  String pin = result.get();
		    	  try{
			         if(pin.length() > 0){
			           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			         }else{
			           accessToken = twitter.getOAuthAccessToken();
			         }
			      } catch (TwitterException te) {
			    	  AlertFactory.createErrorAlert("Ocorreu um erro ao Connectar: " + te.getErrorMessage());
			          te.printStackTrace();
			        
			      }
		      } else {
		    	  return null;
		      }
		      
		      
		    }
		    AlertFactory.createInformationAlert("Conectado com sucesso");
			
			return accessToken;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static User getConnectedUser() {
		if(!Main.isConectado()) return null;
		
		Twitter twitter = getTwitterByAccessToken(Main.accessToken);
		try {
			return twitter.showUser(Main.accessToken.getUserId());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Twitter getTwitterByAccessToken(AccessToken accessToken) {
		
		TwitterFactory factory = new TwitterFactory();
	    Twitter twitter = factory.getInstance();
	    twitter.setOAuthAccessToken(accessToken);
	    
	    return twitter;
	}

}
