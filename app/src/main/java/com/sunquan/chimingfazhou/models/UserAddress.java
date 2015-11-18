package com.sunquan.chimingfazhou.models;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserAddress {
    public static class City {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Province {
        private String id;
        private String name;
        private List<City> citys = new ArrayList<City>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<City> getCitys() {
            return citys;
        }

        public void setCitys(List<City> citys) {
            this.citys = citys;
        }
    }

    private static List<Province> mProvinceList;

    private UserAddress() {

    }

    synchronized public static List<Province> getProvinceList(Context context) {
        if (mProvinceList == null) {
            mProvinceList = parseEditAddressList(context);
        }
        return mProvinceList;
    }

    private static List<Province> parseEditAddressList(Context context) {
        List<Province> provinceList;
        XmlPullParser parser = Xml.newPullParser();
        try {
            provinceList = new ArrayList<Province>();
            Province province = null;
            parser.setInput(new InputStreamReader(context
                    .getApplicationContext().getAssets().open("provinces.xml")));
            int type;
            int provinceIdx = 0;
            int cityIdx = 0;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("province")) {
                        String id = parser.getAttributeValue(0);
                        String name = parser.getAttributeValue(1);
                        province = new Province();
                        province.setId(id);
                        province.setName(name);
                        provinceList.add(province);
                        cityIdx = 0;
                        provinceIdx++;
                    } else if (parser.getName().equals("city")) {
                        String id = parser.getAttributeValue(0);
                        String name = parser.getAttributeValue(1);
                        City city = new City();
                        city.setId(id);
                        city.setName(name);
                        assert province != null;
                        province.getCitys().add(city);
                        cityIdx++;
                    }
                    break;
                // case XmlPullParser.END_TAG:
                // if (parser.getName().equals("province")) {
                // cities.add(citiesOneProvince);
                // }
                // break;
                default:
                    break;
                }
            }
        } catch (XmlPullParserException e) {
            provinceList = null;
            parser = null;
        } catch (IOException e) {
            provinceList = null;
            parser = null;
        }
        return provinceList;
    }

}
