package com.jsut.zachweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    public static final Date getNowDate(){
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = dFormat.format(new Date());
            return dFormat.parse(formatDate);
        }catch (Exception e){
            throw new ServiceException("日期出现异常！");
        }
    }
   /* public static final Date DateFormat(String date){

    }*/
}
