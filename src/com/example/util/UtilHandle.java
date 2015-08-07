package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.DB.WeatherDB;
import com.example.model.City;
import com.example.model.County;
import com.example.model.Province;

/*
 * 解析和处理服务器返回的省市县数据
 */
public class UtilHandle {
	public synchronized static boolean handleProvincesResponse(WeatherDB db,
			String response) {

		if (!TextUtils.isEmpty(response)) {
			String[] provinces = response.split(",");
			if (provinces != null && provinces.length > 0) {
				for (String p : provinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);

					db.saveProvince(province);
				}
				return true;
			}

		}

		return false;
	}

	public static boolean handleCityResponse(WeatherDB db, String response,
			int provinceid) {
		if (!TextUtils.isEmpty(response)) {
			String[] citys = response.split(",");
			if (citys != null && citys.length > 0) {
				for (String s : citys) {
					String[] array = s.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceid(provinceid);

					db.saveCity(city);

				}
			}
			return true;
		}

		return false;
	}

	public static boolean handleCountyResponse(WeatherDB db, String response,
			int cityid) {
		if (!TextUtils.isEmpty(response)) {
			String[] countys = response.split(",");
			if (countys != null && countys.length > 0) {
				for (String s : countys) {
					String[] array = s.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityid(cityid);

					// 将解析出来的数据存储到County表
					db.saveCounty(county);
				}
				return true;
			}

		}
		return false;
	}

	// /**
	// * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	// */
	// public static void handleWeatherResponse(Context context, String
	// response) {
	// String TAG="xx";
	// System.out.println(response.toString());
	// Log.i(TAG, response.toString());
	// try {
	// JSONObject jsonObject = new JSONObject(response);
	// JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
	// String cityName = weatherInfo.getString("city");
	// String weatherCode = weatherInfo.getString("cityid");
	// String temp1 = weatherInfo.getString("temp1");
	// String temp2 = weatherInfo.getString("temp2");
	// String weatherDesp = weatherInfo.getString("weather");
	// String publishTime = weatherInfo.getString("ptime");
	//
	// System.out.println("handle"+cityName.toString());
	//
	// saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
	// weatherDesp, publishTime);
	// } catch (JSONException e) {
	//
	// e.printStackTrace();
	// }
	// }
	// /**
	// * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	// */
	// public static void saveWeatherInfo(Context context, String cityName,
	// String weatherCode, String temp1, String temp2, String weatherDesp,
	// String publishTime) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
	// SharedPreferences.Editor editor = PreferenceManager
	// .getDefaultSharedPreferences(context).edit();
	// editor.putBoolean("city_selected", true);
	// editor.putString("city_name", cityName);
	// editor.putString("weather_code", weatherCode);
	// editor.putString("temp1", temp1);
	// editor.putString("temp2", temp2);
	// editor.putString("weather_desp", weatherDesp);
	// editor.putString("publish_time", publishTime);
	// editor.putString("current_date", sdf.format(new Date()));
	// System.out.println("editor"+editor.toString());
	// editor.commit();
	// }
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");

			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}

}
