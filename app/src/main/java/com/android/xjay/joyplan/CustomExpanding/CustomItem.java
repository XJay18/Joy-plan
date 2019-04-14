package com.android.xjay.joyplan.CustomExpanding;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.xjay.joyplan.R;

public class CustomItem extends RelativeLayout {

    private int index;
    /**
     * Constant defining default animation duration in milliseconds.
     */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /**
     * Member variable to hold the Item Layout. Set by item_layout in ExpandingItem layout.
     */
    private ViewGroup mItemLayout;

    /**
     * The layout inflater.
     */
    private LayoutInflater mInflater;

    /**
     * Member variable to hold the base layout. Should not be changed.
     */
    private RelativeLayout mBaseLayout;

    /**
     * Member variable to hold custom_item. Should not be changed.
     */
    private LinearLayout mBaseListLayout;

    /**
     * Member variable to hold custom_sub_item items. Should not be changed.
     */
    private LinearLayout mBaseSubListLayout;

    /**
     * Member variable to hold the indicator icon.
     * Can be set by {@link #setIndicatorIconRes(int)}} or by {@link #setIndicatorIcon(Drawable)}.
     */
    private ImageView mIndicatorImage;

    /**
     * Member variable to hold the expandable part of indicator. Should not be changed.
     */
    private View mIndicatorBackground;

    /**
     * Stub to hold separator;
     */
    private ViewStub mSeparatorStub;

    /**
     * Member variable to hold the indicator container. Should not be changed.
     */
    private ViewGroup mIndicatorContainer;

    /**
     * Member variable to hold the measured custom_item height.
     */
    private int mItemHeight;

    /**
     * Member variable to hold the measured custom_sub_item custom_item height.
     */
    private int mSubItemHeight;

    /**
     * Member variable to hold the measured custom_sub_item custom_item width.
     */
    private int mSubItemWidth;

    /**
     * Member variable to hold the measured total height of custom_sub_item items.
     */
    private int mCurrentSubItemsHeight;

    /**
     * Member variable to hold the custom_sub_item items count.
     */
    private int mSubItemCount;

    /**
     * Member variable to hold the indicator size. Set by indicator_height in ExpandingItem layout.
     */
    private int mIndicatorHeight;

    /**
     * Member variable to hold the indicator size. Set by indicator_width in ExpandingItem layout.
     */
    private int mIndicatorWidth;

    /**
     * Member variable to hold the animation duration.
     * Set by animation_duration in ExpandingItem layout in milliseconds.
     * Default is 300ms.
     */
    private int mAnimationDuration;

    /**
     * Member variable to hold the indicator margin at left. Set by indicator_margin_left in ExpandingItem layout.
     */
    private int mIndicatorMarginLeft;

    /**
     * Member variable to hold the indicator margin at right. Set by indicator_margin_right in ExpandingItem layout.
     */
    private int mIndicatorMarginRight;

    /**
     * Member variable to hold the indicator margin at top. Set by indicator_margin_left in ExpandingItem layout.
     */
    private int mIndicatorMarginTop;

    /**
     * Member variable to hold the indicator margin at bottom. Set by indicator_margin_left in ExpandingItem layout.
     */
    private int mIndicatorMarginBottom;

    /**
     * Member variable to hold the boolean value that defines if the indicator should be shown.
     * Set by show_indicator in ExpandingItem layout.
     */
    private boolean mShowIndicator;

    /**
     * Member variable to hold the boolean value that defines if the animation should be shown.
     * Set by show_animation in ExpandingItem layout. Default is true.
     */
    private boolean mShowAnimation;

    /**
     * Member variable to hold the boolean value that defines if the custom_sub_item list will start collapsed or not.
     * Set by start_collapsed in ExpandingItem layout. Default is true.
     */
    private boolean mStartCollapsed;

    /**
     * Member variable to hold the state of custom_sub_item items. true if shown. false otherwise.
     */
    private boolean mSubItemsShown;

