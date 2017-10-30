package com.example.btholmes.scavenger11.main;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.PageFragmentAdapter;
import com.example.btholmes.scavenger11.fragment.FriendFragment;
import com.example.btholmes.scavenger11.fragment.GameFragment;
import com.example.btholmes.scavenger11.fragment.LeaderBoardFragment;
import com.example.btholmes.scavenger11.fragment.MessageFragment;
import com.example.btholmes.scavenger11.fragment.NotificationFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private ActionBar actionbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private View parent_view;

    private TextView userEmail;
    private ImageView avatar;
    private TextView displayName;

    private PageFragmentAdapter adapter;

    private GameFragment f_game;
    private FriendFragment f_friend;
    private MessageFragment f_message;
    private NotificationFragment f_notif;
    private LeaderBoardFragment f_leaderBoard;
    private static int[] imageResId = {
            R.drawable.tab_feed,
            R.drawable.tab_friend,
            R.drawable.tab_chat,
            R.drawable.tab_notif,
            R.drawable.tab_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();

        // for system bar in lollipop
//        Tools.systemBarLolipop(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupDrawerLayout();
        prepareActionBar(toolbar);
        changeDefaultIcon();
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        if (!isSearch) {
            settingDrawer();
//        }

    }

    private void changeDefaultIcon(){
        toolbar.post(new Runnable() {
            @Override
            public void run() {

                //THis resizes the drawable
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_people, null);

                Bitmap b = ((BitmapDrawable)d).getBitmap();

                int sizeX = (int)Math.round(d.getIntrinsicWidth() * 0.29);
                int sizeY = (int)Math.round(d.getIntrinsicHeight() * 0.29);

                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

                d = new BitmapDrawable(getResources(), bitmapResized);

                //This changes the color to white
                d.setTint(Color.WHITE);

                toolbar.setNavigationIcon(d);
            }
        });

    }

    private void settingDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        View header = view.getHeaderView(0);

        avatar = (ImageView) header.findViewById(R.id.avatar);
        userEmail = (TextView) header.findViewById(R.id.userEmail);
        displayName = (TextView) header.findViewById(R.id.displayName);

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                if(menuItem.getTitle().equals("Change Display Name")){
//                    Intent i = new Intent(ActivityMain.this, ActivityEditInfo.class);
//                    i.putExtra("view", "Display");
//                    startActivity(i);
                }else if(menuItem.getTitle().equals("Change Email")){
//                    Intent i = new Intent(ActivityMain.this, ActivityEditInfo.class);
//                    i.putExtra("view", "Email");
//                    startActivity(i);
                }else if(menuItem.getTitle().equals("Data Collection")) {
//                    Intent i = new Intent(ActivityMain.this, dataCollectionActivity.class);
//                    startActivity(i);
                }else if(menuItem.getTitle().equals("Change Password")){
//                    Intent i = new Intent(ActivityMain.this, ActivityEditInfo.class);
//                    i.putExtra("view", "Password");
//                    startActivity(i);

                }else if(menuItem.getTitle().equals("Delete Account")){
//                    user = FirebaseAuth.getInstance().getCurrentUser();
//                    if (user != null) {
//                        user.delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(ActivityMain.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(ActivityMain.this, SignupActivity.class));
//                                            finish();
////                                            progressBar.setVisibility(View.GONE);
//                                        } else {
//                                            Toast.makeText(ActivityMain.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
////                                            progressBar.setVisibility(View.GONE);
//                                        }
//                                    }
//                                });
//                    }

                }else if(menuItem.getTitle().equals("Sign Out")){
//                    auth = FirebaseAuth.getInstance();
//                    if(LoginManager.getInstance() != null){
//                        LoginManager.getInstance().logOut();
//                        auth.signOut();
////                        Intent i = new Intent(ActivityMain.this, LoginActivity.class);
////                        startActivity(i);
////                        finish();
//                    }else{
//                        auth.signOut();
////                        Intent i = new Intent(ActivityMain.this, LoginActivity.class);
////                        startActivity(i);
////                        finish();
//                    }
                }

//                Snackbar.make(parent_view, menuItem.getTitle()+" Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_game == null) { f_game = new GameFragment(); }
        if (f_friend == null) { f_friend = new FriendFragment(); }
        if (f_message == null) { f_message = new MessageFragment(); }
        if (f_notif == null) { f_notif = new NotificationFragment(); }
        if (f_leaderBoard == null) { f_leaderBoard = new LeaderBoardFragment(); }
        adapter.addFragment(f_game, getString(R.string.tab_game));
        adapter.addFragment(f_friend, getString(R.string.tab_friend));
        adapter.addFragment(f_message, getString(R.string.tab_message));
        adapter.addFragment(f_notif, getString(R.string.tab_notif));
        adapter.addFragment(f_leaderBoard, getString(R.string.tab_leaderBoard));
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        tabLayout.getTabAt(3).setIcon(imageResId[3]);
        tabLayout.getTabAt(4).setIcon(imageResId[4]);
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                actionbar.setTitle(adapter.getTitle(position));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(getString(R.string.about_text));
                builder.setNeutralButton("OK", null);
                builder.show();
                return true;
            }
            case R.id.action_login: {
//                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
//                startActivity(i);
                return true;
            }
            case R.id.action_settings: {
                Snackbar.make(parent_view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_regist: {
//                Intent i = new Intent(getApplicationContext(), ActivityRegistration.class);
//                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private long exitTime = 0;

    public void doExitApp() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
//            finish();
//        }
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }


    // handle click profile page
    public void actionClick(View view){

//        f_profile.actionClick(view);
    }
}
