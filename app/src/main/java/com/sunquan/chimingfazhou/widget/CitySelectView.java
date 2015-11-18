package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sunquan.chimingfazhou.R;
import com.wheel.OnWheelChangedListener;
import com.wheel.OnWheelScrollListener;
import com.wheel.WheelView;
import com.wheel.adapter.ArrayWheelAdapter;


public class CitySelectView extends FrameLayout {
    
    public interface OnSelectListener {
        public void onSelect(int provinceIdx, int cityIdx);
    }
    
    private class ArrayAddressAdapter<T> extends ArrayWheelAdapter<T> {

        public ArrayAddressAdapter(Context context, T[] items) {
            super(context, items);
        }

        @Override
        protected void configureTextView(TextView view) {
            int padding = getResources().getDimensionPixelSize(R.dimen.edit_address_item_padding);
            view.setPadding(0, padding, 0, padding);
            view.setTextColor(DEFAULT_TEXT_COLOR);
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setTextSize(18);
            view.setSingleLine(true);
            view.setEllipsize(TruncateAt.END);
            view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        }
    }

    private WheelView wheelProvince;
    private WheelView wheelCity;
    
    private String mProvices[];
    private String mCities[][];
    private OnSelectListener mListener;
    private int mProviceIdx;
    private int mCityIdx;
    // Scrolling flag
    private boolean scrolling = false;
    private int proviceIndex;

    public CitySelectView(Context context, String[] provices, int proviceIdx,
            String[][] cities, int cityIdx, OnSelectListener listener) {
        super(context);
        this.mProvices = provices;
        this.mCities = cities;
        if (proviceIdx >= 0 && proviceIdx < mProvices.length) {
            this.mProviceIdx = proviceIdx;
            if (cityIdx >= 0 && cityIdx < cities[proviceIdx].length) {
                this.mCityIdx = cityIdx;
            }
        }
        this.mListener = listener;
        init();
    }


    private void init() {
        LayoutInflater infalter = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.city_select, this);
        wheelProvince = (WheelView) view.findViewById(R.id.wheelProvince);
        wheelProvince.setVisibleItems(5);

        ArrayWheelAdapter<String> adapter = new ArrayAddressAdapter<String>(
                getContext(), mProvices);
        wheelProvince.setViewAdapter(adapter);

        wheelCity = (WheelView) view.findViewById(R.id.wheelCity);
        wheelCity.setVisibleItems(5);

        wheelProvince.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateCities(wheelCity, mCities, newValue);
                }
            }
        });

        wheelProvince.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
                proviceIndex = wheelProvince.getCurrentItem();
            }

            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(wheelCity, mCities, wheelProvince.getCurrentItem());
            }
        });

        wheelProvince.setCurrentItem(mProviceIdx);
        updateCities(wheelCity, mCities, mProviceIdx);
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        int cityIdx = 0;
        if (index == mProviceIdx) {
            cityIdx = mCityIdx;
        }
        ArrayWheelAdapter<String> adapter = new ArrayAddressAdapter<String>(
                getContext(), cities[index]);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cityIdx);
    }
    
    public void select() {
        if (mListener != null) {
            if (!scrolling || proviceIndex == wheelProvince.getCurrentItem()) {
                mListener.onSelect(wheelProvince.getCurrentItem(), wheelCity.getCurrentItem());
            } else {
                mListener.onSelect(wheelProvince.getCurrentItem(), 0);
            }
        }
    }
}