    /**
     * Member variable to hold the layout resource of custom_sub_item items. Set by sub_item_layout in ExpandingItem layout.
     */
    private int mSubItemLayoutId;

    /**
     * Member variable to hold the layout resource of items. Set by item_layout in ExpandingItem layout.
     */
    private int mItemLayoutId;

    /**
     * Member variable to hold the layout resource of separator. Set by separator_layout in ExpandingItem layout.
     */
    private int mSeparatorLayoutId;

    /**
     * Holds a reference to the parent. Used to calculate positioning.
     */
    private ExpandingList mParent;

    /**
     * Member variable to hold the listener of custom_item state change.
     */
    private OnItemStateChanged mListener;


    /**
     * Set the parent in order to auto scroll.
     * @param parent The parent of type {@link ExpandingList}
     */
    protected void setParent(ExpandingList parent) {
        mParent = parent;
    }

    /**
     * Constructor.
     * @param context
     * @param attributeSet
     */
    public CustomItem(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        readAttributes(context,attributeSet);
        setupStateVariables();
        inflateLayouts(context);

        setupIndicator();
        addItem(mItemLayout);
        addView(mBaseLayout);

    }


    /**
     * Method to add the inflated custom_item and set click listener.
     * Also measures the custom_item height.
     * @param item The inflated custom_item layout.
     */
    private void addItem(final ViewGroup item) {
        if (item != null) {
            mBaseListLayout.addView(item);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleExpanded();
                }
            });
            item.post(new Runnable() {
                @Override
                public void run() {
                    mItemHeight = item.getMeasuredHeight();
                }
            });
        }
    }

    private void setupIndicator() {
        if (mIndicatorHeight != 0&&mIndicatorWidth!=0) {
            setIndicatorBackgroundSize();
        }

        mIndicatorContainer.setVisibility(mShowIndicator &&
                mIndicatorHeight != 0 ? VISIBLE : GONE);
    }

    public void toggleExpanded() {
        if (mSubItemCount == 0) {
            return;
        }

        if (!mSubItemsShown) {
            adjustItemPosIfHidden();
        }

        toggleSubItems();
        expandSubItemsWithAnimation(0f);
        //expandIconIndicator(0f);
        animateSubItemsIn();
        int indexExpanded=mParent.getIndexExpanded();
        if(index==indexExpanded){
            mParent.setIndexExpanded(-1);
        }
        else if(indexExpanded==-1)
        {
            mParent.setIndexExpanded(index);
        }
        else if(indexExpanded!=-1)
            {
                mParent.unExpandItem();
                mParent.setIndexExpanded(index);
            }
    }

    /**
     * Method to set index
     */
    public void setIndex(int i){
         index=i;
    }

    /**
     * Tells if the custom_item is expanded.
     * @return true if expanded. false otherwise.
     */
    public boolean isExpanded() {
        return mSubItemsShown;
    }


    /**
     * Returns the count of custom_sub_item items.
     * @return The count of custom_sub_item items.
     */
    public int getSubItemsCount() {
        return mSubItemCount;
    }

    /**
     * Collapses the custom_sub_item items.
     */
    public void collapse() {
        mSubItemsShown = false;
        mBaseSubListLayout.post(new Runnable() {
            @Override
            public void run() {
                CustomViewUtils.setViewHeight(mBaseSubListLayout, 0);
            }
        });
    }

    /**
     * Get a custom_sub_item custom_item at the given position.
     * @param position The custom_sub_item custom_item position. Should be > 0.
     * @return The custom_sub_item custom_item inflated view at the given position.
     */
    public View getSubItemView(int position) {
        if (mBaseSubListLayout.getChildAt(position) != null) {
            return mBaseSubListLayout.getChildAt(position);
        }
        throw new RuntimeException("There is no custom_sub_item custom_item for position " + position +
                ". There are only " + mBaseSubListLayout.getChildCount() + " in the list.");
    }

    /**
     * Remove custom_sub_item custom_item at the given position.
     * @param position The position of the custom_item to be removed.
     */
    public void removeSubItemAt(int position) {
        removeSubItemFromList(mBaseSubListLayout.getChildAt(position));
    }

    /**
     * Remove the given view representing the custom_sub_item custom_item. Should be an existing custom_sub_item custom_item.
     * @param view The custom_sub_item custom_item to be removed.
     */
    public void removeSubItemFromList(View view) {
        if (view != null) {
            mBaseSubListLayout.removeView(view);
            mSubItemCount--;
            expandSubItemsWithAnimation(mSubItemHeight * (mSubItemCount + 1));
            if (mSubItemCount == 0) {
                mCurrentSubItemsHeight = 0;
                mSubItemsShown = false;
            }
            expandIconIndicator(mCurrentSubItemsHeight);
        }
    }

    /**
     * Remove the given view representing the custom_sub_item custom_item, with animation. Should be an existing custom_sub_item custom_item.
     * @param view The custom_sub_item custom_item to be removed.
     */
    public void removeSubItem(View view) {
        animateSubItemAppearance(view, false);
    }

    /**
     * Remove all custom_sub_item items.
     */
    public void removeAllSubItems() {
        mBaseSubListLayout.removeAllViews();
    }


    /**
     * Set a listener to listen custom_item stage changed.
     * @param listener The listener of type {@link OnItemStateChanged}
     */
    public void setStateChangedListener(OnItemStateChanged listener) {
        mListener = listener;
    }

    /**
     * Set the indicator color by resource.
     * @param colorRes The color resource.
     */
    public void setIndicatorColorRes(int colorRes) {
        setIndicatorColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * Set the indicator color by color value.
     * @param color The color value.
     */
    public void setIndicatorColor(int color) {
        ((GradientDrawable) findViewById(R.id.icon_indicator_top).getBackground().mutate()).setColor(color);
        ((GradientDrawable) findViewById(R.id.icon_indicator_bottom).getBackground().mutate()).setColor(color);
        findViewById(R.id.icon_indicator_middle).setBackgroundColor(color);
    }

    /**
     * Set the indicator icon by resource.
     * @param iconRes The icon resource.
     */
    public void setIndicatorIconRes(int iconRes) {
        setIndicatorIcon(ContextCompat.getDrawable(getContext(), iconRes));
    }

    /**
     * Set the indicator icon.
     * @param icon Drawable of the indicator icon.
     */
    public void setIndicatorIcon(Drawable icon) {
        mIndicatorImage.setImageDrawable(icon);
    }


    /**
     * Creates a custom_sub_item custom_item based on sub_item_layout Layout, set as ExpandingItem layout attribute.
     * @return The inflated custom_sub_item custom_item view.
     */


    public View createSubItem() {
        return createSubItem(-1);
    }



    /**
     * Creates a custom_sub_item custom_item based on sub_item_layout Layout, set as ExpandingItem layout attribute.
     * @param position The position to add the new Item. Position should not be greater than the list size.
     *                 If position is -1, the custom_item will be added in the end of the list.
     * @return The inflated custom_sub_item custom_item view.
     */


    public View createSubItem(int position) {
        if (mSubItemLayoutId == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }

        if (position > mBaseSubListLayout.getChildCount()) {
            throw new IllegalArgumentException("Cannot add an custom_item at position " + position +
                    ". List size is " + mBaseSubListLayout.getChildCount());
        }

        ViewGroup subItemLayout = (ViewGroup) mInflater.inflate(mSubItemLayoutId, mBaseSubListLayout, false);
        if (position == -1) {
            mBaseSubListLayout.addView(subItemLayout);
        } else {
            mBaseSubListLayout.addView(subItemLayout, position);
        }
        mSubItemCount++;
        setSubItemDimensions(subItemLayout);

        //Animate custom_sub_item view in
        if (mSubItemsShown) {
            CustomViewUtils.setViewHeight(subItemLayout, 0);
            expandSubItemsWithAnimation(mSubItemHeight * (mSubItemCount));
            expandIconIndicator(mCurrentSubItemsHeight);
            animateSubItemAppearance(subItemLayout, true);
            adjustItemPosIfHidden();
        }

        return subItemLayout;
    }


    /**
     * Creates as many custom_sub_item items as requested in {@param count}.
     * @param count The quantity of custom_sub_item items.
     */
    public void createSubItems(int count) {
        if (mSubItemLayoutId == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }
        for (int i = 0; i < count; i++) {
            createSubItem();
        }
        if (mStartCollapsed) {
            collapse();
        } else {
            mSubItemsShown = true;
            mBaseSubListLayout.post(new Runnable() {
                @Override
                public void run() {
                    expandIconIndicator(0f);
                }
            });
        }
    }

    /**
     * Show indicator animation.
     * @param startingPos The position from where the animation should start. Useful when removing custom_sub_item items.
     */
    private void expandIconIndicator(float startingPos) {
        if (mIndicatorBackground != null) {
            final int totalHeight = (mSubItemHeight * mSubItemCount) - mIndicatorHeight / 2 + mItemHeight / 2;
            mCurrentSubItemsHeight = totalHeight;
            ValueAnimator animation = mSubItemsShown ?
                    ValueAnimator.ofFloat(startingPos, totalHeight) :
                    ValueAnimator.ofFloat(totalHeight, startingPos);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(mAnimationDuration);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    CustomViewUtils.setViewHeight(mIndicatorBackground, (int) val);
                }
            });

            animation.start();
        }
    }

    /**
     * Show custom_sub_item items animation.
     */
    private void animateSubItemsIn() {
        for (int i = 0; i < mSubItemCount; i++) {
            animateSubViews((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
            animateViewAlpha((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
        }
    }

    /**
     * Remove the given custom_sub_item custom_item after animation ends.
     * @param subItem The view representing the custom_sub_item custom_item to be removed.
     * @param isAdding true if adding a view. false otherwise.
     */
    private void animateSubItemAppearance(final View subItem, boolean isAdding) {
        ValueAnimator alphaAnimation = isAdding ?
                ValueAnimator.ofFloat(0f, 1f) : ValueAnimator.ofFloat(1f, 0f);
        alphaAnimation.setDuration(isAdding ? mAnimationDuration * 2 : mAnimationDuration / 2);

        ValueAnimator heightAnimation = isAdding ?
                ValueAnimator.ofFloat(0f, mSubItemHeight) : ValueAnimator.ofFloat(mSubItemHeight, 0f);
        heightAnimation.setDuration(mAnimationDuration / 2);
        heightAnimation.setStartDelay(mAnimationDuration / 2);

        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                subItem.setAlpha(val);
            }
        });

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                CustomViewUtils.setViewHeight(subItem, (int) val);
            }
        });

        alphaAnimation.start();
        heightAnimation.start();

        if (!isAdding) {
            heightAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeSubItemFromList(subItem);
                }
            });
        }
    }



    /**
     * Measure custom_sub_item items dimension.
     * @param v The custom_sub_item custom_item to measure.
     */
    private void setSubItemDimensions(final ViewGroup v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                if (mSubItemHeight <= 0) {
                    mSubItemHeight = v.getMeasuredHeight();
                    mSubItemWidth = v.getMeasuredWidth();
                }
            }
        });
    }


    /**
     * Show custom_sub_item items translation animation.
     * @param viewGroup The custom_sub_item custom_item to animate
     * @param index The custom_sub_item custom_item index. Needed to calculate delays.
     */
    private void animateSubViews(final ViewGroup viewGroup, int index) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.setLayerType(ViewGroup.LAYER_TYPE_HARDWARE, null);
        ValueAnimator animation = mSubItemsShown ?
                ValueAnimator.ofFloat(0f, 1f) :
                ValueAnimator.ofFloat(1f, 0f);
        animation.setDuration(mAnimationDuration);
        int delay = index * mAnimationDuration / mSubItemCount;
        int invertedDelay = (mSubItemCount - index) * mAnimationDuration / mSubItemCount;
        animation.setStartDelay(mSubItemsShown ? delay / 2 : invertedDelay / 2);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                viewGroup.setX((mSubItemWidth / 2 * val) - mSubItemWidth / 2);
            }
        });

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.setLayerType(ViewGroup.LAYER_TYPE_NONE, null);
            }
        });

        animation.start();
    }

    /**
     * Show custom_sub_item items alpha animation.
     * @param viewGroup The custom_sub_item custom_item to animate
     * @param index The custom_sub_item custom_item index. Needed to calculate delays.
     */
    private void animateViewAlpha(final ViewGroup viewGroup, int index) {
        if (viewGroup == null) {
            return;
        }
        ValueAnimator animation = mSubItemsShown ?
                ValueAnimator.ofFloat(0f, 1f) :
                ValueAnimator.ofFloat(1f, 0f);
        animation.setDuration(mSubItemsShown ? mAnimationDuration * 2 : mAnimationDuration);
        int delay = index * mAnimationDuration / mSubItemCount;
        animation.setStartDelay(mSubItemsShown ? delay / 2 : 0);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                viewGroup.setAlpha(val);
            }
        });

        animation.start();
    }

    /**
     * Toggle custom_sub_item items collapsed/expanded
     */
    private void toggleSubItems() {
        mSubItemsShown = !mSubItemsShown;
        if (mListener != null) {
            mListener.itemCollapseStateChanged(mSubItemsShown);
        }
    }


    /**
     * Expand the custom_sub_item items container with animation
     * @param startingPos The position from where the animation should start. Useful when removing custom_sub_item items.
     */
    private void expandSubItemsWithAnimation(float startingPos) {
        if (mBaseSubListLayout != null) {
            final int totalHeight = (mSubItemHeight * mSubItemCount);
            ValueAnimator animation = mSubItemsShown ?
                    ValueAnimator.ofFloat(startingPos, totalHeight) :
                    ValueAnimator.ofFloat(totalHeight, startingPos);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(mAnimationDuration);

            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    CustomViewUtils.setViewHeight(mBaseSubListLayout, (int) val);
                }
            });

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mSubItemsShown) {
                        adjustItemPosIfHidden();
                    }
                }
            });

            animation.start();
        }
    }

    /**
     * Method to adjust Item position in parent if its custom_sub_item items are outside screen.
     */
    private void adjustItemPosIfHidden() {
        int parentHeight = mParent.getMeasuredHeight();
        int[] parentPos = new int[2];
        mParent.getLocationOnScreen(parentPos);
        int parentY = parentPos[1];
        int[] itemPos = new int[2];
        mBaseLayout.getLocationOnScreen(itemPos);
        int itemY = itemPos[1];


        int endPosition = itemY + mItemHeight + (mSubItemHeight * mSubItemCount);
        int parentEnd = parentY + parentHeight;
        if (endPosition > parentEnd) {
            int delta = endPosition - parentEnd;
            int itemDeltaToTop = itemY - parentY;
            if (delta > itemDeltaToTop) {
                delta = itemDeltaToTop;
            }
            mParent.scrollUpByDelta(delta);
        }
    }

    /**
     * Method to inflate all layouts.
     * @param context The custom View Context.
     */
    private void inflateLayouts(Context context) {
        mInflater = LayoutInflater.from(context);
        mBaseLayout = (RelativeLayout) mInflater.inflate(R.layout.expand_item,
                null, false);
        mBaseListLayout = mBaseLayout.findViewById(R.id.base_list_layout);
        mBaseSubListLayout = mBaseLayout.findViewById(R.id.base_sub_list_layout);
        mIndicatorImage = mBaseLayout.findViewById(R.id.indicator_image);
        mBaseLayout.findViewById(R.id.icon_indicator_top).bringToFront();
        mSeparatorStub = mBaseLayout.findViewById(R.id.base_separator_stub);
        mIndicatorBackground = mBaseLayout.findViewById(R.id.icon_indicator_middle);
        mIndicatorContainer = mBaseLayout.findViewById(R.id.indicator_container);
        mIndicatorContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpanded();
            }
        });

        if (mItemLayoutId != 0) {
            mItemLayout = (ViewGroup) mInflater.inflate(mItemLayoutId, mBaseLayout, false);
        }
        if (mSeparatorLayoutId != 0) {
            mSeparatorStub.setLayoutResource(mSeparatorLayoutId);
            mSeparatorStub.inflate();
        }
        mIndicatorImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpanded();
            }
        });
    }

    /**
     * Set the indicator background width, height and margins
     */
    private void setIndicatorBackgroundSize() {
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_top), mIndicatorHeight);
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_bottom), mIndicatorHeight);
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_middle), 0);


        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_top), mIndicatorWidth);
        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_bottom),mIndicatorWidth);
        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_middle),mIndicatorWidth);

        mItemLayout.post(new Runnable() {
            @Override
            public void run() {
                CustomViewUtils.setViewMargin(mIndicatorContainer,
                        mIndicatorMarginLeft, mIndicatorMarginTop, mIndicatorMarginRight, mIndicatorMarginBottom);
            }
        });

        CustomViewUtils.setViewMarginTop(mBaseLayout.findViewById(R.id.icon_indicator_middle), (-1 * mIndicatorHeight / 2));
        CustomViewUtils.setViewMarginTop(mBaseLayout.findViewById(R.id.icon_indicator_bottom), (-1 * mIndicatorHeight / 2));

    }


    /**
     * Setup the variables that defines custom_item state.
     */
    private void setupStateVariables() {
        if (!mShowAnimation) {
            mAnimationDuration = 0;
        }
    }

    /**
     * Read all custom styleable attributes.
     * @param context The custom View Context.
     * @param attributeSet atrributes to be read.
     */
    private void readAttributes(Context context,AttributeSet attributeSet){
        TypedArray array=context.getTheme().obtainStyledAttributes(attributeSet,R.styleable.CustomItem,0,0);

        try{
            mItemLayoutId=array.getResourceId(R.styleable.CustomItem_item_layout,0);
            mSeparatorLayoutId = array.getResourceId(R.styleable.CustomItem_separator_layout, 0);
            mSubItemLayoutId = array.getResourceId(R.styleable.CustomItem_sub_item_layout, 0);
            mIndicatorHeight = array.getDimensionPixelSize(R.styleable.CustomItem_indicator_height, 0);
            mIndicatorWidth =array.getDimensionPixelSize(R.styleable.CustomItem_indicator_width,0);
            mIndicatorMarginLeft = array.getDimensionPixelSize(R.styleable.CustomItem_indicator_margin_left, 0);
            mIndicatorMarginRight = array.getDimensionPixelSize(R.styleable.CustomItem_indicator_margin_right, 0);
            mIndicatorMarginTop=array.getDimensionPixelSize(R.styleable.CustomItem_indicator_margin_top,0);
            mIndicatorMarginBottom=array.getDimensionPixelSize(R.styleable.CustomItem_indicator_margin_bottom,0);
            mShowIndicator = array.getBoolean(R.styleable.CustomItem_show_indicator, true);
            mShowAnimation = array.getBoolean(R.styleable.CustomItem_show_animation, true);
            mStartCollapsed = array.getBoolean(R.styleable.CustomItem_start_collapsed, true);
            mAnimationDuration = array.getInt(R.styleable.CustomItem_animation_duration, DEFAULT_ANIM_DURATION);
        }finally {
            array.recycle();
        }
    }

    public interface OnItemStateChanged {
        /**
         * Notify if custom_item was expanded or collapsed.
         * @param expanded true if expanded. false otherwise.
         */
        void itemCollapseStateChanged(boolean expanded);
    }
}