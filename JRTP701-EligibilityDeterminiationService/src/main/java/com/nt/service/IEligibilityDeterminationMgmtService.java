package com.nt.service;

import com.nt.bindings.EligibilityDetailsOutput;

public interface IEligibilityDeterminationMgmtService {

	public EligibilityDetailsOutput determineEligibility(int caseNo);
}
