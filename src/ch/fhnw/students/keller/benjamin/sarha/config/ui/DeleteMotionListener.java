package ch.fhnw.students.keller.benjamin.sarha.config.ui;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

abstract class DeleteMotionListener implements OnTouchListener{
	public int position;
	private GestureDetector gd;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gd.onTouchEvent(event);
		
		return true;
	}
	abstract void delete(int position);
	abstract void onClick(int position);
	
	public DeleteMotionListener(){
		gd= new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
					delete(position);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				System.out.println("onsingletapup");
				onClick(position);
				return true;
			}});
	}

}
