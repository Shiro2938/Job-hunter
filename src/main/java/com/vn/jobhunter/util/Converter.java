package com.vn.jobhunter.util;

import com.vn.jobhunter.domain.Response.*;
import com.vn.jobhunter.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Converter {
    private final ModelMapper modelMapper;

    public Converter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ResCreateUserDTO toResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUser = modelMapper.map(user, ResCreateUserDTO.class);
        return resCreateUser;
    }

    public ResUserDTO toResUserDTO(User user) {
        ResUserDTO resUser = modelMapper.map(user, ResUserDTO.class);
        return resUser;
    }

    public ResUpdateUserDTO toResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUpdateUser = modelMapper.map(user, ResUpdateUserDTO.class);

        return resUpdateUser;
    }

    public ResLoginDTO toResLoginDTO(User user) {
        ResLoginDTO.UserLogin userLogin = modelMapper.map(user, ResLoginDTO.UserLogin.class);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setUser(userLogin);
        return resLoginDTO;
    }

    public ResultPaginationDTO toResultPaginationDTO(Page<User> userPage) {

        ResultPaginationDTO resultPaginationDTO = this.initResultPagination(userPage);
        List<ResUserDTO> resUserDTO = userPage.getContent().stream()
                .map(user -> this.toResUserDTO(user)).toList();
        resultPaginationDTO.setResult(resUserDTO);


        return resultPaginationDTO;
    }

    public <T> ResultPaginationDTO initResultPagination(Page<T> objectPage) {

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        int page = objectPage.getNumber() + 1;
        int pageSize = objectPage.getSize();
        int pages = objectPage.getTotalPages();
        long total = objectPage.getTotalElements();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta(page, pageSize, pages, total);
        resultPaginationDTO.setMeta(meta);

        return resultPaginationDTO;
    }

    public ResLoginDTO.UserGetAccount toResGetAccountDTO(User user) {
        ResLoginDTO.UserLogin userLogin = modelMapper.map(user, ResLoginDTO.UserLogin.class);

        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        userGetAccount.setUser(userLogin);

        return userGetAccount;
    }
}
