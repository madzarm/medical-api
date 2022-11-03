package com.example.domain.converter;

import com.example.exception.exceptions.DateParseException;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class Converter {

    public LocalDate convertDateToLocalDate(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date convertStringToDate(String date) {
        if (date == null)
            return null;
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (Exception e1) {
                try {
                    return new SimpleDateFormat("dd.MM.yyyy").parse(date);
                } catch (Exception e2) {
                    try {
                        return new SimpleDateFormat("ddMMyyyy").parse(date);
                    } catch (Exception e3) {
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                        } catch (Exception e4) {
                            try {
                                return new SimpleDateFormat("yyyy/MM/dd").parse(date);
                            } catch (Exception e5) {
                                try {
                                    return new SimpleDateFormat("yyyy.MM.dd").parse(date);
                                } catch (Exception e6) {
                                    try {
                                        return new SimpleDateFormat("yyyyMMdd").parse(date);
                                    } catch (Exception e7) {
                                        throw new DateParseException(date);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public LocalDate convertStringToLocalDate(String date) {
        return convertDateToLocalDate(convertStringToDate(date));
    }
}
