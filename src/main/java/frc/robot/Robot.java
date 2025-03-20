// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.SignalLogger;
// import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import frc.robot.subsystems.AutoDriveTest;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import networktablesdesktopclient.NetworkTablesDesktopClient;
import com.ctre.phoenix6.swerve.*;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveControlParameters;

public class Robot extends TimedRobot {
  public static int currentLevel = 0;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public Command m_autonomousCommand;
  public static final RobotContainer m_robotContainer = new RobotContainer();
  String intakeState = "retract";
  Boolean armButtonLastPressed = false, retractElevatorArm = true;
  
  public Robot() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
    // double[] value = table.getEntry("robotPose")
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
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
    // AutoDriveFinal.AutoDriveFinal(0, 0, 0, 0);
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
    Driver_Controller.SwerveControlSet(true);
    Driver_Controller.SwerveCommandEncoderValue=RobotContainer.robotOffset+RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble();//Driver_Controller.SwerveCommandEncoderValue = 0;
    Driver_Controller.SwerveCommandXValue = 0;
    Driver_Controller.SwerveCommandYValue = 0;
    
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

  }

  @Override
  public void autonomousPeriodic() {
    //driveSouthPastLine();
    Driver_Controller.SwerveInputPeriodic();
    //AutoDriveFinal.AutoDriveFinal(0, 0, 0, 0);
    //System.out.println(RobotContainer.drivetrain.getState().Pose.getX());
    // AutoDriveTest.AutoDrive(0, 0, 0);
    //System.out.println(RobotContainer.drivetrain.SwerveDriveState.Pose);

    //Driver_Controller.SwerveCommandXValue = -0.5;
    //Driver_Controller.SwerveCommandEncoderValue = 300;
    // todo: drive 1m south (towards driver/operators)
    // todo: reset rotary_joystick
    // 4.07, 3.25
    if (Driver_Controller.m_Controller2.getRawButton(4)){
      System.out.println(Math.tan((4.07 - RobotContainer.drivetrain.getState().Pose.getX())/(3.25 - RobotContainer.drivetrain.getState().Pose.getY())));
      Driver_Controller.SwerveControlSet(true);
      Driver_Controller.SwerveCommandEncoderValue = Math.tan((4.07 - RobotContainer.drivetrain.getState().Pose.getX())/(3.25 - RobotContainer.drivetrain.getState().Pose.getY()));// * 180/3.14;
    }
    else{
      Driver_Controller.SwerveControlSet(false);
    }
    switch (m_autoSelected) {
      case kDefaultAuto:
      default:
        System.out.println("default ran");
        break;
      case kCustomAuto:
        System.out.println("custom ran");
      break;
    }
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
    RobotContainer.rotaryCalc(true); // reset rotary joystick to robot angle
    Intake.currentState = "none";
    Intake.intakeLastUsed = 'D';
    
    currentLevel = -1;
    Intake.timer = 0;
    Driver_Controller.SwerveControlSet(false);
  }

  @Override
  public void teleopPeriodic() {
    
    // NetworkTablesDesktopClient.getRobotPosition();
    Boolean elevatorArmButton = Driver_Controller.buttonL1();
    Double elevatorPosition = Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10));
    Limelight.limelightOdometryUpdate();
    Driver_Controller.SwerveInputPeriodic();
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
      if (Elevator.elevatorTo(Elevator.l2Position)) currentLevel = -1;
      break;
    case (3):
      if (Elevator.elevatorTo(Elevator.l3Position)) currentLevel = -1;
      break;
    case (4):
      if (Elevator.elevatorTo(Elevator.l4Position)) currentLevel = -1;
      break;
  }
  
  //System.out.println(Elevator.bottomPosition-Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10)));
  if (elevatorArmButton && armButtonLastPressed == false) retractElevatorArm = retractElevatorArm^true;

  if (!retractElevatorArm){
    if (Elevator.extendedOrRetracted != "extended") Elevator.moveElevatorArm("extend");
    else if (elevatorArmButton) Elevator.armMotor.set(ControlMode.PercentOutput, 0.025);
    else Elevator.armMotor.set(ControlMode.PercentOutput, 0.0);
  }else{
    if (Elevator.extendedOrRetracted != "retracted") Elevator.moveElevatorArm("retract");
    else if (elevatorArmButton) Elevator.armMotor.set(ControlMode.PercentOutput, -0.025);
    else Elevator.armMotor.set(ControlMode.PercentOutput, 0.0);
  }
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
    intakeState = "im jealous that justin is with lukas now and im really sad :(";
    switch(intakeState){
      case "reset intake": 
        if (Intake.outsideSwitch.isPressed()) Intake.intakePivot.set(0);
        else Intake.intakePivot.set(-0.3);
        Intake.intakeLastUsed = 'D';
        break;
      case "ground intake": Intake.intakeCoral(Driver_Controller.buttonResetIntake()); break;
      case "station intake": Intake.intakeTo("stationIntakeCoral"); Intake.spinRollers(0.35); break;
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
    Double desiredPosition = 6.5;
    String alliance = "none";
    if (DriverStation.getAlliance().toString().charAt(9) == 'B'){
      alliance = "blue";
      desiredPosition = 6.5;
    }else if (DriverStation.getAlliance().toString().charAt(9) == 'R'){
      alliance = "red";
      desiredPosition = 11.0;
    }
    Limelight.limelightOdometryUpdate();
    System.out.println(RobotContainer.drivetrain.getState().Pose.getX());
    if ((alliance == "blue")?RobotContainer.drivetrain.getState().Pose.getX() > desiredPosition:RobotContainer.drivetrain.getState().Pose.getX() < desiredPosition){
        // move past the starting line
       // don't turn
      Driver_Controller.SwerveCommandXValue=((alliance == "blue")?-0.5:-0.5);
      Driver_Controller.SwerveCommandYValue=0; // drive slowly towards the driver/operator stations
      System.out.println("Robot.java LimelightPose2d is > 5");
    } else {
        // don't move
      //Driver_Controller.SwerveCommandEncoderValue=RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble();
      Driver_Controller.SwerveCommandXValue=0;
      Driver_Controller.SwerveCommandYValue=0;
     System.out.println("Robot.java LimelightPose2d is < 5");
    }
    Driver_Controller.SwerveControlSet(true);
  }
}