package me.onionpie.pandorabox.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.R;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.register_icon)
    ImageView mRegisterIcon;
    @Bind(R.id.account_name)
    MaterialEditText mAccountName;
    @Bind(R.id.auth_code)
    MaterialEditText mAuthCode;
    @Bind(R.id.send_auth_code)
    Button mSendAuthCode;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.password_confirm)
    MaterialEditText mPasswordConfirm;
    @Bind(R.id.to_register)
    Button mToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
}
