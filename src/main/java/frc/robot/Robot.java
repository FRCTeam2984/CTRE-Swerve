// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.auto.AutoBuilder;
// import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPLTVController;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.TimeUnit;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.AutoDriveFinal;
import frc.robot.subsystems.NewAutoDrive;
//import frc.robot.subsystems.Climb;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.LED;
import au.grapplerobotics.CanBridge;
import au.grapplerobotics.LaserCan;
// import networktablesdesktopclient.NetworkTablesDesktopClient;
// import com.ctre.phoenix6.swerve.*;
// import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveControlParameters;
// {0.0, 15.0, 62.0, 116.0, 186.0};
public class Robot extends TimedRobot {
  Boolean elevatorEnabled = true, useNewAutoDrive = true;
  Boolean schedule = false, lastPressed = false, lastSwitch = true;
  public static int scoringPos = (int) Driver_Controller.ReefPosition();
  // private static final String kDefaultAuto = "score + de-algae 1x";
  // private static final String kCustomAuto = "My Auto";
  // private String m_autoSelected;
  // private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public Command m_autonomousCommand;
  public static final RobotContainer m_robotContainer = new RobotContainer();
  Boolean autoDriveLastPressed = false;

  String m_autoSelected, alliance;

  // String state = "drive past line";  // for setting autonomous state

