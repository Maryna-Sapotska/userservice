package com.innowise.userservice.mapper;

import com.innowise.userservice.model.CreateUserDto;
import com.innowise.userservice.model.User;
import com.innowise.userservice.model.UserDTO;
import com.innowise.userservice.model.UserWithCardsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for converting User entities and DTOs.
 */
@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface UserMapper {

    User toEntity(CreateUserDto dto);

    UserDTO toDTO(User user);

    List<UserDTO> toDtoList(List<User> users);

    UserWithCardsDto toUserWithCardsDto(User user);
}
