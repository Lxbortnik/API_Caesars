package com.alex.caesars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//all methods + getters/setters + empty constructor/ toString, etc.
@AllArgsConstructor
public class AuthDTO {
    private String launchToken;
    private String id;

}
