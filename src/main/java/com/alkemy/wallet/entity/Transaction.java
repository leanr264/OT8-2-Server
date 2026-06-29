package com.alkemy.wallet.entity;

import com.alkemy.wallet.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ETransactionType type;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;
}
