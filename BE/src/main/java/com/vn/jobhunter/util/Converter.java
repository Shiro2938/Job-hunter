package com.vn.jobhunter.util;

import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.Job;
import com.vn.jobhunter.domain.Response.Auth.ResLoginDTO;
import com.vn.jobhunter.domain.Response.Job.ResCreateJobDTO;
import com.vn.jobhunter.domain.Response.Job.ResUpdateJobDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Response.Resume.ResCreateResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResResumeDTO;
import com.vn.jobhunter.domain.Response.Resume.ResUpdateResumeDTO;
import com.vn.jobhunter.domain.Response.User.ResCreateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUpdateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUserDTO;
import com.vn.jobhunter.domain.Resume;
import com.vn.jobhunter.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Converter {
    private final ModelMapper modelMapper;

    public Converter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ResCreateUserDTO toResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUser = modelMapper.map(user, ResCreateUserDTO.class);

        if (user.getRole() != null) {
            resCreateUser.setRole(new ResCreateUserDTO.RoleUser(user.getRole()
                    .getId(), user.getRole().getName()));
        }

        if (user.getCompany() != null) {
            resCreateUser.setCompany(new ResCreateUserDTO.CompanyUser(user.getCompany().getId(),
                    user.getCompany().getName()));
        }

        return resCreateUser;
    }

    public ResUserDTO toResUserDTO(User user) {
        ResUserDTO resUser = modelMapper.map(user, ResUserDTO.class);

        if (user.getRole() != null) {
            resUser.setRole(new ResUserDTO.RoleUser(user.getRole()
                    .getId(), user.getRole().getName()));
        }

        if (user.getCompany() != null) {
            resUser.setCompany(new ResUserDTO.CompanyUser(user.getCompany().getId(),
                    user.getCompany().getName()));
        }
        return resUser;
    }

    public ResUpdateUserDTO toResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUpdateUser = modelMapper.map(user, ResUpdateUserDTO.class);
        if (user.getRole() != null) {
            resUpdateUser.setRole(new ResUpdateUserDTO.RoleUser(user.getRole()
                    .getId(), user.getRole().getName()));
        }

        if (user.getCompany() != null) {
            resUpdateUser.setCompany(new ResUpdateUserDTO.CompanyUser(user.getCompany().getId(),
                    user.getCompany().getName()));
        }

        return resUpdateUser;
    }

    public ResLoginDTO toResLoginDTO(User user) {
        ResLoginDTO.UserLogin userLogin = modelMapper.map(user, ResLoginDTO.UserLogin.class);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setUser(userLogin);
        return resLoginDTO;
    }

    public ResultPaginationDTO toResultPaginationUserDTO(Page<User> userPage) {

        ResultPaginationDTO resultPaginationDTO = this.initResultPagination(userPage);
        List<ResUserDTO> resUserDTO = userPage.getContent().stream()
                .map(this::toResUserDTO).toList();
        resultPaginationDTO.setResult(resUserDTO);


        return resultPaginationDTO;
    }

    public <T> ResultPaginationDTO initResultPagination(Page<T> objectPage) {

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        int page = objectPage.getNumber() + 1;
        int pageSize = objectPage.getSize();
        int pages = objectPage.getTotalPages();
        long total = objectPage.getTotalElements();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta(page, pageSize, pages, total);
        resultPaginationDTO.setMeta(meta);

        return resultPaginationDTO;
    }

    public <T> ResultPaginationDTO toDefaultResultPaginationDTO(Page<T> objectPage) {
        ResultPaginationDTO resultPaginationDTO = this.initResultPagination(objectPage);

        resultPaginationDTO.setResult(objectPage.getContent());

        return resultPaginationDTO;
    }

    public ResLoginDTO.UserGetAccount toResGetAccountDTO(User user) {
        ResLoginDTO.UserLogin userLogin = modelMapper.map(user, ResLoginDTO.UserLogin.class);


        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        userGetAccount.setUser(userLogin);

        return userGetAccount;
    }

    public ResCreateJobDTO toResCreateJobDTO(Job job) {
        ResCreateJobDTO resCreateJobDTO = modelMapper.map(job, ResCreateJobDTO.class);
        return resCreateJobDTO;
    }

    public ResUpdateJobDTO toResUpdateJobDTO(Job job) {
        ResUpdateJobDTO resUpdateJobDTO = modelMapper.map(job, ResUpdateJobDTO.class);
        return resUpdateJobDTO;
    }

    public ResCreateResumeDTO toResCreateResumeDTO(Resume resume) {
        return this.modelMapper.map(resume, ResCreateResumeDTO.class);
    }

    public ResUpdateResumeDTO toResUpdateResumeDTO(Resume resume) {
        return this.modelMapper.map(resume, ResUpdateResumeDTO.class);
    }

    public ResResumeDTO toResResumeDTO(Resume resume) {
        ResResumeDTO resResumeDTO = this.modelMapper.map(resume, ResResumeDTO.class);

        ResResumeDTO.UserResume user = new ResResumeDTO.UserResume();
        ResResumeDTO.JobResume job = new ResResumeDTO.JobResume();

        user = this.modelMapper.map(resume.getUser(), ResResumeDTO.UserResume.class);
        job = this.modelMapper.map(resume.getJob(), ResResumeDTO.JobResume.class);

        resResumeDTO.setUser(user);
        resResumeDTO.setJob(job);

        Company company = resume.getJob().getCompany();
        if (company != null) resResumeDTO.setCompanyName(company.getName());

        return resResumeDTO;
    }

    public ResultPaginationDTO toResultPaginationResumeDTO(Page<Resume> resumePage) {
        ResultPaginationDTO resultPaginationDTO = this.initResultPagination(resumePage);

        List<ResResumeDTO> resResumeDTOList = resumePage.getContent().stream()
                .map(this::toResResumeDTO).toList();

        resultPaginationDTO.setResult(resResumeDTOList);

        return resultPaginationDTO;
    }

    public <T> void mapObject(T source, T destination) {
        modelMapper.map(source, destination);
    }
}
