package com.example.btholmes.scavenger11.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btholmes.scavenger11.R;


public class LeaderBoardFragment extends Fragment {
    View view;
    private Boolean ON_PAUSE;

    @Override
    public void onResume() {
        super.onResume();
        //THIS FORCES THE MESSAGE FRAGMENT TO BE RELOADED
//        if(ON_PAUSE){
//            ON_PAUSE = false;
//            getFragmentManager()
//                    .beginTransaction()
//                    .remove(this)
//                    .attach(this)
//                    .commit();
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
        view = inflater.inflate(R.layout.page_fragment_leader_board, container, false);
        ON_PAUSE = false;
        return view;
    }

    public void actionClick(View view){
        switch (view.getId()){
            case R.id.lyt_view_profile:
                Snackbar.make(view, "View Profile Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_group_cat :
                Snackbar.make(view, "Group - Cat Lover Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_group_hangout :
                Snackbar.make(view, "Group - Hangout Friend Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_group_collage :
                Snackbar.make(view, "Group - Collage Clicked", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.lyt_setting :
                Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_help :
                Snackbar.make(view, "Help nad FAQ Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_logout :
                Snackbar.make(view, "Logout Clicked", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

}
