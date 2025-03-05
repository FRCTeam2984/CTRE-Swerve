/*
 * Subsystem to run at RobotInit to read and auto set the controllers
 * so they are configured correctly before driving the robot and 
 * without user intervention.
 */
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


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
    public static CommandXboxController m_Controller0;
    public static XboxController m_Controller1;
    public static XboxController m_Controller2;
    public static XboxController m_Controller3;
    private static XboxController m_tempController;
    public static void define_Controller(){
    for(int i=1; i<=4; i++) {
        switch (i) {
            case 1:
                m_tempController = new XboxController(0);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(0);
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = m_tempController;
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
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = m_tempController;
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
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }
            case 4:
                m_tempController = new XboxController(3);
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller0 = new CommandXboxController(3);
                }
                if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller1 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)) {
                    m_Controller2 = m_tempController;
                }
                if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)) {
                    m_Controller3 = m_tempController;
                }

        }
    }
    
}}
