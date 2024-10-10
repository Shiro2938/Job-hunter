package com.vn.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.jobhunter.domain.Response.ResUserDTO;
import com.vn.jobhunter.domain.Response.ResultPaginationDTO;
import com.vn.jobhunter.domain.User;
import com.vn.jobhunter.service.UserService;
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
    public ResponseEntity<ResUserDTO> createUser(@RequestBody @Valid User user) throws InvalidException {

        //handle create User
        ResUserDTO createdUser = this.userService.handleCreate(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/users")
    public ResponseEntity<ResUserDTO> updateUser(@RequestBody @Valid User user) throws InvalidException {

        //handle update User
        ResUserDTO updatedUser = this.userService.handleUpdate(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable long id) throws InvalidException {

        //find User by ID
        ResUserDTO resUserDTO = this.userService.findById(id);
        return ResponseEntity.ok(resUserDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            Pageable pageable, @Filter Specification<User> specificationUser
    ) throws InvalidException {

        // find all
        ResultPaginationDTO paginateUsers = this.userService.findAll(pageable, specificationUser);
        return ResponseEntity.ok(paginateUsers);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws InvalidException {
        this.userService.deleteById(id);
        System.out.println(">>>>>>>" + id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
