package com.nt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.nt.binding.COSummary;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.EligibilityDetailsEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.ICoTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IEligibilityDetermineRepositoty;

public class CorrespondenceMgmtServiceImpl implements ICorrespondenceMgmtService {
   @Autowired
	private ICoTriggerRepository triggerRepo;
   @Autowired
   private IEligibilityDetermineRepositoty elgiRepo;
   @Autowired
   private IDcCaseRepository caseRepo;
   
   private IApplicationRegistrationRepository citizenRepo;
   @Override
	public COSummary processPendingTriggers() {
		// TODO Auto-generated method stub
		CitizenAppRegistrationEntity citizenEntity=null;
		List<CoTriggersEntity> triggersList = triggerRepo.findByTriggerStatus("pending");
		
		for (CoTriggersEntity triggerEntity : triggersList) {
			
		EligibilityDetailsEntity elgiEntity= elgiRepo.findByCaseNo(triggerEntity.getCaseNo());
		
		Optional<DcCaseEntity> optCaseEntity = caseRepo.findById(triggerEntity.getCaseNo());
		
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity=optCaseEntity.get();
			Integer appId=caseEntity.getAppId();
		   Optional<CitizenAppRegistrationEntity> optCitizenEntity= citizenRepo.findById(appId);
		   if(optCaseEntity.isPresent()) {
			   citizenEntity=optCitizenEntity.get();
		   }
		}
		}
		return null;
	}

}
