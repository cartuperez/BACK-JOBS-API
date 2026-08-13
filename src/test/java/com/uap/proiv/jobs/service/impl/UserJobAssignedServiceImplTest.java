package com.uap.proiv.jobs.service.impl;

import com.uap.proiv.jobs.dto.AssignedResponse;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.service.AssignedService;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)

public class UserJobAssignedServiceImplTest {
    @Mock
    JobService jobService;

    @Mock
    UserService userService;

    @Mock
    AssignedService assignedService;

    @InjectMocks
    UserJobAssignedServiceImpl serviceImpl;

    List<Job> jobs;
    List<User> users;
    List<AssignedResponse> assignedResponses;

    @BeforeEach
    void setup() {
        jobs = new ArrayList<>();
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

        users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail.com");
        user1.setAvatar("null");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        users.add(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("uuuser@gmail.com");
        user2.setAvatar("null");
        user2.setFirstName("caty");
        user2.setLastName("perez");
        users.add(user2);
        
        assignedResponses = new ArrayList<>();
        assignedResponses.add(new AssignedResponse(1, 2));
        assignedResponses.add(new AssignedResponse(2, 1));
    }

    @Test
    @DisplayName("Verifica la respuesta de una sola pagina de ususarios y asiganciones de trabajo")
    void testAssignUserJobResponseass() {
        assertNotNull(jobs);
        assertNotNull(users);
        assertNotNull(assignedResponses);
        assertEquals(2, jobs.size());
        assertEquals(2, users.size());
        assertEquals(2, assignedResponses.size());
    }
}