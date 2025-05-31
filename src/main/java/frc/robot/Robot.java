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
import frc.robot.subsystems.AutoDriveFinal;
//import frc.robot.subsystems.Climb;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.LED;
// import networktablesdesktopclient.NetworkTablesDesktopClient;
// import com.ctre.phoenix6.swerve.*;
// import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveControlParameters;

public class Robot extends TimedRobot {
  Boolean schedule = false, lastPressed = false;
  int scoringPos = (int) Driver_Controller.buttonReefPosition();
  private static final String kDefaultAuto = "score + de-algae 1x";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public Command m_autonomousCommand;
  public static final RobotContainer m_robotContainer = new RobotContainer();
  Boolean autoDriveLastPressed = false;

  String state = "drive past line";  // for setting autonomous state

  public Robot() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
    // double[] value = table.getEntry("robotPose")
    m_chooser.setDefaultOption("score + de-algae 1x", kDefaultAuto);
    m_chooser.addOption("JUST DRIVE OUT OF ZONE", kCustomAuto);
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
    Driver_Controller.SwerveCommandEncoderValue=/*RobotContainer.robotOffset+*/RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble();//Driver_Controller.SwerveCommandEncoderValue = 0;
    Driver_Controller.SwerveCommandXValue = 0;
    Driver_Controller.SwerveCommandYValue = 0;
    
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    state = "drive past line";

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
    // m_autoSelected = kDefaultAuto;
    switch (m_autoSelected) {
      case kDefaultAuto:
        /* int sDSPL = 1;
        int sAutoDrive = 2;
        int sArm = 3; */
        /*
        // System.out.println("works");
        switch(state){
          case "drive past line":
          AutoDriveFinal.AutoDrive();
            //if(driveSouthPastLine()){
              System.out.println("auto");
              //state = "auto";
            //}
            
            break;
          case "auto":
          // AutoDriveFinal.AutoDrive();
          Driver_Controller.SwerveControlSet(true);
            RobotContainer.rotaryCalc(true);
            //RobotContainer.drivingOn = 0;
            if(AutoDriveFinal.AutoDrive()){
              state = "drive";
              System.out.println("switch to outtake");
            }
            
              
            break;
          case "outtake":
            // RobotContainer.drivingOn = 0;

            if((scoringPos == 2) || (scoringPos == 6) ||(scoringPos == 10)){
              if (Elevator.elevatorTo(Elevator.levelPosition[4])){
                state = "outtake 2";
                System.out.println("outtake 2");
              }
            }
            else{
              if (Elevator.elevatorTo(Elevator.levelPosition[3])){  // score at L3
                // System.out.println("elevator L3");
                state = "outtake 2";
                System.out.println("outtake 2");
              }
            }
            // System.out.println("outtake");
            // state = "outtake 2";
            break;
          case "outtake 2":
            if (Elevator.extendedOrRetracted != "extended"){
              if(Elevator.lastExtendOrRetract == "retract"){
                Elevator.armTimer = 0.0;
              }
              Elevator.moveElevatorArm("extend");
            }else{
                System.out.println("switch to elevator down");
                state = "elevator down";
            }
            //state = "elevator down";
            break;
          case "elevator down":
            if (((scoringPos%4) == 1 || (scoringPos%4) == 2)?Elevator.elevatorTo(Elevator.levelPosition[3]+40):Elevator.elevatorTo(Elevator.levelPosition[2]+40)){
              state = "drive back";
              System.out.println("switch to drive back");
            }
            break;
          case "drive back":
            scoringPos = (int) Driver_Controller.buttonReefPosition();
            // System.out.println("drive back");
            if(AutoDriveFinal.driveBackwards(scoringPos)){
              System.out.println("break");
            }
            break;
              
          default:
            System.out.println("default inside");
        }*/

        break;
      case kCustomAuto:
        if(driveSouthPastLine())
          System.out.println("drive past line");

        
      break;
      default:
        System.out.println("default");
    }
  }

  @Override
  public void autonomousExit() {
    Driver_Controller.SwerveControlSet(false);
  }

  @Override
  public void teleopInit() {
    //Elevator.extendedOrRetracted = "retracted";
    Limelight.limelightInit();
    Driver_Controller.define_Controller();
    Driver_Controller.SwerveControlSet(false);
    RobotContainer.rotaryCalc(true); // reset rotary joystick to robot angle
    
    Elevator.currentLevel = -1;
  }

  @Override
  public void teleopPeriodic() {
    // NetworkTablesDesktopClient.getRobotPosition();
    if (Driver_Controller.driverSwitch()) {
      Limelight.limelightOdometryUpdate();
    }
    Driver_Controller.SwerveInputPeriodic();

    // Send Data to LED Arduino
    LED.sendData();

    Boolean outtakeButton = Driver_Controller.buttonL1();
    // setting elevator position
    if (Driver_Controller.buttonResetElevator()) Elevator.currentLevel = 0;
    if (Driver_Controller.buttonL2()) Elevator.currentLevel = 2;
    if (Driver_Controller.buttonL3()) Elevator.currentLevel = 3;
    if (Driver_Controller.buttonL4()) Elevator.currentLevel = 4;
    Elevator.currentLevel = -1;

    // dealing with outtake
    if (outtakeButton) Elevator.armMotor.set(0.5);

    //Climb.letsClimb();
    
    // dealing with intake
    
    if (Driver_Controller.switchAlgaeIntake() == false){
      Intake.intakeState = "intake";
      if (Driver_Controller.buttonScoreAlgae()) Intake.bottomIntake.set(0.3);
      else if (Intake.bottomIntake.getOutputCurrent() < 5) Intake.bottomIntake.set(-0.5);
      else Intake.bottomIntake.set(-0.1);
    }else{
      if (Driver_Controller.buttonCoralStationIntake()){
        Intake.intakeState = "remove";
        Intake.bottomIntake.set(0.3);
      } else{
        Intake.bottomIntake.set(0.0);
        if (Driver_Controller.buttonReverseCoral()){
          Intake.intakeState = "reset";
        }else Intake.intakeState = "retract";
      }
    }

    // handling AutoDrive
    if (Driver_Controller.buttonAutoDrive()){
      Driver_Controller.SwerveControlSet(true);
      RobotContainer.rotaryCalc(true);
      //RobotContainer.drivingOn = 0;
      AutoDriveFinal.AutoDrive();/*
      if (Driver_Controller.getLevel() != 0){
        if (){
          Elevator.elevatorTo((double) Driver_Controller.getLevel());

        }
      }
      else {
        Driver_Controller.SwerveCommandEncoderValue=RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble();
        Driver_Controller.SwerveCommandXValue=0;
        Driver_Controller.SwerveCommandYValue=0;
      }*/
    } 
    else {
      RobotContainer.drivingOn = 1;
      Driver_Controller.SwerveControlSet(false);
      if (autoDriveLastPressed){
        RobotContainer.rotaryCalc(true);
      }
    } 
    autoDriveLastPressed = Driver_Controller.buttonAutoDrive();
    //Elevator.elevatorPeriodic();
    Intake.intakePeriodic();
    //if (Driver_Controller.buttonCoralIntakeGround()) Elevator.elevatorMotor.set(-0.1);

    if (Driver_Controller.buttonResetElevator() && (!lastPressed)){
      m_autonomousCommand = RobotContainer.schedulePathplannerMove("15 - Abs Ideal -- V3 (B1, A2, A1, F2, F1)");
      lastPressed = true;
      schedule = true;
    } else
      lastPressed = Driver_Controller.buttonResetElevator();
    if (Driver_Controller.buttonL1()) schedule = false;
    if (schedule) m_autonomousCommand.schedule();
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
  public boolean driveSouthPastLine() {
    boolean retval = true;
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
      retval = false;
    } else {
        // don't move
      //Driver_Controller.SwerveCommandEncoderValue=RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble();
      Driver_Controller.SwerveCommandXValue=0;
      Driver_Controller.SwerveCommandYValue=0;
     System.out.println("Robot.java LimelightPose2d is < 5");
      retval=true;
    }
    Driver_Controller.SwerveControlSet(true);
    if(RobotContainer.drivetrain.getState().Pose.getX() < 1){
      retval = false;
    }
    return(retval);
  }
}