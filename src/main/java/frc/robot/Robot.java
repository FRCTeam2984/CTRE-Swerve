// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.OperatorConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  public static final RobotContainer m_robotContainer = new RobotContainer();

  public Robot() {
  }
  @Override
  public void robotInit() {
    

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
    Driver_Controller.define_Controller();
    // if (m_autonomousCommand != null) {
    //   m_autonomousCommand.cancel();
    // }
    // if (m_robotContainer.speedSwitch()) System.out.println("speedSwitch");
    // if (m_robotContainer.buttonExtendClimb()) System.out.println("buttonExtendClimb");
    // if (m_robotContainer.buttonRetractClimb()) System.out.println("buttonRetractClimb");
    // if (m_robotContainer.buttonRemoveAlgae()) System.out.println("buttonRemoveAlgae");
    // if (m_robotContainer.buttonTransportPivot()) System.out.println("buttonTransportPivot");
    // if (m_robotContainer.buttonScoreAlgae()) System.out.println("buttonScoreAlgae");
    // if (m_robotContainer.buttonCoralStationIntake()) System.out.println("buttonCoralStationIntake");
    // if (m_robotContainer.switchAlgaeIntake()) System.out.println("switchAlgaeIntake");
    // if (m_robotContainer.switchExtraOnOff()) System.out.println("switchExtraOnOff");
    // if (m_robotContainer.buttonL4()) System.out.println("buttonL4");
    // if (m_robotContainer.buttonL3()) System.out.println("buttonL3");
    // if (m_robotContainer.buttonL2()) System.out.println("buttonL2");
    // if (m_robotContainer.buttonL1()) System.out.println("buttonL1");
    // if (m_robotContainer.buttonResetElevator()) System.out.println("buttonResetElevator");
    // if (m_robotContainer.buttonCoralIntakeGround()) System.out.println("buttonCoralIntakeGround");
    // if (m_robotContainer.buttonReverseCoral()) System.out.println("buttonReverseCoral");
    // System.out.println(m_robotContainer.buttonReefPosition());
    // System.out.println(m_robotContainer.getLevel());
    System.out.println("\nelevator encoder: ");
    System.out.println(Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)));
    System.out.println("\nintake encoder: ");
    System.out.println(Intake.intakeEncoder.getPosition());
    System.out.println("\nintake transport encoder: ");
    System.out.println(Intake.transportEncoder.getPosition());
    AutoDriveTest.driveStraightToCircle();
    switch(0){
      case (0): if (Driver_Controller.buttonResetElevator()) Intake.beltDrive.set(ControlMode.PercentOutput, 0.05); else Intake.beltDrive.set(ControlMode.PercentOutput, 0); break;
      case (1): if (Driver_Controller.buttonResetElevator()) Intake.bottomIntake.set(0.05); else Intake.bottomIntake.set(0); break;
      case (2): if (Driver_Controller.buttonResetElevator()) Intake.topIntake.set(0.05); else Intake.topIntake.set(0); break;
      case (3): if (Driver_Controller.buttonResetElevator()) Intake.transportPivot.set(0.05); else Intake.transportPivot.set(0); break;
      case (4): if (Driver_Controller.buttonResetElevator()) Intake.intakePivot.set(0.05); else Intake.intakePivot.set(0); break;
      case (5): if (Driver_Controller.buttonResetElevator()) Elevator.elevatorMotor.set(0.05); else Elevator.elevatorMotor.set(0); break;
      case (6): if (Driver_Controller.buttonResetElevator()) Elevator.armMotor.set(0.05); else Elevator.armMotor.set(0); break;
      case (7): if (Driver_Controller.buttonResetElevator()) Climb.climb.set(0.05); else Climb.climb.set(0); break;
    }
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println(Rotary_Controller.RotaryJoystick(joystick));
    // AutoDriveTest.determineArea(kDefaultPeriod, kDefaultPeriod, kDefaultPeriod);
    AutoDriveTest.driveStraightToCircle();
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
