package com.nt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.PlanData;
import com.nt.config.AppConfigProperties;
import com.nt.constants.PlanConstants;
import com.nt.entity.PlanCategory;
import com.nt.entity.PlanEntity;
import com.nt.repository.IPlanCategoryRepository;
import com.nt.repository.IPlanRepository;
@Service
public class PlanDataMgmtServiceImpl implements IPlanMgmtService {
	@Autowired
	private IPlanRepository PlanRepo;
	@Autowired
	private IPlanCategoryRepository PlanCategoryRepo;

	private Map<String,String> messages;
	@Autowired
	public PlanDataMgmtServiceImpl(AppConfigProperties props) {
		messages=props.getMessages();
	}

	@Override
	public String registerPlan(PlanData plan) {
		// TODO Auto-generated method stub
		PlanEntity entity = new PlanEntity();
		BeanUtils.copyProperties(plan, entity);
		PlanEntity saved =PlanRepo.save(entity);
		return saved.getPlanId()!=null?messages.get(PlanConstants.SAVE_SUCCESS)+saved.getPlanId():messages.get(PlanConstants.SAVE_FAILURE);
	}

	@Override
	public Map<Integer, String> getPlanCategories() {
		// TODO Auto-generated method stub
		List<PlanCategory> list= PlanCategoryRepo.findAll();
		
		  Map<Integer, String> categoriesMap = new HashMap<Integer, String>();
		  
		  list.forEach(Category->{ categoriesMap.put(Category.getCategoryId(),
		  Category.getCategoryName()); }); 
		  return categoriesMap;
		 
	
		
	}

	@Override
	public List<PlanData> showAllPlans() {
		// TODO Auto-generated method stub
	  List<PlanEntity> listEntities= PlanRepo.findAll();
	  List<PlanData> listPlanData= new ArrayList<PlanData>();
	  
	  listEntities.forEach(entity->{
		  PlanData data = new PlanData();
		  BeanUtils.copyProperties(entity, data);
		  listPlanData.add(data);
		  
	  });
	  return listPlanData;
	}

	@Override
	public PlanData showPlanById(Integer planId) {
		// TODO Auto-generated method stub
		/*
		 * Optional<PlanData> opt = PlanDataRepo.findById(planId);
		 * if(opt.isPresent()) { return opt.get(); }else { throw new
		 * IllegalArgumentException("plan id not found"); }
		 */
		PlanEntity entity = PlanRepo.findById(planId).orElseThrow(()-> new IllegalArgumentException(messages.get(PlanConstants.FIND_BY_ID_FAILURE)));
	
	     PlanData data = new PlanData();
	     BeanUtils.copyProperties(entity, data);
	     return data;
	}

	@Override
	public String updatePlan(PlanData plan) {
		// TODO Auto-generated method stub
		Optional<PlanEntity> optentity = PlanRepo.findById(plan.getPlanId());
		if(optentity.isPresent()) {
			PlanEntity entity = new PlanEntity();
			BeanUtils.copyProperties(plan,entity);
			PlanRepo.save(entity);
			return plan.getPlanId()+messages.get(PlanConstants.UPDATE_SUCCESS);
		}else {
			return messages.get(PlanConstants.UPDATE_FAILURE);
		}
		
	}

	@Override
	public String deletePlan(Integer planid) {
		// TODO Auto-generated method stub
		Optional<PlanEntity> opt = PlanRepo.findById(planid);
		if(opt.isPresent()) {
			PlanRepo.deleteById(planid);
			return planid+messages.get(PlanConstants.DELETE_SUCCESS);
		}else {
			return messages.get(PlanConstants.DELETE_FAILURE);
		}
	}

	@Override
	public String changePlanStatus(Integer planId, String status) {
		// TODO Auto-generated method stub
		Optional<PlanEntity> opt = PlanRepo.findById(planId);
		if(opt.isPresent()) {
		PlanEntity entity = opt.get();
		entity.setActiveSw(status);
		PlanRepo.save(entity);
		return planId+messages.get(PlanConstants.STATUS_CHANGE_SUCCESS);
	}else {
		return planId+messages.get(PlanConstants.STATUS_CHANGE_FAILURE);
	}


}
}
