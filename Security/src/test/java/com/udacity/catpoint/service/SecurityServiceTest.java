package com.udacity.catpoint.service;

import com.udacity.catpoint.data.ArmingStatus;
import com.udacity.catpoint.data.SecurityRepository;
import com.udacity.catpoint.data.Sensor;
import com.udacity.catpoint.data.SensorType;
import com.udacity.catpoint.imageservice.FakeImageService;
import com.udacity.catpoint.imageservice.ImageServiceInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    //Mock the ImageService
    @Mock
    private FakeImageService testImageService;

    //mock the security repository
    @Mock
    private SecurityRepository testSecurityRepository;

    private SecurityService testSecurityService;

    @BeforeAll
    void init() {
        testSecurityService = new SecurityService(testSecurityRepository, testImageService);
    }

    //Test #1: If alarm is armed and a sensor becomes activated
    //Expected Outcome #1: Put the system into pending alarm status
    //handleSensorActivated()
    //@Test
    void validate_ArmedAlarm_ActivatedSensor_equals_SysPendingAlarm(){

        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);
        testSecurityService.setArmingStatus(ArmingStatus.ARMED_HOME);
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);
        System.out.println(testSecurityRepository.getAlarmStatus());
    }

}
