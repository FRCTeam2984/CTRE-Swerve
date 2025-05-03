package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;

public class LED {
    static SerialPort LEDarduino = new SerialPort(9600, SerialPort.Port.kUSB);

    static String LastSense = "none";
    public static void runLED(String LED_State) {
        // if (transportLastSense != Intake.transportArmSensor.get()) {   // if transport sensor has changed
        //     LEDarduino.writeString("t"); // Coral Transport
        // }

        if (LastSense != LED_State) {   // if LED_State is changed
            // if (LED_State == "none" || LastSense == "none") {
            //     LEDarduino.writeString("0");
            // }
            // flashing red
            if (LED_State == "flashing red" || LastSense == "flashing red") {
                LEDarduino.writeString("s");
            }
            // Quickly flash lights: red green yellow blue
            if (LED_State == "4 Light Flash" || LastSense == "4 Light Flash") {
                LEDarduino.writeString("4");
            }
            // Blue, soft lighter and darker waves m
            if (LED_State == "blue waves" || LastSense == "blue waves") {
                LEDarduino.writeString("w");
            }

            // Line of green lights moving backward
            if (LED_State == "green lines" || LastSense == "green lines") {
                LEDarduino.writeString("g");
            }
            // Fade into Dark blue
            if (LED_State == "fade blue" || LastSense == "fade blue") {
                LEDarduino.writeString("f");
            }
            // Yellow fade to green
            if (LED_State == "yellow green fade" || LastSense == "yellow green fade") {
                LEDarduino.writeString("y");
            }
            // light blue waves
            if (LED_State == "light waves" || LastSense == "light waves") {
                LEDarduino.writeString("l");
            }
            // Light blue fade to yellow
            if (LED_State == "blue yellow fade" || LastSense == "blue yellow fade") {
                LEDarduino.writeString("2");
            }
            // Flashing yellow
            if (LED_State == "flashing yellow" || LastSense == "flashing yellow") {
                LEDarduino.writeString("1");
            }
            // pink
            if (LED_State == "pink" || LastSense == "pink") {
                LEDarduino.writeString("p");
            }
            // Pink fade to yellow
            if (LED_State == "pink yellow fade" || LastSense == "pink yellow fade") {
                LEDarduino.writeString("k");
            }
            // Back and forth fade between blue and green 0x
            if (LED_State == "blue green fade" || LastSense == "blue green fade") {
                LEDarduino.writeString("5");
            }
            // Bright white flash
            if (LED_State == "white flash" || LastSense == "white flash") {
                LEDarduino.writeString("h");
            }
            // Purple waves
            if (LED_State == "purple waves" || LastSense == "purple waves") {
                LEDarduino.writeString("h");
            }
            // Solid red
            if (LED_State == "red" || LastSense == "red") {
                LEDarduino.writeString("r");
            }
            // Light blue
            if (LED_State == "blue" || LastSense == "blue") {
                LEDarduino.writeString("0");
            }
        }


        LastSense = LED_State;
    }
    
}