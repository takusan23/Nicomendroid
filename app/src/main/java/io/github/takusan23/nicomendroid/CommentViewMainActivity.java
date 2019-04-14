package io.github.takusan23.nicomendroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.github.takusan23.nicomendroid.Fragment.CommentFragment;
import io.github.takusan23.nicomendroid.Fragment.GiftFragment;
import io.github.takusan23.nicomendroid.Fragment.LicenseFragment;

public class CommentViewMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private static String liveId;
    public static boolean isShowToast = false;
    public static boolean isTTS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view_main);
        //生放送ID
        liveId = getIntent().getStringExtra("id");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //はじめはコメント一覧を出す
        if (getIntent().getStringExtra("fragment").equals("comment_list")) {
            //情報を渡す
            Bundle bundle = new Bundle();
            bundle.putString("response", getIntent().getStringExtra("response"));
            CommentFragment commentFragment = new CommentFragment();
            commentFragment.setArguments(bundle);
            rePlaceFragment(commentFragment);
        }

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
        getMenuInflater().inflate(R.menu.comment_view_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_license:
                rePlaceFragment(new LicenseFragment());
                break;
            case R.id.menu_toast:
                //チェック
                if (!item.isChecked()){
                    isShowToast = true;
                    item.setChecked(true);
                }else {
                    isShowToast = false;
                    item.setChecked(false);
                }
                break;
            case R.id.menu_tts:
                //チェック
                if (!item.isChecked()){
                    isTTS = true;
                    item.setChecked(true);
                }else {
                    isTTS = false;
                    item.setChecked(false);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.menu_commentList:
                //情報を渡す
                bundle.putString("response", getIntent().getStringExtra("response"));
                CommentFragment commentFragment = new CommentFragment();
                commentFragment.setArguments(bundle);
                rePlaceFragment(commentFragment);
                break;
            case R.id.menu_giftList:
                //情報を渡す
                bundle.putString("id", liveId);
                GiftFragment giftFragment = new GiftFragment();
                giftFragment.setArguments(bundle);
                rePlaceFragment(giftFragment);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * フラグメント置き換え
     */
    private void rePlaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //置き換え
        fragmentTransaction.replace(R.id.fragment_area, fragment);
        fragmentTransaction.commit();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public static String getLiveId() {
        return liveId;
    }
}
