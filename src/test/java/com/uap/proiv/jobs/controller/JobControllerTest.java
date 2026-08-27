package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.service.JobService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    @DisplayName("GET /api/job/all retorna trabajos")
    void getAllJobs_success() throws Exception {
        Job job = new Job();
        job.setId(1);
        job.setName("Developer");
        job.setSalary(500);
        job.setHours(80);

        when(jobService.getAllJobs()).thenReturn(List.of(job));

        mockMvc.perform(get("/api/job/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Developer"));
    }

    @Test
    @DisplayName("GET /api/job/{id} retorna error del servicio")
    void getJobById_exception() throws Exception {
        when(jobService.getJobById(99)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/job/99"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Service error"));
    }
}
