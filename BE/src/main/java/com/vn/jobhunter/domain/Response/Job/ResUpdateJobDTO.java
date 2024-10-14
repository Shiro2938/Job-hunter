package com.vn.jobhunter.domain.Response.Job;

import com.vn.jobhunter.domain.Skill;
import com.vn.jobhunter.domain.enumeration.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;

    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private String description;

    private Instant startDate;

    private Instant endDate;

    private List<Skill> skills;

    private boolean active;

    private Instant updatedAt;

    private String updatedBy;
}