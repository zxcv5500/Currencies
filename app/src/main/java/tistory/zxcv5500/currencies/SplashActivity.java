package tistory.zxcv5500.currencies;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import org.json.JSONObject;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

	}

	private class FetchCodesTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return null;
		}
	}
}
