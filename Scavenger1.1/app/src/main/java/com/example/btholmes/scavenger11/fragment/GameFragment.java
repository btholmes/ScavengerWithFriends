package com.example.btholmes.scavenger11.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.GameListAdapter;
import com.example.btholmes.scavenger11.data.Constant;
import com.example.btholmes.scavenger11.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private GameListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_game, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);
        if(!taskRunning){
            new DummyGameLoader().execute("");
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_feed:
                Snackbar.make(view, item.getTitle()+" Clicked", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean taskRunning = false;
    private class DummyGameLoader extends AsyncTask<String, String, String> {
        private String status="";
        private List<Game> items = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            taskRunning = true;
            items.clear();
            progressbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(500);
                items = Constant.getRandomGame(getActivity());
                status = "success";
            }catch (Exception e){
                status = "failed";
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if(status.equals("success")) {
                //set data and list adapter
                mAdapter = new GameListAdapter(getActivity(), items);
                recyclerView.setAdapter(mAdapter);
            }
            taskRunning = false;
            super.onProgressUpdate(values);
        }
    }

}
