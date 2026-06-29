package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;



@AllArgsConstructor
@Setter
@Getter

public class UserInfoResponseDto {
    private String userEmail;
    private String firstName;
    private String lastName;
    private Timestamp creationDate;
    private Timestamp updateDate;


}
