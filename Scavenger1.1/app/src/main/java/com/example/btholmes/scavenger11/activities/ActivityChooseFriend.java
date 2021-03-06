package com.example.btholmes.scavenger11.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.FriendsListAdapter;
import com.example.btholmes.scavenger11.application.ScavengerActivity;
import com.example.btholmes.scavenger11.data.Tools;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.model.Player;
import com.example.btholmes.scavenger11.model.User;
import com.example.btholmes.scavenger11.widget.CircleTransform;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.btholmes.scavenger11.activities.ActivityFriendDetails.friend;

/**
 * Created by btholmes on 11/4/17.
 */

public class ActivityChooseFriend extends ScavengerActivity{


    private SearchView search;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private Game gameObj;

    private ListView listView;
    private FriendsListAdapter friendsListAdapter;
    private Toolbar toolbarSearch;
    private String title;

    private FirebaseListAdapter<User> firebaseListAdapter;

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


        setUpAdBanner();

        title = getIntent().getExtras().getString("title");

        setToolbar();
        initComponents();
        Tools.systemBarLolipop(this);

    }

    private void setUpAdBanner(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
//                    friendsListAdapter.getFilter().filter(s);
                    setUpFirebaseAdapter(s);
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


        listView = (ListView) findViewById(R.id.listView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);

//        friendsListAdapter = new FriendsListAdapter(this, Constant.getFriendsData(this));
//        friendsListAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, Friend obj, int position) {
//                dialogChallengeFriend(obj);
//            }
//        });
        setUpFirebaseAdapter(null);

    }

    private void setUpFirebaseAdapter(String searchQuery) {
        Query query;
        if(searchQuery != null){
            query = mFirebaseDatabaseReference.child("userList").orderByChild("email").startAt(searchQuery).endAt(searchQuery+"\uf8ff");
        }else{
            /**
             * Below query matches everything
             */
            query = mFirebaseDatabaseReference.child("userList").orderByChild("email");
        }
//        .startAt("Burned").endAt("Burned\uf8ff");
        firebaseListAdapter = new FirebaseListAdapter<User>(
                this,
                User.class,
                R.layout.row_friends,
                query
                ){
            @Override
            protected void populateView(View v, final User model, int position) {

                /**
                 * Remove the current user from the results
                 */
                String modelEmail = model.getEmail();
                String userEmail = user.getEmail();

                /**
                 * Doing below actually removes from firebase
                 */
//                if(modelEmail == null || userEmail == null || modelEmail.equals(userEmail)){
////                    DatabaseReference itemRef = getRef(position);
////                    itemRef.setS
//
//                    v.setVisibility(View.GONE);
//                    return;
//                }
                LinearLayout parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
                parent.setMinimumHeight(0);
                android.view.ViewGroup.LayoutParams params = parent.getLayoutParams();

//                v.setLayoutParams(v.findViewById(R.id.lyt_parent));
                params.height = 0;
                v.setLayoutParams(params);

                ImageView image = (ImageView) v.findViewById(R.id.image);
                TextView name = (TextView) v.findViewById(R.id.name);
                TextView wins = (TextView) v.findViewById(R.id.wins);
                TextView losses = (TextView) v.findViewById(R.id.losses);

                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialogChallengeFriend(model);
                    }
                });

                wins.setText("Wins : " + model.getWins());
                losses.setText("Losses: " + model.getLosses());

                Picasso.with(ActivityChooseFriend.this).load(model.getPhotoUrl())
                        .resize(100,100)
                        .transform(new CircleTransform())
                        .into(image);
                name.setText(model.getDisplayName());


            }

        };
        listView.setAdapter(firebaseListAdapter);
    }


    private void dialogChallengeFriend(final User f) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_challenge);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView opponentName, opponentWins, opponentLosses;
        ImageView image = (ImageView)dialog.findViewById(R.id.opponent_image);
                Picasso.with(this).load(f.getPhotoUrl())
                .resize(200, 200)
                .transform(new CircleTransform())
                .into(image);
        opponentName = (TextView) dialog.findViewById(R.id.opponent_name);
        opponentName.setText(f.getDisplayName());

        opponentWins = (TextView) dialog.findViewById(R.id.opponent_wins);
        opponentWins.setText("Wins : 0");
        opponentLosses = (TextView) dialog.findViewById(R.id.opponent_losses);
        opponentLosses.setText("Losses : 0");

        Button challenge = ((Button)dialog.findViewById(R.id.btn_challenge));
        challenge.setText("Challenge");


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPushNotification(f);
                Game newGame = createGame(f);
                addGame(newGame, f);
                onBackPressed();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
//    private void getUserDisplayName(){
//        final Query ref = mFirebaseDatabaseReference.child("userList").child(gameObj.getChallenger());
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//
//                if(dataSnapshot.getKey().equals("displayName")){
//                    String name = dataSnapshot.getValue().toString();
//                    PushNotification msg = new PushNotification("You've Been Challenged", name + " Challenged you", user.getUid() );
//                    mFirebaseDatabaseReference.child("userList").child(friend.getUid()).child("pushNotifications").setValue(msg);
//                }
//
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            @Override
//            public void onCancelled(DatabaseError error) {}
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//        });
//    }


    private void sendPushNotification(){
//        Need to send user display name through Push notification
//        getUserDisplayName();
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

        String challengerUID = user.getUid();
        Player challenger = new Player(challengerUID, list);
        challenger.setAcceptedChallenge(true);
        challenger.setChallenger(true);
//        gameObj = new Game(challengerUID, opponentUID);

        mFirebaseDatabaseReference.child("PlayerInfo").child(gameObj.getGameID()).child(challengerUID).setValue(challenger);
        mFirebaseDatabaseReference.child("PlayerInfo").child(gameObj.getGameID()).child(opponentUID).setValue(opponent);

    }
}
