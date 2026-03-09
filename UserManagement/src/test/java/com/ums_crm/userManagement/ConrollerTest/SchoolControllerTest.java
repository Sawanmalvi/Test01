package com.ums_crm.userManagement.ConrollerTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.ums_crm.userManagement.controller.SchoolController;
import com.ums_crm.userManagement.interfaceAll.SchoolIntr;
import com.ums_crm.userManagement.schoolDTO.SchoolDTO;
import com.ums_crm.userManagement.utility.AuthUtil;
import com.ums_crm.userManagement.utility.SuccessDTO;
import com.ums_crm.userManagement.utility.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class SchoolControllerTest {
//    private AuthUtil authUtil; // optional if static, then need Mockito.mockStatic

    @Mock
    private SchoolIntr schoolintr;

    @InjectMocks
    private SchoolController schoolController;

    private MockedStatic<AuthUtil> authUtilMock;

    @BeforeEach
    void setup() {
        // Mock the static method AuthUtil.getCurrentUser()
        authUtilMock = Mockito.mockStatic(AuthUtil.class);
    }

    @Test
    void testAddSchool_Success() {
        // Prepare mock user
        UserDTO mockUser = new UserDTO();
        mockUser.setVuser_name("TestUser");
        authUtilMock.when(AuthUtil::getCurrentUser).thenReturn(mockUser);

        // Prepare school DTO
        SchoolDTO request = new SchoolDTO();
        request.setVschool_name("School_" + UUID.randomUUID());

        // Mock service call
        when(schoolintr.addSchool(any(SchoolDTO.class))).thenReturn(request);

        // Call controller
        SuccessDTO response = schoolController.addSchool(request);

        // Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals(1, response.getDetailList().size(), "Detail list should contain 1 element");
        assertEquals(request.getVschool_name(),
                ((SchoolDTO) response.getDetailList().get(0)).getVschool_name(),
                "School name should match");

        // Verify service called once
        verify(schoolintr, times(1)).addSchool(any(SchoolDTO.class));
    }
}
