package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Company;
import com.vn.jobhunter.domain.Request.ReqLoginDTO;
import com.vn.jobhunter.domain.Response.Auth.ResLoginDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Response.User.ResCreateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUpdateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUserDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.repository.CompanyRepository;
import com.vn.jobhunter.repository.UserRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.SecurityUtil;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Converter converter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, Converter converter,
                       PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                       CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.companyRepository = companyRepository;
    }

    public User fetchByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public ResCreateUserDTO handleCreate(User user) throws InvalidException {
        //handle create exception

        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidException("User is existing");
        }

        //check company valid
        Company company = this.companyRepository.findById(user.getCompany().getId()).orElse(null);
        if (company == null)
            throw new InvalidException("Company not found");

        //encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCompany(company);

        User createdUser = this.userRepository.save(user);

        return this.converter.toResCreateUserDTO(createdUser);
    }

    public ResUpdateUserDTO handleUpdate(User user) throws InvalidException {

        if (!this.userRepository.existsById(user.getId())) {
            throw new InvalidException("User is not existing");
        }

        //check company exist
        Company company = this.companyRepository.findById(user.getCompany().getId()).orElse(null);
        if (company == null)
            throw new InvalidException("Company not found");

        User userInDB = this.userRepository.findById(user.getId()).get();

        //mapping
        userInDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userInDB.setName(user.getName());
        userInDB.setAddress(user.getAddress());
        userInDB.setAge(user.getAge());
        userInDB.setGender(user.getGender());
        userInDB.setCompany(company);

        this.userRepository.save(userInDB);

        return this.converter.toResUpdateUserDTO(userInDB);
    }

    public ResUserDTO findById(Long id) throws InvalidException {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new InvalidException("User not found");
        }

        ResUserDTO resUserDTO = converter.toResUserDTO(user);

        return resUserDTO;
    }

    public ResultPaginationDTO findAll(Pageable pageable, Specification<User> spec) throws InvalidException {
        Page<User> userPage = this.userRepository.findAll(spec, pageable);

        return this.converter.toResultPaginationUserDTO(userPage);
    }

    public void deleteById(long id) throws InvalidException {
        if (this.userRepository.findById(id).isPresent()) {
            this.userRepository.deleteById(id);
        } else throw new InvalidException("User not found");
    }

    public ResLoginDTO.UserGetAccount getAccountByEmail(String email) {
        User user = this.fetchByEmail(email);

        return this.converter.toResGetAccountDTO(user);
    }

    public void authenticate(ReqLoginDTO loginDTO) throws InvalidException {
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
    }

    public ResLoginDTO addToken(User currentUser) throws InvalidException {

        ResLoginDTO resLoginDTO = converter.toResLoginDTO(currentUser);

        String access_token = securityUtil.createAccessToken(currentUser.getEmail(), resLoginDTO);

        String refresh_token = securityUtil.createRefreshToken(currentUser.getEmail(), resLoginDTO);

        currentUser.setRefreshToken(refresh_token);

        resLoginDTO.setAccessToken(access_token);

        this.userRepository.save(currentUser);
        return resLoginDTO;
    }

    public User updateToken(String email, String token) {
        User user = this.userRepository.findByEmail(email);
        user.setRefreshToken(token);

        return user;
    }

    public String createCookieRefreshToken(String email, String value, long age) {
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", value)
                .maxAge(age)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        return resCookies.toString();
    }

    public User findByEmailAndRefreshToken(String email, String refreshToken) {

        return this.userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }
}
