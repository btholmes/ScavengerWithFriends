package com.example.btholmes.scavenger11.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.btholmes.scavenger11.pushNotifications.NotificationListener;
import com.example.btholmes.scavenger11.tools.Utility;
import com.example.btholmes.scavenger11.widget.CircleTransform;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private SharedPreferences vpPref;
    private int viewPagerPage;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;
    private String imageURL;


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
    private StoreFragment f_store;
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


    Utility utility = Utility.getInstance(this);
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content);

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        user  = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        //SO user doesn't have to log in everytime
        firebaseAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    goToLogin();
                }
            }
        };
//        auth.addAuthStateListener(firebaseAuthListner);


        checkUserNull();

        initComponents();
        initAction();
        setupDrawerLayout();

        setupTabIcons();
        setupTabClick();
        setAvatar();
//        getDisplayName();


        prepareActionBar(toolbar);
        changeDefaultIcon();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initComponents(){
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
        final Dialog dialog = new Dialog(MainActivity.this);
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
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.CHARACTER, user.getDisplayName());
//                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Challenged A Friend");

                ActivityChooseFriend.navigate(MainActivity.this, view, "Challenge Friend");

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
                        ActivityChooseFriend.navigate(MainActivity.this, view, "Find Friend");
                        break;
                }
            }
        });
    }

    private void setAvatar() {
        if(avatar != null){
            final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue().toString();
                    Picasso.with(getApplicationContext()).load(url).resize(100, 100).transform(new CircleTransform()).into(avatar);

                    avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            selectImage();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;


    private void updateDisplayName(String newDisplay){


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplay)
//                .setPhotoUri(Uri.parse(profilePicURL))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Name Changed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("displayName").setValue(newDisplay);
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
//    private void getDisplayName() {
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        displayName.setText(user.getDisplayName());
//        if(user != null){
//            final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid());
//            ref.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                    //  dataSnapshot.getValue(User.class);
//                    if(dataSnapshot.getKey().equals("displayName")){
//                        String userDisplayName = dataSnapshot.getValue(String.class);
//                        if(userDisplayName != null){
//                            displayName.setText(userDisplayName);
//
//                        }else{
//                            displayName.setText("Please set Display Name");
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//                @Override
//                public void onCancelled(DatabaseError error) {}
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//            });
//
//        }
//
//    }


    public void goToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void addTokenToDB(){
        String tkn = FirebaseInstanceId.getInstance().getToken();

        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("userToken").setValue(tkn);
    }

    public void checkUserNull(){
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else{
//            addUserToDB(user);
//            addPhotoUrl(user);





//            authListener = new FirebaseAuth.AuthStateListener() {
//                @Override
//                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    if (user == null) {
//
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            };
//            auth.addAuthStateListener(authListener);

            final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid());
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if(dataSnapshot.getKey().equals("pushNotifications")){
                        startService(new Intent(MainActivity.this, NotificationListener.class));
//        startService(new Intent(this, photoChangeService.class));
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        vpPref = getPreferences(MODE_PRIVATE);
        viewPagerPage = vpPref.getInt("viewPagerPage", 0);

        viewPager.setCurrentItem(viewPagerPage);
        auth.addAuthStateListener(firebaseAuthListner);
//        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int index = viewPager.getCurrentItem();
//        viewPager.getAdapter().no
        getPreferences(MODE_PRIVATE).edit().putInt("viewPagerPage", index).commit();
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

//        if(user.isEmailVerified()){
            userEmail = (TextView) header.findViewById(R.id.userEmail);
            userEmail.setText(user.getEmail());
//        }else{
//            Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
//        }


        displayName = (TextView) header.findViewById(R.id.displayName);
        displayName.setText(user.getDisplayName());

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                if(menuItem.getTitle().equals("Change Display Name")){
                    Intent i = new Intent(MainActivity.this, ActivityEditInfo.class);
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
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[2]);
        tabLayout.getTabAt(2).setIcon(imageResId[1]);
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

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                Snackbar.make(parent_view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_sign_out: {
                  if(user != null){
                      signOut();
                  }else{
                      Toast.makeText(MainActivity.this, "ERROR : Somehow someone is signed in, and user == null", Toast.LENGTH_SHORT).show();
                  }
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

//    private void addPhotoUrl(FirebaseUser user){
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        String url = null;
//        if(user.getPhotoUrl() != null){
//            url = user.getPhotoUrl().toString();
//        }
//        if(url != null){
//            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(url);
//        }else{
//            String defaultPic = "http://vvcexpl.com/wordpress/wp-content/uploads/2013/09/profile-default-male.png";
//            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(defaultPic);
//        }
//    }

//    private void addUserToDB(FirebaseUser userToAdd){
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        mFirebaseDatabaseReference.child("userList").child(userToAdd.getUid()).child("uid").setValue(userToAdd.getUid());
//    }

    private long exitTime = 0;

    public void doExitApp() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//        } else {
            finish();
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

