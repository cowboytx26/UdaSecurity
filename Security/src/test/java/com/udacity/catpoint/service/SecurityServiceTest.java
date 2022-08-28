package com.udacity.catpoint.service;

import com.udacity.catpoint.data.*;
import com.udacity.catpoint.imageservice.FakeImageService;
import com.udacity.catpoint.imageservice.ImageServiceInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        testSecurityRepository = new PretendDatabaseSecurityRepositoryImpl();
        testSecurityService = new SecurityService(testSecurityRepository, testImageService);
    }

    //Test #1: If alarm is armed and a sensor becomes activated
    //Expected Outcome #1: Put the system into pending alarm status
    @Test
    void validate_ArmedAlarm_ActivatedSensor_equals_SysPendingAlarm(){

        //Since I am mocking the repository, I have to make sure it returns PENDING_ALARM when calling
        //the getAlarmStatus method
        when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        //Add a sensor to activate
        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);

        //Arm status should be armed
        //VERIFY: This should have caused a call to setArmingStatus in testSecurityRepository
        testSecurityService.setArmingStatus(ArmingStatus.ARMED_HOME);
        verify(testSecurityRepository).setArmingStatus(ArmingStatus.ARMED_HOME);

        //Alarm status should be set to NO_ALARM to start
        //VERIFY: This should have caused a call to setAlarmStatus in testSecurityRepository
        testSecurityService.setAlarmStatus(AlarmStatus.NO_ALARM);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);

        //Activate the sensor
        //VERIFY: This should also cause a call to getAlarmStatus in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);
        verify(testSecurityRepository).getAlarmStatus();

    }

}
