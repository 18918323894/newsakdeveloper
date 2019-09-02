package com.huaxin.wangshen.sak.RunViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.huaxin.wangshen.sak.R;

/**
 * @author wangshen 2019/07/25
 */
public class SplitLayout extends ViewGroup {

	static final String TAG = "SplitLayout";
	static final boolean DEBUG = true;
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private static final float INVAID_SPLITPOSITION = Float.MIN_VALUE;
	private static final int DEFAULT_SPLIT_HANDLE_SIZE_DP = 16;//这个是控制上下拖动的 拖动时线的粗细.前提是把拖拽图片隐藏
	private static final int DEFAULT_CHILD_MIN_SIZE_DP = 16;//这个是各个方向拉伸 缩小的那边的最小值
	private static final int[] PRESSED_STATE_SET = { android.R.attr.state_pressed };
	private static final int[] EMPTY_STATE_SET = {};

	private int mOrientation;

	public float mSplitFraction;
	public float mSplitPosition = INVAID_SPLITPOSITION;

	public Drawable mHandleDrawable;
	private int mHandleSize;
	private boolean mHandleHapticFeedback;

	private View mChild0, mChild1;
	private float mLastMotionX, mLastMotionY;
	private int mChildMinSize;
//	private int mWidth, mHeight;   //原来
	public int mWidth, mHeight; //改动

	private boolean mIsDragging = false;

	public SplitLayout(Context context) {
		this(context, null, 0);
	}

