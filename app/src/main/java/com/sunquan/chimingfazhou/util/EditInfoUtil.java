package com.sunquan.chimingfazhou.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.AbsListView;

import com.avast.android.dialogs.fragment.BillboardDialogFragment;
import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.models.UserAddress;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.widget.CitySelectView;

import java.util.List;


/**
 * 用户资料编辑帮助类
 * <p/>
 * Created by sunquan1 on 2015/1/4.
 */
public class EditInfoUtil {

    public static final int REQUEST_EDIT_GENDER = 1;
    public static final int REQUEST_DATE_PICKER = 2;
    public static final int REQUEST_ADDRESS_DIALOG = 3;


    private static List<UserAddress.Province> mProvinceList;
    private static String[] provinces;
    private static String[][] citiess;
    public static CitySelectView mCitySelectView;


    /**
     * 编辑地址
     *
     * @param activity
     * @param userInfo
     */
    public static void editAddress(final FragmentActivity activity, final UserInfo userInfo) {
        parseEditAddressList(activity);
        String province = userInfo.getProvince();
        String city = userInfo.getCity();
        if(TextUtils.isEmpty(province)) {
            province = "11";
        }
        if(TextUtils.isEmpty(city)) {
            city = "0";
        }

        if (mProvinceList != null) {
            int[] addresses = getAddressIdx(mProvinceList, Integer.valueOf(province), Integer.valueOf(city));
            mCitySelectView = new CitySelectView(activity, provinces,
                    addresses[0], citiess, addresses[1], new CitySelectView.OnSelectListener() {
                @Override
                public void onSelect(int provinceIdx, int cityIdx) {
                    _onSelect(activity, userInfo, provinceIdx, cityIdx);
                }
            });

            BillboardDialogFragment.createBuilder(activity)
                    .setCustomView(mCitySelectView)
                    .setPositiveButtonText(android.R.string.ok)
                    .setNegativeButtonText(android.R.string.cancel)
                    .setRequestCode(REQUEST_ADDRESS_DIALOG)
                    .setTag("editAddress")
                    .showSinglton();
        }
    }

    public static void editGender(final FragmentActivity activity, String defaultValue) {
        int checkedItem = 0;
        if (activity.getString(R.string.female).equals(defaultValue)) {
            checkedItem = 1;
        }
        ListDialogFragment
                .createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle("性别")
                .setItems(new String[]{
                        activity.getString(R.string.male),//0
                        activity.getString(R.string.female)//1
                })
                .setRequestCode(REQUEST_EDIT_GENDER)
                .setCheckedItems(new int[]{checkedItem})
                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                .show();
    }

    /**
     * province和city在v3中为地址名，在v4为地址索引
     *
     * @param context
     * @param userInfo
     */
    private static void adapterAddress(Context context, UserInfo userInfo) {
        List<UserAddress.Province> mProvinceList = UserAddress.getProvinceList(context);
        if (mProvinceList != null) {
            for (UserAddress.Province p : mProvinceList) {
                if (p.getId().equals(userInfo.getProvince())) {
                    userInfo.setLocation(p.getName());
                    List<UserAddress.City> cityList = p.getCitys();
                    int j = 0;
                    for (UserAddress.City c : cityList) {
                        if (c.getId().equals(userInfo.getCity())) {
                            userInfo.setLocation(userInfo.getLocation() + " " + c.getName());
                            return;
                        }
                        j++;
                    }
                }
            }
        }
    }


    private static int[] getAddressIdx(List<UserAddress.Province> mProvinceList, int provinceId, int cityId) {
        int[] result = new int[]{-1, -1};
        if (mProvinceList != null) {
            int i = -1;
            for (UserAddress.Province province : mProvinceList) {
                i++;
                if (province.getId().equals("" + provinceId)) {
                    result[0] = i;
                    List<UserAddress.City> cityList = province.getCitys();
                    int j = -1;
                    for (UserAddress.City city : cityList) {
                        j++;
                        if (city.getId().equals("" + cityId)) {
                            result[1] = j;
                            return result;
                        }
                    }
                }
            }
        }
        return result;
    }

    private static void parseEditAddressList(final Context context) {
        if (mProvinceList == null) {
            mProvinceList = UserAddress.getProvinceList(context);
            if (mProvinceList != null) {
                provinces = new String[mProvinceList.size()];
                citiess = new String[mProvinceList.size()][];
                int i = 0;
                for (UserAddress.Province province : mProvinceList) {
                    provinces[i] = province.getName();
                    List<UserAddress.City> cityList = province.getCitys();
                    String[] cities = new String[cityList.size()];
                    cities[0] = "";
                    int j = 0;
                    for (UserAddress.City city : cityList) {
                        cities[j] = city.getName();
                        j++;
                    }
                    citiess[i] = cities;
                    i++;
                }
            }
        }
    }

    /**
     * 获取省份和城市
     *
     * @param context
     * @param userInfo
     */
    private static String[] getAddress(Context context, UserInfo userInfo) {
        String[] address = new String[2];

        List<UserAddress.Province> mProvinceList = UserAddress.getProvinceList(context);

        if (mProvinceList != null) {
            for (UserAddress.Province province : mProvinceList) {
                if (province.getId().equals(userInfo.getProvince())) {
                    address[0] = province.getName();

                    List<UserAddress.City> cityList = province.getCitys();
                    for (UserAddress.City city : cityList) {
                        if (city.getId().equals(userInfo.getCity())) {
                            address[1] = city.getName();

                            return address;
                        }
                    }
                }
            }
        }

        return address;
    }

    private static void _onSelect(Context context, UserInfo userInfo, int provinceIdx, int cityIdx) {
        int provinceId = Integer.parseInt(mProvinceList.get(provinceIdx).getId());
        int cityId = Integer.parseInt(mProvinceList.get(provinceIdx).getCitys().get(cityIdx)
                .getId());
        userInfo.setProvince(String.valueOf(provinceId));
        userInfo.setCity(String.valueOf(cityId));
        adapterAddress(context, userInfo);
    }

    public interface OnEditCompleteListener {
        void onEditComplete();
    }
}
