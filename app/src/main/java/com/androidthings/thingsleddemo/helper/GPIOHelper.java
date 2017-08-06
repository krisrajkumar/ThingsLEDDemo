package com.androidthings.thingsleddemo.helper;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Helper class to handle the GPIO services.
 */
public class GPIOHelper extends GpioCallback {
    private static final String TAG = "ThingsGPIO";
    private static final String GPIO_LED_LEFT = "BCM23";
    private static final String GPIO_LED_RIGHT = "BCM24";
    private static final String GPIO_OBSTACLE_SENSOR = "BCM21";

    private static GPIOHelper gpioHelper;
    private PeripheralManagerService service;

    private Gpio obstacleSensorGpio;
    private Gpio leftGpio;
    private Gpio rightGpio;

    private GPIOHelper() throws IOException {
        service = new PeripheralManagerService();
        obstacleSensorGpio = service.openGpio(GPIO_OBSTACLE_SENSOR);
        obstacleSensorGpio.setDirection(Gpio.DIRECTION_IN);
        obstacleSensorGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
        obstacleSensorGpio.setActiveType(Gpio.ACTIVE_LOW);
        leftGpio = service.openGpio(GPIO_LED_LEFT);
        leftGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        rightGpio = service.openGpio(GPIO_LED_RIGHT);
        rightGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        leftGpio.setValue(true);
        rightGpio.setValue(true);
    }

    public static GPIOHelper getGPIOHelper() throws IOException {
        if (gpioHelper == null) {
            gpioHelper = new GPIOHelper();
        }
        return gpioHelper;
    }

    @Override
    public boolean onGpioEdge(Gpio gpio) {
        try {
            leftGpio.setValue(!gpio.getValue());
            rightGpio.setValue(!gpio.getValue());
            Log.v(TAG, String.valueOf("Obstacle Sensor: " + gpio.getValue()));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return super.onGpioEdge(gpio);
    }

    public void startSensingObstacles() {
        if (obstacleSensorGpio != null) {
            try {
                obstacleSensorGpio.registerGpioCallback(this);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void stopSensingObstacles() {
        if (obstacleSensorGpio != null) {
            obstacleSensorGpio.unregisterGpioCallback(this);
            try {
                obstacleSensorGpio.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        if (leftGpio != null) {
            try {
                leftGpio.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        if (rightGpio != null) {
            try {
                rightGpio.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
