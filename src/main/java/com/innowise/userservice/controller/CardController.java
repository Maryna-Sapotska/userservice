package com.innowise.userservice.controller;

import com.innowise.userservice.model.CardDTO;
import com.innowise.userservice.model.CreateCardDto;
import com.innowise.userservice.model.UpdateCardDto;
import com.innowise.userservice.model.UserWithCardsDto;
import com.innowise.userservice.service.CardService;
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
 * REST controller for payment card endpoints.
 */
@RestController
@RequestMapping(CardController.REST_URL)
@RequiredArgsConstructor
public class CardController {

    public static final String REST_URL = "/cards";

    private final CardService cardService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CardDTO> create(@Valid @RequestBody CreateCardDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getById(@PathVariable Long id){
        return ResponseEntity
                .ok(cardService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CardDTO>> getAll(
            @RequestParam(required = false) String holder,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        return ResponseEntity.ok(cardService.getAll(holder, active, pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDTO>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cardService.getByUserId(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CardDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody UpdateCardDto dto){
        return ResponseEntity.ok(cardService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
