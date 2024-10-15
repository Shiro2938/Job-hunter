package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Permission;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.service.PermissionService;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = this.permissionService.handleCreate(permission);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws InvalidException {
        Permission updatedPermission = this.permissionService.handleUpdate(permission);

        return ResponseEntity.ok(updatedPermission);
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResultPaginationDTO> findAllPermissions(Pageable pageable, @Filter Specification<Permission> specification) {

        ResultPaginationDTO resultPaginationDTO = this.permissionService.findAll(pageable, specification);

        return ResponseEntity.ok(resultPaginationDTO);

    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        this.permissionService.handleDeleteById(id);

        return ResponseEntity.ok(null);
    }
}
