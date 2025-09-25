package frc.robot.subsystems;



 import java.io.Serial;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;


import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.apriltag.*;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;
import frc.robot.RobotContainer;
import frc.robot.Constants;



public class PhotonVision {
    public static double prevAmbiguity = 1.0;
    static PhotonCamera camera = new PhotonCamera("photonvision");
    static PhotonPipelineResult result = camera.getLatestResult(); // get [pipeline]
    static boolean hasTargets = result.hasTargets();//GET TARGETs
    static PhotonTrackedTarget target = result.getBestTarget(); //get the best one (declan says that this is called getting the good girl)
    
    public static double seconds = Utils.getCurrentTimeSeconds();
    public static AprilTagFieldLayout aprilTagFieldLayout = new AprilTagFieldLayout(Constants.aprilpath); //idk why nop work
public static Transform3d cameraToRobot;
    public static double CameraHeight = 0.0;
public static Translation2d cameratotarget;
     public static double CameraPitch = 0.0;
    public static double TargetPitch = target.getPitch(); 

    public static void CameraInit(){
        //unused
       
    }
            
    public static void limelightOdometryUpdate(){
         result = camera.getLatestResult(); // get [pipeline]
         hasTargets = result.hasTargets();//GET TARGETs
         target = result.getBestTarget(); //get the best one 
Pose3d robotpose = PhotonUtils.estimateFieldToRobotAprilTag(target.bestCameraToTarget, aprilTagFieldLayout.getTagPose(target.getFiducialId()).get(), cameraToRobot);
 ///               
//Transform2d cameraToTarget, Pose2d fieldToTarget, Transform2d cameraToRobot
Rotation3d rot = robotpose.getRotation();

Pose2d cast = new Pose2d(robotpose.getX(), robotpose.getY(), rot.toRotation2d());
            RobotContainer.drivetrain.addVisionMeasurement(cast, seconds);
            if (/*resetNeeded*/ true){RobotContainer.rotaryCalc(true);}

    
    }
}
*/