package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardResDto;
import org.springframework.data.domain.Pageable;

public interface CardAdminService {

    CardResDto.GetCardForAdminPage getCardForAdmin(Pageable pageable);
    CardResDto.GetCardDetailForAdmin getCardDetailForAdmin(Long memberId);

}
