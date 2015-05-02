import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import javax.json.*;


public class IGNAPIDisplay {
	
	private static String readAll(Reader rd) throws IOException{
		StringBuilder sb = new StringBuilder();
		int current;
		while ((current = rd.read()) != -1) {
			sb.append((char) current);
		}
		return sb.toString();
	}
	
	private static JsonObject getJson(String url) throws IOException, JsonException{
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JsonReader jr = Json.createReader(new StringReader(jsonText));
			JsonObject json = jr.readObject();
			jr.close();
			return json;
		} finally {
			is.close();
		}
	}
	
	private static void printAll(JsonObject json, boolean toggle) throws JsonException, IOException {
		int sum = 0;
		boolean flag = true;
		while(flag) {
			JsonArray list = json.getJsonArray("data");
			for (int i = 0; i < json.getInt("count"); i++) {
				JsonObject j = list.getJsonObject(i).getJsonObject("metadata");
				if (toggle) {
					System.out.println(j.getString("headline"));
				} else {
					System.out.println(j.getString("title"));
				}
				sum++;
				if (sum > json.getInt("startIndex") + json.getInt("count")) {
					flag = false;
					break;
				}
			}
			int newStart = json.getInt("startIndex") + json.getInt("count");
			String type;
			if (toggle) {
				type = "articles";
			} else {
				type = "videos";
			}
			String newURL = "http://ign-apis.herokuapp.com/" + type + "?startIndex=" + newStart + "\u0026count=20";
			json = getJson(newURL);
		}
		
	}
	
	public static void main(String[] args) throws JsonException, IOException {
	    JsonObject articles = getJson("http://ign-apis.herokuapp.com/articles?startIndex=0\u0026count=20");
	    JsonObject videos = getJson("http://ign-apis.herokuapp.com/videos?startIndex=0\u0026count=20");
	    System.out.println("List of Articles\n");
	    printAll(articles, true);
	    System.out.println("\nList of Videos\n");
	    printAll(videos, false);
	}

}