  public Robot() {
    NewAutoDrive.setupAutoDrive();
    CanBridge.runTCP();
    Elevator.sensorInit();
    NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
    // double[] value = table.getEntry("robotPose")
    // m_chooser.setDefaultOption("score + de-algae 1x", kDefaultAuto);
    // m_chooser.addOption("JUST DRIVE OUT OF ZONE", kCustomAuto);
    // SmartDashboard.putData("Auto choices", m_chooser);
    for (int i = 0; i < Intake.historyLength; ++i){
      Intake.rollerSpeed[i] = 0.0;
      Intake.rollerCurrent[i] = 0.0;
    }
    Elevator.enableOuttakeSensors = true;
  }
  @Override
  public void robotInit() {
    SignalLogger.setPath("/media/sda/");
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

  int mustDriveXYA = 1;
  @Override
  public void autonomousInit() {
    Elevator.currentLevel = -1;
    mustDriveXYA = 1;
    // AutoDriveFinal.AutoDriveFinal(0, 0, 0, 0);
    // init controllers???
    Driver_Controller.define_Controller();
    // init limelight
    Limelight.limelightInit();
    // reset robot orientation
    RobotContainer.rotaryCalc(true);
    RobotContainer.sliderAdjustment = 1.0;
    Intake.intakeState = "retract";
    RobotContainer.autoDriveMultiplier = 3.0;
  }
  Double outtakeTimer = 0.0;
  @Override
  public void autonomousPeriodic() {
    Elevator.elevatorPeriodic();
    Intake.intakePeriodic();
    Limelight.limelightOdometryUpdate();
    if (mustDriveXYA == 1){
      if (NewAutoDrive.driveToXYA(14.467, 6.316, -126.870, 2.0)) mustDriveXYA = 2;
      Driver_Controller.SwerveInputPeriodic();
    }else if (mustDriveXYA == 2) {
      m_autonomousCommand = m_robotContainer.getAutonomousCommand();
      if (m_autonomousCommand != null) {
        //m_autonomousCommand.schedule();
      }
      Elevator.currentLevel = 2;
      if ((Elevator.currentPosition-Elevator.levelPosition[2]) < Elevator.maxError && NewAutoDrive.driveToXYA(NewAutoDrive.scoringPosRed[10][0], NewAutoDrive.scoringPosRed[10][1], NewAutoDrive.scoringAngles[10]+180, 0.5)){
        mustDriveXYA = 3;
        outtakeTimer = 0.0;
      }
    }else if (mustDriveXYA == 3){
      NewAutoDrive.periodicDriveToLocation(false, "stay");
      outtakeTimer += 0.02;
      if (outtakeTimer > 0.2){
        Elevator.outtakeMotor.set(0.8);
      }
      if (outtakeTimer > 0.2+0.5){
        mustDriveXYA = 4;
        outtakeTimer = 0.0;
        Elevator.outtakeMotor.set(0.0);
      }
    }

  }

  @Override
  public void autonomousExit() {
    Driver_Controller.SwerveControlSet(false);
  }

  @Override
  public void teleopInit() {
    NewAutoDrive.setupAutoDrive();
    //Elevator.extendedOrRetracted = "retracted";
    Limelight.limelightInit();
    Driver_Controller.define_Controller();
    Driver_Controller.SwerveControlSet(false);
    RobotContainer.rotaryCalc(true); // reset rotary joystick to robot angle
    schedule = false;
    Elevator.currentLevel = -1;
    alliance = (DriverStation.getAlliance().toString().charAt(9) == 'B')?"blue":"red"; // finds the alliance
    Intake.upAdjust = 0.0;
    Elevator.sensorInit();
    RobotContainer.autoDriveMultiplier = 1.0;
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println(Intake.currentPosition);
    //System.out.println(RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble()-((RobotContainer.drivetrain.getState().Pose.getRotation().getDegrees()+ 360*1000 + 180)%360));
    RobotContainer.sliderAdjustment = Driver_Controller.upperDriverSlider()*0.45+0.55;
    if (Driver_Controller.driverSwitch()) {
      Limelight.limelightOdometryUpdate();
    }
    Driver_Controller.SwerveInputPeriodic();

    // Send Data to LED Arduino
    //LED.sendData();

    // setting elevator position from controller inputs
    Elevator.useLaserSensor = !Driver_Controller.switchLaserCan();
    if (Driver_Controller.buttonResetElevator()) Elevator.currentLevel = 0;
    if (Driver_Controller.buttonL2()) Elevator.currentLevel = 2;
    if (Driver_Controller.buttonL3()) Elevator.currentLevel = 3;
    if (Driver_Controller.buttonL4()) Elevator.currentLevel = 4;

    // dealing with outtake
    if (Driver_Controller.buttonL1()){
      Elevator.outtakeMotor.set(0.8);
    }
    else if (Driver_Controller.buttonTransportPivot()) Elevator.outtakeMotor.set(0.3);
    else if (Driver_Controller.buttonRevOuttake()) Elevator.outtakeMotor.set(-0.2);
    else if (Elevator.moveCoral == false)Elevator.outtakeMotor.set(0.0);

    // dealing with intake
    if (!Driver_Controller.switchAlgaeIntake() && lastSwitch){
      Intake.powerFactor = 0.3;
      Intake.upAdjust = 0.0;
    }
    if (Driver_Controller.buttonIntakeAdjust()) Intake.upAdjust += 0.05;
    if (Driver_Controller.switchAlgaeIntake()){
      Intake.intakeState = "intake";
      if (Driver_Controller.buttonScoreAlgae()) Intake.bottomIntake.set(TalonSRXControlMode.PercentOutput, 0.5);
      else Intake.bottomIntake.set(TalonSRXControlMode.PercentOutput, -Intake.powerFactor);
    }else{
      if (Driver_Controller.buttonCoralStationIntake()){
        Intake.intakeState = "remove";
        Intake.bottomIntake.set(TalonSRXControlMode.PercentOutput, 0.3);
      } else{
        Intake.bottomIntake.set(TalonSRXControlMode.PercentOutput, 0.0);
        if (Driver_Controller.buttonResetIntake()){
          Intake.intakeState = "reset";
        }else Intake.intakeState = "retract";
      }
    }
    lastSwitch = Driver_Controller.switchAlgaeIntake();

    // handling auto align
    scoringPos = (int)Driver_Controller.ReefPosition();
    if (useNewAutoDrive == false){
    // if the red button by the joystick is pressed, automatically drives to the position designated by the rotary controller on the operator panel
    if (Driver_Controller.buttonReefAlign()){
      AutoDriveFinal.driveToXYA((alliance == "blue")?AutoDriveFinal.scoringPosBlue[scoringPos][0]:AutoDriveFinal.scoringPosRed[scoringPos][0],
                                (alliance == "blue")?AutoDriveFinal.scoringPosBlue[scoringPos][1]:AutoDriveFinal.scoringPosRed[scoringPos][1],
                                AutoDriveFinal.scoringAngles[scoringPos]+((alliance == "blue")?180:0),
                                2.0);
    // if the yellow button by the joystick is pressed, automatically orients to the position designated by the rotary controller on the operator panel
    } else if (Driver_Controller.buttonRotateToReef()){
      AutoDriveFinal.driveToXYA(RobotContainer.drivetrain.getState().Pose.getX()-RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[0],
                                RobotContainer.drivetrain.getState().Pose.getY()-RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[1],
                                AutoDriveFinal.scoringAngles[scoringPos]+((alliance == "blue")?180:0),
                                1.5);
    //if the nearby white button is pressed, align to the closest HPS station on the current alliance
    } else if (Driver_Controller.buttonHPSalign()){
      Elevator.currentLevel = 1;
      if (alliance == "blue"){
        if (RobotContainer.drivetrain.getState().Pose.getY() > 4.025908052)
        AutoDriveFinal.driveToXYA(1.18, 6.95, 306.0+180, 2.0);
        else AutoDriveFinal.driveToXYA(1.18, 1.11, 54.0+180, 2.0);
      }else{
        if (RobotContainer.drivetrain.getState().Pose.getY() > 4.025908052)
        AutoDriveFinal.driveToXYA(16.37, 6.95, 234.0+180, 2.0);
        else AutoDriveFinal.driveToXYA(16.35, 1.13, 126.0+180, 2.0);
      }
    } else if (Driver_Controller.buttonRemoveAlign()){
      AutoDriveFinal.driveToXYA((alliance == "blue")?AutoDriveFinal.algaeRemoveBlue[scoringPos/2][0]:AutoDriveFinal.algaeRemoveRed[scoringPos/2][0],
                                (alliance == "blue")?AutoDriveFinal.algaeRemoveBlue[scoringPos/2][1]:AutoDriveFinal.algaeRemoveRed[scoringPos/2][1],
                                AutoDriveFinal.scoringAngles[scoringPos]+((alliance == "blue")?180:0),
                                2.0);
    }else if (autoDriveLastPressed){
      // when the buttons are no longer being pressed, reset the robot spinner to the current orientation
      Driver_Controller.SwerveCommandControl = false;
      Driver_Controller.SwerveInputPeriodic();
      RobotContainer.rotaryCalc(true);
    }
    }else{
      if (Driver_Controller.buttonEBrake()){
        NewAutoDrive.periodicDriveToLocation(true, "stay");
      }else if (Driver_Controller.buttonReefAlign())
        NewAutoDrive.periodicDriveToLocation(true, "reef");
      else if (Driver_Controller.buttonRotateToReef())
        NewAutoDrive.periodicDriveToLocation(true, "orient");
      else if (Driver_Controller.buttonHPSalign()){
        Elevator.currentLevel = 1;
        NewAutoDrive.periodicDriveToLocation(true, "hps");
      }else if (Driver_Controller.buttonRemoveAlign())
        NewAutoDrive.periodicDriveToLocation(true, "remove");
      else
        NewAutoDrive.periodicDriveToLocation(false, "reef");
    }
    autoDriveLastPressed = (Driver_Controller.buttonReefAlign() || Driver_Controller.buttonRotateToReef() || Driver_Controller.buttonHPSalign() || Driver_Controller.buttonRemoveAlign() || Driver_Controller.buttonEBrake());

    if (Driver_Controller.buttonEBrake()){
      Elevator.currentLevel = -2;
      Elevator.elevatorMotor.set(0.0);
      Intake.intakeState = "hold";
      Elevator.outtakeMotor.set(0.0);
      Intake.bottomIntake.set(TalonSRXControlMode.PercentOutput, 0.0);
    }

    if (elevatorEnabled) Elevator.elevatorPeriodic();
    Intake.intakePeriodic();

    //fine adjustment for elevator
    if (Driver_Controller.buttonEBrake() == false){
      if (Driver_Controller.buttonElevatorUp() && elevatorEnabled){
        Elevator.elevatorMotor.set(0.15);
        Elevator.currentLevel = -2;
      }
      if (Driver_Controller.buttonElevatorDown() && elevatorEnabled){
        Elevator.elevatorMotor.set(-0.15);
        Elevator.currentLevel = -2;
      }
    }
    /*running a pathplanner path:
      the first if statement is to start running a path. schedule has to be called multiple times. idk why.
      the method schedulePathplannerMove() takes a string input, the name of an auto, and returns a command to schedule.
      the variable schedule has to be set to false eventually, otherwise it will infinitely repeat.
      to interrupt, run schedulePathplannerMove() but IMMEDIATELY set schedule to false.
      */
    /*if (Driver_Controller.buttonResetElevator() && (!lastPressed)){
      m_autonomousCommand = RobotContainer.schedulePathplannerMove("15 - Abs Ideal -- V3 (B1, A2, A1, F2, F1)");
      lastPressed = true;
      schedule = true;
    } else
      lastPressed = Driver_Controller.buttonResetElevator();
    if (Driver_Controller.buttonL1()) schedule = false;
    if (schedule) m_autonomousCommand.schedule();*/

    //if these buttons are pressed, tell LED to activate
    //if (Driver_Controller.buttonExtendClimb()) LED.sendData("pattern 1");
    //if (Driver_Controller.buttonRetractClimb()) LED.sendData("pattern 2");
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
