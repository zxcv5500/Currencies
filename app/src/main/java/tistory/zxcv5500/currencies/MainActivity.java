package tistory.zxcv5500.currencies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

	private Button mCalcButton;
	private TextView mConvertedTextView;
	private EditText mAmountEditText;
	private Spinner mForSpinner, mHomSpinner;
	private String[] mCurrencies;

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

			    break;

			case R.id.mnu_codes:

			    break;
			case R.id.mnu_exit:
			    finish();
			    break;

		}

		return true;
	}
}
