package com.android.xjay.joyplan.CustomExpanding;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by diego on 5/9/16.
 * This is the Expanding List. User can create ExpandingItem by calling {@link #createNewItem(int)}.
 */
public class ExpandingList extends ScrollView {
    /**
     * Member variable to hold the items.
     */
    private LinearLayout mContainer;

    /**
     * The constructor.
     * @param context The View Context.
     * @param attrs The attributes.
     */
    public ExpandingList(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        addView(mContainer);
    }

    public void setItemPadding(int padding){
        mContainer.setPadding(padding,padding,padding,padding);
    }

    /**
     * Method to add a new custom_item.
     * @param item The ExpandingItem custom_item.
     */
    private void addItem(CustomItem item) {
        mContainer.addView(item);
    }

    /**
     * Method to create and add a new custom_item.
     * @param layoutId The custom_item Layout.
     * @return The created custom_item.
     */
    public CustomItem createNewItem(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup item = (ViewGroup) inflater.inflate(layoutId, this, false);
        if (item instanceof CustomItem) {
            CustomItem customItem = (CustomItem) item;
            customItem.setParent(this);
            addItem(customItem);
            return customItem;
        }
        throw new RuntimeException("The layout id not an instance of com.diegodobelo.expandinganimlib.ExpandingItem");
    }

    /**
     * Method to get an Item from the ExpandingList by its index.
     * @param index The index of the custom_item.
     * @return An ExpandingItem in the list.
     */
    public CustomItem getItemByIndex(int index) {
        if (index < 0 || index >= getItemsCount()) {
            throw new RuntimeException("Index must be grater than 0 and lesser than list size");
        }
        return (CustomItem) mContainer.getChildAt(index);
    }

    /**
     * Return the items count.
     * @return Items count.
     */
    public int getItemsCount() {
        return mContainer.getChildCount();
    }

    /**
     * Method to remove an custom_item.
     * @param item The custom_item to be removed.
     */
    public void removeItem(final CustomItem item) {
        mContainer.removeView(item);
    }


    /**
     * Method to remove all items.
     */
    public void removeAllViews() {
        mContainer.removeAllViews();
    }

    /**
     * Scroll up to show custom_sub_item items
     * @param delta The calculated amount to scroll up.
     */
    protected void scrollUpByDelta(final int delta) {
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(-10, getScrollY() + delta);
            }
        });
    }
}