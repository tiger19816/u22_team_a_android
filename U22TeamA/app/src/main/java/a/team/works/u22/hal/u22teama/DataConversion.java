package a.team.works.u22.hal.u22teama;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConversion {
    private static final SimpleDateFormat dfFullDate01 = new SimpleDateFormat("yyyy年MM月dd日 hh時mm分");
    private static final SimpleDateFormat dfFullDate02 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final SimpleDateFormat dfDate01 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat dfDate02 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dfTime01 = new SimpleDateFormat("hh時mm分");
    private static final SimpleDateFormat dfTime02 = new SimpleDateFormat("hh:mm:ss");

    /**
     * yyyy年MM月dd日 hh時mm分から yyyy-MM-dd hh:mm:ss に変換。
     * @param date(String)
     * @return
     */
    public static String getFullDataConversion01(String date){
        String strData = "";
        try {
            Date d = dfFullDate01.parse(date);
            strData = dfFullDate02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのgetFullDataConversion01時。");
        }
        return strData;
    }

    /**
     * yyyy-MM-dd hh:mm:ss から yyyy年MM月dd日 hh時mm分 に変換。
     * @param date(String)
     * @return String
     */
    public static String getFullDataConversion02(String date){
        String strData = "";
        try {
            Date d = dfFullDate02.parse(date);
            strData = dfFullDate01.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのgetFullDataConversion02時。");
        }
        return strData;
    }

    /**
     * yyyy年MM年dd日 から yyyy-MM-dd に変換。
     * @param date(String)
     * @return
     */
    public static String getDataConversion01(String date){
        String strData = "";
        try {
            Date d = dfDate01.parse(date);
            strData = dfDate02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     * yyyy-MM-dd から yyyy年MM年dd日 に変換。
     * @param date(String)
     * @return String
     */
    public static String getDataConversion02(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate01.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     * hh時mm分 から hh:mm:ss に変換。
     * @param date(String)
     * @return String
     */
    public static String getTimeConversion01(String date){
        String strData = "";
        try {
            Date d = dfTime01.parse(date);
            strData = dfTime02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }

    /**
     * hh:mm:ss から hh時mm分 に変換。
     * @param date(String)
     * @return String
     */
    public static String getTimeConversion02(String date){
        String strData = "";
        try {
            Date fullDate = dfFullDate02.parse(date);
            String time02 = dfTime01.format(fullDate);
            strData = time02;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスの時。");
        }
        return strData;
    }
}
