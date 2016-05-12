package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.R;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.img_head)
    ImageView mImgHead;
    @Bind(R.id.account_name)
    MaterialEditText mAccountName;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.to_register)
    TextView mToRegister;
    @Bind(R.id.to_login)
    Button mToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.to_register)
    public void ToRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
