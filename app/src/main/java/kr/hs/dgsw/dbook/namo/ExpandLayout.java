package kr.hs.dgsw.dbook.namo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import kr.hs.dgsw.dbook.R;

public class ExpandLayout extends LinearLayout {
	private static final int DURATION = 1000;
	
	private ExpandLayoutListener listener;
	private ExpandAnimator animator = new ExpandAnimator();
	
	private boolean animating;
	private boolean expanded;
	private boolean expandHorizontal, expandVertical;
	private int expandWidth = -1, expandHeight = -1;
	private int currentWidth, currentHeight;
	private int duration;
	private int minimumWidth = 0, minimumHeight = 0;
	
	public static interface ExpandLayoutListener {
		public void onExpanded();
		
		public void onCollapsed();
	}
	
	public ExpandLayout(Context context) {
		super(context);
		initialize(context, null);
	}
	
	public ExpandLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}
	
	public ExpandLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		expandWidth = getMeasuredWidth();
		expandHeight = getMeasuredHeight();
		
		if (animating)
			setMeasuredDimension(currentWidth, currentHeight);
		else if (expanded)
			setMeasuredDimension(expandWidth, expandHeight);
		else
			setMeasuredDimension(expandHorizontal ? minimumWidth : expandWidth, expandVertical ? minimumHeight : expandHeight);
	}
	
	public void setExpandLayoutListener(ExpandLayoutListener listener) {
		this.listener = listener;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void expand(boolean animate) {
		if (expanded || animating)
			return;
		
		if (animate) {
			animator.start();
		} else {
			expanded = true;
			requestLayout();
		}
	}
	
	public void collapse(boolean animate) {
		if (!expanded || animating)
			return;
		
		if (animate) {
			animator.start();
		} else {
			expanded = false;
			requestLayout();
		}
	}
	
	public void toggle(boolean animate) {
		if (expanded)
			collapse(animate);
		else
			expand(animate);
	}
	
	public boolean isAnimating() {
		return animating;
	}
	
	private void initialize(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandLayout);
			
			duration = a.getInteger(R.styleable.ExpandLayout_android_duration, DURATION);
			animator.setDuration(duration);
			expandHorizontal = a.getBoolean(R.styleable.ExpandLayout_expandHorizontal, false);
			expandVertical = a.getBoolean(R.styleable.ExpandLayout_expandVertical, false);
			minimumWidth = a.getDimensionPixelSize(R.styleable.ExpandLayout_minimumWidth, 0);
			minimumHeight = a.getDimensionPixelSize(R.styleable.ExpandLayout_minimumHeight, 0);
			
			a.recycle();
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		ss.expanded = expanded;
		
		return ss;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		
		expanded = ss.expanded;
	}
	
	static class SavedState extends BaseSavedState {
		boolean expanded;
		
		SavedState(Parcelable superState) {
			super(superState);
		}
		
		private SavedState(Parcel in) {
			super(in);
			expanded = in.readInt() != 0;
		}
		
		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(expanded ? 1 : 0);
		}
		
		public static final Creator<SavedState> CREATOR =
				new Creator<SavedState>() {
					public SavedState createFromParcel(Parcel in) {
						return new SavedState(in);
					}
					
					public SavedState[] newArray(int size) {
						return new SavedState[size];
					}
				};
	}
	
	private class ExpandAnimator extends ValueAnimator {
		ExpandAnimator() {
			setFloatValues(0, 1);
			addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float value = (Float) (animation.getAnimatedValue());
					if (expandHorizontal)
						if (expanded)
							currentWidth = expandWidth - Math.round(value * expandWidth);
						else
							currentWidth = Math.round(value * expandWidth);
					
					if (expandVertical)
						if (expanded)
							currentHeight = expandHeight - Math.round(value * expandHeight);
						else
							currentHeight = Math.round(value * expandHeight);
					
					requestLayout();
				}
			});
			addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					animating = true;
					currentWidth = !expandHorizontal || expanded ? expandWidth : 0;
					currentHeight = !expandVertical || expanded ? expandHeight : 0;
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					animating = false;
					expanded = !expanded;
					
					if (listener != null)
						if (expanded)
							listener.onExpanded();
						else
							listener.onCollapsed();
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {}
				
				@Override
				public void onAnimationRepeat(Animator animation) {}
			});
		}
	}
}
