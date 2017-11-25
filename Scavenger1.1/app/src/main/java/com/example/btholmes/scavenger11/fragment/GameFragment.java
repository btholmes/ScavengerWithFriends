package com.example.btholmes.scavenger11.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.GameListAdapter;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.tools.gameCallback;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GameFragment extends ScavengerFragment {



    private View view;
    private ViewGroup container;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressbar;
//    private RecyclerView recyclerView;
    private ListView listView;
    private FirebaseListAdapter firebaseListAdapter;
    private GameListAdapter mAdapter;
    private static ArrayList<Game> gameList = new ArrayList<>();
    private Boolean ON_PAUSE;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_game, container, false);
        this.container = container;
        // activate fragment menu
        ON_PAUSE = false;
        setHasOptionsMenu(true);
        initComponents();
        return view;
    }


    public void initComponents(){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Collect Game data in background thread
//                        new getGames().execute();
                        setFirebaseAdapter();
                    }
                });


        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        gameList.clear();
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
//        recyclerView.setHasFixedSize(true);
        listView = (ListView) view.findViewById(R.id.listView);
        setFirebaseAdapter();
    }

    private void setFirebaseAdapter() {
        progressbar.setVisibility(View.VISIBLE);

        DatabaseReference ref = mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("games");
        firebaseListAdapter = new FirebaseListAdapter<Game>(
                getActivity(),
                Game.class,
                R.layout.item_list_game,
                ref
        ){
            @Override
            protected void populateView(View v, Game model, int position) {

                ImageView backgroundImage = (ImageView) v.findViewById(R.id.image);
                ImageView icon = (ImageView) v.findViewById(R.id.icon);
                TextView name = (TextView) v.findViewById(R.id.name);
                TextView wordsLeft = (TextView) v.findViewById(R.id.wordsLeft);
                TextView losses = (TextView) v.findViewById(R.id.losses);

                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        Go to game page
                        Toast.makeText(getActivity(), "This action is not set up yet", Toast.LENGTH_SHORT).show();

                    }
                });

//                Picasso.with(getActivity()).load(sdf)
//                        .resize(100,100)
//                        .transform(new CircleTransform())
//                        .into(backgroundImage);
                name.setText(model.getChallengerWordsLeft().toString());


            }

        };
        progressbar.setVisibility(View.GONE);
        listView.setAdapter(firebaseListAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();

//        gameList = new ArrayList<>(Constant.getRandomGame(getContext()));
//        new getGames().execute();
//        if(ON_PAUSE){
//            ON_PAUSE = false;
//            getFragmentManager()
//                    .beginTransaction()
//                    .remove(this)
//                    .attach(this)
//                    .commit();
//        }else{
//            refreshGames();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ON_PAUSE = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        ON_PAUSE = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_new_game:
//                Snackbar.make(view, item.getTitle()+" Clicked", Snackbar.LENGTH_SHORT).show();
//                dialogNewGame();
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

//    private void dialogNewGame() {
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.dialog_new_game);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
////        ImageView image = (ImageView)dialog.findViewById(R.id.image);
//        //        Picasso.with(getActivity()).load(friend.getPhoto())
////                .resize(200, 200)
////                .transform(new CircleTransform())
////                .into(image);
//
//        Button challengeFriend = ((Button)dialog.findViewById(R.id.btn_challenge_friend));
//        Button challengeRandom = ((Button)dialog.findViewById(R.id.btn_challenge_random));
//
//
//        challengeFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.hide();
//
//                ActivityChooseFriend.navigate((MainActivity) getActivity(), view, "Challenge Friend");
//
////                Intent intent = new Intent(getActivity(), ActivityChooseFriend.class);
////////                intent.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
////                startActivity(intent);
////                Toast.makeText(getActivity(), "Challenge Friend Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);
//    }


    public class getGames extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            refreshGames();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void refreshGames(){

        utility.populateGames(new gameCallback() {
            @Override
            public void onSuccess(ArrayList<Game> gameList) {

                mAdapter = new GameListAdapter(getApplicationContext(), gameList);

                    mAdapter.setOnItemClickListener(new GameListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, Game obj, int position) {
                            /**
                             * Just a note, Toast cannot be executed in background
                             */
//                            Toast.makeText(getContext(), "Clicked Game", Toast.LENGTH_SHORT).show();
//                                            if (actionMode != null) {
//                                                myToggleSelection(position);
//                                                return;
//                                            }
//                            ActivitySelectedGame.navigate((MainActivity)getActivity(), v.findViewById(R.id.lyt_parent), obj);
                        }
                    });

//                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
