package com.emotionmaster.emolog.util;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtil {
    public static int getWeekOfMonthByDate(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.US); //월요일 부터 시작
        return date.get(weekFields.weekOfMonth());
    }
}
