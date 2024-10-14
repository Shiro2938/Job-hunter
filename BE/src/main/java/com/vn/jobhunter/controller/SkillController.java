package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Skill;
import com.vn.jobhunter.service.SkillService;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody @Valid Skill skill) throws InvalidException {
        Skill createdSkill = this.skillService.handleCreate(skill);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@RequestBody @Valid Skill skill) throws InvalidException {
        Skill updatedSkill = this.skillService.handleUpdate(skill);

        return ResponseEntity.ok(updatedSkill);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable long id) {
        Skill skill = this.skillService.findById(id);

        return ResponseEntity.ok(skill);
    }

    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getSkills(Pageable pageable,
                                                         @Filter Specification<Skill> specification) {
        ResultPaginationDTO paginationDTO = this.skillService.findAll(pageable, specification);

        
        return ResponseEntity.ok(paginationDTO);
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) throws InvalidException {
        this.skillService.deleteById(id);

        return ResponseEntity.ok(null);
    }

}
