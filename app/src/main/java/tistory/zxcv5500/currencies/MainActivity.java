package tistory.zxcv5500.currencies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private Button mCalcButton;
	private TextView mConvertedTextView;
	private EditText mAmountEditText;
	private Spinner mForSpinner, mHomSpinner;
	private String[] mCurrencies;

	public static final String FOR = "FOR_CURRENCY";
	public static final String HOM = "HOM_CURRENCY";

	// 개발자 키를 저장한다
	private String mKey;

	// openexchangerates.org로부터 환율 JSON 객체를 가져오는데 사용할 상수
	public static final String RATES = "rates";
	public static final String URL_BASE = "https://openexchangerates.org/api/latest.json?app_id=";

	// 환율 데이터의 형식을 지정하는 상수
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##0.00000");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Bundle 객체의 ArrayList 데이터를 배열로 생성한다
		ArrayList<String> arrayList = ((ArrayList<String>) getIntent().getSerializableExtra(SplashActivity.KEY_ARRAYLIST));
		Collections.sort(arrayList);
		mCurrencies = arrayList.toArray(new String[arrayList.size()]);

		mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
		mAmountEditText = (EditText) findViewById(R.id.edt_amount);
		mCalcButton = (Button) findViewById(R.id.btn_calc);
		mForSpinner = (Spinner) findViewById(R.id.spn_for);
		mHomSpinner = (Spinner) findViewById(R.id.spn_hom);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				//컨택스트
				this,
				//뷰 : 화면에 나타나는 레이아웃
				R.layout.spinner_close,
				// 모델 : 통화 코드를 갖고 있는 String 배열
				mCurrencies
		);

		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mHomSpinner.setAdapter(arrayAdapter);
		mForSpinner.setAdapter(arrayAdapter);

		mHomSpinner.setOnItemSelectedListener(this);
		mForSpinner.setOnItemSelectedListener(this);

		if (savedInstanceState == null
				&& (PrefsMgr.getString(this, FOR) == null &&
				PrefsMgr.getString(this, HOM) == null)) {

			mForSpinner.setSelection(findPositionGivenCode("CNY", mCurrencies));
			mHomSpinner.setSelection(findPositionGivenCode("USD", mCurrencies));

			PrefsMgr.setString(this, FOR, "CNY");
			PrefsMgr.setString(this, HOM, "USD");
		} else {

			mForSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this, FOR), mCurrencies));
			mForSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this, HOM), mCurrencies));
		}

		mCalcButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 실행될 코드를 여기에 둔다
				new CurrencyConverterTask().execute(URL_BASE + mKey);

			}
		});

		mKey = getKey("open_key");

	}

	/**
	 * 기기의 네트워크 접속 상태를 체크하는 기능
	 * @return
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * uri문자열을 받아서 해당 문자열을 주소로 가지는 웹브라우저를 띄운다
	 * @param strUri
	 */
	private void launchBrowser(String strUri) {
		if (isOnline()) {
			Uri uri = Uri.parse(strUri);
			// 암시적(implicit) 인텐트를 호출한다
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	private void invertCurrencies() {
		int nFor = mForSpinner.getSelectedItemPosition();
		int nHom = mHomSpinner.getSelectedItemPosition();

		mForSpinner.setSelection(nHom);
		mHomSpinner.setSelection(nFor);

		mConvertedTextView.setText("");

		PrefsMgr.setString(this, FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
		PrefsMgr.setString(this, HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
	}

	private int findPositionGivenCode(String code, String[] currencies) {

		for (int i = 0; i < currencies.length; i++) {
			if (extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)) {
				return i;
			}
		}
		return 0;
	}

	private String extractCodeFromCurrency(String currency) {
		return (currency).substring(0,3);
	}

	private String getKey(String keyName) {
		AssetManager assetManager = this.getResources().getAssets();
		Properties properties = new Properties();
		try {
			InputStream inputStream = assetManager.open("keys.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties.getProperty(keyName);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 메뉴 항목의 생성을 위한 메서드
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 메뉴 항목 클릭 이벤트 시 처리를 위한 메소드
		int id = item.getItemId();
		switch (id) {
			case R.id.mnu_invert:
				invertCurrencies();
			    break;

			case R.id.mnu_codes:
				launchBrowser(SplashActivity.URL_CODES);
			    break;
			case R.id.mnu_exit:
			    finish();
			    break;

		}

		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// parent.getId() 결과를 확인하여 내환과 외환 중 어떤 스피너가 선택되었는지 판단한 후
		// 그에 따른 처리를 하는 코드를 추가한다
		switch (parent.getId()) {

			case R.id.spn_for:
			    // 외환 스피너에서 처리할 일
				PrefsMgr.setString(this, FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
			    break;

			case R.id.spn_hom:
			    // 내환 스피너에서 처리할 일
				PrefsMgr.setString(this, HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
			    break;

			default:
				break;
		}
		mConvertedTextView.setText("");

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private class CurrencyConverterTask extends AsyncTask<String, Void, JSONObject> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setTitle("Calculating Result...");
			progressDialog.setMessage("One moment please...");
			progressDialog.setCancelable(true);

			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CurrencyConverterTask.this.cancel(true);
							progressDialog.dismiss();
						}
					});

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			double dCalculated = 0.0;
			String strForCode =
					extractCodeFromCurrency(mCurrencies[mForSpinner.getSelectedItemPosition()]);
			String strHomCode = extractCodeFromCurrency(mCurrencies[mHomSpinner.getSelectedItemPosition()]);
			String strAmount = mAmountEditText.getText().toString();

			try {
				if (jsonObject == null) {
					throw new JSONException("no data available");
				}

				JSONObject jsonRates = jsonObject.getJSONObject(RATES);
				if (strHomCode.equalsIgnoreCase("USD")) {
					dCalculated = Double.parseDouble(strAmount) / jsonRates.getDouble(strForCode);
				} else if (strForCode.equalsIgnoreCase("USD")) {
					dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode);
				} else {
					dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode) / jsonRates.getDouble(strForCode);
				}
			} catch (JSONException e) {
				Toast.makeText(MainActivity.this, "There's been a JSON exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
				mConvertedTextView.setText("");
				e.printStackTrace();
			}
			mConvertedTextView.setText(DECIMAL_FORMAT.format(dCalculated) + " " + strHomCode);
			progressDialog.dismiss();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return new JSONParser().getJSONFromUrl(params[0]);
		}
	}
}
