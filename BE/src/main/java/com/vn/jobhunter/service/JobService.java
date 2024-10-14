package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.Job;
import com.vn.jobhunter.domain.Response.Job.ResCreateJobDTO;
import com.vn.jobhunter.domain.Response.Job.ResUpdateJobDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Skill;
import com.vn.jobhunter.repository.JobRepository;
import com.vn.jobhunter.repository.ResumeRepository;
import com.vn.jobhunter.repository.SkillRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final Converter converter;
    private final CompanyService companyService;
    private final ResumeRepository resumeRepository;
    private final SkillRepository skillRepository;


    public JobService(JobRepository jobRepository, Converter converter, @Lazy CompanyService companyService, ResumeRepository resumeRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.converter = converter;
        this.companyService = companyService;
        this.resumeRepository = resumeRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO handleCreateJob(Job job) throws InvalidException {

        //Save only skill ID valid
        if (job.getSkills() != null) {
            List<Long> IDSkillList = job.getSkills().stream()
                    .map(item -> item.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = skillRepository.findByIdIn(IDSkillList);
            job.setSkills(skills);
        }

        Company company = this.companyService.findById(job.getCompany().getId());
        job.setCompany(company);

        Job createdJob = this.jobRepository.save(job);
        return this.converter.toResCreateJobDTO(createdJob);
    }

    public ResUpdateJobDTO handleUpdateJob(Job job) throws InvalidException {
        // handle job not found
        Job jobInDB = this.findById(job.getId());

        if (job.getCompany() != null) {
            Company companyInDB = this.companyService.findById(job.getCompany().getId());
            jobInDB.setCompany(companyInDB);
        }

        // skill
        if (job.getSkills() != null) {
            List<Skill> skills = job.getSkills().stream().
                    filter(item -> this.skillRepository.findById(item.getId()).isPresent())
                    .collect(Collectors.toList());
            jobInDB.setSkills(skills);
        }

        //mapping
        jobInDB.setName(job.getName());
        jobInDB.setLocation(job.getLocation());
        jobInDB.setSalary(job.getSalary());
        jobInDB.setLevel(job.getLevel());
        jobInDB.setQuantity(job.getQuantity());
        jobInDB.setActive(job.isActive());
        jobInDB.setDescription(job.getDescription());
        jobInDB.setEndDate(job.getEndDate());

        jobInDB = this.jobRepository.save(jobInDB);
        return this.converter.toResUpdateJobDTO(jobInDB);
    }

    public Job findById(Long id) throws InvalidException {
        Job job = this.jobRepository.findById(id).orElse(null);
        if (job == null) throw new InvalidException("Job not found");
        return job;
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<Job> specification) {
        Page<Job> jobs = this.jobRepository.findAll(specification, pageable);

        return this.converter.toDefaultResultPaginationDTO(jobs);
    }

    public void deleteById(long id) {

        //delete resume
        Job job = this.jobRepository.findById(id).orElse(null);
        if (job != null) {
            this.resumeRepository.deleteAll(job.getResumes());
            this.jobRepository.deleteById(id);
        }

    }
}
