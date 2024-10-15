package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Role;
import com.vn.jobhunter.service.RoleService;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody @Valid Role role) throws InvalidException {
        Role createdRole = this.roleService.handleCreate(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody @Valid Role role) throws InvalidException {
        Role updatedRole = this.roleService.handleUpdated(role);

        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable long id) throws InvalidException {
        Role role = this.roleService.findById(id);

        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(Pageable pageable,
                                                           @Filter Specification<Role> specification) throws InvalidException {
        ResultPaginationDTO resultPaginationDTO = this.roleService.findAll(pageable, specification);

        return ResponseEntity.ok(resultPaginationDTO);
    }


    @DeleteMapping("/roles/{id}")

}
