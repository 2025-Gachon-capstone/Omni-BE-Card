package org.example.omnibecard.repository;

import org.example.omnibecard.entity.CardBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {

    @Query("SELECT cb FROM CardBenefit cb WHERE cb.benefitId = :benefitId AND cb.status NOT IN ('EXPIRED', 'DELETED')")
    List<CardBenefit> findValidByBenefitId(@Param("benefitId") Long benefitId);

    List<CardBenefit> findAllByBenefitId(Long benefitId);

}
