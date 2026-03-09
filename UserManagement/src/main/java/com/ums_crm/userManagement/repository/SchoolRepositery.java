package com.ums_crm.userManagement.repository;

import com.ums_crm.userManagement.schoolDTO.SchoolDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
@Repository
public interface SchoolRepositery extends JpaRepository<SchoolDTO, Serializable> {
}
