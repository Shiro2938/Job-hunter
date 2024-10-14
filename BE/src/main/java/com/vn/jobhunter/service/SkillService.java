package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Skill;
import com.vn.jobhunter.repository.SkillRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final Converter converter;
    private final JobService jobService;

    public SkillService(SkillRepository skillRepository, Converter converter, JobService jobService) {
        this.skillRepository = skillRepository;
        this.converter = converter;
        this.jobService = jobService;
    }

    public Skill handleCreate(Skill skill) throws InvalidException {
        if (this.findByName(skill.getName()) != null) throw new InvalidException("Skill name is existing");
        return this.skillRepository.save(skill);
    }


    public Skill findByName(String name) {
        return this.skillRepository.findByName(name);
    }

    public boolean isExistOtherSkillByName(Skill skill) {
        Skill otherSkill = this.skillRepository.findByName(skill.getName());

        if (otherSkill == null) return false;

        return otherSkill.getId() != skill.getId();
    }

    public Skill handleUpdate(Skill skill) throws InvalidException {

        // skill id not found
        if (!this.skillRepository.existsById(skill.getId())) throw new InvalidException("Skill ID not found");

        //updated name is existing
        if (this.isExistOtherSkillByName(skill)) throw new InvalidException("Name skill is existing");

        Skill skillInDB = this.skillRepository.findById(skill.getId()).get();
        skillInDB.setName(skill.getName());

        return this.skillRepository.save(skillInDB);
    }

    public Skill findById(long id) {
        return this.skillRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<Skill> specification) {
        Page<Skill> skillsPage = this.skillRepository.findAll(specification, pageable);

        return this.converter.toDefaultResultPaginationDTO(skillsPage);
    }

    public void deleteById(long id) throws InvalidException {
        Skill skill = this.skillRepository.findById(id).orElse(null);
        if (skill == null) throw new InvalidException("Skill ID not found");

        //delete job have skill
        if (skill.getJobs() != null) {
            skill.getJobs().forEach(job -> this.jobService.deleteById(job.getId()));
        }

        this.skillRepository.delete(skill);
    }
}
