package com.example.btholmes.scavenger11.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.activities.ActivityChatDetails;
import com.example.btholmes.scavenger11.adapter.MessageListAdapter;
import com.example.btholmes.scavenger11.data.Constant;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.Message;


public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageListAdapter mAdapter;
    private View view;
    private SearchView search;
    private ViewGroup container;
    private LayoutInflater inflater;
    private Boolean ON_DETACH;
    private Boolean ON_PAUSE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_message, container, false);
        ON_PAUSE = false;
        this.container = container;
        this.inflater = inflater;
        // activate fragment menu
        setHasOptionsMenu(true);
        initComponents();
        return view;
    }

    public void initComponents(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new MessageListAdapter(getActivity(), Constant.getMessageData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Message obj, int position) {
//                getFragmentManager()
//                .beginTransaction()
//                        .attach(new MessageFragment())
//                        .commit();

                ActivityChatDetails.navigate((MainActivity)getActivity(), v.findViewById(R.id.lyt_parent), obj.getFriend(), obj.getSnippet(), true);
            }
        });
    }


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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_message, menu);search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setQueryHint("Search Message...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {

                }
                return true;
            }
        });
        search.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_new_message:
////                Intent i = new Intent(getActivity(), ActivitySelectFriend.class);
////                startActivity(i);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

}
