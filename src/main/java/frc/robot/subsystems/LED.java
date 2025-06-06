package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;

public class LED {
    static SerialPort LEDarduino = new SerialPort(9600, SerialPort.Port.kUSB);
    //private static boolean transportLastSense = Intake.transportArmSensor.get();
    //private static boolean ElevatorLastSense = Intake.outsideTransportSwitch.isPressed();
    static String lastSense = null;
    public static void sendData(String pattern) {
        if (lastSense != pattern){
            switch(pattern){
                case "pattern 1":
                    LEDarduino.writeString("t");
                    break;
                case "pattern 2":
                    LEDarduino.writeString("e");
                    break;
            }
        }
        lastSense = pattern;
        /*if (transportLastSense != Intake.transportArmSensor.get()) {   // if transport sensor has changed
            LEDarduino.writeString("t"); // Coral Transport
        }

        if (ElevatorLastSense != Intake.outsideTransportSwitch.isPressed()) {   // if elevator limit switch sensor has changed
            LEDarduino.writeString("e"); // Coral Elevator
        }*/


    }
    
}