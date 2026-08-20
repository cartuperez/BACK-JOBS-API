package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.AssignRequest;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import com.uap.proiv.jobs.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {
    @Mock
    UserService userService;

    @Mock
    JobService jobService;

    @Mock
    UserJobAssignedService userJobAssignedService;

    @InjectMocks
    JobController jobController;

    private MockMvc mockMvc;
    
    private UserApiResponse userApiResponse;
    private List<User> users;
    private List<Job> jobs;//Por las dudas
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
        objectMapper = new ObjectMapper();

        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(10);
        user1.setEmail("user1@example.com");
        user1.setAvatar("null");
        user1.setFirstName("Juan");
        user1.setLastName("Garcia");
        users.add(user1);

        User user2 = new User();
        user2.setId(20);
        user2.setEmail("user2@example.com");
        user2.setAvatar("null");
        user2.setFirstName("Diana");
        user2.setLastName("Diaz");
        users.add(user2);

        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);


        jobs = new ArrayList<>();
    }

    @Test
    @DisplayName("GET /api/job/users/{page} retorna usuarios")
    void  getUsers_succes() throws Exception {
        // userApiResponse.setPage(3);
        when(userService.search(1))
            .thenReturn(userApiResponse)
            .thenThrow(new RuntimeException("MSG")) // Simula una excepción en la segunda llamada
            .thenReturn(userApiResponse); // Simula una respuesta exitosa en la tercera llamada

        //Usar MockMvc para realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/api/job/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.total").value(2));

        mockMvc.perform(get("/api/job/users/1"))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(get("/api/job/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/job/users/{page} - Excepcion retornada por el service")
    void getUsers_exception() throws Exception {
        when(userService.search(2)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/job/users/2"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Service error"));
    }

    @Test
    @DisplayName("Post /api/job/assign ")
    void postAssign_success() throws Exception {
        AssignRequest assignRequest = new AssignRequest();
        assignRequest.setRequestNumber(123);
        assignRequest.setClientName("Name");

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Developer");
        job1.setSalary(500);
        job1.setHours(80);
        jobs.add(job1);

        Job job2 = new Job();
        job2.setId(2);
        job2.setName("Tester");
        job2.setSalary(400);
        job2.setHours(75);
        jobs.add(job2);


        List<UserJobAssigned> userJobAssignedList = new ArrayList<>();
        userJobAssignedList.add(new UserJobAssigned(users, job1));
        userJobAssignedList.add(new UserJobAssigned(List.of(users.getFirst()), job2));

        when(userJobAssignedService.assign()).thenReturn(userJobAssignedList);

        mockMvc.perform(post("/api/job/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Assign").isNotEmpty())
                .andExpect(jsonPath("$.Assign[0].job.name").value("Developer"))
                .andExpect(jsonPath("$.Assign[1].job.name").value("Tester"))
                .andExpect(jsonPath("$.Assign[1].users[0].first_name").value("Juan"))
                .andExpect(jsonPath("$.Request_Number").value(123))
                .andExpect(jsonPath("$.Client").value("Name"));
        }
}