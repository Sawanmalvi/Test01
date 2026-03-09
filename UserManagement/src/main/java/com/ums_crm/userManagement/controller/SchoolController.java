package com.ums_crm.userManagement.controller;

import com.ums_crm.userManagement.interfaceAll.SchoolIntr;
import com.ums_crm.userManagement.schoolDTO.SchoolDTO;
import com.ums_crm.userManagement.utility.AuthUtil;
import com.ums_crm.userManagement.utility.SuccessDTO;
import com.ums_crm.userManagement.utility.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolIntr schoolintr;

    @PostMapping("/addSchool")
    public SuccessDTO addSchool(@RequestBody SchoolDTO request) {
        SuccessDTO sdto = new SuccessDTO();

        try {
            UserDTO userdto = AuthUtil.getCurrentUser();
            System.out.println("name***********" + userdto.getVuser_name());
            List l = new ArrayList();
            request = schoolintr.addSchool(request);
            l.add(request);
            sdto.setDetailList(l);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdto;
    }
}
