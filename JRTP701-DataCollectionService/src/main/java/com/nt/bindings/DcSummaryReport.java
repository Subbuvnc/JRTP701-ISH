package com.nt.bindings;

import java.util.List;

import lombok.Data;
@Data
public class DcSummaryReport {

	private PlanSelectionInputs planDetails;
	private EducationInputs eduDetails;
	private  List<ChildInputs> childrenDetails;
	private IncomeInputs incomeDeatils;
	private CitizenAppRegistrationInputs citizenDetails;
	private String planName;
	
}
