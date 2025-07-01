package org.example.omnibecard.repository;

import org.example.omnibecard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);
    Boolean existsByMemberId(Long memberId);
    Optional<Card> findByMemberId(Long memberId);
    Optional<Card> findByCardNumber(String cardNumber);
    List<Card> findAllByMemberId(Long memberId);

}
