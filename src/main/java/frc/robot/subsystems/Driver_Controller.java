/*
 * Subsystem to run at RobotInit to read and auto set the controllers
 * so they are configured correctly before driving the robot and 
 * without user intervention.
 * 
 * Also used to return button values to other subsystems instead of 
 * previous RobotContainer method.
 */
package frc.robot.subsystems;

import java.lang.ModuleLayer.Controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.RobotContainer;


public class Driver_Controller {
    
    /*
     * Buttons 11 and 12 off   = Drive Joystick
     * Button 11 off and 12 on = Rotary Encoder and Buttons
     * Button 11 on and 12 off = Rotary Switch and Buttons
     * Buttons 11 and 12 on    = Right Side Buttons
     * 
     * m_Controller0           = Drive Joystick
     * m_Controller1           = Rotary Encoder and Buttons
     * m_Controller2           = Rotary Switch and Buttons
     * m_Controller3           = Right Side Buttons
     */
    public static CommandXboxController m_Controller0 = new CommandXboxController(0); //Set Temp value to complete initialization of swervedrive
    public static Joystick m_Controller1 = new Joystick(1);
    public static XboxController m_Controller2 = new XboxController(0);
    public static XboxController m_Controller3 = new XboxController(3);
    private static XboxController m_tempController;
    public static int SwerveCommandXboxControllerPort; // Value of joystick for controlling swerve drive
    public static int SwerveRotaryEncoderPort;         // Value of rotary encoder for controlling swerve drive

    public static boolean SwerveCommandControl;       // Controls Swerve command shifting, false = Controller and true = Command
    public static double SwerveEncoderPassthrough;     // Encoder pass back to swervedrive system
    public static double SwerveXPassthrough;           // X value pass back to swervedrive system
    public static double SwerveYPassthrough;           // Y value pass back to swervedrive system
    public static double SwerveCommandEncoderValue;    // Command requested encoder value for swervedrive
    public static double SwerveCommandXValue;          // Command requested X value for swervedrive
    public static double SwerveCommandYValue;          // Command requested Y value for swervedrive

