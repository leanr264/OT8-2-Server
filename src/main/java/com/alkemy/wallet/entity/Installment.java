package com.alkemy.wallet.entity;

import com.alkemy.wallet.enums.EInstallmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "installments")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "INSTALLMENT_NUMBER")
    private int installmentNumber;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private EInstallmentStatus status;

    @ManyToOne
    @JoinColumn(name="LOAN_ID", referencedColumnName = "ID")
    private Loan loan;
}
