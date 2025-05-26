// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.util.FileVersionException;

import org.json.simple.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  Boolean lastPressed = false, schedule;

  private final RobotContainer m_robotContainer;

  public Robot() {
    m_robotContainer = new RobotContainer();
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
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

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
    RobotContainer.rotaryCalc(true);
    //m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    /*if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }*/
    Driver_Controller.define_Controller();
    Dance.timer = 0.0; Dance.currentX = 0.0; Dance.currentY = 0.0; Dance.desiredAngle = 0.0; Dance.currentElevator = 0.0; Dance.speed = 2.1;
    Dance.intakeState = "ground intake";
    Dance.activated = false;
    Elevator.extendedOrRetracted = "retracted";
    Limelight.limelightInit();
    schedule = false;
  }

  @Override
  public void teleopPeriodic() {
    /*try {
      String content = new String(Files.readAllBytes(Paths.get("deploy.pathplanner.paths.test")));
      //JSONObject changederThing = new JSONObject(content);
      //changederThing.
    } catch(IOException e){
      e.printStackTrace();
    }*/
      

    Limelight.limelightOdometryUpdate();
    //Double elevatorPosition = Double.parseDouble(Elevator.elevatorMotor.getRotorPosition().toString().substring(0, 10));
    //if (Elevator.elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround"){Elevator.bottomPosition = elevatorPosition;}
    if (Intake.outsideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition();}
    //else if (Intake.insideSwitch.isPressed()){Intake.inPosition = Intake.intakeEncoder.getPosition() - 48.64;}

    if (Driver_Controller.buttonResetElevator() && (!lastPressed)){
      m_autonomousCommand = RobotContainer.startPathplannerMove("Shark Attack A");
      m_autonomousCommand.schedule();
      
      lastPressed = true;
      schedule = true;
    } else
    //Dance.dance();
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
}
