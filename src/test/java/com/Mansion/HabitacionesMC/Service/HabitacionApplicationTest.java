package com.Mansion.HabitacionesMC.Service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Mansion.HabitacionesMC.Repository.HabitacionRepository;


@ExtendWith(MockitoExtension.class)
public class HabitacionApplicationTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;
    private Faker faker = new Faker();
    @BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

}
