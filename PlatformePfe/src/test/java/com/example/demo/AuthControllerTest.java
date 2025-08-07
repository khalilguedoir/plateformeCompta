package com.example.demo;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.CompanyDto;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterCompany() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testcompany");
        request.setPassword("password123");
        request.setRole(Role.COMPANY);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("testcompany");
        request.setCompany(companyDto);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void testLogin() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testcompany");
        request.setPassword("password123");
        request.setTenant("testcompany");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
