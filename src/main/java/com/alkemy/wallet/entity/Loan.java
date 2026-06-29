package com.alkemy.wallet.entity;

import com.alkemy.wallet.enums.ELoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "MONTHS", nullable = false)
    private int months;

    @Enumerated(EnumType.STRING)
    private ELoanStatus status;

    @CreationTimestamp
    @Column(name = "LOAN_DATE")
    private Timestamp LoanDate;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Installment> installments;
}
