package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class LoginDto {
    private String username;
    private String password;
}
