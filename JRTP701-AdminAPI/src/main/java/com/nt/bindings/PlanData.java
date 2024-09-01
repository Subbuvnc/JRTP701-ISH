package com.nt.bindings;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class PlanData {

		private Integer planId;
		private String planName;
		private LocalDate startDate;
		private LocalDate endDate;
		private String description;
		private String activeSw;
        		
}
