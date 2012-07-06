/*
 * Copyright (C) 2012 0xlab - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authored by Julian Chu <walkingice AT 0xlab.org>
 */

package org.zeroxlab.widget;

// update the package name to match your app
import org.zeroxlab.demo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationLayout extends ViewGroup {

    public final static int DURATION = 250;

    protected boolean mOpened;
    protected View mSidebar;
    protected View mContent;
    protected int mSidebarHeight = 150; /* assign default value. It will be overwrite
                                          in onMeasure by Layout xml resource. */

    protected Animation mAnimation;
    protected OpenListener    mOpenListener;
    protected CloseListener   mCloseListener;
    protected Listener mListener;
    
    protected GestureDetector gestureDetector;
    protected AnimationLayoutGesturesListener gestureListener;

    protected boolean mPressed = false;

    public AnimationLayout(Context context) {
        this(context, null);
    }

    public AnimationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gestureListener = new AnimationLayoutGesturesListener(this, ViewConfiguration.get(context));
        this.gestureDetector = new GestureDetector(getContext(), this.gestureListener);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mSidebar = findViewById(R.id.animation_layout_sidebar);
        mContent = findViewById(R.id.animation_layout_content);

        if (mSidebar == null) {
            throw new NullPointerException("no view id = animation_sidebar");
        }

        if (mContent == null) {
            throw new NullPointerException("no view id = animation_content");
        }

        mOpenListener = new OpenListener(mSidebar, mContent);
        mCloseListener = new CloseListener(mSidebar, mContent);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        /* the title bar assign top padding, drop it */
    	int contentHeight = b - t;
        mSidebar.layout(l, contentHeight - mSidebarHeight , r, b);
        if (mOpened) {
            mContent.layout(l, -mSidebarHeight, r, contentHeight - mSidebarHeight);
        } else {
            mContent.layout(l, 0 , r, contentHeight);
        }
    }

    @Override
    public void onMeasure(int w, int h) {
        super.onMeasure(w, h);
        super.measureChildren(w, h);
        mSidebarHeight = mSidebar.getMeasuredHeight();
    }

    @Override
    protected void measureChild(View child, int parentWSpec, int parentHSpec) {
        /* the max width of Sidebar is 90% of Parent */
        if (child == mSidebar) {
            int mode = MeasureSpec.getMode(parentHSpec);
            int height = (int)(getMeasuredHeight() * 0.9);
            super.measureChild(child, parentWSpec, MeasureSpec.makeMeasureSpec(height, mode));
        } else {
            super.measureChild(child, parentWSpec, parentHSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	return this.gestureDetector.onTouchEvent(ev);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    /* to see if the Sidebar is visible */
    public boolean isOpening() {
        return mOpened;
    }

    public void toggleSidebar() {
        if (mContent.getAnimation() != null) {
            return;
        }

        if (mOpened) {
            /* opened, make close animation*/
            mAnimation = new TranslateAnimation(0, 0, 0, mSidebarHeight);
            mAnimation.setAnimationListener(mCloseListener);
        } else {
            /* not opened, make open animation */
            mAnimation = new TranslateAnimation(0, 0, 0, -mSidebarHeight);
            mAnimation.setAnimationListener(mOpenListener);
        }
        mAnimation.setDuration(DURATION);
        mAnimation.setFillAfter(true);
        mAnimation.setFillEnabled(true);
        mContent.startAnimation(mAnimation);
    }

    public void openSidebar() {
        if (!mOpened) {
            toggleSidebar();
        }
    }

    public void closeSidebar() {
        if (mOpened) {
            toggleSidebar();
        }
    }

    class OpenListener implements Animation.AnimationListener {
        View iSidebar;
        View iContent;

        OpenListener(View sidebar, View content) {
            iSidebar = sidebar;
            iContent = content;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            iSidebar.setVisibility(View.VISIBLE);
        }

        public void onAnimationEnd(Animation animation) {
            iContent.clearAnimation();
            mOpened = !mOpened;
            requestLayout();
            if (mListener != null) {
                mListener.onSidebarOpened();
            }
        }
    }

    class CloseListener implements Animation.AnimationListener {
        View iSidebar;
        View iContent;

        CloseListener(View sidebar, View content) {
            iSidebar = sidebar;
            iContent = content;
        }

        public void onAnimationRepeat(Animation animation) {
        }
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            iContent.clearAnimation();
            iSidebar.setVisibility(View.INVISIBLE);
            mOpened = !mOpened;
            requestLayout();
            if (mListener != null) {
                mListener.onSidebarClosed();
            }
        }
    }
    
    public View getContentView(){
    	return this.mContent;
    }
    
    public View getSidebarView(){
    	return this.mSidebar;
    }
    
    public interface Listener {
        public void onSidebarOpened();
        public void onSidebarClosed();
        public boolean onContentTouchedWhenOpening();
    }
}
