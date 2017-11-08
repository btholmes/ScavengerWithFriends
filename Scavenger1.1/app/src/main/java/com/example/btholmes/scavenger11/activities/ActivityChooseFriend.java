package com.example.btholmes.scavenger11.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.FriendsListAdapter;
import com.example.btholmes.scavenger11.data.Constant;
import com.example.btholmes.scavenger11.data.Tools;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.Friend;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.model.Player;
import com.example.btholmes.scavenger11.model.PushNotification;
import com.example.btholmes.scavenger11.widget.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.btholmes.scavenger11.activities.ActivityFriendDetails.friend;

/**
 * Created by btholmes on 11/4/17.
 */

public class ActivityChooseFriend extends AppCompatActivity{

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private SearchView search;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private Game gameObj;

    private RecyclerView recyclerView;
    private FriendsListAdapter friendsListAdapter;
    private Toolbar toolbarSearch;
    private String title;

    public static List<String> list = new ArrayList<>();


    public static void navigate(AppCompatActivity activity, View transitionImage, String title) {
        Intent intent = new Intent(activity, ActivityChooseFriend.class);
        intent.putExtra("title", title);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);

        //This goes straight to start??
        ActivityCompat.startActivity(activity, intent, null);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);

        title = getIntent().getExtras().getString("title");


        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //        setContentView(R.layout.activity_choose_friend);
        setToolbar();
        initComponents();
        Tools.systemBarLolipop(this);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_friend, menu);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setQueryHint("Search Friend...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    friendsListAdapter.getFilter().filter(s);
                } catch (Exception e) {}
                return true;
            }
        });
        search.onActionViewCollapsed();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_new_friend:
//                Toast.makeText(ActivityChooseFriend.this, "Add New Friend Clicked", Toast.LENGTH_SHORT).show();
//                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ActivityChooseFriend.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setTitle(title);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

    }

    public void initComponents(){


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        friendsListAdapter = new FriendsListAdapter(this, Constant.getFriendsData(this));
        friendsListAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Friend obj, int position) {
                dialogChallengeFriend(obj);
            }
        });
        recyclerView.setAdapter(friendsListAdapter);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }

    private void dialogChallengeFriend(final Friend f) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_challenge);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView opponentName, opponentWins, opponentLosses;
        ImageView image = (ImageView)dialog.findViewById(R.id.opponent_image);
                Picasso.with(this).load(f.getPhoto())
                .resize(200, 200)
                .transform(new CircleTransform())
                .into(image);
        opponentName = (TextView) dialog.findViewById(R.id.opponent_name);
        opponentName.setText(f.getName());

        opponentWins = (TextView) dialog.findViewById(R.id.opponent_wins);
        opponentWins.setText("Wins : 0");
        opponentLosses = (TextView) dialog.findViewById(R.id.opponent_losses);
        opponentLosses.setText("Losses : 0");

        Button challenge = ((Button)dialog.findViewById(R.id.btn_challenge));
        challenge.setText("Challenge");


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String challengerUID = mFirebaseUser.getUid();
//                String opponentUID = f.getUid();
//                Game gameObj = new Game(challengerUID, opponentUID, list);
//
//                addGameToFirebase(gameObj);
//                setPlayerInfo();
//                Toast.makeText(ActivityChooseFriend.this, "Challenged Sent!", Toast.LENGTH_SHORT).show();
//                sendPushNotification();

                Intent intent = new Intent(ActivityChooseFriend.this, MainActivity.class);
                intent.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private void getUserDisplayName(){
        final Query ref = mFirebaseDatabaseReference.child("userList").child(gameObj.getChallenger());
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                if(dataSnapshot.getKey().equals("displayName")){
                    String name = dataSnapshot.getValue().toString();
                    PushNotification msg = new PushNotification("You've Been Challenged", name + " Challenged you", mFirebaseUser.getUid() );
                    mFirebaseDatabaseReference.child("userList").child(friend.getUid()).child("pushNotifications").setValue(msg);
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


    private void sendPushNotification(){
//        Need to send user display name through Push notification
        getUserDisplayName();
    }

    private void addGameToFirebase(Game gameObj){

//        mFirebaseDatabaseReference.child("Games").child(user.getUid()).child("currentGameFolder").push().setValue(url);
//        mFirebaseDatabaseReference.child("Games").child(gameObj.getGameID()).setValue(gameObj);
        mFirebaseDatabaseReference.child("userList").child(gameObj.getChallenger()).child("games").child(gameObj.getGameID()).setValue(gameObj);
        mFirebaseDatabaseReference.child("userList").child(gameObj.getOpponent()).child("games").child(gameObj.getGameID()).setValue(gameObj);

    }

    private void setPlayerInfo(){

        String opponentUID = friend.getUid();
        Player opponent = new Player(opponentUID, list);
        opponent.setAcceptedChallenge(false);
        opponent.setChallenger(false);

        String challengerUID = mFirebaseUser.getUid();
        Player challenger = new Player(challengerUID, list);
        challenger.setAcceptedChallenge(true);
        challenger.setChallenger(true);
//        gameObj = new Game(challengerUID, opponentUID);

        mFirebaseDatabaseReference.child("PlayerInfo").child(gameObj.getGameID()).child(challengerUID).setValue(challenger);
        mFirebaseDatabaseReference.child("PlayerInfo").child(gameObj.getGameID()).child(opponentUID).setValue(opponent);

    }
}
