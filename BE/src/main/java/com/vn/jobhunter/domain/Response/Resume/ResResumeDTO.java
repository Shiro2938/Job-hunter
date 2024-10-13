package com.vn.jobhunter.domain.Response.Resume;

import com.vn.jobhunter.domain.enumeration.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResResumeDTO {
    private Long id;

    private String email;

    private String url;

    private StatusEnum status;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    private String companyName;

    private UserResume user;

    private JobResume job;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
