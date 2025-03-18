package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.RobotContainer;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoDriveFinal{
    public static double slope;
    public int scoringPos;
    static double scoringPosRed[][] = {  // coordinates for circle E
        {14.3472	,4.3872},
            {14.3472,	4.0592},
            {14.02107295, 3.094024613},
            {13.56387295, 3.216924613},
            {12.55398329, 3.216924613},
            {12.09678329, 3.094024613},
            {11.7698, 4.3872},
            {11.7698, 4.0592},
            {12.09678329, 4.957791491},
            {12.55398329, 4.834891491},
            {13.56387295, 4.834891491},
            {14.02107295,4.957791491},  // close 1
        };
        static double scoringPosBlue[][] = {  // coordinates for circle E
            {3.1998, 4.0592},
            {3.1998, 4.3872},
            {3.527314148, 4.957791491},
            {3.984514148, 4.834891491},
            {4.99440381, 4.834891491},
            {5.45160381, 4.957791491},
            {5.7782, 4.0592},
            {5.7782, 4.3872},
            {5.45160381, 3.094024613},
            {4.99440381, 3.216924613},
            {3.984514148, 3.216924613},
            {3.527314148, 3.04024613},  // close 1
            };
    public static void AutoDriveFinal(double odox, double odoy, int scoringPos, int alliance){
        Driver_Controller.SwerveControlSet(true);
        odoy = RobotContainer.drivetrain.getState().Pose.getY();
        odox = RobotContainer.drivetrain.getState().Pose.getX();
        scoringPos = 1; // find how to get rotary control value
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = 1;  // blue
        }
        if(DriverStation.getAlliance().toString().charAt(9) == 'R'){   
            alliance = 2;  // blue
        }
        drive(odox,odoy, 2, 1);
    }
    public static void drive(double odox, double odoy, int scoringPos, int alliance){
        double odoangle;
        if(alliance == 1){  // blue
            odoangle = Math.tan(((scoringPosBlue[scoringPos][0] - odox)/(scoringPosBlue[scoringPos][1] - odoy)) * (Math.PI/180));
            Driver_Controller.SwerveXPassthrough = (scoringPosBlue[scoringPos][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveXPassthrough = (scoringPosBlue[scoringPos][1] - odoy) * Math.sin(odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle;

        }
        else{  // then its red
            odoangle = Math.tan(((scoringPosRed[scoringPos][0] - odox)/(scoringPosRed[scoringPos][1] - odoy)) * (Math.PI/180));
            Driver_Controller.SwerveXPassthrough = (scoringPosRed[scoringPos][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveXPassthrough = (scoringPosRed[scoringPos][1] - odoy) * Math.sin(odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle;
        }

    }
}