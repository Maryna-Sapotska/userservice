package com.innowise.userservice.service;

import com.innowise.userservice.exception.BusinessException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.*;
import com.innowise.userservice.repository.CardRepository;
import com.innowise.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void createCard_shouldCreateSuccessfully() {
        CreateCardDto dto = new CreateCardDto();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Card card = new Card();
        Card savedCard = new Card();

        CardDTO cardDTO = new CardDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.countUserCards(1L)).thenReturn(1L);
        when(cardMapper.toEntity(dto)).thenReturn(card);
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);
        when(cardMapper.toDto(savedCard)).thenReturn(cardDTO);

        CardDTO result = cardService.create(dto);

        assertNotNull(result);

        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void createCard_shouldThrow_whenLimitExceeded() {
        CreateCardDto dto = new CreateCardDto();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.countUserCards(1L)).thenReturn(5L);

        assertThrows(BusinessException.class,
                () -> cardService.create(dto));

        verify(cardRepository).countUserCards(1L);
    }

    @Test
    void getById_shouldReturnCard() {

        Long id = 1L;

        Card card = new Card();
        card.setId(id);

        CardDTO dto = new CardDTO();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardMapper.toDto(card)).thenReturn(dto);

        CardDTO result = cardService.getById(id);

        assertNotNull(result);
    }

    @Test
    void update_shouldModifyCard() {

        Long id = 1L;

        UpdateCardDto dto = new UpdateCardDto();
        dto.setNumber("1234123412341234");
        dto.setHolder("JOHN DOE");
        dto.setExpirationDate(LocalDate.now().plusYears(1));

        Card card = new Card();
        card.setId(id);

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(new CardDTO());

        CardDTO result = cardService.update(id, dto);

        assertNotNull(result);
    }

    @Test
    void delete_shouldDeleteCard() {

        Long id = 1L;

        cardService.delete(id);

        verify(cardRepository).deleteById(id);
    }

    @Test
    void update_shouldActivateCard() {

        Card card = new Card();
        card.setActive(false);

        UpdateCardDto dto = new UpdateCardDto();
        dto.setActive(true);

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));
        when(cardRepository.save(card))
                .thenReturn(card);

        cardService.update(1L, dto);

        assertTrue(card.isActive());
        verify(cardRepository).save(card);
    }

    @Test
    void update_shouldDeactivateCard() {

        Card card = new Card();
        card.setActive(true);

        UpdateCardDto dto = new UpdateCardDto();
        dto.setActive(false);

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));

        when(cardRepository.save(card))
                .thenReturn(card);

        cardService.update(1L, dto);

        assertFalse(card.isActive());

        verify(cardRepository).save(card);
    }
}