	public SplitLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SplitLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SplitLayout, defStyleAttr, 0);
		mOrientation = a.getInteger(R.styleable.SplitLayout_splitOrientation, HORIZONTAL);
		mChildMinSize = a.getDimensionPixelSize(R.styleable.SplitLayout_splitChildMinSize,
				dp2px(DEFAULT_CHILD_MIN_SIZE_DP));

		//defValue是控制左右比例，左边的占比
		mSplitFraction = a.getFloat(R.styleable.SplitLayout_splitFraction, 0.5f);
		checkSplitFraction();
		mHandleDrawable = a.getDrawable(R.styleable.SplitLayout_splitHandleDrawable);
		if (mHandleDrawable == null) {
			StateListDrawable stateListDrawable = new StateListDrawable();
			stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(0x990288d1));
			stateListDrawable.addState(new int[] {}, new ColorDrawable(Color.TRANSPARENT));
			stateListDrawable.setEnterFadeDuration(150);
			stateListDrawable.setExitFadeDuration(150);
			mHandleDrawable = stateListDrawable;
		}
		mHandleDrawable.setCallback(this);
		mHandleSize = Math.round(a.getDimension(R.styleable.SplitLayout_splitHandleSize, 0f));
		if (mHandleSize <= 0) {
			mHandleSize = mOrientation == HORIZONTAL ? mHandleDrawable.getIntrinsicWidth() : mHandleDrawable
					.getIntrinsicHeight();
		}
		if (mHandleSize <= 0) {
			mHandleSize = dp2px(DEFAULT_SPLIT_HANDLE_SIZE_DP);
		}
		mHandleHapticFeedback = a.getBoolean(R.styleable.SplitLayout_splitHandleHapticFeedback, false);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		checkChildren();
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSize > 0 && heightSize > 0) {
			mWidth = widthSize;
//			mHeight = heightSize;  //修改   获取到的heightSize值有问题  特别小  应该是系统bug 所以外部自己重新赋值
//			setMeasuredDimension(widthSize, heightSize);//原来
            setMeasuredDimension(mWidth, mHeight);//修改

            checkSplitPosition();
			final int splitPosition = Math.round(mSplitPosition);
			if (mOrientation == VERTICAL) {
				mChild0.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(splitPosition - mHandleSize / 2, MeasureSpec.EXACTLY));
				mChild1.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize - splitPosition - mHandleSize / 2, MeasureSpec.EXACTLY));
			} else {
				mChild0.measure(MeasureSpec.makeMeasureSpec(splitPosition - mHandleSize / 2, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
				mChild1.measure(
						MeasureSpec.makeMeasureSpec(widthSize - splitPosition - mHandleSize / 2, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
			}
		} else {
			throw new IllegalStateException("SplitLayout with or height must not be MeasureSpec.UNSPECIFIED");
		}
	}

	//初始化会调用，拖拽布局之后也会调用
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int w = r - l;
		int h = b - t;
		final int splitPosition = Math.round(mSplitPosition);
		if (mOrientation == VERTICAL) {
			mChild0.layout(0, 0, w, splitPosition - mHandleSize / 2);
			mChild1.layout(0, splitPosition + mHandleSize / 2, w, h);
		} else {
			mChild0.layout(0, 0, splitPosition - mHandleSize / 2, h);
			mChild1.layout(splitPosition + mHandleSize / 2, 0, w, h);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		float x = ev.getX();
		float y = ev.getY();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			if (isUnderSplitHandle(x, y)) {
				if (mHandleHapticFeedback) {
					performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				}
				if (mHandleDrawable!=null){
                    mHandleDrawable.setState(PRESSED_STATE_SET);//新增
                }
//				mHandleDrawable.setState(PRESSED_STATE_SET);
				mIsDragging = true;
				getParent().requestDisallowInterceptTouchEvent(true);
				invalidate();
			} else {
				mIsDragging = false;
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		}
		case MotionEvent.ACTION_MOVE:
			if (mIsDragging) {
				getParent().requestDisallowInterceptTouchEvent(true);
				if (mOrientation == VERTICAL) {
					float deltaY = y - mLastMotionY;
					updateSplitPositionWithDelta(deltaY);
				} else {
					float deltaX = x - mLastMotionX;
					updateSplitPositionWithDelta(deltaX);
				}
				mLastMotionX = x;
				mLastMotionY = y;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mIsDragging) {
			    if (mHandleDrawable != null){
                    mHandleDrawable.setState(EMPTY_STATE_SET);  //if判断是新增
                }
//				mHandleDrawable.setState(EMPTY_STATE_SET);
				if (mOrientation == VERTICAL) {
					float deltaY = y - mLastMotionY;
					updateSplitPositionWithDelta(deltaY);
				} else {
					float deltaX = x - mLastMotionX;
					updateSplitPositionWithDelta(deltaX);
				}
				mLastMotionX = x;
				mLastMotionY = y;
				mIsDragging = false;
			}

			break;
		}
		return mIsDragging;
	}

	private boolean isUnderSplitHandle(float x, float y) {
		if (mOrientation == VERTICAL) {
			return y >= (mSplitPosition - mHandleSize / 2) && y <= (mSplitPosition + mHandleSize / 2);
		} else {
			return x >= (mSplitPosition - mHandleSize / 2) && x <= (mSplitPosition + mHandleSize / 2);
		}

	}

	private void updateSplitPositionWithDelta(float delta) {
		mSplitPosition = mSplitPosition + delta;
		checkSplitPosition();
		requestLayout();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mSplitPosition != INVAID_SPLITPOSITION && mHandleDrawable != null) {
			final int splitPosition = Math.round(mSplitPosition);
			if (mOrientation == VERTICAL) {
				mHandleDrawable.setBounds(0, splitPosition - mHandleSize / 2, mWidth, splitPosition + mHandleSize / 2);
			} else {
				mHandleDrawable.setBounds(splitPosition - mHandleSize / 2, 0, splitPosition + mHandleSize / 2, mHeight);
			}
			mHandleDrawable.draw(canvas);
		}
	}

	private void checkSplitFraction() {
		if (mSplitFraction < 0) {
			mSplitFraction = 0;
		} else if (mSplitFraction > 1) {
			mSplitFraction = 1;
		}
	}

	private void checkSplitPosition() {
		if (mOrientation == VERTICAL) {
			if (mSplitPosition == INVAID_SPLITPOSITION) {
				mSplitPosition = mHeight * mSplitFraction ;

			}
			final int min = mChildMinSize + mHandleSize / 2;
			if (mSplitPosition < min) {
				mSplitPosition = min;
			} else {
				final int max = mHeight - mChildMinSize - mHandleSize / 2;
				if (mSplitPosition > max) {
					mSplitPosition = max;
				}
			}
		} else {
			if (mSplitPosition == INVAID_SPLITPOSITION) {
				mSplitPosition = mWidth * mSplitFraction;
			}
			final int min = mChildMinSize + mHandleSize / 2;
			if (mSplitPosition < min) {
				mSplitPosition = min;
			} else {
				final int max = mWidth - mChildMinSize - mHandleSize / 2;
				if (mSplitPosition > max) {
					mSplitPosition = max;
				}
			}
		}
	}

	private void checkChildren() {
		if (getChildCount() == 2) {
			mChild0 = getChildAt(0);
			mChild1 = getChildAt(1);
		} else {
			throw new IllegalStateException("SplitLayout ChildCount must be 2.");
		}
	}

	@Override
	public void jumpDrawablesToCurrentState() {
		super.jumpDrawablesToCurrentState();
		if (mHandleDrawable != null) {
			mHandleDrawable.jumpToCurrentState();
		}

	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		updateSplitPositionWithDelta(0);
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || who == mHandleDrawable;
	}

	private int dp2px(float dp) {
		return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
	}

}
