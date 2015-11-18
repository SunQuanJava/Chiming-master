package com.baizhi.baseapp.widget;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * A tab has a tab indicator, content, and a tag that is used to keep
 * track of it.  This builder helps choose among these options.
 */
public class NavigationSpec {

    private String mTag;

    private IndicatorStrategy<?> mIndicatorStrategy;

    public static NavigationSpec newInstance(String tag) {
        return new NavigationSpec().setTag(tag);
    }

    /**
     * Specify a view as the tab indicator.
     */
    public NavigationSpec setIndicator(View view) {
        mIndicatorStrategy = new ViewIndicatorStrategy(view);
        return this;
    }
    
    /**
     * Specify a view as the tab indicator.
     */
    public NavigationSpec setIndicator(Fragment f) {
    	mIndicatorStrategy = new FragmentIndicatorStrategy(f);
    	return this;
    }
    
    public IndicatorStrategy<?> getIndicator() {
        return mIndicatorStrategy;
    }

    public NavigationSpec setTag(String tag) {
    	mTag=tag;
    	return this;
    }
    
    public String getTag() {
        return mTag;
    }
  
    private static class FragmentIndicatorStrategy implements IndicatorStrategy<Fragment> {

    	Fragment mIndicator;
		@Override
		public void setIndicatorStrategy(Fragment t) {
			this.mIndicator=t;
		}

		@Override
		public Fragment getIndicatorStrategy() {
			return mIndicator;
		}
  
		public FragmentIndicatorStrategy(Fragment f){
    		this.mIndicator=f;
    	}

    }
    
    private static class ViewIndicatorStrategy implements IndicatorStrategy<View> {
    	
    	View mIndicator;
    	@Override
    	public void setIndicatorStrategy(View t) {
    		this.mIndicator=t;
    	}
    	
    	@Override
    	public View getIndicatorStrategy() {
    		return mIndicator;
    	}
    	
    	public ViewIndicatorStrategy(View f){
    		this.mIndicator=f;
    	}
    	
    }
    /**
     * Specifies what you do to create a indicator.
     */
    static interface IndicatorStrategy<T> {
    	public void setIndicatorStrategy(T t);
    	public T getIndicatorStrategy();
    }
}
