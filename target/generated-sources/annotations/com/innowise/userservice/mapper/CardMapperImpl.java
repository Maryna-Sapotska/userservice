package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.CardDTO;
import com.innowise.userservice.model.dto.CreateCardDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-27T12:59:04+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.10 (Amazon.com Inc.)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardDTO toDto(Card card) {
        if ( card == null ) {
            return null;
        }

        CardDTO cardDTO = new CardDTO();

        cardDTO.setUserId( cardUserId( card ) );
        cardDTO.setId( card.getId() );
        cardDTO.setNumber( card.getNumber() );
        cardDTO.setHolder( card.getHolder() );
        cardDTO.setExpirationDate( card.getExpirationDate() );
        cardDTO.setActive( card.isActive() );

        return cardDTO;
    }

    @Override
    public List<CardDTO> toDtoList(List<Card> cards) {
        if ( cards == null ) {
            return null;
        }

        List<CardDTO> list = new ArrayList<CardDTO>( cards.size() );
        for ( Card card : cards ) {
            list.add( toDto( card ) );
        }

        return list;
    }

    @Override
    public Card toEntity(CreateCardDto dto) {
        if ( dto == null ) {
            return null;
        }

        Card card = new Card();

        card.setNumber( dto.getNumber() );
        card.setHolder( dto.getHolder() );
        card.setExpirationDate( dto.getExpirationDate() );

        return card;
    }

    private Long cardUserId(Card card) {
        if ( card == null ) {
            return null;
        }
        User user = card.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
