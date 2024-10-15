package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Role;
import com.vn.jobhunter.repository.RoleRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Converter converter;

    public RoleService(RoleRepository roleRepository, Converter converter) {
        this.roleRepository = roleRepository;
        this.converter = converter;
    }

    public Role handleCreate(Role role) throws InvalidException {
        // check existing name
        if (this.existByName(role.getName())) throw new InvalidException("Role name is existing");

        // handle create
        Role createdRole = this.roleRepository.save(role);

        return createdRole;
    }

    public boolean existByName(String name) {
        return this.roleRepository.findByName(name) != null;
    }

    public boolean existOtherByName(Role role) {
        Role otherRole = this.roleRepository.findByName(role.getName());
        if (otherRole == null) return false;
        else {
            return otherRole.getId() != role.getId();
        }
    }

    public Role handleUpdated(Role role) throws InvalidException {
        // check not found
        Role roleInDB = this.roleRepository.findById(role.getId()).orElse(null);
        if (roleInDB == null) throw new InvalidException("Role not found");

        //name exist in other role
        if (this.existOtherByName(role)) throw new InvalidException("Role name already exists");

        // mapping
        this.converter.mapObject(role, roleInDB);

        return this.roleRepository.save(roleInDB);
    }

    public Role findById(long id) throws InvalidException {
        Role role = this.roleRepository.findById(id).orElse(null);

        if (role == null) throw new InvalidException("Role not found");

        return role;
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<Role> specification) {
        Page<Role> roles = this.roleRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = this.converter.toDefaultResultPaginationDTO(roles);

        return resultPaginationDTO;
    }
}
