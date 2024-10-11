package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.repository.CompanyRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final Converter converter;
    private final UserService userService;
    private final JobService jobService;

    public CompanyService(CompanyRepository companyRepository, Converter converter, UserService userService, JobService jobService) {
        this.companyRepository = companyRepository;
        this.converter = converter;
        this.userService = userService;
        this.jobService = jobService;
    }

    public Company handleCreate(Company company) {
        return this.companyRepository.save(company);
    }

    public Company handleUpdate(Company company) throws InvalidException {

        //handle not found
        Company companyInDB = this.companyRepository.findById(company.getId()).orElse(null);
        if (companyInDB == null) throw new InvalidException("Company not found");

        //mapping
        companyInDB.setName(company.getName());
        companyInDB.setAddress(company.getAddress());
        companyInDB.setLogo(company.getLogo());
        companyInDB.setDescription(company.getDescription());

        return this.companyRepository.save(companyInDB);
    }

    public Company findById(long id) throws InvalidException {
        Company company = this.companyRepository.findById(id).orElse(null);
        if (company == null) throw new InvalidException("Company not found");
        return company;
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<Company> specification) {
        Page<Company> companyPage = this.companyRepository.findAll(specification, pageable);
        return this.converter.toDefaultResultPaginationDTO(companyPage);
    }

    public void handleDelete(long id) throws InvalidException {


        Company company = this.companyRepository.findById(id).orElse(null);
        if (company == null) throw new InvalidException("Company not found");

        //delete user
        company.getUsers().stream().forEach(item -> {
            try {
                this.userService.deleteById(item.getId());
            } catch (InvalidException e) {
                throw new RuntimeException(e);
            }
        });

        //delete job
        company.getJobs().forEach(item -> jobService.deleteById(item.getId()));

        this.companyRepository.deleteById(id);
    }
}
