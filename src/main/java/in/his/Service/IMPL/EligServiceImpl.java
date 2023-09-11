package in.his.Service.IMPL;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.his.Binding.EligResponse;
import in.his.Entity.CitizenAppEntity;
import in.his.Entity.CoTriggerEntity;
import in.his.Entity.DcCaseEntity;
import in.his.Entity.DcChildrenEntity;
import in.his.Entity.DcEducationEntity;
import in.his.Entity.DcIncomeEntity;
import in.his.Entity.EligDtlsEntity;
import in.his.Entity.PlanEntity;
import in.his.Repo.CitizenAppRepository;
import in.his.Repo.DcCaseRepo;
import in.his.Repo.DcChildrenRepo;
import in.his.Repo.DcEducationRepo;
import in.his.Repo.DcIncomeRepo;
import in.his.Repo.EligiDtlsRepo;
import in.his.Repo.coTriggerReopo;
import in.his.Repo.planRepo;
import in.his.Service.EligService;

@Service
public class EligServiceImpl implements EligService {

	
	@Autowired
	private DcCaseRepo caseRepo;
	
	@Autowired
	private planRepo planrepo;
	
	@Autowired
	private DcIncomeRepo incomeRepo;
	
	@Autowired
	private DcChildrenRepo childRepo;
	
	@Autowired
	private CitizenAppRepository citizenRepo; 
	
	@Autowired
	private DcEducationRepo eduRepo;
	
	@Autowired
	private EligiDtlsRepo eligRepo;
	
	@Autowired
	private coTriggerReopo triggerRepo;
	
	
	@Override
	public EligResponse determineEligibility(Long caseNum) {
	
		Optional<DcCaseEntity> caseEntity = caseRepo.findById(caseNum);
		
		Integer planId = null;
		Integer appId = null;
//		String planNames = null;
		
		if(caseEntity.isPresent()) {
			
			DcCaseEntity dcCase =  caseEntity.get();
			planId = dcCase.getPlanId();
			appId  =  dcCase.getAppId();
		}
		
		
		Optional<PlanEntity> planCheck = planrepo.findById(planId);
		String planName = null;
		
		if(planCheck.isPresent()) {
			PlanEntity pc =  planCheck.get();
		    planName = pc.getPlanName();
		}
		
		Optional<CitizenAppEntity> citizenAppId = citizenRepo.findById(appId);
		CitizenAppEntity citizenId = null;
		Integer age = 0;
		if(citizenAppId.isPresent()) {
			
			 citizenId = citizenAppId.get();
			 LocalDate takeDob = citizenId.getDob();
			
			LocalDate now = LocalDate.now();
			 age = Period.between(takeDob, now).getYears();
		}
		
		EligResponse eligres =  executePlanConditions(caseNum, planName, age);
		
		EligDtlsEntity eligEntity = new EligDtlsEntity();
		BeanUtils.copyProperties(eligres, eligEntity);
		
		eligEntity.setCaseNum(caseNum);
		eligEntity.setHolderName(citizenId.getFullName());
		eligEntity.setHolderSsn(citizenId.getSsn());
		
		eligRepo.save(eligEntity);
		
		CoTriggerEntity triggerEntity = new CoTriggerEntity();
		triggerEntity.setCaseNum(caseNum);
		triggerEntity.setTrigStatus("Pending");
		
		triggerRepo.save(triggerEntity);
		
		return eligres;
	}
	
	
	private EligResponse executePlanConditions(Long caseNum, String planName, Integer age) {
		
		EligResponse eligRes = new EligResponse();
		eligRes.setPlanName(planName);
		
		DcIncomeEntity income  = incomeRepo.findByCaseNum(caseNum);
		
		if("SNAP".equals(planName)) {
			
			Double empIncome = income.getEmpIncome();
			
			if(empIncome <= 300) {
				eligRes.setPlanStatus("AP");
			}
			else {
				eligRes.setPlanStatus("DN");
				eligRes.setDenielReson("High Income");
			}
			
		}
		else if("CCAP".equals(planName)) {
			
			boolean ageCondition = true;
			boolean kidsCountCondition = false;
			
			List<DcChildrenEntity> childsEntity = childRepo.findByCaseNum(caseNum);
			
			if(!childsEntity.isEmpty()) {
				
				kidsCountCondition = true;
				
				for(DcChildrenEntity  entity : childsEntity) {
					Integer childAge = entity.getChildAge();
					
					if(childAge > 16) {
						ageCondition = false;
					}
				}
			}
			
			if(income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition ) {
				eligRes.setPlanStatus("AP");
			}
			else {
				eligRes.setPlanStatus("DL");
				eligRes.setDenielReson("Age not satisfied");
			}
		}
		
		
		else if("Medicaid".equals(planName)) {
			
			Double empIncomes = income.getEmpIncome();
			Double propIncome = income.getPropertyIncome();
			
			if(empIncomes < 300 && propIncome==0) {
				eligRes.setPlanStatus("AP");
			}
			else {
				eligRes.setPlanStatus("DN");
				eligRes.setDenielReson("High Income");
			}
		}
		else if("Medicare".equals(planName)) {
				
				if(age >= 65) {
					eligRes.setPlanStatus("AP");
				}
				else {
					eligRes.setPlanStatus("DN");
					eligRes.setDenielReson("Age is not Satisfied");
				}
			}
	
	
		else if("NJW".equals(planName)) {
			
			DcEducationEntity eduEntity = eduRepo.findByCaseNum(caseNum);
			Integer graduationYear = eduEntity.getGraduationYear();
			
			int currentYear = LocalDate.now().getYear();
			
			if(income.getEmpIncome() == 0 && graduationYear < currentYear) {
				eligRes.setPlanStatus("AP");
			}
			else {
				eligRes.setPlanStatus("DN");
				eligRes.setDenielReson("Rules Not Satisfied");
			}
		}
		
		if(eligRes.getPlanStatus().equals("AP")) {
			
			eligRes.setPlanStartDate(LocalDate.now());
			eligRes.setPlanEndDate(LocalDate.now().plusMonths(6));
            eligRes.setBenefitAmount(450.00);
		}
		
		return eligRes;
		
	}

}
