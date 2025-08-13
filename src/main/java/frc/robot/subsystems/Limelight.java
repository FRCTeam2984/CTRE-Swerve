package frc.robot.subsystems;



import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;
import frc.robot.RobotContainer;

public class Limelight {
    public static double prevAmbiguity = 1.0;
    public static boolean hasTarget = LimelightHelpers.getTV("limelight"); // Do you have a valid target?
    public static Pose2d LimelightGeneratedPose2d = LimelightHelpers.getBotPose2d_wpiBlue("limelight");
    public static double seconds = Utils.getCurrentTimeSeconds();
    public static void limelightInit(){
        // Set camera offset from middle of robot
        LimelightHelpers.setCameraPose_RobotSpace("limelight", 
        -0.25,    // Forward offset (meters)
        0.03,    // Side offset (meters)
        0.365,    // Height offset (meters)
        0.0,    // Roll (degrees)
        0.0,   // Pitch (degrees)
        180.0     // Yaw (degrees)
        );
    }
                  
    public static void limelightOdometryUpdate(){
        hasTarget = LimelightHelpers.getTV("limelight");
        LimelightGeneratedPose2d = LimelightHelpers.getBotPose2d_wpiBlue("limelight");
        seconds = Utils.getCurrentTimeSeconds();
        double newAmbiguity = 1.0;
        RawFiducial[] fiducials = LimelightHelpers.getRawFiducials("");
        if (fiducials.length > 0)newAmbiguity = fiducials[fiducials.length-1].ambiguity;
        boolean withinAmbiguityRange = (newAmbiguity < 0.5);// && newAmbiguity != prevAmbiguity);
        if (withinAmbiguityRange) prevAmbiguity = newAmbiguity;
        //System.out.println(newAmbiguity);

        if (hasTarget && withinAmbiguityRange){
            Boolean resetNeeded = (Math.abs(LimelightGeneratedPose2d.getRotation().getDegrees()-RobotContainer.drivetrain.getState().Pose.getRotation().getDegrees()) > 5.0); 
            RobotContainer.drivetrain.addVisionMeasurement(LimelightGeneratedPose2d, seconds);
            if (resetNeeded) RobotContainer.rotaryCalc(true);
            System.out.println(LimelightGeneratedPose2d);
        }
    }
}
