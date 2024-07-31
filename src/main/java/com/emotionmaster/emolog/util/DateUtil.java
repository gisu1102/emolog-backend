package com.emotionmaster.emolog.util;

import com.emotionmaster.emolog.config.error.errorcode.DiaryErrorCode;
import com.emotionmaster.emolog.config.error.exception.DiaryException;
import com.emotionmaster.emolog.diary.domain.Diary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtil {
    public static int getWeekOfMonthByDate(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.UK); //월요일 부터 시작
        return date.get(weekFields.weekOfMonth());
    }

    public static LocalDate strToDate(String date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date,dtf);
    }
}
