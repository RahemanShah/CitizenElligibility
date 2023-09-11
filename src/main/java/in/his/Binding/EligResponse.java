package in.his.Binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligResponse {

	//private Long caseNum;
	private String planName;	
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmount;
	private String denielReson;
}
