package org.zeroxlab.widget;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class AnimationLayoutGesturesListener extends GestureDetector.SimpleOnGestureListener{
	private AnimationLayout layout;
	
	public AnimationLayoutGesturesListener(AnimationLayout animationLayout) {
		super();
		this.layout = animationLayout;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
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

		if(e1.getY() - e2.getY() > 0){
			this.layout.openSidebar();
		}
		else if (e1.getY() - e2.getY() == 0){
			return false;
		}
		else {
			this.layout.closeSidebar();
		}

		return true;
	}

}
