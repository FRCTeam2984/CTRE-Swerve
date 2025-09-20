package frc.robot.subsystems;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Driver_Controller;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.AutoDriveFinal;
import frc.robot.Robot;
import frc.robot.Constants;
public class NewAutoDrive{
    public static Boolean isDriving = false, goBehindReef = true;
    public static Double accelFac = 0.0, autoDriveMaxSpeed = 2.0;
    public static String alliance = "red";
    public static void driveToXYA(Double x, Double y, Double angle, Double speed, Double slowDownDist){
        Driver_Controller.SwerveControlSet(true);
        Double odoAngle = ((RobotContainer.drivetrain.getState().Pose.getRotation().getDegrees()+ 360*1000 + 180)%360);
        //((RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble() + 360*1000 + 180)%360) - 180;
        Double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        Double odox = RobotContainer.drivetrain.getState().Pose.getX();

        // use pythagorean theorum to calculate distance and if it is close enough
        
        Double dist = Constants.pythagorian(odox-x,odoy-y);
        Double driveAngle = Math.atan2(y - odoy, x - odox);
        // limit max "speed" to autoDriveMaxSpeed
        if (speed > autoDriveMaxSpeed) speed = autoDriveMaxSpeed;
        if (speed < -autoDriveMaxSpeed) speed = -autoDriveMaxSpeed;

        // accelerate power when starting and lower power when approaching the destination, add a bit to dist so it goes faster at the end
        Double accelMult = Math.min(1.0, Math.min((dist+0.1)/slowDownDist, accelFac));
        speed *= accelMult;
        speed += 0.15; // add a little bit of speed to avoid dead spot
        speed *= ((alliance == "red")?1:-1); // opposite directions if red vs blue
        if (dist > 0.05){
            Driver_Controller.SwerveCommandXValue = -speed*Math.cos(driveAngle);
            Driver_Controller.SwerveCommandYValue = -speed*Math.sin(driveAngle);
        }else{
            Driver_Controller.SwerveCommandXValue = 0.0;
            Driver_Controller.SwerveCommandYValue = 0.0;
        }
        Driver_Controller.SwerveCommandEncoderValue = odoAngle*0 + angle;
    }
    public static void driveToXYA(Double x, Double y, Double angle, Double speed){
        driveToXYA(x, y, angle, speed, 2.0);
    }
    public static double scoringAngles[] = {0, 0, -60, -60, -120, -120, -180, -180, -240, -240, -300, -300};
    public static double[][] scoringPosRed = new double[12][2], scoringPosBlue = new double[12][2];
    public static double reefY = 4.025908052, reefXBlue = (5.321056642+3.657607315)/2.0, reefXRed = (13.89052578+12.22733045)/2.0;
    public static double reefAltitude = Math.abs(3.657607315-reefXBlue);
    public static double pillarOffset = 0.165, robotOffsetLeft = 0.249, robotOffsetBack = 0.435;
    public static String lastLocation = "";
    public static double algaeRemoveRed[][] = { 
        {14.32552578, 3.725908052},
        {13.43216533, 2.779603562},
        {12.16607567, 3.079603562},
        {11.79233045, 4.325908052},
        {12.68569091, 5.272212542},
        {13.95178057, 4.972212542},  
    };
    public static double algaeRemoveBlue[][] = { 
        {3.222861316, 4.325908052},
        {4.116221769, 5.272212542},
        {5.382311431, 4.972212542},
        {5.756056642, 3.725908052},
        {4.862696189, 2.779603562},
        {3.596606527, 3.079603562},  
    };

