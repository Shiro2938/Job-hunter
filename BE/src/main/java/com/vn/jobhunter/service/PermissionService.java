package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Permission;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.repository.PermissionRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final Converter converter;

    public PermissionService(PermissionRepository permissionRepository, Converter converter) {
        this.permissionRepository = permissionRepository;
        this.converter = converter;
    }

    public boolean existsByApiPathAndMethodAndModule(Permission permission) {
        return this.permissionRepository.
                findByApiPathAndMethodAndModule(permission.getApiPath(),
                        permission.getMethod(), permission.getModule()) != null;
    }

    public Permission handleCreate(Permission permission) {
        // check existing

        if (this.existsByApiPathAndMethodAndModule(permission))
            throw new IllegalArgumentException("Api path and Method and Module already exists");

        return this.permissionRepository.save(permission);
    }

    public Permission findById(long id) {
        return this.permissionRepository.findById(id).orElse(null);
    }

    public Permission handleUpdate(Permission permission) throws InvalidException {
        // check not found
        Permission permissionInDB = this.findById(permission.getId());

        if (permissionInDB == null) throw new InvalidException("Permission not found");

        // check already exist
        if (this.existOther(permission)) throw new InvalidException("Permission already exists");

        //mapping
        this.converter.mapObject(permission, permissionInDB);

        return this.permissionRepository.save(permissionInDB);
    }

    public boolean existOther(Permission permission) {
        Permission otherPermission = this.permissionRepository.
                findByApiPathAndMethodAndModuleAndName(permission.getApiPath(),
                        permission.getMethod(),
                        permission.getModule(),
                        permission.getName());
        return otherPermission != null && otherPermission.getId() != permission.getId();
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<Permission> spec) {
        Page<Permission> permissions = this.permissionRepository.findAll(spec, pageable);

        ResultPaginationDTO resultPaginationDTO = this.converter.toDefaultResultPaginationDTO(permissions);

        return resultPaginationDTO;
    }

    public void handleDeleteById(long id) {

        //remove permission  in role
        Permission permission = this.findById(id);
        if (permission == null) throw new IllegalArgumentException("Permission not found");
        permission.getRoles().forEach(role -> role.getPermissions().remove(permission));

        this.permissionRepository.deleteById(id);
    }
}
