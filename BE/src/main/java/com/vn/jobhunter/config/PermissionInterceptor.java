package com.vn.jobhunter.config;

import com.vn.jobhunter.domain.Permission;
import com.vn.jobhunter.domain.Role;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.service.UserService;
import com.vn.jobhunter.util.SecurityUtil;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;


public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin();
        if (email != null && !email.isEmpty()) {
            User user = this.userService.fetchByEmail(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));

                    if (isAllow == false) {
                        throw new InvalidException("Bạn không có quyền truy cập endpoint này.");
                    }
                } else {
                    throw new InvalidException("Bạn không có quyền truy cập endpoint này.");
                }
            }
        }

        return true;

    }
}