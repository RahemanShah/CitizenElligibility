package in.his.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PLAN_MASTER")
public class PlanEntity {
	
	
	@Id
	@GeneratedValue
	private Integer planId;
	private String planName;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private String activeSw;
	private LocalDate createDate;
	private LocalDate updatedate;
	private String createdBy;
	private String updatedBy;
	

}
