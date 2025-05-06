package org.example.omnibecard.repository;

import org.example.omnibecard.entity.CardBenefit;
import org.example.omnibecard.entity.type.CardBenefitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {

    @Query("SELECT cb FROM CardBenefit cb WHERE cb.benefitId = :benefitId AND cb.status NOT IN ('EXPIRED', 'DELETED')")
    List<CardBenefit> findValidByBenefitId(@Param("benefitId") Long benefitId);

    List<CardBenefit> findAllByBenefitId(Long benefitId);

    List<CardBenefit> findByBenefitIdAndStatusIn(Long benefitId, List<CardBenefitStatus> statuses);

    Page<CardBenefit> findByCard_CardId(Long cardId, Pageable pageable);

    List<CardBenefit> findByCard_CardIdAndStatus(Long cardId, CardBenefitStatus status);

    boolean existsByBenefitId(Long benefitId);

}
