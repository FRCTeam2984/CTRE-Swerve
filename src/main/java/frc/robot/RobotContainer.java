// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import java.util.List;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.events.EventTrigger;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.util.FileVersionException;

import frc.robot.Constants;




import frc.robot.subsystems.CommandSwerveDrivetrain;
/* Import Subsystems */
// import frc.robot.subsystems.Climb;
// import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Driver_Controller;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Rotary_Controller;

import frc.robot.generated.TunerConstants;
//import frc.robot.testChassis.TunerConstants; 

public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
    // private final CommandSwerveDrivetrain m_commandSwerveDrivetrain = new CommandSwerveDrivetrain();
    public static Double sliderAdjustment = 1.0;
    private final Driver_Controller m_driverController = new Driver_Controller();
    private final Elevator m_elevator = new Elevator();
    private final Intake m_intake = new Intake();
    //private final LED m_LED = new LED();
    private final Limelight m_limelight = new Limelight();
    private static boolean wasCommandAngle = false;
    private static double rotaryOffset = 0;
    private final Rotary_Controller m_rotaryController = new Rotary_Controller();


    
    public static Boolean needToReset = true;
    public static int drivingOn = 1;
    public static double robotOffset = 0;
    public static double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed

        private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    
        public double SpeedModifier = .1;
        public double TurnModifier = .2;
    
        // Setting up bindings for necessary control of the swerve drive platform 
        private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
                .withDeadband(MaxSpeed * 0.05).withRotationalDeadband(MaxAngularRate * 0.05) // Add a 10% deadband
                .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
        private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
        private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    
        private final Telemetry logger = new Telemetry(MaxSpeed);
    
        
        //private static CommandXboxController joystick = new CommandXboxController(Driver_Controller.SwerveCommandXboxControllerPort);// = new CommandXboxController(0);
        
        //private final XboxController joystick2 = new XboxController(Driver_Controller.SwerveRotaryEncoderPort);// = new Joystick(1);
    
        public static final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

        


        
        


        private final SendableChooser<Command> autoChooser;// = new SendableChooser<>();

                public RobotContainer() {
                    // Configure the trigger bindings
                    configureBindings();
                    new EventTrigger("L4").onTrue(Triggers.move());
                    new EventTrigger("SCORE").onTrue(Triggers.move1());

                    autoChooser = AutoBuilder.buildAutoChooser();
                    //autoChooser.addOption("Testing Path", Constants.TestPath);

                    SmartDashboard.putData("Auto Chooser", autoChooser);

                    //FollowPathCommand.warmupCommand().schedule();
                }



                public static double rotaryCalc(Boolean resetToRobot){
                    double pigeonYaw = drivetrain.getPigeon2().getYaw().getValueAsDouble();    //Read from the pigeon (Gyro angle as a double)             // Grab the yaw value from the swerve drive IMU as a double
                    pigeonYaw=((drivetrain.getState().Pose.getRotation().getDegrees()+ 360*1000 + 180)%360);
                    double rotaryJoystickInput;    //Define the variable rotaryJoystickInput as a double
                    
                    // Determine if Command control is enabled
                    if (Driver_Controller.SwerveCommandControl){
                        rotaryJoystickInput = Driver_Controller.SwerveEncoderPassthrough;
                        wasCommandAngle = true;
                    }
                    else{
                        rotaryJoystickInput = Rotary_Controller.RotaryJoystick(Driver_Controller.m_Controller1);               // Get input from the rotary controller (ID from m_controller1)
                        if (wasCommandAngle || needToReset){
                            rotaryOffset = (pigeonYaw + (360 * 1000))% 360;
                            wasCommandAngle = false;
                            rotaryOffset = (rotaryOffset - rotaryJoystickInput);
                            needToReset = false;
                        }
                        
                        rotaryJoystickInput = rotaryJoystickInput + rotaryOffset;
                    }
                    //Math to calculate the maximum turn speed
                    double diff = pigeonYaw - (rotaryJoystickInput);
                    double diffmod180 = ((diff + 360*1000 + 180)%360) - 180;
                    if (resetToRobot){
                        needToReset = true;
                    }
                    /*
                    if (needToReset && Driver_Controller.SwerveCommandControl == false){
                        robotOffset -= diffmod180;
                        needToReset = false;
                    }*/
                    double powerCurved = -diffmod180;
                    powerCurved = Math.max(-45,Math.min(45,powerCurved));
                    double angleDiff = ((pigeonYaw + (360 * 1000) + 180) % 180) - (rotaryJoystickInput - 180);
                    
                    // Stop turing if within a degree of target
                    if((angleDiff <= 0.5) && (angleDiff >= -0.5)){
                        return 0;
                    }

                    //Limit the power to a max of 7
                                        
                                        if (powerCurved < 8 && powerCurved > 2){
                                            powerCurved = 8;// * ((powerCurved + 3)/ 10);
                                        }
                                        if (powerCurved > -8 && powerCurved < -2){
                                            powerCurved = -8;// * ((Math.abs(powerCurved) + 3)/ 10);
                                        }
                    // Return a slightly lowered powercurved value (i.e. Slightly lowered turn speed)
                    return powerCurved * 0.1;
            }
        

            final static double pos[] = {-1.0,-0.75,-0.5,-0.1 ,-0.03, 0,0.03, 0.1, 0.5, 0.75,1};
            final static double pwr[] = {-1  , -0.3,-0.1,-0.02,    0, 0,   0,0.02, 0.1,   .3,1};

            public static double joystick_curve(double joy) {
                int i;
                if (joy<=-1) joy=-1;
                if (joy>=1) joy=1;
                for (i=0;i<10;i++) {
                if ((pos[i]<=joy) && (pos[i+1]>=joy)) {
                    //System.out.println( ((joy-pos[i]) / (pos[i+1]-pos[i]) * (pwr[i+1]-pwr[i]) + pwr[i]) * MaxSpeed);
                    return(((joy-pos[i]) / (pos[i+1]-pos[i]) * (pwr[i+1]-pwr[i]) + pwr[i]) * MaxSpeed);
                    }
                }
                return(0);
            }

            public static double[] betterJoystickCurve(double x, double y) {
                double radius = Math.sqrt((x*x)+(y*y));
                double angle = Math.atan2(y, x);
                radius = joystick_curve(radius);
                double[] returnValue = {Math.sin(angle)*radius, Math.cos(angle)*radius};
                return returnValue;
            }


            private void configureBindings() {
                




                // Note that X is defined as forward according to WPILib convention,
                // and Y is defined as to the left according to WPILib convention.
                boolean drive_enable=true;
                drivetrain.setDefaultCommand(
                    // Drivetrain will execute this command periodically
                    drivetrain.applyRequest(() ->
                        drive.withVelocityX(Driver_Controller.SwerveXPassthrough * ((Driver_Controller.SwerveCommandControl)?1:sliderAdjustment)) // Drive forward with negative Y (forward)
                            .withVelocityY(Driver_Controller.SwerveYPassthrough * ((Driver_Controller.SwerveCommandControl)?1:sliderAdjustment)) // Drive left with negative X (left)
                            .withRotationalRate(rotaryCalc(false) * MaxAngularRate * TurnModifier * drivingOn * ((Driver_Controller.SwerveCommandControl == true)?0.45:1)) // Drive counterclockwise with negative X (left)
                    )
                );
        

        
                drivetrain.registerTelemetry(logger::telemeterize);
            }
        
            public Command getAutonomousCommand() {
                return autoChooser.getSelected();
            }

    
}