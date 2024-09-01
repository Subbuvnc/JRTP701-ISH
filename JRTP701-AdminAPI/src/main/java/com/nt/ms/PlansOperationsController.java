package com.nt.ms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.PlanData;
import com.nt.service.IPlanMgmtService;


@RestController
@RequestMapping("/plan-api")
public class PlansOperationsController {
    @Autowired
	private IPlanMgmtService planService;
	@GetMapping("/categories")
    public ResponseEntity<Map<Integer, String>> showPlanCategories(){
    	
    		Map<Integer, String> mapCategories=planService.getPlanCategories();
    	    return new ResponseEntity<Map<Integer,String>>(mapCategories,HttpStatus.OK);
    	
    }
	
	@PostMapping("/register")
	public ResponseEntity<String> savePlan(@RequestBody PlanData plan){
		
			String msg = planService.registerPlan(plan);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		
	}
	@GetMapping("/all")
	public ResponseEntity<List<PlanData>> getAllPlans(){
		
		List<PlanData> list= planService.showAllPlans();
		return new ResponseEntity<List<PlanData>>(list,HttpStatus.OK);
	}

	
	@GetMapping("/find/{planId}")
	public ResponseEntity<?> getTravelPlanById(@PathVariable Integer planId){
		
			PlanData plan = planService.showPlanById(planId);
			return new ResponseEntity<PlanData>(plan,HttpStatus.OK);
	}
	@PutMapping("/update")
	public ResponseEntity<?> updateTravelPlan(@RequestBody PlanData plan){
		
			String msg = planService.updatePlan(plan);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	@DeleteMapping("/delete/{planId}")
	public ResponseEntity<?> removeTravelPlanByplanId(@PathVariable Integer planId){
		
			String msg = planService.deletePlan(planId);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		}
	@PutMapping("/status-change/{planId}/{status}")
	public ResponseEntity<String> removeTravelPlanByPlanId(@PathVariable Integer planId,@PathVariable String status){
		
			String msg = planService.changePlanStatus(planId, status);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		
	}
	
}
