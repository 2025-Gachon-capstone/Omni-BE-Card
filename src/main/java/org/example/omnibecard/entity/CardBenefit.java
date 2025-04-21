package org.example.omnibecard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.omnibecard.entity.base.BaseEntity;
import org.example.omnibecard.entity.type.CardBenefitStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CardBenefit")
public class CardBenefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardBenefitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private Long benefitId;

    @Enumerated(EnumType.STRING)
    private CardBenefitStatus status;

    @Version
    private Long version;

}
