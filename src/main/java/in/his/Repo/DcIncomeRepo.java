package in.his.Repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.his.Entity.DcIncomeEntity;

public interface DcIncomeRepo extends JpaRepository<DcIncomeEntity, Serializable> {
	
	public DcIncomeEntity findByCaseNum(Long caseNum);

}
