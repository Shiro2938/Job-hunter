package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Job;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Response.Resume.ResCreateResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResUpdateResumeDTO;
import com.vn.jobhunter.domain.Resume;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.repository.JobRepository;
import com.vn.jobhunter.repository.ResumeRepository;
import com.vn.jobhunter.repository.UserRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.SecurityUtil;
import com.vn.jobhunter.util.SpecificationUtil;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final Converter converter;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, Converter converter, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.converter = converter;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResCreateResumeDTO handleCreate(Resume resume) throws InvalidException {
        if (!ValidUserAndJob(resume)) throw new InvalidException("User / Job not found");

        Resume createdResume = resumeRepository.save(resume);
        return this.converter.toResCreateResumeDTO(createdResume);
    }

    public boolean ValidUserAndJob(Resume resume) {
        Job job = this.jobRepository.findById(resume.getJob().getId()).orElse(null);
        User user = userRepository.findById(resume.getUser().getId()).orElse(null);

        return job != null && user != null;
    }

    public ResResumeDTO findById(long id) throws InvalidException {
        Resume resume = resumeRepository.findById(id).orElse(null);

        if (resume == null) throw new InvalidException("Resume not found");

        return this.converter.toResResumeDTO(resume);
    }

    public ResUpdateResumeDTO handleUpdate(Resume resume) throws InvalidException {
        this.findById(resume.getId());

        Resume resumeInDB = this.resumeRepository.findById(resume.getId()).get();

        resumeInDB.setStatus(resume.getStatus());

        resumeInDB = this.resumeRepository.save(resumeInDB);

        return this.converter.toResUpdateResumeDTO(resumeInDB);
    }

    public ResultPaginationDTO getAllResumesByCompanyUser(Pageable pageable, Specification<Resume> specification) {

        User currentUser = this.userRepository.findByEmail(SecurityUtil.getCurrentUserLogin());

        if (currentUser != null) {
            specification = specification.and(SpecificationUtil.inCompanyWithID(currentUser.getCompany().getId()));
        }
        Page<Resume> resumePage = this.resumeRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = this.converter.toResultPaginationResumeDTO(resumePage);

        return resultPaginationDTO;
    }

    public void deleteById(long id) throws InvalidException {
        if (this.resumeRepository.findById(id).isEmpty()) throw new InvalidException("Resume not found");
        else this.resumeRepository.deleteById(id);
    }
}
