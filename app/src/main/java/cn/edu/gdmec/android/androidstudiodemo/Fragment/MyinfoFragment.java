package cn.edu.gdmec.android.androidstudiodemo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import cn.edu.gdmec.android.androidstudiodemo.LoginActivity;
import cn.edu.gdmec.android.androidstudiodemo.R;
import cn.edu.gdmec.android.androidstudiodemo.ServerUrlActivity;
import cn.edu.gdmec.android.androidstudiodemo.SettingActivity;
import cn.edu.gdmec.android.androidstudiodemo.UserInfoActivity;
import cn.edu.gdmec.android.androidstudiodemo.utils.AnalysisUtils;
import cn.edu.gdmec.android.androidstudiodemo.utils.VoteApi;
import cn.edu.gdmec.android.androidstudiodemo.utils.ZSHttpUtil;


public class MyinfoFragment extends Fragment {

    private ImageView iv_head_icon;//
    private TextView tv_user_name, tv_user_logout;
    private LinearLayout ll_head;
    private RelativeLayout rl_course_history;
    private RelativeLayout rl_setting;
    private RelativeLayout rl_server_url;
    //private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_head = (LinearLayout) view.findViewById(R.id.ll_head);
        iv_head_icon = (ImageView) view.findViewById(R.id.iv_head_icon);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        rl_course_history = (RelativeLayout) view.findViewById(R.id.rl_course_history);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        tv_user_logout = (TextView) view.findViewById(R.id.tv_user_logout);
        rl_server_url = (RelativeLayout) view.findViewById(R.id.rl_server_url);
        setLoginParams(AnalysisUtils.readLoginStatus(getActivity()));
        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnalysisUtils.readLoginStatus(getActivity())) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //startActivity(intent);  若是登录完自动跳转到课程界面，则直接用这个方法即可，这里修改以防后面需要
                    /**
                     * 优化修改，注意，这里不是从fragment打开activity，而是从主页活动打开登陆活动
                     * 若不是，则无法在主页活动直接使用onActivityResult()
                     * **/
                    getActivity().startActivityForResult(intent, 1);
                }
            }
        });
        rl_course_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnalysisUtils.readLoginStatus(getActivity())) {
                    Toast.makeText(getActivity(), "播放历史界面", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnalysisUtils.readLoginStatus(getActivity())) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    getActivity().startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getActivity(), "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnalysisUtils.readLoginStatus(getActivity())) {
                    VoteApi vote_api = new VoteApi(getActivity());
                    vote_api.user_logout(new ZSHttpUtil.ZSHttpCallBack() {
                        @Override
                        public void onDataSuccess(JSONObject jsonObject) {
                            Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT).show();
                            AnalysisUtils.clearLoginStatus(getActivity());
                            Intent data = new Intent();
                            data.putExtra("isLogin", false);
                            getActivity().setResult(getActivity().RESULT_OK, data);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_body, new MyinfoFragment()).commit();
                        }

                        @Override
                        public void onDataFailed(JSONObject jsonObject) {
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rl_server_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServerUrlActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 这个方法用在onViewCreated()，每次初始化这个界面都会启动
     * 通过登录后留在此页面并且立刻刷新用户名会在MainActivity的onActivityResult中处理
     **/
    private void setLoginParams(boolean isLogin) {
        if (isLogin) {
            tv_user_logout.setVisibility(View.VISIBLE);
            tv_user_name.setText(AnalysisUtils.readLoginUserName(getActivity()));
        } else {
            tv_user_logout.setVisibility(View.GONE);
            tv_user_name.setText("点击登录");
        }
    }
}
