package com.paybook.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class MonthsResponse {
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private String value;
}
