package com.twitter.olx.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.twitter.olx.test.R;
import com.twitter.olx.test.adapter.LittleTweetAdapter;
import com.twitter.olx.test.client.TwitterClient;
import com.twitter.olx.test.client.session.SessionManager;
import com.twitter.olx.test.dialog.DialogFragmentHelper;
import com.twitter.olx.test.dialog.ProgressDialogFragment;
import com.twitter.olx.test.domain.LittleTweet;
import com.twitter.olx.test.dto.ErrorDto;
import com.twitter.olx.test.dto.ResponseDto;
import com.twitter.olx.test.dto.TweetDto;
import com.twitter.olx.test.task.RestSafeAsyncTask;
import com.twitter.olx.test.view.ItemRowView;

import java.util.List;

import retrofit.client.Response;
import roboguice.inject.InjectView;

/**
 * Created by nicolas on 8/24/14.
 */
public class MainFragment extends AbstractFragment<MainFragment.Callback> implements AdapterView.OnItemClickListener {



    /**
     * MainFragment callback interface
     */
    public interface Callback {
        void onOpenTwitter();
    }

    public static final String SAVED_DATA = "saved_data";

    @InjectView(R.id.fragment_main_edit_text)
    private EditText searchInput;

    @InjectView(R.id.fragment_main_button)
    private Button searchButton;

    @InjectView(R.id.fragment_main_list_view)
    private ListView listView;

    @Inject
    private ProgressDialogFragment progressDialogFragment;

    @Inject
    private TwitterClient twitterClient;

    @Inject
    private SessionManager sessionManager;

    private LittleTweetAdapter adapter;

    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();
        listView.setOnItemClickListener(this);

        if(sessionManager.getToken() == null) {
            new AunthenticateTask(getActivity()).execute();
        }
    }

    private void setListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTweets();
            }
        });

        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_SEARCH:
                            getTweets();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void createProgressDialog() {
        Bundle arguments = new Bundle();
        arguments.putString(ProgressDialogFragment.MESSAGE, getString(R.string.Authenticating));
        DialogFragmentHelper.show(getActivity(), progressDialogFragment, arguments);
    }

    private void createProgressDialog(int resId) {
        Bundle arguments = new Bundle();
        arguments.putString(ProgressDialogFragment.MESSAGE, getString(resId));
        DialogFragmentHelper.show(getActivity(), progressDialogFragment, arguments);
    }

    private void getTweets() {
        String keyword = searchInput.getText().toString();
        new GetTweetsTask(getActivity(), keyword).execute();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(adapter != null && !adapter.getTweets().isEmpty()) {
            outState.putSerializable(SAVED_DATA, adapter.getTweets());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(SAVED_DATA)) {
            List<LittleTweet> tweets = (List<LittleTweet>) savedInstanceState.getSerializable(SAVED_DATA);
            updateUi(tweets);
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }

    private void updateUi(List<LittleTweet> tweets) {
        if(!tweets.isEmpty()) {
            adapter = new LittleTweetAdapter(tweets);
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(getActivity(), R.string.no_results_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        callbacks.onOpenTwitter();
    }

    /**
     * AunthenticateTask
     */
    private class AunthenticateTask extends RestSafeAsyncTask<ResponseDto> {

        protected AunthenticateTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() throws Exception {
            super.onPreExecute();
            createProgressDialog();
        }

        @Override
        public ResponseDto call() throws Exception {
            return twitterClient.authenticate();
        }

        @Override
        protected void onSuccess(ResponseDto response) throws Exception {
            super.onSuccess(response);
            sessionManager.setSession(response.getAccessToken(), response.getTokenType());
        }

        @Override
        protected void onFinally() throws RuntimeException {
            super.onFinally();
            progressDialogFragment.dismiss();
        }

        @Override
        protected void onApiError(ErrorDto errorDto) {
            Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * GetTweetsTask
     */
    private class GetTweetsTask extends RestSafeAsyncTask<TweetDto> {
        private String keyword;

        protected GetTweetsTask(Context context, String keyword) {
            super(context);
            this.keyword = keyword;
        }

        @Override
        protected void onPreExecute() throws Exception {
            super.onPreExecute();
            createProgressDialog(R.string.getting_tweets);
        }

        @Override
        public TweetDto call() throws Exception {
            return twitterClient.getTweets(this.keyword);
        }

        @Override
        protected void onSuccess(TweetDto response) throws Exception {
            super.onSuccess(response);
            updateUi(response.getStatuses());
        }

        @Override
        protected void onFinally() throws RuntimeException {
            super.onFinally();
            closeKeyboard();
            progressDialogFragment.dismiss();
        }

        @Override
        protected void onApiError(ErrorDto errorDto) {
            Toast.makeText(getContext(), errorDto.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
