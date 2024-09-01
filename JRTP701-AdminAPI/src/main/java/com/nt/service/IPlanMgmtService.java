package com.nt.service;

import java.util.List;
import java.util.Map;

import com.nt.bindings.PlanData;

public interface IPlanMgmtService {
	
	public String registerPlan(PlanData plan);
	public Map<Integer,String> getPlanCategories();
	public List<PlanData> showAllPlans();
	public PlanData showPlanById(Integer planId);
    public String updatePlan(PlanData plan);
    public String deletePlan(Integer planid);
    public String changePlanStatus(Integer planId,String status);
}
