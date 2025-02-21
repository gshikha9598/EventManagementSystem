package org.example.dto;

import lombok.Data;

@Data
public class TokenResponse {  //json convert to java  object

    private String access_token;
    private String refresh_token;

}
