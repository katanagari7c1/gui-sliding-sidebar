package org.zeroxlab.widget;

import org.zeroxlab.widget.AnimationLayout;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


public class AnimationLayoutGesturesListener extends GestureDetector.SimpleOnGestureListener{
	private ViewConfiguration viewConfiguration;
	private AnimationLayout layout;
	
	public AnimationLayoutGesturesListener(AnimationLayout animationLayout, ViewConfiguration viewConfiguration) {
		super();
		this.layout = animationLayout;
		this.viewConfiguration = viewConfiguration;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		if(!this.layout.isOpening()){
			return false;
		}
		
		View content = this.layout.getContentView();
		float x = e.getX();
		float y = e.getY();

		if(content.getLeft() < x 
			&& content.getRight() > x
			&& content.getTop() < y
			&& content.getBottom() > y) {
			this.layout.closeSidebar();
			return true;
		}

		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		int scrollSlop = this.viewConfiguration.getScaledTouchSlop();
		
		float scrollDelta = e1.getY() - e2.getY();
		
		if(scrollDelta > scrollSlop){
			this.layout.openSidebar();
		}
		else if (scrollDelta < -scrollSlop){
			this.layout.closeSidebar();
		}
		else {
			return false;
		}

		return true;
	}

}
