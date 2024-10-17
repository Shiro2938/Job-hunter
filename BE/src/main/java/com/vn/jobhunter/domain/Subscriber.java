package com.vn.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vn.jobhunter.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email(regexp = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", message = "Email is invalid")
    private String email;


    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = {"subscribers"})
    private List<Skill> skills;

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
