package tistory.zxcv5500.currencies;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * monkey 사용 커맨드
 * adb shell -p tistory.zxcv5500.currencies -v 100
 *
 * Created by sharpen on 2017. 6. 12..
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private Button mCalcButton;
	private TextView mConvertedTextView;
	private EditText mAmountEditText;
	private Spinner mForSpinner, mHomSpinner;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		// ** 테스트에 필요한 리소스에 연결하고 인텐트에 데이터(Bundle)객체에 전달하며 테스트할 액티비티와 그것의 UI 객체 참조를 얻는다


		// 기본 통화 코드를 전달한다
		ArrayList<String> bogusCurrencies = new ArrayList<>();
		bogusCurrencies.add("USD|United States Dollar");
		bogusCurrencies.add("EUR|Euro");
		Intent intent = new Intent();
		intent.putExtra(SplashActivity.KEY_ARRAYLIST, bogusCurrencies);
		setActivityIntent(intent);


		// 테스트할 액티비티 참조를 얻는다
		mActivity = getActivity();

		// 각 뷰 객체 참조를 알아내어 멤버 변수에 보존한다
		mCalcButton = (Button) mActivity.findViewById(R.id.btn_calc);
		mConvertedTextView = (TextView) mActivity.findViewById(R.id.txt_converted);
		mAmountEditText = (EditText) mActivity.findViewById(R.id.edt_amount);
		mForSpinner = (Spinner) mActivity.findViewById(R.id.spn_for);
		mHomSpinner = (Spinner) mActivity.findViewById(R.id.spn_hom);

	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		// 테스트가 끝난 후 모든 리소스를 해제하는데 사용할 수 있다
	}

	public void testInteger() throws Throwable {
		proxyCurrencyConvertTask("12");
	}

	public void testFloat() throws Throwable {
		proxyCurrencyConvertTask("12.3");
	}

	public void proxyCurrencyConvertTask (final String str) throws Throwable {

		final CountDownLatch latch = new CountDownLatch(1);

		mActivity.setCurrencyTaskCallback(new MainActivity.CurrencyTaskCallback() {
			@Override
			public void executionDone() {
				latch.countDown();
				assertEquals(convertToDouble(mConvertedTextView.getText().toString().substring(0, 5)), convertToDouble(str));
			}
		});

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAmountEditText.setText(str);
				mForSpinner.setSelection(0);
				mHomSpinner.setSelection(0);
				mCalcButton.performClick();
			}
		});

		latch.await(30, TimeUnit.SECONDS);
	}

	private double convertToDouble(String str) throws NumberFormatException {
		double dReturn = 0;
		try {
			dReturn = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			throw e;
		}
		return dReturn;
	}


}















