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

import frc.robot.mainChassis.TunerConstants;
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

        

        // PathPlannerAuto V1 = new PathPlannerAuto("15sec -- V1 (A2, A1, F2, F1)");
        // PathPlannerAuto V2 = new PathPlannerAuto("Normal -- V2 (B1, A2, A1, F2, F1)");
        // PathPlannerAuto V3 = new PathPlannerAuto("15 - Abs Ideal -- V3 (B1, A2, A1, F2, F1)");
        // PathPlannerAuto SafeAndSlow = new PathPlannerAuto("Normal -- V2 (B1, A2, A1, F2, F1)");
        
        public static Command ScheduleTestingPath = RobotContainer.schedulePathplannerMove("Testing path");
        
        // autos for kV1Auto
        // auto folder - USE - 1 piece, BLUE Side C1 and Stop
        public static Command Schedule1 = RobotContainer.schedulePathplannerMove("E. Blue Side C1 and Stop");

        // autos for kV2Auto
        // auto folder - USE - 1 piece, BLUE Side C2 and Stop
        public static Command Schedule2 = RobotContainer.schedulePathplannerMove("F. Blue Side C2 and Stop");

        // autos for kV3Auto
        // auto folder - USE - 1 piece, RED F2 and Stop
        // FOLDER EMPTY - find autos - siena
        //public static Command Schedule3 = RobotContainer.schedulePathplannerMove(SOMETHING);

        // autos for kV4Auto
        // auto folder - USE - 1 piece, RED Side F1 and Stop
        public static Command Schedule4 = RobotContainer.schedulePathplannerMove("G. Red Side F1 and Stop");

        // autos for kV5Auto
        // auto folder - USE - 2 piece, BLUE Side A
        // FOLDER EMPTY - find autos - siena
        // public static Command Schedule5 = RobotContainer.schedulePathplannerMove(SOMETHING);

        // autos for kV6Auto
        // auto folder - USE - 4 piece, BLUE Sides A and B
        // NOT WORKING AEURIOHAEUIORH
        public static Command Schedule6_1 = RobotContainer.schedulePathplannerMove("1. Blue Pos1 To B2");
        public static Command Schedule6_2 = RobotContainer.schedulePathplannerMove("2. Blue B2 To HPS");
        public static Command Schedule6_3 = RobotContainer.schedulePathplannerMove("3. Blue HPS To B1");
        public static Command Schedule6_4 = RobotContainer.schedulePathplannerMove("4. Blue B1 To Hps");
        public static Command Schedule6_5 = RobotContainer.schedulePathplannerMove("5. Blue HPS To A2");
        public static Command Schedule6_6 = RobotContainer.schedulePathplannerMove("6. Blue A2 To HPS");
        public static Command Schedule6_7 = RobotContainer.schedulePathplannerMove("7. Blue HPS To A1");

        // autos for kV7Auto
        // auto folder - USE - 4 piece, BLUE Sides D and E
        public static Command Schedule7_1 = RobotContainer.schedulePathplannerMove("1. Blue Pos3 To D1");
        public static Command Schedule7_2 = RobotContainer.schedulePathplannerMove("2. Blue D1 To HPS");
        public static Command Schedule7_3 = RobotContainer.schedulePathplannerMove("3. Blue HPS to D2");
        public static Command Schedule7_4 = RobotContainer.schedulePathplannerMove("4. Blue D2 To HPS");
        public static Command Schedule7_5 = RobotContainer.schedulePathplannerMove("5. Blue HPS To E1");
        public static Command Schedule7_6 = RobotContainer.schedulePathplannerMove("6. Blue E1 To HPS");
        public static Command Schedule7_7 = RobotContainer.schedulePathplannerMove("7. Blue HPS To E2");

        // autos for kV8Auto
        // auto folder - USE - 4 piece, RED Sides A and B
        // FOLDER EMPTY - find autos - siena
        // public static Command Schedule8 = RobotContainer.schedulePathplannerMove(SOMETHING);

        // autos for kV9Auto
        // auto folder - USE - 4 piece, RED, Sides E and D
        public static Command Schedule9_1 = RobotContainer.schedulePathplannerMove("1. Red Pos1 to E2");
        public static Command Schedule9_2 = RobotContainer.schedulePathplannerMove("2. Red E2 To HPS");
        public static Command Schedule9_3 = RobotContainer.schedulePathplannerMove("3. Red HPS To E1");
        public static Command Schedule9_4 = RobotContainer.schedulePathplannerMove("4. Red E1 To HPS");
        public static Command Schedule9_5 = RobotContainer.schedulePathplannerMove("5. Red HPS to D2");
        public static Command Schedule9_6 = RobotContainer.schedulePathplannerMove("6. Red D2 To HPS");
        public static Command Schedule9_7 = RobotContainer.schedulePathplannerMove("7. Red HPS To D1");

        public static final SendableChooser<String> autoChooser = new SendableChooser<>();

                public RobotContainer() {
                    // Configure the trigger bindings
                    configureBindings();



                    autoChooser.setDefaultOption("Pass the Line", Constants.kPassTheLine);
                    autoChooser.addOption("Testing Path", Constants.kTestingPathAuto);
                    autoChooser.addOption("V1", Constants.kV1Auto);
                    autoChooser.addOption("V2", Constants.kV2Auto);
                    autoChooser.addOption("V3", Constants.kV3Auto);
                    autoChooser.addOption("V4", Constants.kV4Auto);
                    autoChooser.addOption("V5", Constants.kV5Auto);
                    autoChooser.addOption("V6", Constants.kV6Auto);
                    autoChooser.addOption("V7", Constants.kV7Auto);
                    autoChooser.addOption("V8", Constants.kV8Auto);
                    autoChooser.addOption("V9", Constants.kV9Auto);
                    // autoChooser.addOption("V3", "15 - Abs Ideal -- V3 (B1, A2, A1, F2, F1)");
                    // autoChooser = AutoBuilder.buildAutoChooser("Normal -- V2 (B1, A2, A1, F2, F1)");
        
                    SmartDashboard.putData("Auto Chooser", autoChooser);
                    //SmartDashboard.putData("Auto Mode", autoChooser);
                    //joystick = Driver_Controller.m_Controller0;
                    //joystick2 = Driver_Controller.m_Controller1;
                    // configureBindings();
                    FollowPathCommand.warmupCommand().schedule();
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
        
                //Driver_Controller.m_Controller0.a().whileTrue(drivetrain.applyRequest(() -> brake));
                //Driver_Controller.m_Controller0.b().whileTrue(drivetrain.applyRequest(() ->
                //    point.withModuleDirection(new Rotation2d(-Driver_Controller.m_Controller0.getLeftY(), -Driver_Controller.m_Controller0.getLeftX()))
                //));
        
                // Run SysId routines when holding back/start and X/Y.
                // Note that each routine should be run exactly once in a single log.
                // Driver_Controller.m_Controller0.back().and(Driver_Controller.m_Controller0.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
                // Driver_Controller.m_Controller0.back().and(Driver_Controller.m_Controller0.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
                // Driver_Controller.m_Controller0.start().and(Driver_Controller.m_Controller0.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
                // Driver_Controller.m_Controller0.start().and(Driver_Controller.m_Controller0.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        
                // reset the field-centric heading on left bumper press
                //Driver_Controller.m_Controller0.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        
                drivetrain.registerTelemetry(logger::telemeterize);
            }
        
            public static Command getAutonomousCommand() {
        // // return autoChooser.getSelected();
        // try {
        //     List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
        //             new Pose2d(1.0, 1.0, Rotation2d.fromDegrees(0)),
        //             new Pose2d(3.0, 1.0, Rotation2d.fromDegrees(0)),
        //             new Pose2d(5.0, 3.0, Rotation2d.fromDegrees(90))
        //     );

        //     PathConstraints constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 * Math.PI); // The constraints for this path.
        //     // PathConstraints constraints = PathConstraints.unlimitedConstraints(12.0); // You can also use unlimited constraints, only limited by motor torque and nominal battery voltage

        //     // Create the path using the waypoints created above
        //     PathPlannerPath path = new PathPlannerPath(
        //             waypoints,
        //             constraints,
        //             null, // The ideal starting state, this is only relevant for pre-planned paths, so can be null for on-the-fly paths.
        //             new GoalEndState(0.0, Rotation2d.fromDegrees(-90)) // Goal end state. You can set a holonomic rotation here. If using a differential drivetrain, the rotation will have no effect.
        //     );

        //     // Prevent the path from being flipped if the coordinates are already correct
        //     path.preventFlipping = true;
        //     return AutoBuilder.followPath(path);
        //   //PathPlannerPath examplePath = PathPlannerPath.fromPathFile("Example Path");
        //   //return AutoBuilder.buildAuto("New Auto");
        // } catch (FileVersionException e) {
        //   // TODO Auto-generated catch block
        //   e.printStackTrace();
        // //} //catch (IOException e) {
        //   // TODO Auto-generated catch block
        //   //e.printStackTrace();
        // //} //catch (ParseException e) {
        //   // TODO Auto-generated catch block
        //   //e.printStackTrace();
        // }
        return Commands.print("No autonomous command configured");
    }

    public static Command schedulePathplannerMove(String move) {
        try {
          return AutoBuilder.buildAuto(move);
        } catch (FileVersionException e) {
          e.printStackTrace();
        }
        return Commands.print("No autonomous command configured");
    }   
}