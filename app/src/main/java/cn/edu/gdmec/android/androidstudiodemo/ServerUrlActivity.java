package cn.edu.gdmec.android.androidstudiodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.gdmec.android.androidstudiodemo.utils.VoteApi;

/**
 * Created by Jack on 2018/4/10.
 */

public class ServerUrlActivity extends AppCompatActivity {
    private EditText et_server_url;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_url);
        init();
    }
    private void init() {
        final VoteApi vote_api = new VoteApi(this);
        et_server_url = (EditText) findViewById(R.id.et_server_url);
        et_server_url.setText(vote_api.readServerUrl());
        btn_save = ((Button) findViewById(R.id.btn_save));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String server_url = et_server_url.getText().toString().trim();
                if(TextUtils.isEmpty(server_url)){
                    Toast.makeText(ServerUrlActivity.this, "请输入服务器地址", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(ServerUrlActivity.this, "服务器地址设置成功", Toast.LENGTH_SHORT).show();
                    vote_api.modifyServerUrl(server_url);
                }
            }
        });
    }

}