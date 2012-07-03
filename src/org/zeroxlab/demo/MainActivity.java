package org.zeroxlab.demo;

import org.zeroxlab.widget.AnimationLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements AnimationLayout.Listener {
    public final static String TAG = "Demo";

    protected AnimationLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mLayout = (AnimationLayout) findViewById(R.id.animation_layout);
        mLayout.setListener(this);
    }

    public void onClickContentButton(View v) {
        mLayout.toggleSidebar();
    }

    @Override
    public void onBackPressed() {
        if (mLayout.isOpening()) {
            mLayout.closeSidebar();
        } else {
            finish();
        }
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public void onSidebarOpened() {
        Log.d(TAG, "opened");
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public void onSidebarClosed() {
        Log.d(TAG, "opened");
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public boolean onContentTouchedWhenOpening() {
//        // the content area is touched when sidebar opening, close sidebar
//        Log.d(TAG, "going to close sidebar");
//        mLayout.closeSidebar();
        return true;
    }
}

