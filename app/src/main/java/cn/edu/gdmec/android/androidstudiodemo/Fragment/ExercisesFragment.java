package cn.edu.gdmec.android.androidstudiodemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import java.util.HashMap;
import java.util.ArrayList;

import cn.edu.gdmec.android.androidstudiodemo.R;
import cn.edu.gdmec.android.androidstudiodemo.utils.VoteApi;
import cn.edu.gdmec.android.androidstudiodemo.utils.ZSHttpUtil;
import android.widget.SimpleAdapter;


public class ExercisesFragment extends Fragment {
    private ListView list_view;

    public ExercisesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_view = (ListView) view.findViewById(R.id.list_view);
        updateData();
    }

    public void updateData(){
        VoteApi vote_api = new VoteApi(getActivity());
        vote_api.vote_list(new ZSHttpUtil.ZSHttpCallBack() {
            @Override
            public void onDataSuccess(JSONObject jsonObject) {
                // 生成动态数组，加入数据
                final JSONArray data_list = jsonObject.getJSONArray("result");
                ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < data_list.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("vote_icon", data_list.getJSONObject(i).getString("icon"));
                    map.put("vote_title", data_list.getJSONObject(i).getString("title"));
                    map.put("vote_content", data_list.getJSONObject(i).getString("content"));
                    map.put("vote_count", data_list.getJSONObject(i).getString("count"));
                    map.put("vote_rate", data_list.getJSONObject(i).getString("rate"));
                    listItem.add(map);
                }
                // 生成适配器的Item和动态数组对应的元素
                SimpleAdapter listItemAdapter = new SimpleAdapter(getActivity(), listItem,// 数据源
                        R.layout.vote_item,
                        new String[] { "vote_title", "vote_content", "vote_count", "vote_rate" },
                        new int[] { R.id.vote_title, R.id.vote_content, R.id.vote_count, R.id.vote_rate }){
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        final String code = data_list.getJSONObject(position).getString("code");
                        Button tv_user_vote = (Button) view.findViewById(R.id.tv_user_vote);
                        tv_user_vote.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                VoteApi vote_api = new VoteApi(getActivity());
                                vote_api.user_vote(code, new ZSHttpUtil.ZSHttpCallBack() {
                                    @Override
                                    public void onDataSuccess(JSONObject jsonObject) {
                                        Toast.makeText(getActivity(), "投票成功", Toast.LENGTH_SHORT).show();
                                        updateData();
                                    }

                                    @Override
                                    public void onDataFailed(JSONObject jsonObject) {
                                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        return view;
                    }
                };

                // 添加并且显示
                list_view.setAdapter(listItemAdapter);
            }

            @Override
            public void onDataFailed(JSONObject jsonObject) {
                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
