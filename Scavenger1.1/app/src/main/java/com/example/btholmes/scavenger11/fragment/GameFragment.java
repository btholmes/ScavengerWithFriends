package com.example.btholmes.scavenger11.fragment;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.activities.ActivityChooseFriend;
import com.example.btholmes.scavenger11.adapter.GameListAdapter;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.tools.Utility;
import com.example.btholmes.scavenger11.tools.gameCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GameFragment extends Fragment {

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseUser currentUser;
    private Utility utility;

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private GameListAdapter mAdapter;
    private static ArrayList<Game> gameList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utility = new Utility(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_game, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        //Collect Game data in background thread
//                        new getGames().execute();
//                    }
//                });


        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);



        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        progressbar.setVisibility(View.GONE);
        gameList.clear();
//        gameList = new ArrayList<>(Constant.getRandomGame(getContext()));
        new getGames().execute();
//        refreshGames();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_game:
                Snackbar.make(view, item.getTitle()+" Clicked", Snackbar.LENGTH_SHORT).show();
                dialogNewGame();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogNewGame() {
        final Dialog dialog = new Dialog(getActivity());
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

                ActivityChooseFriend.navigate((MainActivity) getActivity(), view);

//                Intent intent = new Intent(getActivity(), ActivityChooseFriend.class);
//////                intent.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
//                startActivity(intent);
//                Toast.makeText(getActivity(), "Challenge Friend Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public class getGames extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            refreshGames();
            return null;
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
                            Toast.makeText(getContext(), "Clicked Game", Toast.LENGTH_SHORT).show();
//                                            if (actionMode != null) {
//                                                myToggleSelection(position);
//                                                return;
//                                            }
//                            ActivitySelectedGame.navigate((MainActivity)getActivity(), v.findViewById(R.id.lyt_parent), obj);
                        }
                    });

                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
        progressbar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }


}