    public static void define_Controller(){
    for(int i = 0; i < 3; ++i){
        m_tempController = new XboxController(i);
        if (m_tempController.getRawButton(14) == true){
            m_Controller0 = new CommandXboxController(i);
            m_Controller1 = new Joystick(i);
        }
        else if (m_tempController.getRawButton(13) == true){
            m_Controller3 = m_tempController;
        }
        else{
            m_Controller2 = m_tempController;
        }
    }/*
    for(int i=1; i<=4; i++) {
        switch (i) {
            case 1:
                m_tempController = new XboxController(3);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(3);
                    
                    SwerveCommandXboxControllerPort = 3;
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = new Joystick(3);
                    SwerveRotaryEncoderPort = 3;
                    //System.out.println("Set controller port 3");
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }
            case 2:
                m_tempController = new XboxController(1);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(1);
                    
                    SwerveCommandXboxControllerPort = 1;
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = new Joystick(1);
                    SwerveRotaryEncoderPort = 1;
                    //System.out.println("Set controller port 1");
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }
            case 3:
                m_tempController = new XboxController(2);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(2);
                    
                    SwerveCommandXboxControllerPort = 2;
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = new Joystick(2);
                    SwerveRotaryEncoderPort = 2;
                    //System.out.println("Set controller port 2");
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }
            case 4:
                m_tempController = new XboxController(0);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(0);
                    
                    SwerveCommandXboxControllerPort = 0;
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = new Joystick(0);
                    SwerveRotaryEncoderPort = 0;
                    //System.out.println("Set controller port 0");
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }
        }
        // m_Controller0 = new CommandXboxController(0);
        // m_Controller1 = new Joystick(1);
        // m_Controller2 = new XboxController(2);
        // m_Controller3 = new XboxController(3);
    }*/
    //System.out.println(Driver_Controller.SwerveRotaryEncoderPort);
}

public static Boolean driverSwitch(){
    return m_Controller1.getRawButton(6);}
public static Boolean buttonReefAlign(){
    return m_Controller1.getRawButton(1);}
public static Boolean buttonRotateToReef(){
    return m_Controller1.getRawButton(3);}
public static Boolean buttonHPSalign(){
    return m_Controller1.getRawButton(2);}
public static Boolean buttonRemoveAlign(){
    return m_Controller1.getRawButton(5);}
public static Boolean buttonLaserCan(){
    return m_Controller1.getRawButton(4);}
public static Double upperDriverSlider(){
    return m_Controller1.getRawAxis(3);}
public static Double lowerDriverSlider(){
    return m_Controller1.getRawAxis(4);}
    
    
public static Boolean buttonScoreAlgae(){
    return m_Controller3.getRawButton(3);}
public static Boolean buttonCoralStationIntake(){
    return m_Controller3.getRawButton(4);}
public static Boolean buttonIntakeAdjust(){
    return m_Controller3.getRawButton(7);}
public static Boolean buttonResetIntake(){
    return m_Controller3.getRawButton(5);}
public static Boolean buttonTransportPivot(){
    return m_Controller3.getRawButton(6);}
public static Boolean buttonRevOuttake(){
    return m_Controller3.getRawButton(8);}
public static Boolean switchAlgaeIntake(){
    return !(m_Controller3.getRawButton(10));}
public static Boolean switchLaserCan(){
    return m_Controller3.getRawButton(9);}
public static Boolean buttonEBrake(){
    return m_Controller3.getRawButton(1);}

/*unused buttons: 
    m_Controller2.getRawButton(5);
    m_Controller3.getRawButton(2);
    m_Controller3.getRawButton(12);
*/
public static Boolean buttonTestTrigger(){
    return m_Controller3.getRawButton(2);
}     
public static Boolean buttonElevatorUp(){
    return m_Controller2.getRawButton(6);}
public static Boolean buttonElevatorDown(){
    return m_Controller2.getRawButton(7);}
public static Boolean buttonL4(){
    return m_Controller2.getRawButton(1);}
public static Boolean buttonL3(){
    return m_Controller2.getRawButton(2);}
public static Boolean buttonL2(){
    return m_Controller2.getRawButton(3);}
public static Boolean buttonL1(){
    return m_Controller2.getRawButton(4);}
public static Boolean buttonResetElevator(){
    return m_Controller2.getRawButton(8);}
      
public static double ReefPosition(){
    return (22-((m_Controller2.getRawButton(11))?1:0)-((m_Controller2.getRawButton(10))?2:0)-((m_Controller2.getRawButton(9))?3:0)-((m_Controller2.getRawButton(12))?6:0))%12;
    //return (22-Math.round((m_Controller2.getRawAxis(3)+1)/2)-Math.round((m_Controller2.getRawAxis(1)+1))-Math.round((m_Controller2.getRawAxis(0)+1)*1.5)-Math.round((m_Controller2.getRawAxis(4)+1)*3))%12;
    /*if (m_Controller2.getRawButton(10))
        return (((2-m_Controller2.getRawAxis(0))%3 + Math.round(m_Controller2.getRawAxis(1))*3)+5)%12+1;
    return ((-m_Controller2.getRawAxis(0)+2)%3 + Math.round(m_Controller2.getRawAxis(1))*3+11)%12+1;*/
}
      
public static int getLevel(){
    if(buttonL2()) return 2;
    if(buttonL3()) return 3;
    if(buttonL4()) return 4;
    return 0;
}

public static void SwerveControlSet(boolean command){
    SwerveCommandControl = command;
    SwerveInputPeriodic();
}
public static void SwerveInputPeriodic(){
    if (SwerveCommandControl){ // Command Mode
        SwerveEncoderPassthrough = SwerveCommandEncoderValue;
        SwerveXPassthrough = SwerveCommandXValue;
        SwerveYPassthrough = SwerveCommandYValue;
    }
    else{ //Controller Mode
        SwerveEncoderPassthrough = Rotary_Controller.RotaryJoystick(m_Controller1);
        SwerveXPassthrough = -RobotContainer.betterJoystickCurve(m_Controller0.getLeftX(), m_Controller0.getLeftY())[0];
        SwerveYPassthrough = -RobotContainer.betterJoystickCurve(m_Controller0.getLeftX(), m_Controller0.getLeftY())[1];
        //SwerveXPassthrough = -RobotContainer.joystick_curve(m_Controller0.getLeftY());
        //SwerveYPassthrough = -RobotContainer.joystick_curve(m_Controller0.getLeftX());
    }
}


}
