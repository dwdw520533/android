package cn.edu.gdmec.android.androidstudiodemo.utils;
import java.util.Map;
import java.util.HashMap;


public class VoteApi {
    private String server_url = "http://192.168.5.65:8000";

    public void user_login(String userName, String userPassword, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("password", userPassword);
        ZSHttpUtil.getHttp(server_url + "/user/login", params, zsHttpCallBack);
    }

    public void user_register(String userName, String userPassword, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("password", userPassword);
        ZSHttpUtil.getHttp(server_url + "/user/register", params, zsHttpCallBack);
    }

    public void user_logout(final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        ZSHttpUtil.getHttp(server_url + "/user/logout", params, zsHttpCallBack);
    }

    public void user_vote(String code, final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        ZSHttpUtil.getHttp(server_url + "/user/vote", params, zsHttpCallBack);
    }


    public void vote_list(final ZSHttpUtil.ZSHttpCallBack zsHttpCallBack) {
        Map<String, String> params = new HashMap<>();
        ZSHttpUtil.getHttp(server_url + "/vote/list", params, zsHttpCallBack);
    }
}
