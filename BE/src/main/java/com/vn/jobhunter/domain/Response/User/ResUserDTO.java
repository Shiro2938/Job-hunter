package com.vn.jobhunter.domain.Response.User;

import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.enumeration.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private String age;
    private GenderEnum gender;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Company company;
}
