package com.nurturey.limited.GeneralControllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Base activity created to be extended by every activity in this application. This class provides
 * dependency injection using ButterKnife Android library configuration and some methods
 * common to every activity.
 *
 * Created by lavakush.v on 07-07-2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        injectViews();
    }

    protected abstract int getActivityLayout();

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     */
    private void injectViews() {
        ButterKnife.inject(this);
    }
}
