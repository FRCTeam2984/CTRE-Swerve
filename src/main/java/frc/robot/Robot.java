// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
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
    Limelight.limelightInit();
    SignalLogger.enableAutoLogging(true);
    //SignalLogger.stop();
    Driver_Controller.define_Controller();
    Intake.currentState = "none";
    
  }

  @Override
  public void teleopPeriodic() {
    Limelight.limelightOdometryUpdate();
    Intake.intakeLastUsed = 'C';
    if (Intake.outsideSwitch.isPressed()){
      Intake.inPosition = Intake.intakeEncoder.getPosition();
    } else if (Intake.insideSwitch.isPressed()){
      Intake.inPosition = Intake.intakeEncoder.getPosition() - 48.64;
    }
   SignalLogger.enableAutoLogging(false);
    SignalLogger.stop();
  if (Driver_Controller.buttonL2())Elevator.elevatorMotor.set(1);// Elevator.elevatorTo(20.0);
  if (Driver_Controller.buttonL1() && Elevator.elevatorMotor.getReverseLimit().getValue().toString() != "ClosedToGround"){
    Elevator.elevatorMotor.set(-0.5);
  }else if (Driver_Controller.buttonResetElevator()){
    Elevator.elevatorMotor.set(0);
  }

  if (Driver_Controller.buttonRemoveAlgae()) Intake.moveCoral();
  if (Driver_Controller.buttonTransportPivot()) Intake.currentState = "start";
  Climb.letsClimb();
  System.out.println(Intake.intakeEncoder.getPosition()-Intake.inPosition);

    if (Driver_Controller.buttonCoralStationIntake()) Intake.retractIntake();
    else if (Driver_Controller.buttonCoralIntakeGround()) Intake.intakeCoral(Driver_Controller.buttonReverseCoral());
    else Intake.intakePivot.set(0);
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
