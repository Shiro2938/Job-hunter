package com.vn.jobhunter.util;

import com.vn.jobhunter.domain.Response.ResLoginDTO;
import com.vn.jobhunter.domain.Response.ResUserDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
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

    public ResUserDTO toResCreateUserDTO(User user) {
        ResUserDTO resCreateUser = modelMapper.map(user, ResUserDTO.class);
        return resCreateUser;
    }

    public ResUserDTO toResUserDTO(User user) {
        ResUserDTO resUser = modelMapper.map(user, ResUserDTO.class);
        return resUser;
    }

    public ResUserDTO toResUpdateUserDTO(User user) {
        ResUserDTO resUpdateUser = modelMapper.map(user, ResUserDTO.class);

        resUpdateUser.setCreatedAt(null);
        resUpdateUser.setCreatedBy(null);

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
}
