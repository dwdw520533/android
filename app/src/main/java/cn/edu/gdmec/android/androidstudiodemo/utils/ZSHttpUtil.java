package cn.edu.gdmec.android.androidstudiodemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public class ZSHttpUtil {

	/**
	 * Http请求回调
	 */
	public static abstract class ZSHttpCallBack {

		/**
		 * 服务器正常返回数据 success = true
		 * @param jsonObject 格式化数据为FastJson的JSONObject
         */
		public abstract void onDataSuccess(JSONObject jsonObject);
		/**
		 * 服务器返回，但数据不正确 success = false
		 * @param jsonObject 格式化数据为FastJson的JSONObject
		 */
		public void onDataFailed(JSONObject jsonObject) {

		}

		/**
		 * 服务器不通
		 * 没有网络
		 * 服务器返回不是JSON数据
		 */
		public void onException() {

		}

		/**
		 * 一个Http请求结束，无论返回结果是什么
		 */
		public void onHttpAfter() {

		}

		/**
		 * 服务器返回数据，无论数据是什么
		 * @param response 服务器返回的数据
         */
		public void onDataResponse(String response) {

		}
	}


	/**
	 * @param url 接口地址
	 * @param params 接口参数
	 * @param zsHttpCallBack 服务器返回回调
	 */
	public static void getPostHttp(String url, Map<String, String> params,
								   final ZSHttpCallBack zsHttpCallBack) {

		if(null == params) {
			params = new HashMap<>();
		} else {
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if(null == params.get(key)) {
					params.put(key, "");
				}
			}
		}

		Iterator<String> it = params.keySet().iterator();
		String str = "";
		while (it.hasNext()) {
			String key =  it.next();
			str += key + "=" + params.get(key) + "&";
		}
		System.out.println("server_param" + str.substring(0, str.length() - 1));

		OkHttpUtils.post()
				.url(url)
				.params(params)
				.build()
				.execute(new Callback<String>() {

					@Override
					public void onBefore(Request request, int id) {
						super.onBefore(request, id);
						// do something
					}

					@Override
					public void onAfter(int id) {
						super.onAfter(id);
						;
						// do something
					}

					@Override
					public String parseNetworkResponse(Response response, int id) throws Exception {
						return response.body().string();
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						zsHttpCallBack.onException();
					}

					@Override
					public void onResponse(String response, int id) {
						zsHttpCallBack.onDataResponse(response);
						if(response == null || response.isEmpty()) {
							zsHttpCallBack.onException();
						} else {
							JSONObject jsonObject;
							try {
								jsonObject = JSON.parseObject(response);
								switch (jsonObject.getIntValue("code")) {
									case 0:
										if (jsonObject.get("data") instanceof JSONObject) {
											zsHttpCallBack.onDataSuccess(jsonObject.getJSONObject("data"));
										} else {
											zsHttpCallBack.onDataSuccess(jsonObject);
										}
										break;
									default:
										zsHttpCallBack.onDataFailed(jsonObject);


								}
							} catch (Exception e) {
								e.printStackTrace();
								zsHttpCallBack.onException();
							}
						}
					}

				});
	}

	public static void getHttp(VoteApi api, String url, Map<String, String> params, final ZSHttpCallBack zsHttpCallBack) {

		String session_id = api.readSessionId();
		Set<String> keySet = params.keySet();
		String[] strings = new String[keySet.size()];
		strings = keySet.toArray(strings);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strings.length; i++){
			if (i == 0) {
				stringBuilder.append("?");
			} else {
				stringBuilder.append("&");
			}
			stringBuilder.append(strings[i]);
			stringBuilder.append("=");
			stringBuilder.append(params.get(strings[i]));
		}
		url += stringBuilder.toString();
		GetBuilder getBuilder = OkHttpUtils.get();
		getBuilder.url(url);
		getBuilder.addHeader("Cookie", "sessionid=" + session_id);
		getBuilder.build()
				.execute(new Callback<String>() {

					@Override
					public void onBefore(Request request, int id) {
						super.onBefore(request, id);
						// do something
					}

					@Override
					public void onAfter(int id) {
						super.onAfter(id);
						zsHttpCallBack.onHttpAfter();
						// do something
					}

					@Override
					public String parseNetworkResponse(Response response, int id) throws Exception {
						return response.body().string();
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						zsHttpCallBack.onException();
					}

					@Override
					public void onResponse(String response, int id) {
						if(response == null || response.isEmpty()) {
							zsHttpCallBack.onException();
						} else {
							JSONObject jsonObject;
							try {
								jsonObject = JSON.parseObject(response);
								switch (jsonObject.getIntValue("code")) {
									case 0:
										zsHttpCallBack.onDataSuccess(jsonObject);
										break;
									default:
										zsHttpCallBack.onDataFailed(jsonObject);
								}
							} catch (Exception e) {
								e.printStackTrace();
								zsHttpCallBack.onException();
							}
						}
					}

				});

	}
}
