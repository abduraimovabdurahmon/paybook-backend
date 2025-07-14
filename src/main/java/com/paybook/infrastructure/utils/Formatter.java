package com.paybook.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Formatter {
    public String formatMonthTitle(LocalDate date) {
        // O'zbekcha oy nomlari
        String[] uzbekMonths = {
                "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
                "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
        };
        int month = date.getMonthValue();
        int year = date.getYear();
        return uzbekMonths[month - 1] + " " + year;
    }
}
