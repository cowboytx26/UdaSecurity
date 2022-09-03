package com.udacity.catpoint.service;

import com.udacity.catpoint.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


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

    //Test #10: The system is armed (testSecurityService.setArmingStatus(ArmingStatus.ARMED)
    //Expected Outcome #10: all sensors set to inactive (assertEquals(testSecurityRepository.anySensorActivated(), false)
    @ParameterizedTest
    @EnumSource(value = ArmingStatus.class, names = {"ARMED_HOME", "ARMED_AWAY"})
    public void validate_SysArmed_AnySensorActivated_IsFalse(ArmingStatus testStatus) {

        testSecurityRepository.getSensors().forEach(s -> s.setActive(true));

        testSecurityRepository.setArmingStatus(testStatus);

        assertEquals(testSecurityRepository.anySensorActivated(), false);

    }

}
