package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardReqDto;


public interface CardService {

    void createCard(CardReqDto.CreateCard createCardDto);
    void verifyCard(String loginId, String cardPassword);
}
