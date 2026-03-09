package com.ums_crm.userManagement.service.SchoolServiceImp;

import com.ums_crm.userManagement.interfaceAll.SchoolIntr;
import com.ums_crm.userManagement.repository.SchoolRepositery;
import com.ums_crm.userManagement.schoolDTO.SchoolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImp implements SchoolIntr {
    @Autowired
    private SchoolRepositery schoolRepositery;

    public SchoolDTO addSchool(SchoolDTO dto){
        try {
            schoolRepositery.save(dto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }
}
