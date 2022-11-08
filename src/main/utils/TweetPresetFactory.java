package main.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TweetPresetFactory {
	
	public static final String DEFAULT_MIDIA_LABEL = "midia";
	
	public static String encode(String tweet, Map<String, String> params) {
		
		for(Map.Entry<String, String> entry : params.entrySet()) {
			
			tweet = replaceParam(tweet, entry.getKey(), entry.getValue());
						
		}
		
		return tweet;
		
	}
	
	public static List<Param> getParams(String tweet){
		ArrayList<Param> out = new ArrayList<>();
		
		int indexOf = tweet.indexOf("{")+1;
		while(indexOf > 0) {
			String rawParam = tweet.substring(indexOf, tweet.indexOf("}", indexOf));
			
			String[] infos = rawParam.split(":");
			
			if(infos.length == 1) {
				out.add(new Param(infos[0]));
			} else if (infos[1].startsWith("l")){
				
				out.add(new Param(infos[0], ParamTypeEnum.LIST, FileResourceUtils.getLista(infos[1].toLowerCase().substring(1)).getListaAsString()));
				
			} else if (infos[1].startsWith("n")){
				out.add(new Param(infos[0], ParamTypeEnum.NUMBER));
			} else if (infos[1].startsWith("t")) {
				out.add(new Param(infos[0], ParamTypeEnum.TEXT));
			} else if (infos[1].startsWith("o")) {
				
				if(!out.contains(new Param(infos[1].substring(1)))) {
					List<Object> lista = FileResourceUtils.getLista(infos[1].toLowerCase().substring(1)).getLista();
					lista.forEach(item -> {
						((LinkedHashMap) item).put("listName", infos[1].toLowerCase().substring(1));
					});
					
					out.add(new Param(infos[0], ParamTypeEnum.OBJECT, lista));
				}
			} else if (infos[1].startsWith("d")) {
				out.add(new Param(infos[0], ParamTypeEnum.DATE, infos[1].substring(1)));
			} else {
				out.add(new Param(infos[0]));
			}
			
			indexOf = tweet.indexOf("{", indexOf)+1;			
		}
		
		
		return out;
	}
	
	public static String replaceParam(String str, String param, String value) {
		int paramIndex = str.indexOf(param);
		
		if(paramIndex == -1) return str;
		
		int firstIndex = str.lastIndexOf("{", paramIndex);
		int lastIndex = str.indexOf("}", paramIndex);
		
		String regex = str.substring(firstIndex, lastIndex+1)
				.replace("{", "\\{")
				.replace("}", "\\}");
		
		return str.replaceFirst(regex, value);
	}
	
	public static String toTweetUpperCase(String tweet) {
		StringBuilder sb = new StringBuilder();
		
		String[] partes = tweet.split(" ");
		
		for (String p:partes) {
			if(!p.contains("@") && !p.contains("#")) {
				sb.append(p.toUpperCase() + " ");
			} else {
				sb.append(p + " ");
			}
		}
		
		return sb.toString();
	}
	
}
