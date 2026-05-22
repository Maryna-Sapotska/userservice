package com.innowise.userservice.service;

import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.*;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing users.
 * Provides CRUD operations, filtering, caching,
 * and activation/deactivation functionality.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String USER_NOT_FOUND = "User not found";

    /**
     * Creates a new user.
     *
     * @param dto request object containing user data
     * @return created user DTO
     */
    public UserDTO create(CreateUserDto dto) {
        User user = userMapper.toEntity(dto);
        user.setActive(true);
        return userMapper.toDTO(userRepository.save(user));
    }

    /**
     * Retrieves user by id.
     *
     * @param id user identifier
     * @return user DTO
     * @throws UserNotFoundException if user does not exist
     */
    @Cacheable(value = CacheNames.USERS, key = "#id")
    public UserDTO getById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)));
    }

    /**
     * Returns paginated list of users with optional filters.
     *
     * @param name user name filter
     * @param surname user surname filter
     * @param active active status filter
     * @param pageable pagination information
     * @return page of users
     */
    public Page<UserDTO> getAll(String name, String surname, Boolean active, Pageable pageable) {

        return userRepository.findAll(Specification
                .where(UserSpecification
                        .hasName(name))
                .and(UserSpecification
                        .hasSurname(surname))
                .and(UserSpecification
                        .isActive(active)),
                pageable)
                .map(userMapper::toDTO);
    }

    /**
     * Updates user information.
     *
     * @param id user identifier
     * @param dto updated user data
     * @return updated user with cards
     */
    @Transactional
    @Caching(put = @CachePut(value = CacheNames.USERS, key = "#id"),
            evict = {
                    @CacheEvict(value = CacheNames.USERS_WITH_CARDS, key = "#id")
            })
    public UserWithCardsDto update(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getSurname() != null) {
            user.setSurname(dto.getSurname());
        }

        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        userRepository.save(user);

        User reloaded = userRepository.findByIdWithCards(id)
                .orElseThrow();
        return userMapper.toUserWithCardsDto(reloaded);
    }

    /**
     * Returns user with associated payment cards.
     *
     * @param id user identifier
     * @return user with cards DTO
     */
    @Transactional(readOnly = true)
    @Cacheable(value = CacheNames.USERS_WITH_CARDS, key = "#id")
    public UserWithCardsDto getUserWithCards(Long id) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return userMapper.toUserWithCardsDto(user);
    }

    /**
     * Deletes user by identifier.
     *
     * @param id user identifier
     * @throws UserNotFoundException if user does not exist
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USERS, key = "#id"),
            @CacheEvict(value = CacheNames.USERS_WITH_CARDS, key = "#id")
    })
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
