package in.his.Service.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.his.Binding.EligResponse;
import in.his.Service.EligService;

@RestController
public class EligiblityRestController {
	
	@Autowired
	private EligService service;
	
	@GetMapping("/case/{caseNum}")
	public ResponseEntity<EligResponse> DeterminerEligibility(@PathVariable Long caseNum){	
		
		EligResponse eligResponse = service.determineEligibility(caseNum);
		
		return new ResponseEntity<EligResponse>(eligResponse, HttpStatus.OK);
	}
	

}
