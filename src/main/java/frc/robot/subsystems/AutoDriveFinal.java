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

    //use these arrays
    public static double scoringPosRed[][] = { 
        {14.32552578, 3.941908052},
        {14.32552578, 3.611908052},
        {13.61922682, 2.887603562},
        {13.33343843, 2.722603562},
        {12.35313715, 2.971603562},
        {12.06734877, 3.136603562},
        {11.79233045, 4.109908052},
        {11.79233045, 4.439908052},
        {12.49862942, 5.164212542},
        {12.78441780, 5.329212542},
        {13.76471908, 5.080212542},
        {14.05050747, 4.915212542},  
    };
    public static double scoringPosBlue[][] = { 
        {3.222861316, 3.911908052},
        {3.222861316, 4.439908052},
        {3.929160282, 5.164212542},
        {4.214948665, 5.329212542},
        {5.195249944, 5.080212542},
        {5.481038327, 4.915212542},
        {5.756056642, 3.941908052},
        {5.756056642, 3.611908052},
        {5.049757676, 2.887603562},
        {4.763969293, 2.722603562},
        {3.783668014, 2.971603562},
        {3.497879631, 3.136603562},  
    };
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
    public static void driveToXYA(Double x, Double y, Double angle, Double speed){
        Driver_Controller.SwerveControlSet(true);
        String alliance = "red";
        if(DriverStation.getAlliance().toString().charAt(9) == 'B'){   
            alliance = "blue";  // blue
        }
        Double odoAngle = ((RobotContainer.drivetrain.getState().Pose.getRotation().getDegrees()+ 360*1000 + 180)%360) - 180;
        //((RobotContainer.drivetrain.getPigeon2().getYaw().getValueAsDouble() + 360*1000 + 180)%360) - 180;
        Double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        Double odox = RobotContainer.drivetrain.getState().Pose.getX();
        //Double speedMult = 10.0*((alliance == "red")?-1:1);
        speed *= ((alliance == "red")?1:-1);
        // use pythagorean theorum to calculate distance and if it is close enough
        Double dist = Math.pow(Math.pow((odox-x), 2) + Math.pow((odoy-y), 2), 0.5); 
        if (dist > 0.05){
            //Driver_Controller.SwerveCommandXValue = speedMult*(x - odox) * Math.cos(odoAngle);
            //Driver_Controller.SwerveCommandYValue = speedMult*(y - odoy) *  Math.sin(((Math.PI/2)) - odoAngle);
            Double driveAngle = Math.atan2(y - odoy, x - odox);
            Driver_Controller.SwerveCommandXValue = Intake.clamp(0.0, 1.0, dist*0.3+0.15)*-speed*Math.cos(driveAngle);
            Driver_Controller.SwerveCommandYValue = Intake.clamp(0.0, 1.0, dist*0.3+0.15)*-speed*Math.sin(driveAngle);
        }else{
            Driver_Controller.SwerveCommandXValue = 0.0;
            Driver_Controller.SwerveCommandYValue = 0.0;
        }
        Driver_Controller.SwerveCommandEncoderValue = odoAngle*0 + angle;// + ((alliance == "red")?180:0);
    }
    public static Boolean AutoDrive(){
        // alliance = 1;
        Driver_Controller.SwerveControlSet(true);
        double odoy = RobotContainer.drivetrain.getState().Pose.getY();
        double odox = RobotContainer.drivetrain.getState().Pose.getX();
        int scoringPos = (int) Driver_Controller.ReefPosition(); // find how to get rotary control value
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