package com.paybook.application.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class UserDetailsUpdateRequest {
    // Getters and setters
    private String name;
    private String username;


    public UserDetailsUpdateRequest() {

    }

}
