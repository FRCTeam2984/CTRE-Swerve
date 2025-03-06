// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Rotary_Controller;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.AutoDriveTest;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  public static final NewerOLDRobotContainer m_robotContainer = new NewerOLDRobotContainer();

  public Robot() {
  }
  @Override
  public void robotInit() {
      GenericHID m_driverController1, m_driverController2, m_driverController3, m_driverController4;
    GenericHID m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort1);
    // // Trigger controllerIDbutton1 = m_tempController.button(11);
    // // Trigger controllerIDbutton2 = m_tempController.button(12);

    // m_tempController.button(11)
    //     .and(m_tempController.button(12))
    //     .onTrue(
    //       private final CommandGenericHID m_driverController1 =
    //           new CommandGenericHID(OperatorConstants.kDriverControllerPort1);
    //     )

    for(int i=1; i<=4; i++) {
      switch(i) {
        case 1:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort1);
        case 2:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort2);
        case 3:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort3);
        case 4:
          m_tempController = new GenericHID(OperatorConstants.kDriverControllerPort4);
      }

      if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == false)){
        m_driverController1 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == false) && (m_tempController.getRawButton(12) == true)){
        m_driverController2 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == true)){
        m_driverController3 = m_tempController;
        System.out.println(m_tempController);
      }
      if ((m_tempController.getRawButton(11) == true) && (m_tempController.getRawButton(12) == false)){
        m_driverController4 = m_tempController;
        System.out.println(m_tempController);
      }
  }
}
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    //if (m_robotContainer.speedSwitch()) System.out.println("speedSwitch");
    //if (m_robotContainer.buttonExtendClimb()) System.out.println("buttonExtendClimb");
    //if (m_robotContainer.buttonRetractClimb()) System.out.println("buttonRetractClimb");
  //   if (m_robotContainer.buttonRemoveAlgae()) System.out.println("buttonRemoveAlgae");
  //   if (m_robotContainer.buttonTransportPivot()) System.out.println("buttonTransportPivot");
  //   if (m_robotContainer.buttonScoreAlgae()) System.out.println("buttonScoreAlgae");
  //   if (m_robotContainer.buttonCoralStationIntake()) System.out.println("buttonCoralStationIntake");
  //   if (m_robotContainer.switchAlgaeIntake()) System.out.println("switchAlgaeIntake");
  //   if (m_robotContainer.switchExtraOnOff()) System.out.println("switchExtraOnOff");
  //   if (m_robotContainer.buttonL4()) System.out.println("buttonL4");
  //   if (m_robotContainer.buttonL3()) System.out.println("buttonL3");
  //   if (m_robotContainer.buttonL2()) System.out.println("buttonL2");
  //   if (m_robotContainer.buttonL1()) System.out.println("buttonL1");
  //   if (m_robotContainer.buttonResetElevator()) System.out.println("buttonResetElevator");
  //   if (m_robotContainer.buttonCoralIntakeGround()) System.out.println("buttonCoralIntakeGround");
  //   if (m_robotContainer.buttonReverseCoral()) System.out.println("buttonReverseCoral");
  //   System.out.println(m_robotContainer.buttonReefPosition());
  //   System.out.println(m_robotContainer.getLevel());
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println(Rotary_Controller.RotaryJoystick(joystick));
    AutoDriveTest.determineArea(0.0,0.0,0.0);
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {
    
  }
}
