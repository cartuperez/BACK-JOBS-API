package com.uap.proiv.jobs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uap.proiv.jobs.dto.AssignRequest;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AssignControllerTest {

    @Mock
    private UserJobAssignedService userJobAssignedService;

    @InjectMocks
    private AssignController assignController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(assignController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/assign retorna asignaciones")
    void postAssign_success() throws Exception {
        AssignRequest assignRequest = new AssignRequest();
        assignRequest.setRequestNumber(123);
        assignRequest.setClientName("Name");

        User user = new User();
        user.setId(10);
        user.setEmail("user1@example.com");
        user.setFirstName("Juan");
        user.setLastName("Garcia");

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Developer");
        job1.setSalary(500);
        job1.setHours(80);

        Job job2 = new Job();
        job2.setId(2);
        job2.setName("Tester");
        job2.setSalary(400);
        job2.setHours(75);

        List<UserJobAssigned> assignments = List.of(
                new UserJobAssigned(List.of(user), job1),
                new UserJobAssigned(List.of(user), job2)
        );

        when(userJobAssignedService.assign()).thenReturn(assignments);

        mockMvc.perform(post("/api/assign")
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
