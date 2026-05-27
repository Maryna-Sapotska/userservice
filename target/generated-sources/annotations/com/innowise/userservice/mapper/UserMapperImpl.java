package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.CreateUserDto;
import com.innowise.userservice.model.dto.UserDTO;
import com.innowise.userservice.model.dto.UserWithCardsDto;
import com.innowise.userservice.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-27T12:59:04+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.10 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private CardMapper cardMapper;

    @Override
    public User toEntity(CreateUserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setName( dto.getName() );
        user.setSurname( dto.getSurname() );
        user.setBirthDate( dto.getBirthDate() );
        user.setEmail( dto.getEmail() );

        return user;
    }

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );
        userDTO.setSurname( user.getSurname() );
        userDTO.setBirthDate( user.getBirthDate() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setActive( user.isActive() );

        return userDTO;
    }

    @Override
    public List<UserDTO> toDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public UserWithCardsDto toUserWithCardsDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserWithCardsDto userWithCardsDto = new UserWithCardsDto();

        userWithCardsDto.setId( user.getId() );
        userWithCardsDto.setName( user.getName() );
        userWithCardsDto.setSurname( user.getSurname() );
        userWithCardsDto.setBirthDate( user.getBirthDate() );
        userWithCardsDto.setEmail( user.getEmail() );
        userWithCardsDto.setActive( user.isActive() );
        userWithCardsDto.setCards( cardMapper.toDtoList( user.getCards() ) );

        return userWithCardsDto;
    }
}
