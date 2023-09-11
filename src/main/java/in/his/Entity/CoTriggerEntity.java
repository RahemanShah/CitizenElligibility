package in.his.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="Co_Triggers")
public class CoTriggerEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trigId;
	
	private Long caseNum;
	
	@Lob
	private byte[] coPdf;
	
	private String trigStatus;

}
