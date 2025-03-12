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
import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkLimitSwitch;
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
    SignalLogger.enableAutoLogging(true);
    //SignalLogger.stop();
    Driver_Controller.define_Controller();
    Intake.currentState = "none";
    Intake.inPosition = Intake.intakeEncoder.getPosition();
  }

  @Override
  public void teleopPeriodic() {
    SignalLogger.enableAutoLogging(false);
    SignalLogger.stop();
  /*if (Driver_Controller.buttonL2()) Elevator.elevatorTo(20.0);
  if (Driver_Controller.buttonL1() && Elevator.elevatorMotor.getReverseLimit().getValue().toString() != "ClosedToGround"){
    Elevator.elevatorMotor.set(-0.05);
  }else if (Driver_Controller.buttonResetElevator()){
    Elevator.elevatorMotor.set(0);
  }
  
  if (Driver_Controller.buttonRemoveAlgae()) Intake.moveCoral();
  if (Driver_Controller.buttonTransportPivot()) Intake.currentState = "start";
  Climb.letsClimb();
  System.out.println(Intake.intakeEncoder.getPosition()-Intake.inPosition);*/
    if (Driver_Controller.buttonCoralStationIntake()) Intake.retractIntake();
    else if (Driver_Controller.buttonCoralIntakeGround()) Intake.intakeCoral(Driver_Controller.buttonReverseCoral());
    else Intake.intakePivot.set(0.0);
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
