package com.vn.jobhunter.service;

import com.vn.jobhunter.domain.Response.ResCreateUserDTO;
import com.vn.jobhunter.domain.Response.ResUpdateUserDTO;
import com.vn.jobhunter.domain.Response.ResUserDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.repository.UserRepository;
import com.vn.jobhunter.util.Converter;
import com.vn.jobhunter.util.error.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Converter converter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, Converter converter,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
    }

    public User fetchByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public ResCreateUserDTO handleCreate(User user) throws InvalidException {
        //handle create exception

        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidException("User is existing");
        }
        //encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User createdUser = this.userRepository.save(user);

        return this.converter.toResCreateUserDTO(createdUser);
    }

    public ResUpdateUserDTO handleUpdate(User user) throws InvalidException {

        if (!this.userRepository.existsById(user.getId())) {
            throw new InvalidException("User is not existing");
        }

        User userInDB = this.userRepository.findById(user.getId()).get();

        //mapping
        userInDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userInDB.setName(user.getName());
        userInDB.setAddress(user.getAddress());
        userInDB.setAge(user.getAge());
        userInDB.setGender(user.getGender());

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

        return this.converter.toResultPaginationDTO(userPage);
    }

    public void deleteById(long id) throws InvalidException {
        if (this.userRepository.findById(id).isPresent()) {
            this.userRepository.deleteById(id);
        } else throw new InvalidException("User not found");
    }
}
