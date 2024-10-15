package com.vn.jobhunter.domain;

import com.vn.jobhunter.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must be not blank")
    private String name;

    @NotBlank(message = "API Path must be not blank")
    private String apiPath;

    @NotBlank(message = "Method must be not blank")
    private String method;

    @NotBlank(message = "Module must be not blank")
    private String module;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;


    public Permission(String name,
                      String apiPath,
                      String method,
                      String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @PrePersist
    public void beforeCreatedAt() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin();
    }

    @PreUpdate
    public void beforeUpdatedAt() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin();
    }
}
