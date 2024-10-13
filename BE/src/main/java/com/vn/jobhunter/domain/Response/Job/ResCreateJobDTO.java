package com.vn.jobhunter.domain.Response.Job;

import java.time.Instant;
import java.util.List;

import com.vn.jobhunter.domain.enumeration.LevelEnum;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResCreateJobDTO {
    private long id;

    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private String description;

    private Instant startDate;

    private Instant endDate;

    private List<String> skills;

    private boolean active;

    private Instant CreatedAt;

    private String CreatedBy;
}