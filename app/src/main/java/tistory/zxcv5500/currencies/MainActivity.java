package tistory.zxcv5500.currencies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
}
