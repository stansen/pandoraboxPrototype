package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jiudeng007.barcodelib.ScanCodeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Model.PasswordInfoModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment.OnListFragmentInteractionListener;
import me.onionpie.pandorabox.UI.Fragment.PasswordRuleFragment;
import me.onionpie.pandorabox.Utils.CommonPreference;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentInteractionListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
        mNavView.setCheckedItem(R.id.password_list);
        replaceFragment(R.id.main_content_container, new PasswordListFragment());
        mToolbar.setTitle(getString(R.string.password_list));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_scan_code || id == R.id.nav_send) {

        } else
            mToolbar.setTitle(item.getTitle());
        if (id == R.id.password_list) {
            replaceFragment(R.id.main_content_container, new PasswordListFragment());
            // Handle the camera action
        } else if (id == R.id.password_rule_setting) {
            replaceFragment(R.id.main_content_container, new PasswordRuleFragment());
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_scan_code) {
            Intent intent = new Intent(this, ScanCodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.password_export) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(PasswordInfoModel item) {

    }
    private void validate2DCode(){
        if (CommonPreference.getBoolean(this, Constans.QRCODE_VALID)){

        }else {

        }
    }
}
