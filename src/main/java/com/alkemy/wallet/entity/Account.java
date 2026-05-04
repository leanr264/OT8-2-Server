package com.alkemy.wallet.entity;

import com.alkemy.wallet.enums.ECurrency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="CURRENCY", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    @Column(name="TRANSACTION_LIMIT", nullable=false)
    private Double transactionLimit;

    @Column(name="BALANCE", nullable = false)
    private Double balance;

    @CreationTimestamp
    @Column(name="CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name="UPDATE_DATE")
    private Timestamp updateDate;


    @Column(name="SOFT_DELETE")
    private Boolean softDelete;


    @ManyToOne
    @JoinColumn(name="USER_ID", referencedColumnName = "ID")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account")
    private List<FixedTermDeposit> fixedTermDeposits;

    @OneToMany(mappedBy = "account")
    private List<Loan> loans;
}