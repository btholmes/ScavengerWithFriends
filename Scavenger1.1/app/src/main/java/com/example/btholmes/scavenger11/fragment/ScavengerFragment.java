package com.example.btholmes.scavenger11.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.btholmes.scavenger11.tools.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by btholmes on 11/15/17.
 */

public class ScavengerFragment extends Fragment{

    protected DatabaseReference mFirebaseDatabaseReference;
    protected FirebaseUser user;
    protected Utility utility;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        utility = new Utility(getActivity());

    }
}
