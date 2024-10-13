package com.vn.jobhunter.domain.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
    @NotBlank(message = "Username must be not blank")
    private String username;

    @NotBlank(message = "Password must be not blank")
    private String password;
}
