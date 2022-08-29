package com.udacity.catpoint.service;

import com.udacity.catpoint.data.*;
import com.udacity.catpoint.imageservice.FakeImageService;
import com.udacity.catpoint.imageservice.ImageServiceInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

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

    @BeforeEach
    void init() {
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
        //VERIFY: This should also cause a call to getAlarmStatus and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);
        verify(testSecurityRepository).getAlarmStatus();
        verify(testSecurityRepository).updateSensor(windowSensor);

    }

    //Test #2: If alarm is armed, a sensor becomes activated, and the system is already pending alarm,
    //Expected Outcome #2: The system should set the alarm status to alarm
    @Test
    void validate_ArmedAlarm_ActivatedSensor_sysPendingAlarm_equals_SysAlarm(){

        //Since I am mocking the repository, I have to make sure it returns ALARM when calling
        //the getAlarmStatus method
        when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        //Add a sensor to activate
        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);

        //Arm status should be armed
        //VERIFY: This should have caused a call to setArmingStatus in testSecurityRepository
        testSecurityService.setArmingStatus(ArmingStatus.ARMED_HOME);
        verify(testSecurityRepository).setArmingStatus(ArmingStatus.ARMED_HOME);

        //Alarm status should be set to PENDING_ALARM to start
        //VERIFY: This should have caused a call to setAlarmStatus in testSecurityRepository
        testSecurityService.setAlarmStatus(AlarmStatus.PENDING_ALARM);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.PENDING_ALARM);

        //Activate the sensor
        //VERIFY: This should also cause a call to getAlarmStatus and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);
        verify(testSecurityRepository).getAlarmStatus();
        verify(testSecurityRepository).updateSensor(windowSensor);
    }

    //Test #3: If pending alarm and all sensors are inactive
    //Expected Outcome #3: return to no alarm state
    @Test
    void validate_pendingAlarm_inactiveSensors_equals_SysNoAlarm(){

        //Since I am mocking the repository, I have to make sure it returns NO_ALARM when calling
        //the getAlarmStatus method
        when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);

        //Add a sensor to activate then inactivate
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
        //VERIFY: This should also cause a call to getAlarmStatus (verified at the end),
        //setAlarmStatus(PENDING_ALARM), and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.PENDING_ALARM);

        //Inactivate the sensor
        //VERIFY: This should also cause a call to getAlarmStatus (verified at the end),
        //setAlarmStatus(NO_ALARM), and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.FALSE);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);

        //VERIFY: The previous two calls to the testSecurityRepository.getAlarmStatus()
        verify(testSecurityRepository, times(2)).getAlarmStatus();

        //VERIFY: The previous two calls to the testSecurityRepository.updateSensor()
        verify(testSecurityRepository, times(2)).updateSensor(windowSensor);
    }

    //Test #4: Alarm is active and change in sensor state
    //Expected Outcome #4: No effect on the alarm state
    @Test
    void validate_alarmActive_sensorChanged_equals_sysNoChange(){

        //Since I am mocking the repository, I have to make sure it returns ALARM when calling
        //the getAlarmStatus method
        when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        //Add a sensor to activate then inactivate
        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);

        //Activate the sensor
        //VERIFY: This should also cause a call to getAlarmStatus (verified at the end),
        //and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);

        //Inactivate the sensor
        //VERIFY: This should also cause a call to getAlarmStatus,
        //setAlarmStatus(ALARM), and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.FALSE);

        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.ALARM);

        //VERIFY: The previous two calls to the testSecurityRepository.getAlarmStatus()
        verify(testSecurityRepository, times(2)).getAlarmStatus();

        //VERIFY: The previous two calls to the testSecurityRepository.updateSensor()
        verify(testSecurityRepository, times(2)).updateSensor(windowSensor);
    }

    //Test #5: A sensor is activated while already active and the system is in pending state
    //Expected Outcome #5: The system should change to alarm state
    @Test
    void validate_sensorActivated_sysPendingAlarm_equals_sysAlarm(){

        //Since I am mocking the repository, I have to make sure it returns PENDING_ALARM when calling
        //the getAlarmStatus method
        when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        //Add a sensor to activate
        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);
        windowSensor.setActive(Boolean.TRUE);

        //Activate the sensor again
        //VERIFY: This should also cause a call to getAlarmStatus (verified at the end),
        //setAlarmStatus(ALARM), and updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.TRUE);

        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.ALARM);

        //VERIFY: The previous call to the testSecurityRepository.getAlarmStatus()
        verify(testSecurityRepository).getAlarmStatus();

        //VERIFY: The previous call to the testSecurityRepository.updateSensor()
        verify(testSecurityRepository).updateSensor(windowSensor);
    }

    //Test #6: A sensor is deactivated while already inactive
    //Expected Outcome #6: the alarm state should not change
    @Test
    void validate_sensorDeactivated_equals_sysNoChange() {

        //Since I am mocking the repository, I have to make sure it returns PENDING_ALARM when calling
        //the getAlarmStatus method
        //when(testSecurityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        //Add a sensor to deactivate
        Sensor windowSensor = new Sensor("Front Window", SensorType.WINDOW);
        testSecurityRepository.addSensor(windowSensor);
        windowSensor.setActive(Boolean.FALSE);

        //Deactivate the sensor again
        //VERIFY: This should also cause no call to getAlarmStatus (verified at the end),
        //no call to setAlarmStatus(), and only one call to updateSensor in testSecurityRepository
        testSecurityService.changeSensorActivationStatus(windowSensor, Boolean.FALSE);

        //VERIFY: The previous call to the testSecurityRepository.updateSensor()
        verify(testSecurityRepository, times(1)).updateSensor(windowSensor);

        //VERIFY: There should be no call to getAlarmStatus
        verify(testSecurityRepository, never()).getAlarmStatus();

        //VERIFY: There should be no call to setAlarmStatus
        verify(testSecurityRepository, never()).setAlarmStatus(any(AlarmStatus.class));
    }

    //Test #7: The image service identifies an image containing a cat while the system is armed-home
    //Expected Outcome #7: the system should be in alarm status
    @Test
    void validate_catImage_sysArmedHome_equals_sysAlarm() {

        BufferedImage catImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        when(testImageService.imageContainsCat(any(), ArgumentMatchers.anyFloat())).thenReturn(true);
        when(testSecurityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        testSecurityService.processImage(catImage);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }

    //Test #8: The image service identifies an image that does not contain a cat and the sensors are not active
    //Expected Outcome #8: System status should be no alarm
    @Test
    void validate_catImageFalse_sensorsNotActive_equals_sysNoAlarm(){
        BufferedImage catImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        when(testImageService.imageContainsCat(any(), ArgumentMatchers.anyFloat())).thenReturn(false);
        when(testSecurityRepository.anySensorActivated()).thenReturn(false);
        testSecurityService.processImage(catImage);

        verify(testSecurityRepository, never()).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    //Test #9: The system is disarmed
    //Expected Outcome #9: The status of the system is set to no alarm
    @Test
    void validate_sysDisArmed_equals_sysNoAlarm() {

        testSecurityService.setArmingStatus(ArmingStatus.DISARMED);
        verify(testSecurityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);

    }

    //Test #10: The system is armed
    //Expected Outcome #10: all sensors set to inactive.
}
