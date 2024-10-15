package com.vn.jobhunter.repository;

import com.vn.jobhunter.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface PermissionRepository extends JpaRepository<Permission, Long>,
        JpaSpecificationExecutor<Permission> {
    public Permission findByApiPathAndMethodAndModule(String apiPath, String method, String module);

    public Permission findByApiPathAndMethodAndModuleAndName(String apiPath, String method, String module, String name);
}
