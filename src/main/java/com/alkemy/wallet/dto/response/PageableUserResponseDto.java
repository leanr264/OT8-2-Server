package com.alkemy.wallet.dto.response;

import com.alkemy.wallet.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PageableUserResponseDto {
    private long count;
    private int pages;
    private String prev;
    private String next;
    private List<UserDto> results;
}
