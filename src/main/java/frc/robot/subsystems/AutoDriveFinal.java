package frc.robot.subsystems;

import frc.robot.RobotContainer;


import edu.wpi.first.wpilibj.DriverStation;

public class AutoDriveFinal{
    public int scoringPos;
    public static double odoangle;
    public static int alliance;
    public static double scoringAngles[] = {0, 0, -60, -60, -120, -120, -180, -180, -240, -240, -300, -300};
    public static double backPosRed[][] = {
        {14.64772578, 4.393108052},
        {14.64772578, 4.065108052},
        {14.17107748, 2.834170177},
        {13.88702114,2.670170177},
        {12.58278782, 2.466970177},
            {12.29873148, 2.630970177},
            {11.47013045, 3.658708052},
            {11.47013045, 3.986708052},
            {11.94677876, 5.217645927},
            {12.23083509,5.381645927},
            {13.53506842, 5.584845927},
            {13.81912475, 5.420845927},
    };
    public static double backPosBlue[][] ={   
        {2.900661316, 3.658708052},
            {2.900661316, 3.986708052},
            {3.37730962, 5.217645927},
            {3.661365952, 5.381645927},
            {4.965599282, 5.584845927},
            {5.249655614, 5.420845927},
            {6.078256642, 4.393108052},
            {6.078256642, 4.065108052},
            {5.601608338, 2.834170177},
            {5.317552006, 2.670170177},
            {4.013318676, 2.466970177},
            {3.729262344, 2.630970177}
    };
    static double scoringPosRed[][] = { 
        {14.34772578, 4.393108052},
        {14.34772578, 4.065108052},
        {14.02107748, 3.093977798},
        {13.73702114, 2.929977798},
        {12.73278782, 2.726777798},
        {12.44873148, 2.890777798},
        {11.77013045, 3.658708052},
        {11.77013045, 3.986708052},
        {12.09677876, 4.957838306},
        {12.38083509, 5.121838306},
        {13.38506842, 5.325038306},
        {13.66912475, 5.161038306},  
    };
    static double scoringPosBlue[][] = { 
        {3.200661316, 3.658708052},
        {3.200661316, 3.986708052},
        {3.52730962, 4.957838306},
        {3.811365952, 5.121838306},
        {4.815599282, 5.325038306},
        {5.099655614, 5.161038306},
        {5.778256642, 4.393108052},
        {5.778256642, 4.065108052},
        {5.451608338, 3.093977798},
        {5.167552006, 2.929977798},
        {4.163318676, 2.726777798},
        {3.879262344, 2.890777798},  
    };
    public static Boolean AutoDrive(){
        // alliance = 1;
        Driver_Controller.SwerveControlSet(true);
        double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        double odox = RobotContainer.drivetrain.getState().Pose.getX();
        int scoringPos = (int) Driver_Controller.buttonReefPosition(); // find how to get rotary control value
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = 1;  // blue
        }
        if(DriverStation.getAlliance().toString().charAt(9) == 'R'){   
            alliance = 2;  // red
        }
        return drive(odox,odoy, scoringPos, alliance);
    }
    /* public static Boolean AutoDriveSecond(){  // created this method just for auto and to be able to change rotary values without actually moving the rotary
        Driver_Controller.SwerveControlSet(true);
        double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        double odox = RobotContainer.drivetrain.getState().Pose.getX();
        int scoringPos = (int) Driver_Controller.buttonReefPosition() + 2; // find how to get rotary control value
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = 1;  // blue
        }
        if(DriverStation.getAlliance().toString().charAt(9) == 'R'){   
            alliance = 2;  // red
        }
        return drive(odox,odoy, scoringPos, alliance);
    } */
    public static Boolean drive(double odox, double odoy, int scoringPos, int alliance){
        double speedMult = 1;  // change through testing
        if(alliance == 1){  // blue
            odoangle = Math.tan(((scoringPosBlue[scoringPos-1][0] - odox)/(scoringPosBlue[scoringPos-1][1] - odoy)) * ((Math.PI)/180));
            System.out.println("angle");
            // odoangle = ((scoringPosBlue[scoringPos-1][0] - odox)/(scoringPosBlue[scoringPos-1][1] - odoy)) * ((Math.PI)/180);
            // Driver_Controller.SwerveCommandXValue = speedMult*(scoringPosBlue[scoringPos-1][0] - odox) *  Math.sin(Math.PI/2 - odoangle);//Math.cos(odoangle);
            // Driver_Controller.SwerveCommandYValue = speedMult*(scoringPosBlue[scoringPos-1][1] - odoy) * Math.cos(odoangle); //Math.sin((Math.PI)/2 - odoangle);
            Driver_Controller.SwerveCommandXValue = speedMult*(scoringPosBlue[scoringPos-1][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveCommandYValue = speedMult*(scoringPosBlue[scoringPos-1][1] - odoy) * Math.sin(((Math.PI)/2) - odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle+scoringAngles[(scoringPos-1)];//odoangle * 180/Math.PI;
            //System.out.println("Driving at " + (scoringPosBlue[scoringPos-1][0] - odox) * Math.cos(odoangle) + ", " + (scoringPosBlue[scoringPos-1][1] - odoy) * Math.sin(odoangle) + ". With a rotation of " + odoangle);
            // return(true)
            if((odox <= (scoringPosBlue[scoringPos -1][0] + .01)) && (odox >= (scoringPosBlue[scoringPos - 1][0]) - 0.01) && (odoy <= (scoringPosBlue[scoringPos - 1][1]) + .01) && (odoy >= (scoringPosBlue[scoringPos - 1][1] - .01))){
                System.out.println("true");    
                return(true);
            }
            
        }
        else{  // then its red
            odoangle = Math.tan(((scoringPosRed[scoringPos-1][0] - odox)/(scoringPosRed[scoringPos-1][1] - odoy)) * (Math.PI/180));
            // odoangle = ((scoringPosRed[scoringPos-1][0] - odox)/(scoringPosRed[scoringPos-1][1] - odoy));
            Driver_Controller.SwerveCommandXValue = -speedMult*(scoringPosRed[scoringPos-1][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveCommandYValue = -speedMult*(scoringPosRed[scoringPos-1][1] - odoy) *  Math.sin(((Math.PI/2)) - odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle+scoringAngles[scoringPos-1] + 180 ;
            // return(true)
            if((odox <= (scoringPosRed[scoringPos -1][0] + .01)) && (odox >= (scoringPosRed[scoringPos - 1][0]) - 0.01) && (odoy <= (scoringPosRed[scoringPos - 1][1]) + .01) && (odoy >= (scoringPosRed[scoringPos - 1][1] - .01)))
                return(true);
        }
        // return(false);
        return (Math.abs(Driver_Controller.SwerveCommandXValue) < 0.01 && Math.abs(Driver_Controller.SwerveCommandYValue) < 0.01);
    }
    public static Boolean driveBackwards(int scoringPos){
        Driver_Controller.SwerveControlSet(true);
        double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        double odox = RobotContainer.drivetrain.getState().Pose.getX();
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = 1;  // blue
        }
        if(DriverStation.getAlliance().toString().charAt(9) == 'R'){   
            alliance = 2;  // red
        }
        double speedMult = 1;
        if(alliance == 1){  // blue
            odoangle = Math.tan(((backPosBlue[scoringPos-1][0] - odox)/(backPosBlue[scoringPos-1][1] - odoy)) * ((Math.PI)/180));
            // odoangle = ((scoringPosBlue[scoringPos-1][0] - odox)/(scoringPosBlue[scoringPos-1][1] - odoy)) * ((Math.PI)/180);
            // Driver_Controller.SwerveCommandXValue = speedMult*(scoringPosBlue[scoringPos-1][0] - odox) *  Math.sin(Math.PI/2 - odoangle);//Math.cos(odoangle);
            // Driver_Controller.SwerveCommandYValue = speedMult*(scoringPosBlue[scoringPos-1][1] - odoy) * Math.cos(odoangle); //Math.sin((Math.PI)/2 - odoangle);
            Driver_Controller.SwerveCommandXValue = speedMult*(backPosBlue[scoringPos-1][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveCommandYValue = speedMult*(backPosBlue[scoringPos-1][1] - odoy) * Math.sin(((Math.PI)/2) - odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle+scoringAngles[scoringPos-1];//odoangle * 180/Math.PI;
            //System.out.println("Driving at " + (scoringPosBlue[scoringPos-1][0] - odox) * Math.cos(odoangle) + ", " + (scoringPosBlue[scoringPos-1][1] - odoy) * Math.sin(odoangle) + ". With a rotation of " + odoangle);
            // return(true);
        }
        else{  // then its red
            odoangle = Math.tan(((backPosRed[scoringPos-1][0] - odox)/(backPosRed[scoringPos-1][1] - odoy)) * (Math.PI/180));
            // odoangle = ((scoringPosRed[scoringPos-1][0] - odox)/(scoringPosRed[scoringPos-1][1] - odoy));
            Driver_Controller.SwerveCommandXValue = -speedMult*(backPosRed[scoringPos-1][0] - odox) * Math.cos(odoangle);
            Driver_Controller.SwerveCommandYValue = -speedMult*(backPosRed[scoringPos-1][1] - odoy) *  Math.sin(((Math.PI/2)) - odoangle);
            Driver_Controller.SwerveCommandEncoderValue = odoangle+scoringAngles[scoringPos-1] + 180;
            // return(true);
        }
        return (Math.abs(Driver_Controller.SwerveCommandXValue) < 0.05 && Math.abs(Driver_Controller.SwerveCommandYValue) < 0.05);
    }
}