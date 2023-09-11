package in.his.Repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.his.Entity.DcCaseEntity;


public interface DcCaseRepo extends JpaRepository<DcCaseEntity, Serializable> {
	
	public DcCaseEntity findByAppId(Integer appId);

}
