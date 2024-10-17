package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Response.Resume.ResCreateResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResUpdateResumeDTO;
import com.vn.jobhunter.domain.Resume;
import com.vn.jobhunter.service.ResumeService;
import com.vn.jobhunter.util.annotation.APIMessage;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @APIMessage("Create Resume successful!")
    public ResponseEntity<ResCreateResumeDTO> createResume(@RequestBody @Valid Resume resume) throws InvalidException {
        ResCreateResumeDTO createdResume = this.resumeService.handleCreate(resume);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @PutMapping("/resumes")
    @APIMessage("Update Resume successful!")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws InvalidException {
        ResUpdateResumeDTO updateResume = this.resumeService.handleUpdate(resume);

        return ResponseEntity.ok(updateResume);
    }

    @GetMapping("/resumes/{id}")
    @APIMessage("Get Resume by ID successful!")
    public ResponseEntity<ResResumeDTO> getResume(@PathVariable long id) throws InvalidException {
        ResResumeDTO resResumeDTO = this.resumeService.findById(id);

        return ResponseEntity.ok(resResumeDTO);
    }

    @GetMapping("/resumes")
    @APIMessage("Get All Resumes successful!")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(Pageable pageable, @Filter Specification<Resume> specification) {
        ResultPaginationDTO resultPaginationDTO = this.resumeService.getAllResumesByCompanyUser(pageable, specification);

        return ResponseEntity.ok(resultPaginationDTO);
    }

    @DeleteMapping("/resumes/{id}")
    @APIMessage("Delete Resume by ID successful!")
    public ResponseEntity<Void> deleteResume(@PathVariable long id) throws InvalidException {
        this.resumeService.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
