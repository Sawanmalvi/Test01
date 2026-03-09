package com.ums_crm.userManagement.service;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.ums_crm.userManagement.schoolDTO.SchoolDTO;
//import com.ums_crm.userManagement.service.SchoolServiceImp;
import com.ums_crm.userManagement.repository.SchoolRepositery;
import com.ums_crm.userManagement.service.SchoolServiceImp.SchoolServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceImpTest {

    @Mock
    private SchoolRepositery schoolRepositery;

    @InjectMocks
    private com.ums_crm.userManagement.service.SchoolServiceImp.SchoolServiceImp schoolService;

    public SchoolServiceImpTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddSchool_withDynamicData() {
        // Generate dynamic school name
        String schoolName = "School_" + UUID.randomUUID();
        SchoolDTO dto = new SchoolDTO();
        dto.setVschool_name(schoolName);
        dto.setVschool_location("Location_" + UUID.randomUUID());

        // Mock save method to return the same DTO
        when(schoolRepositery.save(org.mockito.ArgumentMatchers.any(SchoolDTO.class)))
                .thenAnswer(invocation -> (SchoolDTO) invocation.getArgument(0));        // Call service method
        SchoolDTO result = schoolService.addSchool(dto);

        // Verify save called once
        verify(schoolRepositery, times(1)).save(dto);

        // Assert returned object has same name
        Assertions.assertEquals(schoolName, result.getVschool_name());
    }


}
