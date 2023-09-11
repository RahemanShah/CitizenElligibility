package in.his.Repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.his.Entity.PlanEntity;

public interface planRepo extends JpaRepository<PlanEntity, Serializable> {

}
