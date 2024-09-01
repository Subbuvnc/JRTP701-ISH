package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcIncomeEntity;

public interface IDcChildrenRepository extends JpaRepository<DcChildrenEntity, Integer> {

	public List<DcChildrenEntity> findByCaseNo(int caseNo);
}
