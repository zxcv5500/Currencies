package tistory.zxcv5500.currencies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 데이터베이스를 읽고 쓰는 것보다 Preferences에 데이터를 저장하고 읽고 쓰는 것은 느리다.
 * 간단한 데이터일 경우에만 프리퍼런스 클래스를 활용해 데이터를 저장한다.
 * Created by sharpen on 2017. 6. 11..
 */

public class PrefsMgr {
	private static SharedPreferences sSharedPreferences;

	public static void setString(Context context,String locale, String code) {
		sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sSharedPreferences.edit();
		editor.putString(locale, code);
		editor.commit();
	}

	public static String getString(Context context, String locale) {
		sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sSharedPreferences.getString(locale, null);
	}
}