    public static void setupAutoDrive(){
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = "blue";  // blue
        }
        for (int i = 0; i < 12; ++i){
            double mult1 = Math.sin(Math.toRadians(scoringAngles[i])), mult2 = Math.cos(Math.toRadians(scoringAngles[i]));
            scoringPosBlue[(i+6)%12][0] = reefXBlue+(robotOffsetBack+reefAltitude)*mult2 + (robotOffsetLeft - ((i%2 == 0)?1:-1) * pillarOffset)*mult1;
            scoringPosBlue[(i+6)%12][1] = reefY+(robotOffsetBack+reefAltitude)*mult1 - (robotOffsetLeft - ((i%2 == 0)?1:-1) * pillarOffset)*mult2; 
        }
        for (int i = 0; i < 12; ++i){
            scoringPosRed[i][0] = scoringPosBlue[(i+6)%12][0]+8.569469139;
            scoringPosRed[i][1] = scoringPosBlue[(i+6)%12][1];
        }
    }
    public static void periodicDriveToLocation(boolean willDrive, String Location){
        int position = Robot.scoringPos;
        Double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        Double odox = RobotContainer.drivetrain.getState().Pose.getX();
        Double targetX = -100.0, targetY = -100.0;
        if (willDrive && !isDriving) goBehindReef = true;
        if (willDrive){
            accelFac = Math.min(1, accelFac+0.05);
            switch(Location){
                case "reef":
                    targetX = ((alliance == "blue")?scoringPosBlue:scoringPosRed)[position][0]-Math.sin(Math.toRadians(scoringAngles[position]+((alliance == "blue")?90:-90)));
                    targetY = ((alliance == "blue")?scoringPosBlue:scoringPosRed)[position][1]+Math.cos(Math.toRadians(scoringAngles[position]+((alliance == "blue")?90:-90)));
                    if (goBehindReef) driveToXYA(targetX, targetY, scoringAngles[position]+((alliance == "blue")?0:180), 1.0, 0.5);
                    break;
                case "hps":
                    if (alliance == "blue"){
                        if (RobotContainer.drivetrain.getState().Pose.getY() > reefY){
                            driveToXYA(1.18, 6.95, 306.0, 2.0);
                        }else{
                            driveToXYA(1.18, 1.11, 54.0, 2.0);
                        }
                    }else{
                        if (RobotContainer.drivetrain.getState().Pose.getY() > reefY){
                            driveToXYA(16.37, 6.95, 234.0, 2.0);//+122.66-54, 2.0);
                        }else{
                            driveToXYA(16.35, 1.13, 126.0, 2.0);
                        }
                    }
                    break;
                case "orient":
                    driveToXYA(RobotContainer.drivetrain.getState().Pose.getX()-RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[0],
                    RobotContainer.drivetrain.getState().Pose.getY()-RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[1],
                    AutoDriveFinal.scoringAngles[position]+((alliance == "blue")?180:0),
                    1.5);
                    Driver_Controller.SwerveCommandEncoderValue = scoringAngles[position];
                    Driver_Controller.SwerveXPassthrough = -RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[0];
                    Driver_Controller.SwerveYPassthrough = -RobotContainer.betterJoystickCurve(Driver_Controller.m_Controller0.getLeftX(), Driver_Controller.m_Controller0.getLeftY())[1];

                    break;
                case "remove":
                    driveToXYA((alliance == "blue")?algaeRemoveBlue[position/2][0]:algaeRemoveRed[position/2][0],
                    (alliance == "blue")?algaeRemoveBlue[position/2][1]:algaeRemoveRed[position/2][1],
                    scoringAngles[position]+((alliance == "red")?180:0),
                    2.0);
                    break;
            }
        }else accelFac = 0.0;
        Driver_Controller.SwerveCommandControl = willDrive;

        if (willDrive && (goBehindReef == false ||(willDrive && Math.abs(targetX-odox) < 0.2 && Math.abs(targetY-odoy) < 0.2))){
            driveToXYA(((alliance == "blue")?scoringPosBlue:scoringPosRed)[position][0],
            ((alliance == "blue")?scoringPosBlue:scoringPosRed)[position][1],
            scoringAngles[position]+((alliance == "blue")?0:180), 1.0);
            goBehindReef = false;
        }
        isDriving = willDrive;
    }

    
}