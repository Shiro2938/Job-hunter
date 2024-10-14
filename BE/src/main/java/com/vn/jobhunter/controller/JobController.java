package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Job;
import com.vn.jobhunter.domain.Response.Job.ResCreateJobDTO;
import com.vn.jobhunter.domain.Response.Job.ResUpdateJobDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.service.JobService;
import com.vn.jobhunter.util.annotation.APIMessage;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @APIMessage("Create Job successful")
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody @Valid Job job) throws InvalidException {
        ResCreateJobDTO resCreateJobDTO = this.jobService.handleCreateJob(job);

        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateJobDTO);
    }

    @PutMapping("/jobs")
    @APIMessage("Update Job successful")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody @Valid Job job) throws InvalidException {
        ResUpdateJobDTO resUpdateJobDTO = this.jobService.handleUpdateJob(job);
        return ResponseEntity.ok(resUpdateJobDTO);
    }

    @GetMapping("/jobs/{id}")
    @APIMessage("Get Job by ID successful")
    public ResponseEntity<Job> getJob(@PathVariable long id) throws InvalidException {
        Job job = this.jobService.findById(id);

        return ResponseEntity.ok(job);
    }

    @GetMapping("/jobs")
    @APIMessage("Get All Jobs successful")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            Pageable pageable, @Filter Specification<Job> specificationJob
    ) {
        ResultPaginationDTO resultPaginationDTO =
                this.jobService.findAll(pageable, specificationJob);
        return ResponseEntity.ok(resultPaginationDTO);

    }

    @DeleteMapping("/jobs/{id}")
    @APIMessage("Delete Job by ID successful")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) {
        this.jobService.deleteById(id);

        return ResponseEntity.ok(null);
    }
}
