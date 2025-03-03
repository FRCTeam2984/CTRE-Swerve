// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Rotary_Controller;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private Joystick joystick;
  private final NewerOLDRobotContainer m_robotContainer;

  public Robot() {
    m_robotContainer = new NewerOLDRobotContainer();
  }
  @Override
  public void robotInit() {
    joystick = new Joystick(1); // Create a joystick on port 1
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
    if (m_robotContainer.speedSwitch()) System.out.println("speedSwitch");
    if (m_robotContainer.buttonExtendClimb()) System.out.println("buttonExtendClimb");
    if (m_robotContainer.buttonRetractClimb()) System.out.println("buttonRetractClimb");
    if (m_robotContainer.buttonRemoveAlgae()) System.out.println("buttonRemoveAlgae");
    if (m_robotContainer.buttonTransportPivot()) System.out.println("buttonTransportPivot");
    if (m_robotContainer.buttonScoreAlgae()) System.out.println("buttonScoreAlgae");
    if (m_robotContainer.buttonCoralStationIntake()) System.out.println("buttonCoralStationIntake");
    if (m_robotContainer.switchAlgaeIntake()) System.out.println("switchAlgaeIntake");
    if (m_robotContainer.switchExtraOnOff()) System.out.println("switchExtraOnOff");
    if (m_robotContainer.buttonL4()) System.out.println("buttonL4");
    if (m_robotContainer.buttonL3()) System.out.println("buttonL3");
    if (m_robotContainer.buttonL2()) System.out.println("buttonL2");
    if (m_robotContainer.buttonL1()) System.out.println("buttonL1");
    if (m_robotContainer.buttonResetElevator()) System.out.println("buttonResetElevator");
    if (m_robotContainer.buttonCoralIntakeGround()) System.out.println("buttonCoralIntakeGround");
    if (m_robotContainer.buttonReverseCoral()) System.out.println("buttonReverseCoral");
    System.out.println(m_robotContainer.buttonReefPosition());
    System.out.println(m_robotContainer.getLevel());
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println(Rotary_Controller.RotaryJoystick(joystick));
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
