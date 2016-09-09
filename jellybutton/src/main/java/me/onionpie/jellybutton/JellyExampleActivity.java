package me.onionpie.jellybutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JellyExampleActivity extends AppCompatActivity {
    JellyButton mJellyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jelly_example);
        mJellyButton = (JellyButton)findViewById(R.id.jelly);
        mJellyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
