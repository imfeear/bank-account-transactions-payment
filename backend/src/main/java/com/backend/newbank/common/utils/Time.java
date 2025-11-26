package com.backend.newbank.common.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Time {
    public static Timestamp currentTimestamp(){
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static LocalDate currentLocalDate(){
        Timestamp now = currentTimestamp();
        return now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String actualMonthReference(){
        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() >= 2) {
            return date.format(DateTimeFormatter.ofPattern("MM/yy"));
        }
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern("MM/yy"));
    }

   public static String monthReferenceFrom(String monthReference, int distance){
       if (distance == 0) {
           return monthReference;
       }
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
       YearMonth yearMonth = YearMonth.parse(monthReference, formatter);
       YearMonth newYearMonth = yearMonth.plusMonths(distance);
       LocalDate newDate = newYearMonth.atDay(1);
       return newDate.format(formatter);
    }
}
