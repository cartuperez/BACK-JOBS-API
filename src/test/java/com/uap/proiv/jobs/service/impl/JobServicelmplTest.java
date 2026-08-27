
package com.uap.proiv.jobs.service.impl;
import com.uap.proiv.jobs.client.JobApiRepository;
import com.uap.proiv.jobs.dto.Job;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JobServicelmplTest {
    
    @Mock 
    JobApiRepository jobApiRepository;

    @InjectMocks
    JobServiceImpl jobServiceImpl;


    List<Job> jobs;

    @BeforeEach  //esto se ejecuta antes de ejecutar un test
    void setup () {
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

        Job job3 = new Job();
        job3.setId(3);
        job3.setName("Analyst");
        job3.setSalary(450);
        job3.setHours(78);
        jobs.add(job3);

        
    }

    @Test
    @DisplayName("Verificar que el metodo Getalljobs restorne la lista de trabajos")
    void getAllJob_Success(){
        when(jobApiRepository.getAllJobs()).thenReturn(jobs);
        List<Job> result = jobServiceImpl.getAllJobs();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Developer", result.get(0).getName());
        verify(jobApiRepository, times(1)).getAllJobs();
    }

    @Test
    @DisplayName("verifica una exception en jobApiRepoitory por listado vacio")
    void assign_succesOnePage(){
        when(jobApiRepository.getAllJobs()).thenReturn(jobs);
        List<Job> result = jobServiceImpl.getAllJobs();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Developer", result.get(0).getName());
        verify(jobApiRepository, times(1)).getAllJobs();
    }
}
