package fr.it.exalt.infrastructure.entity;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jackpot")
public class JackpotEntity {

    @Id
    UUID customerId;

    BigDecimal amount;

    int checkoutNumber;

    @Column(unique = true)
    String email;

    public Jackpot toDomain() {
        return new Jackpot(customerId, amount, checkoutNumber, email);
    }

    public static JackpotEntity toEntity(Jackpot jackpot) {
        return JackpotEntity.builder()
                            .customerId(jackpot.customerId())
                            .amount(jackpot.amount())
                            .checkoutNumber(jackpot.checkoutNumber())
                            .email(jackpot.email())
                            .build();
    }
}
