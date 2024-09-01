package com.nt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exceptions.InvalidSSNException;
import com.nt.repository.IApplicationRegistrationRepository;

import reactor.core.publisher.Mono;

@Service
public class CitizenApplicationRegistrationServiceImpl
		implements ICitizenApplicationRegistrationService {
        @Autowired
	    private IApplicationRegistrationRepository citizenRepo;
		/*
		 * @Autowired private RestTemplate template;
		 */
	    @Value("${ar.ssa-web.url}")
	    private String endPointUrl;
	    @Value("${ar.state}")
	    private String targetState;
	    @Autowired
	    private WebClient client;
	    
	    
	    @Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException {
		// TODO Auto-generated method stub
		
		//String SSAWebUrl="http://localhost:9090/ssa-web-api/first/{ssn}";
		
		/*
		 * HttpHeaders headears = new HttpHeaders();
		 * headears.set("accept","application/json"); HttpEntity entity = new
		 * HttpEntity(headears); ResponseEntity<String> response =
		 * template.exchange(SSAWebUrl,
		 * HttpMethod.GET,entity,String.class,inputs.getSsn());
		 */
		
		// here get mode request and only path variable then headeras are not requried
		//ResponseEntity<String> response =template.exchange(endPointUrl,HttpMethod.GET,null,String.class,inputs.getSsn());
		
	    	//now use web clinet without using rest template 
	    	
	    	/*String stateName=client.get().uri(endPointUrl,inputs.getSsn()).retrieve().bodyToMono(String.class).block();	
		if(stateName.equalsIgnoreCase(targetState)) {
			CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			
			int appId=citizenRepo.save(entity).getAppId();
			return appId;
		}
	        throw new InvalidSSNException("invalid SSN");
	}*/

	    	// for handeling external server error
	    	Mono<String> response=client.get().uri(endPointUrl,inputs.getSsn()).retrieve().onStatus(HttpStatus.BAD_REQUEST::equals,res->res.bodyToMono(String.class).map(ex-> new InvalidSSNException("invalid ssn"))).bodyToMono(String.class);	
			String stateName=response.block();
	    	if(stateName.equalsIgnoreCase(targetState)) {
				CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
				BeanUtils.copyProperties(inputs, entity);
				entity.setStateName(stateName);
				
				int appId=citizenRepo.save(entity).getAppId();
				return appId;
			}
		        throw new InvalidSSNException("invalid SSN");
	    }

}
