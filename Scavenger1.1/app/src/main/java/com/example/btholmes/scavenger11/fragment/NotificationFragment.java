package com.example.btholmes.scavenger11.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.NotifListAdapter;
import com.example.btholmes.scavenger11.data.Constant;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private NotifListAdapter mAdapter;
    private Boolean ON_PAUSE;

    @Override
    public void onResume() {
        super.onResume();

        //        if(item.getName().equals(MessageFragment.class.getSimpleName())){
//            Log.e("wow", "wow");
//        }
        //THIS FORCES THE MESSAGE FRAGMENT TO BE RELOADED
//        if(ON_PAUSE){
//            ON_PAUSE = false;
//            getFragmentManager()
//                    .beginTransaction()
//                    .remove(this)
//                    .attach(this)
//                    .commit();
//////            ON_DETACH = false;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_notif, container, false);

        ON_PAUSE = false;
        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new NotifListAdapter(getActivity(), Constant.getNotifData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notif, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
