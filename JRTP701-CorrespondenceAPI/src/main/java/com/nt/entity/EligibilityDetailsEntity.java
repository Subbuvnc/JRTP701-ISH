package com.nt.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "JR701_ELIGIBILITY_DETERMINATION")
@Data

public class EligibilityDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer edTraceId;
    private Integer caseNo; 
    @Column(length = 30)
    private String holderName;
    private Long holderSSN;
    @Column(length = 30)
    private String planName;
    @Column(length = 30)
    private String planStatus;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private Double benifitAmt;
    @Column(length = 30)
    private String denialReason;
    
    
    
}
