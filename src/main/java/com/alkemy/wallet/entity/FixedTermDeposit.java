package com.alkemy.wallet.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "fixed_term_deposits")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "INTEREST", nullable = false)
    private Double interest;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "CLOSING_DATE")
    private Timestamp closingDate;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;

}