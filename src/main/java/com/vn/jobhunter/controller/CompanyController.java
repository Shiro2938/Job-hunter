package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.service.CompanyService;
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
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @APIMessage("Create company successful!")
    public ResponseEntity<Company> createCompany(@RequestBody @Valid Company company) {
        Company createdCompany = this.companyService.handleCreate(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/companies")
    @APIMessage("Update company by ID successful!")
    public ResponseEntity<Company> updateCompany(@RequestBody @Valid Company company) throws InvalidException {
        Company createdCompany = this.companyService.handleUpdate(company);
        return ResponseEntity.ok(createdCompany);
    }

    @GetMapping("/companies/{id}")
    @APIMessage("Get company by ID successful!")
    public ResponseEntity<Company> getCompany(@PathVariable long id) throws InvalidException {
        Company company = this.companyService.findById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/companies")
    @APIMessage("Get all companies successful!")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            Pageable pageable, @Filter Specification<Company> specificationCompany
    ) {
        ResultPaginationDTO resultPaginationDTO = this.companyService.findAll(pageable, specificationCompany);
        return ResponseEntity.ok(resultPaginationDTO);
    }

    @DeleteMapping("/companies/{id}")
    @APIMessage("Delete company by ID successful!")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) throws InvalidException {
        this.companyService.handleDelete(id);

        return ResponseEntity.ok(null);
    }
}
