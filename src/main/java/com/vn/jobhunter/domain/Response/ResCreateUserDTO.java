package com.vn.jobhunter.domain.Response;

import com.vn.jobhunter.domain.enumeration.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private String age;
    private GenderEnum gender;
    private Instant createdAt;
    private String createdBy;
}
