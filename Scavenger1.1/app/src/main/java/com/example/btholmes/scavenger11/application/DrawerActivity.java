package com.example.btholmes.scavenger11.application;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.activities.ActivityChooseFriend;
import com.example.btholmes.scavenger11.activities.ActivityEditInfo;
import com.example.btholmes.scavenger11.adapter.PageFragmentAdapter;
import com.example.btholmes.scavenger11.data.Tools;
import com.example.btholmes.scavenger11.fragment.FriendFragment;
import com.example.btholmes.scavenger11.fragment.GameFragment;
import com.example.btholmes.scavenger11.fragment.LeaderBoardFragment;
import com.example.btholmes.scavenger11.fragment.MessageFragment;
import com.example.btholmes.scavenger11.fragment.NotificationFragment;
import com.example.btholmes.scavenger11.fragment.StoreFragment;
import com.example.btholmes.scavenger11.login.LoginActivity;
import com.example.btholmes.scavenger11.widget.CircleTransform;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by btholmes on 11/11/17.
 */

/**
 * This is the activity which sets up the main view for the application
 */
public class DrawerActivity extends ScavengerActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    protected ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout drawerLayout;

    protected Toolbar toolbar;
    protected ActionBar actionbar;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected FloatingActionButton fab;

    protected TextView userEmail;
    protected ImageView avatar;
    protected TextView displayName;

    protected PageFragmentAdapter adapter;

    protected GameFragment f_game;
    protected FriendFragment f_friend;
    protected StoreFragment f_store;
    protected MessageFragment f_message;
    protected NotificationFragment f_notif;
    protected LeaderBoardFragment f_leaderBoard;
    private static int[] imageResId = {
            R.drawable.tab_feed,
            R.drawable.tab_friend,
            R.drawable.tab_chat,
            R.drawable.tab_notif,
            R.drawable.tab_profile
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        initComponents();
        initAction();
        setupDrawerLayout();

        setupTabIcons();
        setupTabClick();
//        setAvatar();
//        getDisplayName();
        displayName.setText(user.getDisplayName());

        prepareActionBar(toolbar);
        changeDefaultIcon();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void dialogNewGame() {
        final Dialog dialog = new Dialog(DrawerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_new_game);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

//        ImageView image = (ImageView)dialog.findViewById(R.id.image);
        //        Picasso.with(getActivity()).load(friend.getPhoto())
//                .resize(200, 200)
//                .transform(new CircleTransform())
//                .into(image);

        Button challengeFriend = ((Button)dialog.findViewById(R.id.btn_challenge_friend));
        Button challengeRandom = ((Button)dialog.findViewById(R.id.btn_challenge_random));


        challengeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();

                /**
                 * Reporting to Firebase Analytics
                 */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CHARACTER, user.getDisplayName());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Challenged A Friend");
                logFirebaseEvent("Challenge A Friend", bundle);


                ActivityChooseFriend.navigate(DrawerActivity.this, view, "Challenge Friend");

//                Intent intent = new Intent(getActivity(), ActivityChooseFriend.class);
//////                intent.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
//                startActivity(intent);
//                Toast.makeText(getActivity(), "Challenge Friend Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void initAction(){
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();
                switch (position) {
                    case 0 :
//                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        dialogNewGame();
                        break;
                    case 1 :
                        ActivityChooseFriend.navigate(DrawerActivity.this, view, "Find Friend");
                        break;
                }
            }
        });
    }

    private void setAvatar() {
        if(avatar != null){

            Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).resize(100, 100).transform(new CircleTransform()).into(avatar);
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                            selectImage();
                }
            });

        }
    }




    private void updateDisplayName(String newDisplay){


//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(newDisplay)
////                .setPhotoUri(Uri.parse(profilePicURL))
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Name Changed", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("displayName").setValue(newDisplay);
//        setupDrawerLayout();
//        setAvatar();
    }

    private void updateEmail(String newEmail){


//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
////                .setDisplayName(newDisplay)
////                .setPhotoUri(Uri.parse(profilePicURL))
//                .
//                .build();

//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Name Changed", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("displayName").setValue(newDisplay);
//        setupDrawerLayout();
//        setAvatar();
    }


    /**
     * This function calls Firebase for the DisplayName from userList
     */
    private void getDisplayNameDUD() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        displayName.setText(user.getDisplayName());
        if(user != null){
            final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid());
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    //  dataSnapshot.getValue(User.class);
                    if(dataSnapshot.getKey().equals("displayName")){
                        String userDisplayName = dataSnapshot.getValue(String.class);
                        if(userDisplayName != null){
                            displayName.setText(userDisplayName);

                        }else{
                            displayName.setText("Please set Display Name");
                        }

                    }

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError error) {}

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
            });

        }

    }

    protected void goToLogin(){
        Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        setAvatar();


        userEmail = (TextView) header.findViewById(R.id.userEmail);
        userEmail.setText(user.getEmail());



        displayName = (TextView) header.findViewById(R.id.displayName);
        displayName.setText(user.getDisplayName());

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                if(menuItem.getTitle().equals("Change Display Name")){
                    Intent i = new Intent(DrawerActivity.this, ActivityEditInfo.class);
                    i.putExtra("view", "Display");
                    startActivity(i);
                    finish();
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
                    signOut();
                }
//                Snackbar.make(parent_view, menuItem.getTitle()+" Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_game == null) { f_game = new GameFragment(); }
//        if (f_friend == null) { f_friend = new FriendFragment(); }
        if (f_store == null) { f_store = new StoreFragment(); }
        if (f_message == null) { f_message = new MessageFragment(); }
        if (f_notif == null) { f_notif = new NotificationFragment(); }
        if (f_leaderBoard == null) { f_leaderBoard = new LeaderBoardFragment(); }
        adapter.addFragment(f_game, getString(R.string.tab_game));
        adapter.addFragment(f_message, getString(R.string.tab_message));
//        adapter.addFragment(f_friend, getString(R.string.tab_friend));
        adapter.addFragment(f_store, "Store");
        adapter.addFragment(f_notif, getString(R.string.tab_notif));
        adapter.addFragment(f_leaderBoard, getString(R.string.tab_leaderBoard));
        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(5);
    }
    private void setupTabIcons() {

        FrameLayout.LayoutParams frameParams;
        DisplayMetrics dm;
/**
 * Tab One
 */
        FrameLayout tabOne = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        dm = tabOne.getResources().getDisplayMetrics();


        TextView text = (TextView) tabOne.findViewById(R.id.text1);
        text.setText("100");


        ImageView icon = (ImageView) tabOne.findViewById(R.id.icon);




        icon.setBackgroundResource(imageResId[0]);
        tabLayout.getTabAt(0).setCustomView(tabOne);

/**
 * Tab Two
 */
        FrameLayout tabTwo = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        dm = tabTwo.getResources().getDisplayMetrics();


        text = (TextView) tabTwo.findViewById(R.id.text1);
        text.setText("2");

        icon = (ImageView) tabTwo.findViewById(R.id.icon);




        icon.setBackgroundResource(imageResId[1]);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

/**
 * Tab Three
 */
        FrameLayout tabThree = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        dm = tabThree.getResources().getDisplayMetrics();

        text = (TextView) tabThree.findViewById(R.id.text1);
        text.setVisibility(View.GONE);

        icon = (ImageView) tabThree.findViewById(R.id.icon);
        icon.setBackgroundResource(imageResId[2]);

        /**
         * Set Image View layout Parmas
         */
        frameParams = (FrameLayout.LayoutParams) icon.getLayoutParams();
        frameParams.setMargins(convertDpToPx(0,dm),convertDpToPx(4, dm),convertDpToPx(0,dm),convertDpToPx(0,dm));
        icon.setLayoutParams(frameParams);
        icon.requestLayout();


        tabLayout.getTabAt(2).setCustomView(tabThree);

/**
 * Tab Four
 */
        FrameLayout tabFour = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        dm = tabFour.getResources().getDisplayMetrics();


        text = (TextView) tabFour.findViewById(R.id.text1);
        text.setText("1");


        icon = (ImageView) tabFour.findViewById(R.id.icon);


        icon.setBackgroundResource(imageResId[3]);
        tabLayout.getTabAt(3).setCustomView(tabFour);


/**
 * Tab Five
 */
        FrameLayout tabFive = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        dm = tabFive.getResources().getDisplayMetrics();

        text = (TextView) tabFive.findViewById(R.id.text1);
        text.setVisibility(View.GONE);
//        text.setText("10");


        icon = (ImageView) tabFive.findViewById(R.id.icon);
        /**
         * Set Image View layout Parmas
         */
        frameParams = (FrameLayout.LayoutParams) icon.getLayoutParams();
        frameParams.setMargins(convertDpToPx(0,dm),convertDpToPx(4, dm),convertDpToPx(0,dm),convertDpToPx(0,dm));
        icon.setLayoutParams(frameParams);
        icon.requestLayout();


        icon.setBackgroundResource(imageResId[4]);
        tabLayout.getTabAt(4).setCustomView(tabFive);


    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                actionbar.setTitle(adapter.getTitle(position));
                switch(position){
                    case 0 :
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 1 :
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 2 :
                        fab.setVisibility(View.GONE);
                        break;
                    case 3 :
                        fab.setVisibility(View.GONE);
                        break;
                    case 4 :
                        fab.setVisibility(View.GONE);
                        break;
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goToLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_sign_out: {
                if(user != null){
                    signOut();
                }else{
                    Toast.makeText(DrawerActivity.this, "ERROR : Somehow someone is signed in, and user == null", Toast.LENGTH_SHORT).show();
                }
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

    protected void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }

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
