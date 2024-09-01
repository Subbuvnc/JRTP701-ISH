package com.nt.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.EligibilityDetailsOutput;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.EligibilityDetailsEntity;
import com.nt.entity.PlanEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.ICoTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IDcChildrenRepository;
import com.nt.repository.IDcEducationRepository;
import com.nt.repository.IDcIncomeRepository;
import com.nt.repository.IEligibilityDetermineRepositoty;
import com.nt.repository.IPlanRepository;
@Service
public class EligibilityDeterminationMgmtServiceImpl implements IEligibilityDeterminationMgmtService {

	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcIncomeRepository incomeRepo;
	@Autowired
	private IDcChildrenRepository childrenRepo;
	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
	@Autowired
	private IDcEducationRepository educationRepo;
	@Autowired
	private IEligibilityDetermineRepositoty egRepo;
	@Autowired
	private ICoTriggerRepository triggerRepo;
	@Override
	public EligibilityDetailsOutput determineEligibility(int caseNo) {
		// TODO Auto-generated method stub
		Integer appId=null;
		Integer planId=null;
		
		Optional<DcCaseEntity> optCaseEntity=caseRepo.findById(caseNo);
		if(optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			planId=caseEntity.getPlanId();
			appId=caseEntity.getAppId();
		}
		String planName=null;
		Optional<PlanEntity> optPlanEntity=planRepo.findById(planId);
		if(optPlanEntity.isPresent()) {
			PlanEntity planEntity=optPlanEntity.get();
			planName=planEntity.getPlanName();
		}
		int citizenAge=0;
		String citizenName=null;
		Optional<CitizenAppRegistrationEntity> optCitizenEntity = citizenRepo.findById(appId);
		if(optCitizenEntity.isPresent()) {
			CitizenAppRegistrationEntity citizenEntity = optCitizenEntity.get();
			LocalDate citizenDOB=citizenEntity.getDob();
			citizenName=citizenEntity.getFullNmae();
			LocalDate sysDate=LocalDate.now();
			citizenAge=Period.between(citizenDOB, sysDate).getYears();
					}
		EligibilityDetailsOutput elgiOutput = applyPlanCondition(caseNo,planName,citizenAge);
		
		elgiOutput.setHolderName(citizenName);
		EligibilityDetailsEntity elgiEntity = new EligibilityDetailsEntity();
		BeanUtils.copyProperties(elgiOutput, elgiEntity);
		egRepo.save(elgiEntity);
		
		CoTriggersEntity triggersEntity = new CoTriggersEntity();
		triggersEntity.setCaseNo(caseNo);
		triggersEntity.setTriggerStatus("Pending");
		
		triggerRepo.save(triggersEntity);
		return elgiOutput;
	}
	private EligibilityDetailsOutput applyPlanCondition(Integer caseNo, String planName,int citizenAge) {
		// TODO Auto-generated method stub
		EligibilityDetailsOutput elgiOutput = new EligibilityDetailsOutput();
		elgiOutput.setPlanName(planName);
		
		DcIncomeEntity incomeEntity=incomeRepo.findByCaseNo(caseNo);
		double empIncome=incomeEntity.getEmpIncome();
		double propertyIncome=incomeEntity.getPropertyIncome();
		if (planName.equalsIgnoreCase("SNAP")) {
			if(empIncome<=300) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(200.0);
			}
			else {
				elgiOutput.setPlanStatus(planName);
				elgiOutput.setDenialReason("HighIncome");
			}
			
		}
		else if(planName.equalsIgnoreCase("CCAP")) {
			boolean kidsCountCondition=false;
			boolean kidsAgeCondition=true;
			
			List<DcChildrenEntity> listChilds = childrenRepo.findByCaseNo(caseNo);
			if (!listChilds.isEmpty()) {
				kidsCountCondition=true;
			
			for(DcChildrenEntity child:listChilds) {
				int kidAge=Period.between(child.getChildDOB(),LocalDate.now()).getYears();
						if(kidAge>16) {
							kidsAgeCondition=false;
				break;
						}
			}
			}
			if(empIncome<=300&&kidsCountCondition&&kidsCountCondition) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(300.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("CCAP rules are not satisfied");
			}
			
		}
		else if(planName.equalsIgnoreCase("MEDCARE")) {
			
			if(citizenAge>=65) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(350.0);
				
			}else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("MEDCARE rules are not satisfied");
			}
		}
		else if(planName.equalsIgnoreCase("MEDAID")) {
			if(empIncome<=300&&propertyIncome==0) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(200.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("MEDAID rules are not satisfied");
			}
			
		}
		else if(planName.equalsIgnoreCase("CAJW")) {
			
			DcEducationEntity EducationEntity = educationRepo.findByCaseNo(caseNo);
		    int passOutYear=EducationEntity.getPassOutYear();
		    if(empIncome==0&&passOutYear<LocalDate.now().getYear()) {
		    	elgiOutput.setPlanStatus("Approved");
		    	elgiOutput.setBenifitAmt(300.0);
		    }
		    else {
		    	elgiOutput.setPlanStatus("Denied");
		    	elgiOutput.setDenialReason("rules are not satisfied");
		    }
		
		}
		else if(planName.equalsIgnoreCase("QHP")) {
			if(citizenAge>=1) {
				elgiOutput.setPlanStatus("Approved");
			}else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("QHP rules are not satisfied");
			}
		}
		if(elgiOutput.getPlanStatus().equalsIgnoreCase("Approved")) {
		
		elgiOutput.setPlanStartDate(LocalDate.now());
		elgiOutput.setPlanEndDate(LocalDate.now().plusYears(2));
		}
		return elgiOutput;
	}

}
