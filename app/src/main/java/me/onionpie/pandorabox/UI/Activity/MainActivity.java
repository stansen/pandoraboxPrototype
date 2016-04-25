package me.onionpie.pandorabox.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.onionpie.pandorabox.Model.PasswordInfoModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment;

public class MainActivity extends BaseActivity implements PasswordListFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(PasswordInfoModel item) {

    }
}
