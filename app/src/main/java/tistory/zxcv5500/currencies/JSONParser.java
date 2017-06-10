package tistory.zxcv5500.currencies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sharpen on 2017. 6. 10..
 */

public class JSONParser {
	static InputStream sInputStream = null;
	static JSONObject sReturnJSONObject = null;
	static String sRawJsonString = "";


	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) {
		try {
			URL surl = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) surl.openConnection();
			sInputStream = urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(sInputStream, "iso-8859-1"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			sInputStream.close();
			sRawJsonString = stringBuilder.toString();
		} catch (Exception e) {
			Log.e("Error reading data: " + e.toString() , this.getClass().getSimpleName());
		}

		try {
			sReturnJSONObject = new JSONObject(sRawJsonString);

		} catch (JSONException e) {
			Log.e("Parser", "Error when parsing data : " + e.toString());
		}

		// JSON 객체를 반환한다
		return sReturnJSONObject;

	}
}
