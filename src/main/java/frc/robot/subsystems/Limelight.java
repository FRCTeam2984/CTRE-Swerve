package frc.robot.subsystems;



import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;

public class Limelight {
    static boolean hasTarget = LimelightHelpers.getTV("limelight"); // Do you have a valid target?
    static Pose2d LimelightGeneratedPose2d = LimelightHelpers.getBotPose2d_wpiBlue("limelight");
    static double seconds = Utils.getCurrentTimeSeconds();
    public static void limelightInit(){
        // Set camera offset from middle of robot
        LimelightHelpers.setCameraPose_RobotSpace("limelight", 
        0.5,    // Forward offset (meters)
        0.0,    // Side offset (meters)
        0.5,    // Height offset (meters)
        0.0,    // Roll (degrees)
        30.0,   // Pitch (degrees)
        0.0     // Yaw (degrees)
        );
    }
                  
    public static void limelightOdometryUpdate(){
        System.out.println(hasTarget);
        
        if (hasTarget){
            RobotContainer.drivetrain.addVisionMeasurement(LimelightGeneratedPose2d, seconds);
            System.out.println(LimelightGeneratedPose2d);
        }
    }
}
