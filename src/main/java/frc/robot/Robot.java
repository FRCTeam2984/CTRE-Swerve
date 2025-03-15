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
  public static int currentLevel = 0;
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
    // todo: init controllers???
    // todo: init limelight
    // todo: reset robot orientation
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {

    // todo: drive 1m south (towards driver/operators)
    // todo: reset rotary_joystick
  }

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    Elevator.extendedOrRetracted = "retracted";
    Limelight.limelightInit();
    Driver_Controller.define_Controller();
    Intake.currentState = "none";
    RobotContainer.rotaryCalc(true); // reset rotary joystick to robot angle
    currentLevel = 0;
  }

  @Override
  public void teleopPeriodic() {
    Double elevatorPosition = Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10));
    Limelight.limelightOdometryUpdate();
    Intake.intakeLastUsed = 'C';
    if (Elevator.elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround"){Elevator.bottomPosition = elevatorPosition;}
    if (Intake.outsideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition();}
    else if (Intake.insideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition() - 48.64;}

  if (Driver_Controller.buttonResetElevator()) currentLevel = 0;
  if (Driver_Controller.buttonL2()) currentLevel = 2;
  if (Driver_Controller.buttonL3()) currentLevel = 3;
  if (Driver_Controller.buttonL4()) currentLevel = 4;
  currentLevel = -1;
  switch (currentLevel){
    case (0):
      if (elevatorPosition > 0) Elevator.elevatorTo(-99999.0);
      else if (Elevator.elevatorMotor.getReverseLimit().getValue().toString() != "ClosedToGround") Elevator.elevatorMotor.set(-0.3);
      else Elevator.elevatorMotor.set(0.0);
      break;
    case (2):
      Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l2Position);
      break;
    case (3):
      Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l3Position);
      break;
    case (4):
      Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l4Position);
      break;

  }
  
  System.out.println(Elevator.bottomPosition-Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)));

  if (Driver_Controller.switchAlgaeIntake() && Elevator.extendedOrRetracted != "extended") Elevator.moveElevatorArm("extend"); 
  else if (Driver_Controller.switchAlgaeIntake() == false && Elevator.extendedOrRetracted != "retracted") Elevator.moveElevatorArm("retract");

  Intake.moveCoral();
  if (Driver_Controller.buttonTransportPivot()) Intake.currentState = "start";
  Climb.letsClimb();

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
  public void driveSouthPastLine() {
    Limelight.limelightOdometryUpdate();
    if (Limelight.LimelightGeneratedPose2d.getY() > 5)
     // swerveOverrideJoy_x = 0;
     // swerveOverrideJoy_y = 0.1; // should be a fairly slow drive
     // swerveOverrideJoy_rotate = 0;
     System.out.println("Robot.java LimelightPose2d is > 5");
    else {
     // swerveOverrideJoy_y = 0;
     // swerveOverrideJoy_x = 0;
     // swerveOverrideJoy_rotate = 0;
     System.out.println("Robot.java LimelightPose2d is < 5");
    }
    //serveOverrideJoy = true;
  }
}
