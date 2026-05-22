package com.innowise.userservice.service;

import com.innowise.userservice.exception.BusinessException;
import com.innowise.userservice.exception.CardNotFoundException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.*;
import com.innowise.userservice.repository.CardRepository;
import com.innowise.userservice.repository.CardSpecification;
import com.innowise.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing payment cards.
 * Contains CRUD operations and business validations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    private static final int MAX_CARDS = 5;

    private static final String MAX_CARDS_ALLOWED = "Max 5 cards allowed";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String CARD_NOT_FOUND = "Card not found";

    /**
     * Creates a new payment card for user.
     * One user can own maximum 5 cards.
     *
     * @param dto card creation request
     * @return created card DTO
     * @throws BusinessException if card limit exceeded
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_WITH_CARDS, key = "#dto.userId"),
            @CacheEvict(value = CacheNames.CARDS, allEntries = true)
    })
    public CardDTO create(CreateCardDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        long cardsCount = cardRepository.countUserCards(dto.getUserId());

        if (cardsCount >= MAX_CARDS) {
            throw new BusinessException(MAX_CARDS_ALLOWED);
        }

        Card card = cardMapper.toEntity(dto);

        if (card == null) {
            throw new BusinessException("Card mapping failed");
        }

        card.setUser(user);
        card.setActive(true);

        Card savedCard = cardRepository.save(card);

        return cardMapper.toDto(savedCard);
    }

    /**
     * Retrieves card by identifier.
     *
     * @param id card identifier
     * @return card DTO
     */
    @Cacheable(value = CacheNames.CARDS, key = "#id")
    public CardDTO getById(Long id) {
        return cardMapper.toDto(cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(CARD_NOT_FOUND)));
    }

    /**
     * Returns all cards belonging to user.
     *
     * @param userId user identifier
     * @return list of card DTOs
     */
    public List<CardDTO> getByUserId(Long userId) {
        return cardMapper.toDtoList(cardRepository
                .findByUser_Id(userId));
    }

    /**
     * Updates payment card information.
     *
     * @param id card identifier
     * @param dto updated card data
     * @return updated card DTO
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS_WITH_CARDS, key = "#dto.userId"),
            @CacheEvict(value = CacheNames.CARDS, key = "#id")
    })
    public CardDTO update(Long id, UpdateCardDto dto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(CARD_NOT_FOUND));

        if (dto.getNumber() != null) {
            card.setNumber(dto.getNumber());
        }

        if (dto.getHolder() != null) {
            card.setHolder(dto.getHolder());
        }

        if (dto.getExpirationDate() != null) {
            card.setExpirationDate(dto.getExpirationDate());
        }

        if (dto.getActive() != null) {
            card.setActive(dto.getActive());
        }

        return cardMapper.toDto(cardRepository.save(card));
    }

    /**
     * Deletes card by identifier.
     *
     * @param id card identifier
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.CARDS, key = "#id"),
            @CacheEvict(value = CacheNames.USERS_WITH_CARDS, allEntries = true)
    })
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    /**
     * Returns paginated list of cards with optional filters.
     *
     * @param holder card holder filter
     * @param active active status filter
     * @param pageable pagination information
     * @return page of card DTOs
     */
    public Page<CardDTO> getAll(String holder, Boolean active, Pageable pageable) {
        return cardRepository.findAll(Specification
                .where(CardSpecification
                        .hasHolder(holder))
                .and(CardSpecification
                        .isActive(active)),
                pageable)
                .map(cardMapper::toDto);
    }
}
