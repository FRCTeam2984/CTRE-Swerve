// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import com.ctre.phoenix6.SignalLogger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.AutoDriveTest;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
public class Robot extends TimedRobot {
  public static int currentLevel = 0;
  public Command m_autonomousCommand;
  public static final RobotContainer m_robotContainer = new RobotContainer();
  String intakeState = "retract";
  Boolean armButtonLastPressed = false, retractElevatorArm = true;

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
    Driver_Controller.SwerveControlSet(true);
    // init controllers???
    Driver_Controller.define_Controller();
    // init limelight
    Limelight.limelightInit();
    // reset robot orientation
    RobotContainer.rotaryCalc(true);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    Driver_Controller.SwerveInputPeriodic();
    //Driver_Controller.SwerveCommandXValue = -0.5;
    //Driver_Controller.SwerveCommandEncoderValue = 300;
    // todo: drive 1m south (towards driver/operators)
    // todo: reset rotary_joystick
  }

  @Override
  public void autonomousExit() {
    Driver_Controller.SwerveControlSet(false);
  }

  @Override
  public void teleopInit() {
    Elevator.extendedOrRetracted = "retracted";
    Limelight.limelightInit();
    Driver_Controller.define_Controller();
    Intake.currentState = "none";
    Intake.intakeLastUsed = 'D';
    RobotContainer.rotaryCalc(true); // reset rotary joystick to robot angle
    currentLevel = -1;
    Intake.timer = 0;
  }

  @Override
  public void teleopPeriodic() {
    Boolean elevatorArmButton = Driver_Controller.buttonL1();
    Double elevatorPosition = Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10));
    Limelight.limelightOdometryUpdate();
    Driver_Controller.SwerveInputPeriodic();
    Intake.intakeLastUsed = 'C';
    if (Elevator.elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround"){Elevator.bottomPosition = elevatorPosition;}
    if (Intake.outsideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition();}
    else if (Intake.insideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition() - 48.64;}

  if (Driver_Controller.buttonResetElevator()) currentLevel = 0;
  if (Driver_Controller.buttonL2()) currentLevel = 2;
  if (Driver_Controller.buttonL3()) currentLevel = 3;
  if (Driver_Controller.buttonL4()) currentLevel = 4;
  //currentLevel = 0;
  switch (currentLevel){
    case (0):
      if (elevatorPosition > 3 && Driver_Controller.buttonResetElevator()) Elevator.elevatorTo(-99999.0);
      else if (Elevator.elevatorMotor.getReverseLimit().getValue().toString() != "ClosedToGround" && Driver_Controller.buttonResetElevator()) Elevator.elevatorMotor.set(-0.3);
      else Elevator.elevatorMotor.set(0.0);
      break;
    case (2):
      if (Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l2Position)) currentLevel = -1;
      break;
    case (3):
      if (Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l3Position)) currentLevel = -1;
      break;
    case (4):
      if (Elevator.elevatorTo(Elevator.bottomPosition+Elevator.l4Position)) currentLevel = -1;
      break;
  }
  
  //System.out.println(Elevator.bottomPosition-Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)));
  if (elevatorArmButton && armButtonLastPressed == false) retractElevatorArm = retractElevatorArm^true;
  if (!retractElevatorArm && Elevator.extendedOrRetracted != "extended") Elevator.moveElevatorArm("extend"); 
  else if (retractElevatorArm && Elevator.extendedOrRetracted != "retracted") Elevator.moveElevatorArm("retract");
  armButtonLastPressed = elevatorArmButton;
    
    if (Driver_Controller.buttonTransportPivot()){
      Intake.currentState = "run belt";
      Intake.timer = 0;
    }
    Intake.moveCoral();
    Climb.letsClimb();
    
    intakeState = "retract";
    if (Driver_Controller.switchAlgaeIntake() == false){
      intakeState = "intake algae";
      if (Driver_Controller.buttonScoreAlgae())
        Intake.bottomIntake.set(0.5);
      else{
        if (Intake.bottomIntake.getOutputCurrent() < 5){
          Intake.bottomIntake.set(-0.5);
        }else{
          Intake.bottomIntake.set(-0.1);
        }
      }
    }
    else if (Driver_Controller.buttonReverseCoral()) intakeState = "reset intake";
    else if (Driver_Controller.buttonCoralIntakeGround()) intakeState = "ground intake";
    else if (Driver_Controller.buttonCoralStationIntake()) intakeState = "station intake";
    
    switch(intakeState){
      case "reset intake": 
        if (Intake.outsideSwitch.isPressed()) Intake.intakePivot.set(0);
        else Intake.intakePivot.set(-0.3);
        Intake.intakeLastUsed = 'D';
        break;
      case "ground intake": Intake.intakeCoral(Driver_Controller.buttonResetIntake()); break;
      case "station intake": Intake.intakeTo("stationIntakeCoral"); Intake.spinRollers(0.5); break;
      case "intake algae": Intake.intakeTo("intakeAlgae"); break;
      case "retract": Intake.retractIntake();
    }
    
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
      SwerveCommandControl
        SwerveCommandEncoderValue;
        SwerveCommandXValue;
        SwerveCommandYValue;
     // swerveOverrideJoy_y = 0;
     // swerveOverrideJoy_x = 0;
     // swerveOverrideJoy_rotate = 0;
     System.out.println("Robot.java LimelightPose2d is < 5");
    }
    //serveOverrideJoy = true;
  }
}
