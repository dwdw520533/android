package cn.edu.gdmec.android.androidstudiodemo.utils;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.HashMap;


public class VoteApi {
    private String server_url;
    private Context current_context;

    public VoteApi(Context context) {
        current_context = context;
        server_url = readServerUrl();
    }

    public void user_login(String userName, String userPassword, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("password", userPassword);
        ZSHttpUtil.getHttp(this,server_url + "/user/login", params, zsHttpCallBack);
    }

    public void user_register(String userName, String userPassword, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("password", userPassword);
        ZSHttpUtil.getHttp(this,server_url + "/user/register", params, zsHttpCallBack);
    }

    public void user_logout(final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        ZSHttpUtil.getHttp(this,server_url + "/user/logout", params, zsHttpCallBack);
    }

    public void user_vote(String code, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        ZSHttpUtil.getHttp(this,server_url + "/user/vote", params, zsHttpCallBack);
    }


    public void vote_list(final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        ZSHttpUtil.getHttp(this,server_url + "/vote/list", params, zsHttpCallBack);
    }

    public void modifyServerUrl(String server_url) {
        SharedPreferences sp = current_context.getSharedPreferences("loginInfo", current_context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("server_url", server_url);
        editor.commit();
    }

    public String readServerUrl() {
        SharedPreferences sp = current_context.getSharedPreferences("loginInfo", current_context.MODE_PRIVATE);
        return sp.getString("server_url", "http://127.0.0.1:8000");
    }

    public void modifySessionId(String session_id) {
        SharedPreferences sp = current_context.getSharedPreferences("loginInfo", current_context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("session_id", session_id);
        editor.commit();
    }

    public String readSessionId() {
        SharedPreferences sp = current_context.getSharedPreferences("loginInfo", current_context.MODE_PRIVATE);
        return sp.getString("session_id", "");
    }
}
