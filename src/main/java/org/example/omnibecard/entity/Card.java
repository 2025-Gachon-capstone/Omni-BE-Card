package org.example.omnibecard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.omnibecard.entity.base.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Card")
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardPassword;

    @Column(nullable = false)
    private String securityCode;

    private LocalDateTime expiredDate;

    @Version
    private Long version;

}
