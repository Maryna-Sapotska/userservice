package com.innowise.userservice.mapper;

import com.innowise.userservice.model.Card;
import com.innowise.userservice.model.CardDTO;
import com.innowise.userservice.model.CreateCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for converting Card entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "user.id", target = "userId")
    CardDTO toDto(Card card);

    List<CardDTO> toDtoList(List<Card> cards);

    @Mapping(target = "user", ignore = true)
    Card toEntity(CreateCardDto dto);
}
