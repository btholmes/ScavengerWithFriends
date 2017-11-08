package com.example.btholmes.scavenger11.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.adapter.MessageDetailsListAdapter;
import com.example.btholmes.scavenger11.data.Constant;
import com.example.btholmes.scavenger11.data.Tools;
import com.example.btholmes.scavenger11.model.Friend;
import com.example.btholmes.scavenger11.model.MessageDetails;

import java.util.ArrayList;
import java.util.List;


public class ActivityChatDetails extends AppCompatActivity {

    public static String KEY_FRIEND     = "FRIEND";
    public static String KEY_SNIPPET   = "SNIPPET";
    public static String KEY_FROM_MESSAGE_FRAGMENT = "FROM_MESSAGE_FRAGMENT";
    public static boolean FROM_MESSAGE_FRAGMENT;

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj, String snippet, boolean fromMessageFragment) {
        Intent intent = new Intent(activity, ActivityChatDetails.class);
        intent.putExtra(KEY_FRIEND, obj);
        intent.putExtra(KEY_SNIPPET, snippet);
        intent.putExtra(KEY_FROM_MESSAGE_FRAGMENT, fromMessageFragment);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Button btn_send;
    private EditText et_content;
    public static MessageDetailsListAdapter adapter;

    private ListView listview;
    private ActionBar actionBar;
    private Friend friend;
    private List<MessageDetails> items = new ArrayList<>();
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_FRIEND);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);
        String snippets = intent.getStringExtra(KEY_SNIPPET);
        FROM_MESSAGE_FRAGMENT = intent.getBooleanExtra(KEY_FROM_MESSAGE_FRAGMENT, false);
        initToolbar();

        iniComponen();
        if(snippets != null){
            items.add(new MessageDetails(999, "09:55", friend, snippets, false));
        }
        items.addAll(Constant.getMessageDetailsData(this, friend));

        adapter = new MessageDetailsListAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setSelectionFromTop(adapter.getCount(), 0);
        listview.requestFocus();
        registerForContextMenu(listview);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(friend.getName());
    }

    public void bindView() {
        try {
            adapter.notifyDataSetChanged();
            listview.setSelectionFromTop(adapter.getCount(), 0);
        } catch (Exception e) {

        }
    }

    public void iniComponen() {
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                items.add(items.size(), new MessageDetails( items.size(), Constant.formatTime(System.currentTimeMillis()),friend, et_content.getText().toString(), true));
                items.add(items.size(), new MessageDetails( items.size(), Constant.formatTime(System.currentTimeMillis()), friend, et_content.getText().toString(), false));
                et_content.setText("");
                bindView();
                hideKeyboard();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setEnabled(false);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
            //draft.setContent(etd.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_details, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(FROM_MESSAGE_FRAGMENT){
//            Intent intent = new Intent(ActivityChatDetails.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        finish();
    }

    /**
     * Handle click on action bar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.view_profile :
                Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.block_user:
                Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.delete_conversation:
                Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}