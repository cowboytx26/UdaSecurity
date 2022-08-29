package com.udacity.catpoint.service;

import com.udacity.catpoint.data.PretendDatabaseSecurityRepositoryImpl;
import com.udacity.catpoint.data.SecurityRepository;
import com.udacity.catpoint.data.Sensor;
import com.udacity.catpoint.data.SensorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityRepositoryTest {

    private SecurityRepository testSecurityRepository;

    @BeforeEach
    public void init(){
        testSecurityRepository = new PretendDatabaseSecurityRepositoryImpl();
    }

    @Test
    public void validate_AnySensorsActivated_IsFalse() {

        testSecurityRepository.getSensors().forEach(s -> s.setActive(false));
        assertEquals(testSecurityRepository.anySensorActivated(), false);
    }

    @Test
    public void validate_AnySensorsActivated_IsTrue() {

        testSecurityRepository.getSensors().forEach(s -> s.setActive(false));

        Sensor doorSensor = new Sensor("Front Door", SensorType.DOOR);
        testSecurityRepository.addSensor(doorSensor);
        doorSensor.setActive(Boolean.TRUE);

        testSecurityRepository.addSensor(doorSensor);
        assertEquals(testSecurityRepository.anySensorActivated(), true);
    }
}
