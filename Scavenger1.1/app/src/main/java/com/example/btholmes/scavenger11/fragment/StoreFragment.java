package com.example.btholmes.scavenger11.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.FriendsListAdapter;
import com.example.btholmes.scavenger11.data.Constant;
import com.example.btholmes.scavenger11.model.Friend;
import com.example.btholmes.scavenger11.widget.DividerItemDecoration;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.Wallet;


public class StoreFragment extends Fragment  {

    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    private View view;
    private SearchView search;
    private Boolean ON_PAUSE;
    private LinearLayoutManager mLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;


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
        view = inflater.inflate(R.layout.page_fragment_store, container, false);

        ON_PAUSE = false;
        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mDividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                mLayoutManager.getOrientation()
        );
        recyclerView.addItemDecoration(mDividerItemDecoration);

        //set data and list adapter
        mAdapter = new FriendsListAdapter(getActivity(), Constant.getFriendsData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {
//                ActivityFriendDetails.navigate((MainActivity) getActivity(), v, obj);
                payForItem();
            }
        });
        return view;
    }


    public void payForItem(){
        Wallet.Payments.isReadyToPay(Constant.getGoogleApiClient(getContext())).setResultCallback(
                new ResultCallback<BooleanResult>() {
            @Override
            public void onResult(@NonNull BooleanResult booleanResult) {
//                hideProgressDialog();
                if (booleanResult.getStatus().isSuccess()) {
                    if (booleanResult.getValue()) {
                        // Show Android Pay buttons alongside regular checkout button
                        // ...
                    } else {
                        // Hide Android Pay buttons, show a message that Android Pay
                        // cannot be used yet, and display a traditional checkout button
                        // ...
                    }
                } else {
                    // Error making isReadyToPay call
                    Log.e("Pay", "isReadyToPay:" + booleanResult.getStatus());
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_store, menu);
//        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        search.setIconified(false);
//        search.setQueryHint("Search Friend...");
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                try {
//                    mAdapter.getFilter().filter(s);
//                } catch (Exception e) {}
//                return true;
//            }
//        });
//        search.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_new_friend:
//                Snackbar.make(view, item.getTitle()+" Clicked", Snackbar.LENGTH_SHORT).show();
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

}
