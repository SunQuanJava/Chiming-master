package com.baizhi.baseapp.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NavigationBarView extends RelativeLayout implements OnPageChangeListener {

	private OnTabChangeListener mOnTabChangeListener;
    private OnSameTabClickListener mOnSameTabClickListener;
	private LinearLayout mTabWidget;
	private ViewPager mTabContent;
	private int mTabCount;
	private SparseArray<NavigationSpec> mTabSpecs;
	private int mCurrTab=-1;
    private boolean mIsSmoothly;

	public NavigationBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTabHost();
	}

	public NavigationBarView(Context context) {
		this(context, null);
	}

    public void setSmoothly(boolean isSmoothly) {
        this.mIsSmoothly = isSmoothly;
    }

    private void initTabHost() {
		setFocusableInTouchMode(true);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		mTabSpecs = new SparseArray<>();
	}

	public void addTab(String tag, Class<? extends Fragment> clazz) {
        addTab(tag,clazz,null);
	}

    public void addTab(String tag, Class<? extends Fragment> clazz, Bundle bundle) {
        if(bundle==null) {
            bundle = new Bundle();
        }
        bundle.putString("tag",tag);
        mTabSpecs.append(mTabSpecs.size(), NavigationSpec.newInstance(tag).setIndicator(
                Fragment.instantiate(getContext(), clazz.getName(), bundle)));
    }

	public void addTab(String tag, Fragment f) {
		mTabSpecs.append(mTabSpecs.size(), NavigationSpec.newInstance(tag).setIndicator(f));
	}

	public void addTab(String tag, View v) {
		mTabSpecs.append(mTabSpecs.size(), NavigationSpec.newInstance(tag).setIndicator(v));
	}

    public void reset() {
        mTabSpecs.clear();
        mCurrTab = -1;
        if(mTabContent!=null) {
            mTabContent.setAdapter(null);
        }
    }

	/**
	 * instantiate all of tabs and pagers 
	 * 
	 */
	private void instantiateTab() {
		mTabWidget = (LinearLayout) findViewById(android.R.id.tabs);
		if (mTabWidget == null) {
			throw new RuntimeException(
					"Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
		}
        final int childCount = getChildCount();
        for(int i=0;i<childCount;i++) {
            if(getChildAt(i) instanceof ViewPager) {
                mTabContent = (ViewPager) getChildAt(i);
            }
        }
		if (mTabContent == null) {
			throw new RuntimeException(
					"Your TabHost must have a ViewPager whose id attribute is "
							+ "'android.R.id.tabcontent'");
		}
		mTabCount = mTabWidget.getChildCount();
		if (mTabSpecs.size() != mTabCount) {
			throw new RuntimeException(
					"Your TabHost's ViewPager must only have " + mTabCount
							+ ".");
		}
		mTabContent.setOnPageChangeListener(this);
		mTabContent.setOffscreenPageLimit(mTabCount);
		for (int i = 0; i < mTabCount; i++) {
			final int curr = i;
			final View tab = mTabWidget.getChildAt(i);
			tab.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setCurrentTab(curr);
				}
			});
		}
	}
	
	/**
	 * 
	 * @see #setup(android.support.v4.app.FragmentManager)
	 */
	public void setup() {
		instantiateTab();
		mTabContent.setAdapter(new ViewsPagerAdapter());
		
	}
	
	/**
	 * when user fragment to show something used this method ,otherwise if the 
	 * viewpager's item is a view used{@link #setup()} 
	 * @param fragmentManager
	 */
	public void setup(FragmentManager fragmentManager) {
		instantiateTab();
		mTabContent.setAdapter(new FragmentPagerAdapter(fragmentManager) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mTabCount;
			}

			@Override
			public Fragment getItem(int i) {

				return (Fragment) (mTabSpecs.get(i).getIndicator()
                        .getIndicatorStrategy());
			}
		});
	}

	/**
	 * Register a callback to be invoked when the selected state of any of the
	 * mItems in this list changes
	 * 
	 * @param l The callback that will run
	 */
	public void setOnTabChangedListener(OnTabChangeListener l) {
		mOnTabChangeListener = l;
	}

    /**
     * Register a callback to be invoked when the selected state of any of the
     * mItems in this list changes
     *
     * @param l The callback that will run
     */
    public void setOnSameTabClickListener(OnSameTabClickListener l) {
        mOnSameTabClickListener = l;
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int state, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int i) {
		selectedCurrentTab(i);
	}

	public int getCurrentTab() {
		return mCurrTab;
	}
	
	public void setCurrentTab(int currentTab) {
        if(mCurrTab == -1) {
            mCurrTab = currentTab;
            for (int j = 0; j < mTabCount; j++) {
                mTabWidget.getChildAt(j).setSelected(j == currentTab);
            }
            mTabContent.setCurrentItem(mCurrTab);
            invokeOnTabChangeListener(mCurrTab);
            return;
        }
        if(mCurrTab == currentTab) {
            invokeOnSameTabClickListener(currentTab);
        } else {
            mTabContent.setCurrentItem(currentTab, mIsSmoothly);
        }

	}
	
	/**
	 * Selected the tab in index of currentTab
	 * @param currentTab
	 */
	private void selectedCurrentTab(int currentTab) {
		if (mCurrTab==currentTab) {
			return;
		}
		mCurrTab=currentTab;
		for (int j = 0; j < mTabCount; j++) {
			mTabWidget.getChildAt(j).setSelected(j == currentTab);
		}
		invokeOnTabChangeListener(currentTab);
	}

	private void invokeOnTabChangeListener(int i) {
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabChanged(i, mTabSpecs.get(i).getTag());
		}
	}

    private void invokeOnSameTabClickListener(int i) {
        if (mOnSameTabClickListener != null) {
            mOnSameTabClickListener.onSameTabClicked(i, mTabSpecs.get(i).getTag());
        }
    }

	public interface OnTabChangeListener {
		void onTabChanged(int i, String tabName);
	}

    public interface OnSameTabClickListener {
        void onSameTabClicked(int i, String tabName);
    }
	
	/**
	 * a mAdapter for a views array or collection to pushed the ViewPager
	 *
	 */
	private class ViewsPagerAdapter extends PagerAdapter{
		@Override
		public boolean isViewFromObject(View v, Object o) {
			// TODO Auto-generated method stub
			return v==o;
		}
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View item = null;
			NavigationSpec tSpec=mTabSpecs.get(position);
			if (tSpec!=null&&tSpec.getIndicator()!=null) {
				item = (View) tSpec.getIndicator().getIndicatorStrategy();
			}
	
			container.addView(item);
			return item;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabCount;
		}
	}
}
