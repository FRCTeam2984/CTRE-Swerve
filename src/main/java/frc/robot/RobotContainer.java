// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Rotary_Controller;
import frc.robot.subsystems.Driver_Controller;

import frc.robot.Constants;

public class RobotContainer {
  
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    public double SpeedModifier = .1;
    public double TurnModifier = .2;

    // Setting up bindings for necessary control of the swerve drive platform 
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    
    //private static CommandXboxController joystick = new CommandXboxController(Driver_Controller.SwerveCommandXboxControllerPort);// = new CommandXboxController(0);
    
    //private final XboxController joystick2 = new XboxController(Driver_Controller.SwerveRotaryEncoderPort);// = new Joystick(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    
    
    
    public RobotContainer() {
        //joystick = Driver_Controller.m_Controller0;
        //joystick2 = Driver_Controller.m_Controller1;
        configureBindings();
    }
    private double rotaryCalc(){
        //Driver_Controller.SwerveInputPeriodic();
        double pigeonYaw = drivetrain.getPigeon2().getYaw().getValueAsDouble() /* (180/3.1415) */;                 // Grab the yaw value from the swerve drive IMU as a double
        double rotaryJoystickInput = Rotary_Controller.RotaryJoystick(Driver_Controller.m_Controller1);               // Get input from the rotary controller (ID from joystick2)
        //System.out.println(pigeonYaw);
        //System.out.println(rotaryJoystickInput);
        //double joystickClamped = Math.max(Math.min(45, (((((pigeonYaw - (rotaryJoystickInput - 180))+ (360*1000) + 180) % 360) - 180))), -45);    // Get a clamped value of the joystick input
        //System.out.println(joystickClamped);
        double diff = pigeonYaw - (rotaryJoystickInput);
        double diffmod180 = ((diff + 360*1000 + 180)%360) - 180;
        //System.out.println(diffmod180);
        double powerCurved = -diffmod180;
        powerCurved = Math.max(-45,Math.min(45,powerCurved));
        //double angleDiff = ((pigeonYaw + (360 * 1000) + 180) % 180) - (rotaryJoystickInput - 180);
        if((diffmod180 <= 1) && (diffmod180 >= -1)){
            //return 0;
        }
        System.out.println(powerCurved *0.9/45.0);
        return powerCurved * 0.09;
    }
    final double pos[] = {-1.0,-0.75,-0.5,-0.1, 0, 0.1, 0.5, 0.75,1};
    final double pwr[] = {-1, -0.3,-0.1,    0, 0,    0, 0.1,   .3,1};
    private double joystick_curve(double joy) {

        int i;
        if (joy<=-1) joy=-1;
        if (joy>=1) joy=1;
        for (i=0;i<8;i++) {
            if ((pos[i]<=joy) && (pos[i+1]>=joy)) {
                    System.out.println( ((joy-pos[i]) / (pos[i+1]-pos[i]) * (pwr[i+1]-pwr[i]) + pwr[i]) * MaxSpeed);
                    return(((joy-pos[i]) / (pos[i+1]-pos[i]) * (pwr[i+1]-pwr[i]) + pwr[i]) * MaxSpeed);
                }
        }
        return(0);
    }
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        boolean drive_enable=true;
        if (drive_enable){
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(joystick_curve(Driver_Controller.m_Controller0.getLeftY())) // Drive forward with negative Y (forward)
                    .withVelocityY(joystick_curve(Driver_Controller.m_Controller0.getLeftX())) // Drive left with negative X (left)
                    .withRotationalRate(rotaryCalc() * MaxAngularRate * TurnModifier*0) // Drive counterclockwise with negative X (left)
            )
        );}

        Driver_Controller.m_Controller0.a().whileTrue(drivetrain.applyRequest(() -> brake));
        Driver_Controller.m_Controller0.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-Driver_Controller.m_Controller0.getLeftY(), -Driver_Controller.m_Controller0.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        Driver_Controller.m_Controller0.back().and(Driver_Controller.m_Controller0.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        Driver_Controller.m_Controller0.back().and(Driver_Controller.m_Controller0.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        Driver_Controller.m_Controller0.start().and(Driver_Controller.m_Controller0.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        Driver_Controller.m_Controller0.start().and(Driver_Controller.m_Controller0.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        Driver_Controller.m_Controller0.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
