package com.paybook.application.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class UserDetailsResponse {
    private String name;
    private String username;
    private String phoneNumber;

    public UserDetailsResponse() {

    }

}
