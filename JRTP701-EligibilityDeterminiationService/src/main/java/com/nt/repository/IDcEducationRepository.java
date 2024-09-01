package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
	public DcEducationEntity findByCaseNo(int caseNo);
}
