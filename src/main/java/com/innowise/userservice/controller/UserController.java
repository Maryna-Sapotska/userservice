package com.innowise.userservice.controller;

import com.innowise.userservice.model.*;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management endpoints.
 */
@RestController
@RequestMapping(UserController.REST_URL)
@RequiredArgsConstructor
public class UserController {

    public static final String REST_URL = "/users";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .ok(userService.getById(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserWithCardsDto> getUserWithCards(@PathVariable Long userId){
        return ResponseEntity
                .ok(userService.getUserWithCards(userId));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        return ResponseEntity
                .ok(userService.getAll(name, surname, active, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserWithCardsDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateUserDto dto) {
        return ResponseEntity
                .ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
