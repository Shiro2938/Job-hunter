package com.vn.jobhunter.controller;

import com.vn.jobhunter.domain.Request.ReqLoginDTO;
import com.vn.jobhunter.domain.Response.ResLoginDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.service.UserService;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final Converter converter;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          Converter converter,
                          UserService userService,
                          SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.converter = converter;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> getLogin(@RequestBody @Valid ReqLoginDTO loginDTO) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword()
        );

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = this.userService.fetchByEmail(loginDTO.getUsername());

        ResLoginDTO resLoginDTO = converter.toResLoginDTO(currentUser);

        String access_token = securityUtil.createAccessToken(currentUser.getEmail(), resLoginDTO);

        String refresh_token = securityUtil.createRefreshToken(currentUser.getEmail(), resLoginDTO);

        currentUser.setRefreshToken(refresh_token);

        resLoginDTO.setAccessToken(access_token);

        ResponseCookie resCookies = ResponseCookie.from(refresh_token)
                .maxAge(86400)
                .secure(true)
                .httpOnly(true)
                .path("/user/")
                .domain("example.com").build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(resLoginDTO);
    }
}
