package org.example.omnibecard.repository;

import org.example.omnibecard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);
    Boolean existsByMemberId(Long memberId);

}
