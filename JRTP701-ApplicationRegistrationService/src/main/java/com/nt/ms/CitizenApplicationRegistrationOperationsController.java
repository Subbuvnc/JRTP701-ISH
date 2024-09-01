package com.nt.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.service.ICitizenApplicationRegistrationService;

@RestController
@RequestMapping("/CitizenAR-api")
public class CitizenApplicationRegistrationOperationsController {
    @Autowired
	private ICitizenApplicationRegistrationService registrationService;
	/*@PostMapping("/save")
	public ResponseEntity<String> saveCitizenApplicatin(@RequestBody CitizenAppRegistrationInputs inputs){
		
		try {
			int appId=registrationService.registerCitizenApplication(inputs);
			if(appId>0)
			return new ResponseEntity<String>("Citizen Applicationis Registered with the id:"+appId,HttpStatus.CREATED);
			else
		    return new ResponseEntity<String>("Invali ssn or citizen must belong to california state: ",HttpStatus.OK);
		
		}
		catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
			}
	}*/
    
    
 //   by using custom exception
    @PostMapping("/save")
	public ResponseEntity<String> saveCitizenApplicatin(@RequestBody CitizenAppRegistrationInputs inputs)throws Exception{
		
		
			int appId=registrationService.registerCitizenApplication(inputs);
			return new ResponseEntity<String>("Citizen Applicationis Registered with the id:"+appId,HttpStatus.CREATED);
			
	}
}
