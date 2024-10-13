package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.Response.User.ResCreateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUpdateUserDTO;
import com.vn.jobhunter.domain.Response.User.ResUserDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.service.UserService;
import com.vn.jobhunter.util.annotation.APIMessage;
import com.vn.jobhunter.util.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    @APIMessage("Create user successful!")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid User user) throws InvalidException {

        //handle create User
        ResCreateUserDTO createdUser = this.userService.handleCreate(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/users")
    @APIMessage("Update user by ID successful!")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody @Valid User user) throws InvalidException {

        //handle update User
        ResUpdateUserDTO updatedUser = this.userService.handleUpdate(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/{id}")
    @APIMessage("Get user by ID successful!")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable long id) throws InvalidException {

        //find User by ID
        ResUserDTO resUserDTO = this.userService.findById(id);
        return ResponseEntity.ok(resUserDTO);
    }

    @GetMapping("/users")
    @APIMessage("Get all users successful!")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            Pageable pageable, @Filter Specification<User> specificationUser
    ) throws InvalidException {

        // find all
        ResultPaginationDTO paginateUsers = this.userService.findAll(pageable, specificationUser);
        return ResponseEntity.ok(paginateUsers);
    }

    @DeleteMapping("/users/{id}")
    @APIMessage("Delete user by ID successful!")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws InvalidException {
        this.userService.deleteById(id);
        System.out.println(">>>>>>>" + id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
