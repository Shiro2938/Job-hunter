package com.vn.jobhunter.controller;

import com.vn.jobhunter.domain.Request.ReqLoginDTO;
import com.vn.jobhunter.domain.Response.ResCreateUserDTO;
import com.vn.jobhunter.domain.Response.ResLoginDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.service.UserService;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.SecurityUtil;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {


    private final Converter converter;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    public AuthController(
            Converter converter,
            UserService userService,
            SecurityUtil securityUtil) {

        this.converter = converter;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> getLogin(@RequestBody @Valid ReqLoginDTO loginDTO) throws InvalidException {

        //authenticate
        this.userService.authenticate(loginDTO);

        //add token
        User currentUser = this.userService.fetchByEmail(loginDTO.getUsername());
        ResLoginDTO resLoginDTO = this.userService.addToken(currentUser);

        //cookie
        String resCookies = this.userService.createCookieRefreshToken(loginDTO.getUsername(), currentUser.getRefreshToken(),
                this.securityUtil.getRefreshTokenExpiration());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin();
        System.out.println(">>>>>>>>>" + email);
        ResLoginDTO.UserGetAccount account = this.userService.getAccountByEmail(email);

        return ResponseEntity.ok(account);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User user) throws InvalidException {
        ResCreateUserDTO resCreateUserDTO = this.userService.handleCreate(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        String email = this.securityUtil.getCurrentUserLogin();

        //delete refreshToken
        this.userService.updateToken(email, null);

        //delete cookie
        String deletedCookie = this.userService.createCookieRefreshToken(email, null, 0);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, deletedCookie.toString())
                .body(null);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) throws InvalidException {
        String email = this.securityUtil.checkValidRefreshToken(refreshToken).getSubject();

        User user = this.userService.findByEmailAndRefreshToken(email, refreshToken);

        if (user == null) throw new InvalidException("Refresh Token không hợp lệ");

        // create new token
        ResLoginDTO resLoginDTO = this.userService.addToken(user);

        //cookie
        String resCookies = this.userService.createCookieRefreshToken(email, user.getRefreshToken(),
                this.securityUtil.getRefreshTokenExpiration());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(resLoginDTO);
    }
}
