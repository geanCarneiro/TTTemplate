package com.gean.tttemplate.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gean.tttemplate.Main;
import com.gean.tttemplate.model.UserInfo;
import com.gean.tttemplate.model.UsersMeResponse;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20Service;
import com.twitter.clientlib.model.Get2UsersMeResponse;
import com.twitter.clientlib.model.TweetCreateRequest;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import twitter4j.AccessToken;
import twitter4j.OAuthAuthorization;
import twitter4j.RequestToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.v1.Status;
import twitter4j.v1.StatusUpdate;
import twitter4j.v1.UploadedMedia;

public class TwitterUtils {
	
	public static final String MULTI_PARTE_MSG = "PARTE %d/%d";
	
	public static void twittar(String tweet) throws Exception {
		

            TwitterApi twitter = new TwitterApi(Main.userInfo.getCredentials());
            TweetCreateRequest createRequest = new TweetCreateRequest();
            createRequest.setText(tweet);
            
            twitter.tweets().createTweet(createRequest);

		
	}
	
	public static void twittarComImagem(String tweet, File media) throws Exception {
            
            Twitter apiInstance = getInstance();
            
            if(apiInstance == null){
                SwingUtilities.invokeLater(() -> AlertFactory.createErrorAlert("Você precisa connectar no twitter para postar imagem"));
                return;
            }
            
            StatusUpdate update = StatusUpdate.of(tweet);
            update.media(media);
            
            apiInstance.v1().tweets().updateStatus(update);
            

		
	}
        
        private static AccessToken requestAuth1Token() throws TwitterException, URISyntaxException, IOException{
            OAuthAuthorization oAuth = OAuthAuthorization.getInstance();
            RequestToken requestToken = oAuth.getOAuthRequestToken("oob");
            AccessToken accessToken = null;
            String url = requestToken.getAuthorizationURL();
            
            if( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) ){
                Desktop.getDesktop().browse(new URI(url));
            } else {
                AlertFactory.createInformationAlert("Não foi possivel abrir o navegador automaticamente, favor abrir o navegador e entrar no link abaixo para poder prosseguir:/n" + url);
            }
            
            final String pin = JOptionPane.showInputDialog(null, "Entre com o codigo gerado:", "Conectar ao Twitter", JOptionPane.INFORMATION_MESSAGE);
            
            if(pin != null && !pin.isEmpty()) {
                
                accessToken = oAuth.getOAuthAccessToken(requestToken, pin);
                
            }
            
            
            return accessToken;
            
        }
	
	public static void twittarComVideo(String tweet, File media) throws Exception {

            Twitter twitter = getInstance();
            
            if(twitter == null){
                SwingUtilities.invokeLater(() -> AlertFactory.createErrorAlert("Você precisa se connectar para postar com Video"));
                return;
            }

            try(FileInputStream fis = new FileInputStream(media)) {       
                UploadedMedia uploadedMedia = twitter.v1().tweets().uploadMediaChunked(media.getName(), fis);


                StatusUpdate status = StatusUpdate.of(tweet);		
                status.mediaIds(uploadedMedia.getMediaId());


                twitter.v1().tweets().updateStatus(status);
            }

	}
	
	public static void twittarComVariosVideos(String tweet, File... medias) throws Exception {

            Twitter twitter = getInstance();
            
            if(twitter == null){
                SwingUtilities.invokeLater(() -> AlertFactory.createErrorAlert("Você precisa se connectar para postar varios videos"));
                return;
            }

            
		Status statusAnterior = null;
		long[] mediaIds = new long[medias.length];
		
		// preparar midias
		try {
			
			
			for(int i = 0; i < medias.length; i++) {
				
				System.out.println("Preparando arquivo " + (i+1) + "/" + medias.length);
				
				try (FileInputStream fis = new FileInputStream(medias[i])) {
					UploadedMedia uploadedMedia = twitter.v1().tweets().uploadMediaChunked(medias[i].getName(), fis);
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
		        
				StatusUpdate status = StatusUpdate.of(sb.toString());		
				status.mediaIds(mediaIds[i]);
				if(statusAnterior != null) {
					status.inReplyToStatusId(statusAnterior.getId());
				}
				
				statusAnterior = twitter.v1().tweets().updateStatus(status);

				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
        
        
	}
	
	public static boolean userOAuth() throws Exception{
		
                    
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File("twitter4j.properties")));

            TwitterOAuth20Service service = new TwitterOAuth20Service(
                    properties.getProperty("oauth.clientId"),
                    properties.getProperty("oauth.clientSecret"),
                    "http://127.0.0.1:5500/",
                    "offline.access tweet.read tweet.write users.read");

            final String secretState = "state";
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            String url = service.getAuthorizationUrl(pkce, secretState);
            
            if( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) ){
                Desktop.getDesktop().browse(new URI(url));
            } else {
                AlertFactory.createInformationAlert("Não foi possivel abrir o navegador automaticamente, favor abrir o navegador e entrar no link abaixo para poder prosseguir:/n" + url);
            }
            
            final String pin = JOptionPane.showInputDialog(null, "Entre com o codigo gerado:", "Conectar ao Twitter", JOptionPane.INFORMATION_MESSAGE);
            
            if(pin == null || pin.isEmpty()) {
                return false;
            }
            
            OAuth2AccessToken accessToken = service.getAccessToken(pkce, pin);
            Main.userInfo = new UserInfo();
            Main.userInfo.setCredentials(new TwitterCredentialsOAuth2(
                    properties.getProperty("oauth.clientId"),
                    properties.getProperty("oauth.clientSecret"),
                    accessToken.getAccessToken(),
                    accessToken.getRefreshToken()));
                        
            TwitterApi apiInstance = new TwitterApi(Main.userInfo.getCredentials());
            
            Get2UsersMeResponse resp = apiInstance.users().findMyUser().execute();
            
            Main.userInfo.setUsers(new ObjectMapper().readValue(resp.toJson(), UsersMeResponse.class));

            return true;
	}
        
        private static Twitter getInstance() throws IOException, TwitterException, URISyntaxException{
            if(Main.OAuth1AccesToken == null){
                Main.OAuth1AccesToken = requestAuth1Token();
                FileResourceUtils.saveObjectOnFile(FileResourceUtils.OAUTH1_ACCESS_TOKEN, Main.OAuth1AccesToken);
            }
            
            if(Main.OAuth1AccesToken == null) return null;
            
            Properties p = new Properties();
            p.load(new FileInputStream(new File("twitter4j.properties")));
            
            return Twitter.newBuilder()
                    .oAuthConsumer(p.getProperty("oauth.consumerKey"), p.getProperty("oauth.consumerSecret"))
                    .oAuthAccessToken(Main.OAuth1AccesToken).build();
            
            
        }
}
