package com.twitter.olx.test.task;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twitter.olx.test.R;
import com.twitter.olx.test.dto.ErrorDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.util.RoboAsyncTask;

/**
 * Created by nicolas on 8/24/14.
 */
public abstract class RestSafeAsyncTask<T> extends RoboAsyncTask<T> {


    protected RestSafeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        try {
            if(e instanceof RetrofitError) {
                RetrofitError retrofitError = (RetrofitError) e;

                if(retrofitError.getResponse() != null) {
                    if (retrofitError.getResponse().getStatus() > 500 ){
                        String msg = "Network error HTTP ("+retrofitError.getResponse().getStatus()+")";
                        if (retrofitError.getMessage()!=null && !retrofitError.getMessage().isEmpty()){
                            msg += ": "+retrofitError.getMessage();
                        }
                        super.onException(e);
                    }else if (retrofitError.getBody()==null){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }else if (retrofitError.getCause() instanceof ConnectException){
                        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    }else if (retrofitError.getCause() instanceof SocketTimeoutException){
                        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    }else{
                        BufferedReader reader = new BufferedReader(new InputStreamReader(((RetrofitError) e).getResponse().getBody().in()));
                        ErrorDto errorDto = new Gson().fromJson(reader, ErrorDto.class);
                        onApiError(errorDto);
                    }
                }else if(retrofitError.isNetworkError()){
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            }else {
                super.onException(e);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onInterrupted(Exception e) {
        Log.d("BACKGROUND_TASK", "Interrupting background task " + this);
    }

    // If the activity is destroyed, this handler will make sure
    // that this background task gets canceled.
    protected void onActivityDestroy(@Observes OnDestroyEvent ignored ) {
        cancel(true);
    }

    protected abstract void onApiError(ErrorDto errorDto);
}
