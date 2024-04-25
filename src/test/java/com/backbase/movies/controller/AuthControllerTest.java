package com.backbase.movies.controller;

import com.backbase.movies.model.UserRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    //@MockBean
    //private JwtTokenService jwtTokenService;

//    @MockBean
//    private ILoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin_Successful() throws Exception {
        // Given
//        UserRecord userDTO = new UserRecord("username", "password");
//        when(loginService.login(userDTO)).thenReturn(Optional.of(123));
//        when(jwtTokenService.generateToken(123, "USER")).thenReturn("token");
//
//        // When
//        var result = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Then
//        int response = result.getResponse().getStatus();
//        assertEquals(200, response);
    }

    @Test
    public void testLogin_Unsuccessful() throws Exception {
        // Given
        UserRecord userDTO = new UserRecord("username", "password");
       // when(loginService.login(userDTO)).thenReturn(Optional.empty());

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized())
                .andReturn();

        //int response = result.getResponse().getStatus();
        //assertEquals(401, response);
    }

    @Test(expected = Exception.class)
    public void testLogin_ThrowsException() throws Exception {
        // Given
        UserRecord userDTO = new UserRecord("username", "password");
       // when(loginService.login(userDTO)).thenThrow(new Exception("Error"));

        // When
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));
    }
}