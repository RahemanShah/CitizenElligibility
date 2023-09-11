package in.his.Repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.his.Entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Serializable> {

	public DcEducationEntity findByCaseNum(Long caseNum);
}
