package com.donesk.moneytracker.dto.credentials;


import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
