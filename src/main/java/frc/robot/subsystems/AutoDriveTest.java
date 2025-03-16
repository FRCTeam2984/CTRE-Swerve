package frc.robot.subsystems;

import frc.robot.Robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
public class AutoDriveTest{   
    public static double odox = 7;// the x axis in m given by odometry;
    public static double odoy = 6;// y axis m value given by odometry;
    public static double odoangle = 0;// degrees given by odometry, will be impt for turning and driving
    // if lost refer to team home and or desmos graph called "autodrive math" or siena

    //statemachine stages
        private static int stage;
        private static int coralNumber = 0;
        private static int farRight1 = 1;
        private static int farRight2 = 2;
        private static int right1 = 3;
        private static int right2 = 4;
        private static int close1 = 5;
        private static int close2 = 6;
        private static int left1 = 7;
        private static int left2 = 8;
        private static int farLeft1 = 9;
        private static int farLeft2 = 10;
        private static int far1 = 11;
        private static int far2 = 12;

        static String areaName;
        static String bigCircle;
        static String areaB;
        static String areaBPrime;
        static String areaA;
        static String areaAPrime;
        static String areaC;
        static String areaCPrime;
        static String areaD;
        static String areaE;
        static double scorePos[][] = {    // idk whether this is needed or not
                        {4.5,4},  // reef
                        {3.74, 3.84}, // close 1
                        {3.75, 4.1}, // close 2
                    };
    
        static double DCoords[][] = {  // coordinates for circle D
                        {2.86,3.52} // close 1
                        };
        static double ECoords[][] = {  // coordinates for circle E
                        {2.86, 4.19}  // close 1
                        };
        static double DCoordsOriginBased[][] = {
            {-1.64,-.48}  // clsoe 1
        };
        static double ECoordsOriginBased[][] = {
            {-1.64,.19}  // close 1
        };

        static double ECoordsBottom [][] = {  // origin based           for curve along big circle
                        {-1.99,.19}  // close 1
                        };
        static double DCoordsBottom[][] = {
            {-1.96, -.48}
        };
        static double inequCoords[][] = {  // coordinates for inequ coords
                    {4.23,2.51}  // close 1
                    };
        static Double bigCircleR=2.032;   // 80 in
        static Double centerToSmallCirc = 64.5/39.37; // 64.5 in from center of reef to final straight position   1.64     A on the diagram
    
        static double xValueRSide = 0.15;  // will change with testing put in number just to have one
        static double xValueLSide = 0.001 ;  // will change with testing put in number just to have one
        static Double BRMinusDist = (bigCircleR*bigCircleR) - (centerToSmallCirc * centerToSmallCirc); //4.13 - 2.7 =1.43         radius^2 - A^2
        static double rPrime = (BRMinusDist-(xValueRSide * xValueRSide))/(bigCircleR*2 + (2*xValueRSide));  // = .12 m
        static Double rDblePrime = (BRMinusDist-(xValueLSide * xValueLSide))/(bigCircleR*2 + (2*xValueLSide)); //   =.35 m
        static double DCircleDist =  rPrime * rPrime;
        static double ECircleDist = rDblePrime *rDblePrime;
    
    
        static Double bigCircleDist = bigCircleR * bigCircleR;
            
        static double dFReef =(Math.pow((odox - 4.5), 2) + Math.pow((odoy - 4), 2));  // dFReef = distance from reef, checks if you are in big circle or not
        static double circleDCheck =Math.pow((odox-DCoords[0][0]), 2) + Math.pow((odoy - DCoords[0][1]),2);  //checks if robot is in circle d
        static double circleECheck = Math.pow((odox - ECoords[0][0]), 2) + Math.pow((odoy - ECoords[0][1]),2);
            
        
        

        static double originBasedOdoX = odox - scorePos[0][0];  // because we will subtract the reef (4.5,4) from the og points as we want reef to be at origin for cleaner math to find tangent line to circle   // this is c on desmos
        static double originBasedOdoY = odoy - scorePos[0][1];  // this is b on desmos
        static double originBasedOdoXSquared = originBasedOdoX * originBasedOdoX;   
        static double originBasedOdoYSquared = originBasedOdoY * originBasedOdoY;
        static double DInequality;
        static double EInequality;
        static double reciprocalDInequ;
        static double reciprocalEInequ;
                                        // the blue and red lines perpendicular to d and e inequal
        public static void AutoDrive(double odox, double odoy, double odoangle){
            // do i need parameters????????????????????????
            //AutoDrivetest math = new AutoDriveTest();
            AutoDriveTest.determineArea(odox, odoy, odoangle);
            AutoDriveTest.driveStraightToCircle();
        }
        public static void determineArea(double odox, double odoy, double odoangle){
            Optional ally = DriverStation.getAlliance(); //Optional[Alliance]
            int dF;
            if (ally.isPresent()) {
            if (ally.get().toString() == "Red") { 
            Driver_Controller.SwerveControlSet(true);
                        stage = close1; // will delete later
                        System.out.println("works!!");
                        if(stage == close1){
                            if(bigCircleDist >= dFReef){
                                areaName = bigCircle;
                                System.out.println("115");
                                System.out.println(bigCircle);
                                if(circleDCheck <= DCircleDist){
                                    areaName = areaD;
                                    System.out.println(areaD);
                                }
                                else{ if(circleECheck <= ECircleDist){
                                    areaName = areaE;
                                    System.out.println(areaE);
                                }}
                                DInequality = ((scorePos[0][0]-DCoords[0][0])/(scorePos[0][1]-DCoords[0][1]))*((odoy-scorePos[0][1]) + odox);
                                EInequality = (-(scorePos[0][0]-ECoords[0][0])/(scorePos[0][1]-ECoords[0][1])*(odoy-scorePos[0][1]) + odox);  
                                // these lines go through the center and point and d circle (black on desmos) and E circle (red on desmos)
                                reciprocalDInequ =  (-(scorePos[0][1]-DCoords[0][1])/(scorePos[0][0]-DCoords[0][0])*((odoy-scorePos[0][1]) + odox));
                                reciprocalEInequ = ((scorePos[0][1]-ECoords[0][1])/(scorePos[0][0]-ECoords[0][0])*(odoy-scorePos[0][1]) + odox);   
                                //else if(odox <=  DInequality && odox<  EInequality){ 
                                // checks red inequality and black in equality   checks whether u are in the c, cprime, or score
                                
                                    if(odox<= DCoords[0][0]){
                                        if(odoy <= scorePos[1][1]){
                                            areaName = areaC;
                                            System.out.println(areaC);
                                            // drive then return bool if it is in right position
                                        }
                                        else {
                                            areaName = areaCPrime;
                                            System.out.println(areaCPrime);
                                        }
                                    }
                                    else{
                                        areaName = "score automatically";
                                        System.out.println("score automatically");
                                    }
                                }
                                else if(odoy <= scorePos[0][1]){
                                    areaName = areaA;
                                    System.out.println(areaA);
                                }
                                else{
                                    areaName = areaAPrime;
                                    System.out.println(areaAPrime);
                                }
                                                            
                            }
                                                    
                                                        
                                                            
                            else{
                                                                
                                //if(odox >= reciprocalDInequ && odox >= reciprocalEInequ ){
                                    if(odoy>scorePos[1][1]){  // means ur in B
                                        areaName = areaB;
                                        System.out.println(areaB);
                                    }
                                    else{
                                        areaName = areaBPrime;
                                        System.out.println(areaBPrime);
                                    }
                                                            
                                }
                            }
                                //System.exit(0);  // may need to delete
                        }
                        //else if(stage == close2){
                            if(bigCircleDist >= dFReef){
                                areaName = bigCircle;
                                
                                System.out.println(bigCircle);
                                if(circleDCheck <= DCircleDist){
                                    areaName = areaD;
                                    System.out.println(areaD);
                                }
                                else if(circleECheck <= ECircleDist){
                                    areaName = areaE;
                                    System.out.println(areaE);
                                }
                                else if(odox <=  DInequality && odox<  EInequality){ 
                                // checks red inequality and black in equality   checks whether u are in the c, cprime, or score
                                    if(odox<= DCoords[0][0]){
                                        if(odoy <= scorePos[1][1]){
                                            areaName = areaC;
                                            System.out.println(areaC);
                                            // drive then return bool if it is in right position
                                        }
                                        else {
                                            areaName = areaCPrime;
                                            System.out.println(areaCPrime);
                                        }
                                    }
                                    else{
                                        areaName = "score automatically";
                                        System.out.println("score automatically");
                                    }
                                }
                                else if(odoy <= scorePos[0][1]){
                                    areaName = areaA;
                                    System.out.println(areaA);
                                }
                                else{
                                    areaName = areaAPrime;
                                    System.out.println(areaAPrime);
                                }
                                                            
                            }
                                                            
                                                        
                                                            
                            else{
                                                                
                                if(odox >= reciprocalDInequ && odox >= reciprocalEInequ ){
                                    if(odoy>scorePos[1][1]){  // means ur in B
                                        areaName = areaB;
                                        System.out.println(areaB);
                                    }
                                    else{
                                        areaName = areaBPrime;
                                        System.out.println(areaBPrime);
                                    }
                                                            
                                }
                            }
                                //System.exit(0);  // may need to delete
                        }
                
                
            
                
                    /* Else{
                                            if(odoy>scorePos[1][1]){  // means ur in C
                                                System.out.println(“big circle”);
                                            }
                                            Else{
                                                // ur in C prime
                                            } *
            
                                        }
                                }
                            }
                            if (ally.get() == Alliance.Blue) {
                                if(// check if ur in A or D or E or inside C or inside c primer){
                                
                                }
                                else{
                                    // check if your in b or outside c or outside c prime or b prime
                                }
                                }
            
                        
            
                            if(// check if ur in A or D or E or inside C or inside c primer){
                                
                            }
                            else{
                                // check if your in b or outside c or outside c prime or b prime
                            }
                        }
            
            
            
                    }*/
   
                    public static void driveStraightToCircle(){
                        double originBasedOdoX = odox - scorePos[0][0];  // because we will subtract the reef (4.5,4) from the og points as we want reef to be at origin for cleaner math to find tangent line to circle   // this is c on desmos
                        double originBasedOdoY = odoy - scorePos[1][0];  // this is b on desmos
                         double originBasedOdoXSquared = originBasedOdoX * originBasedOdoX;   
                         double originBasedOdoYSquared = originBasedOdoY * originBasedOdoY;   
            
                        // reference to straight line to circle desmos graph, REMEMBER x and y r switched compared to the desmos
                        // may have to swap originbasedodoy and originbasedodox with each other idk
                       
                        
                        double pointY = ((2*originBasedOdoY)*(bigCircleDist) + (Math.sqrt((Math.pow((-2*originBasedOdoY*(bigCircleDist)),2)-4*((originBasedOdoXSquared)+(originBasedOdoYSquared))*((bigCircleDist*bigCircleDist)-bigCircleDist*(originBasedOdoXSquared))))))/(2*(originBasedOdoXSquared + originBasedOdoYSquared));
                        double pointYPrime = ((2*originBasedOdoY)*(bigCircleDist) - (Math.sqrt((Math.pow((-2*originBasedOdoY*(bigCircleDist)),2)-4*((originBasedOdoXSquared)+(originBasedOdoYSquared))*((bigCircleDist*bigCircleDist)-bigCircleDist*(originBasedOdoXSquared))))))/(2*(originBasedOdoXSquared + originBasedOdoYSquared));
                        double pointX = ((bigCircleDist)/originBasedOdoX) - ((originBasedOdoY*(pointY))/originBasedOdoX);
                        double pointXPrime = ((bigCircleDist)/originBasedOdoX) - ((originBasedOdoY*(pointYPrime))/originBasedOdoX);
                        double distBetweenPointAndCircleE = (Math.pow((pointX - ECoords[0][0]), 2) + Math.pow((pointY- ECoords[0][1]), 2));
                        double distBetweenPointPrimeAndCircleE = (Math.pow((pointXPrime - ECoords[0][0]), 2) + Math.pow((pointYPrime- ECoords[0][1]), 2));   // NOT SQUARE ROOTED NEED TO TAKE INTO ACCOUNT WHEN DOING MATH DONT FORGET PLS
                        double distBetweenPointAndCircleD = (Math.pow((pointX - DCoords[0][0]), 2) + Math.pow((pointY- DCoords[0][1]), 2));
                        double distBetweenPointPrimeAndCircleD = (Math.pow((pointXPrime - DCoords[0][0]), 2) + Math.pow((pointYPrime- DCoords[0][1]), 2));   // NOT SQUARE ROOTED NEED TO TAKE INTO ACCOUNT WHEN DOING MATH DONT FORGET PLS
            
                        System.out.println("("+pointX + "," + pointY+")");
                        System.out.println("("+pointXPrime + "," + pointYPrime+")");
                        double angleToDriveToPoint;
                        areaName = areaB;

            
                        if(areaName == areaB){
                            if((distBetweenPointAndCircleE > distBetweenPointPrimeAndCircleE) && (distBetweenPointAndCircleD > distBetweenPointPrimeAndCircleD)){  // find the closer dot
                                // go to point (not prime point)
                                angleToDriveToPoint = ((pointY - originBasedOdoY)/(pointX - originBasedOdoX));
                                // may need to convert this angle to something but this should be the degrees needed to turn (slope) to eventually get to the point
                                // drive to the point
            
                            }
                            else{
                                angleToDriveToPoint = ((pointYPrime - originBasedOdoY)/(pointXPrime - originBasedOdoY));  // drives to point prime
                                // may need to convert this angle to something but this should be the degrees needed to turn (slope) to eventually get to the point
                                // drive to point prime
                            }
                            System.out.println(angleToDriveToPoint);
                            Driver_Controller.SwerveCommandEncoderValue = angleToDriveToPoint;  // slope will be constantly changing, somehow adjust code to do this

                        }
                    }
            
                    public static void curveAlongCircle(){
                        boolean goAlongCircle = true;
                        double slopeForGoingAlongCircle;
                        double newYPoint;
                        while(goAlongCircle = true){
                            if(originBasedOdoY < 0)  // on left or right side of circle
                                newYPoint = -Math.sqrt(bigCircleDist - (originBasedOdoXSquared*originBasedOdoXSquared));  // may need to change negative signs
                            else{
                                newYPoint = Math.sqrt(bigCircleDist - (originBasedOdoXSquared*originBasedOdoXSquared));
                            }
                            if((originBasedOdoX < 0) && (originBasedOdoY > 0)){  // quad 2
                                slopeForGoingAlongCircle = (originBasedOdoX/newYPoint);  // we are changing the slope based on the new x value which will change cuz we are moving
                            }
                            else{
                                slopeForGoingAlongCircle = (-originBasedOdoX/newYPoint); // not sure if this negative is needed or not
                            }                        
                        
                            Driver_Controller.SwerveCommandEncoderValue = slopeForGoingAlongCircle;  // slope will be constantly changing, somehow adjust code to do this
                            Driver_Controller.SwerveCommandXValue = -1;  // DO I NEED THIS             might also be positive instead 
                            if(((ECoordsBottom[0][0] +.1) <= originBasedOdoX) && (originBasedOdoX >= (ECoordsBottom[0][0] - .1)) && ((ECoordsBottom[0][1] - .1) <= originBasedOdoY) && (originBasedOdoY <= (ECoordsBottom[0][1] + .1))){  // checks if its at ecoordbottom[0] but with error
                                goAlongCircle = false;  // than u can exit loop and starting turning into the little circle
                                areaName = areaE;
                            }
                            else if(((DCoordsBottom[0][0] +.1) <= originBasedOdoX) && (originBasedOdoX >= (DCoordsBottom[0][0] - .1)) && ((DCoordsBottom[0][1] - .1) <= originBasedOdoY) && (originBasedOdoY <= (DCoordsBottom[0][1] + .1))){ 
                                 // checks if its at D circle bottom but room for error, will change with testing
                                 goAlongCircle = false;  // than u can exit loop and starting turning into the little circle
                                 areaName = areaD;
                            }
                        }
                    }
                    public static void curveAlongTinyCircle(){  // basically the same thing as the earlier method but a dif circle equation
                        boolean goAlongTinyCircle = true;
                        boolean whichCircle; // true = circle E    false = circle D
                        double newYPoint;
                        double slopeForGoingAlongTinyCircle;
                        if(areaName == areaE){
                            whichCircle = true;
                        }
                        else{  // ur in circle d
                            whichCircle = false;
                        }
                        while(goAlongTinyCircle = true){
                            if(whichCircle){
                                // radius is rdoubleprime
                                if(originBasedOdoX < (ECoords[0][0] - scorePos[0][0])){  // determines if u need top or bottom of circle
                                    // bottom part of circle
                                    newYPoint = -Math.sqrt((rDblePrime*rDblePrime) - (originBasedOdoX - ECoordsOriginBased[0][0])*(originBasedOdoX - ECoordsOriginBased[0][0])) - ECoordsOriginBased[0][1];
                                }
                                else{
                                    newYPoint = Math.sqrt((rDblePrime*rDblePrime) - (originBasedOdoX - ECoordsOriginBased[0][0])*(originBasedOdoX - ECoordsOriginBased[0][0])) - ECoordsOriginBased[0][1];
                                }
                            }
                            else{
                                if(originBasedOdoX < (DCoords[0][0] - scorePos[0][0])){  // determines if u need top or bottom of circle
                                    // bottom part of circle
                                    newYPoint = -Math.sqrt((rPrime*rPrime) - (originBasedOdoX - DCoordsOriginBased[0][0])*(originBasedOdoX - DCoordsOriginBased[0][0])) - DCoordsOriginBased[0][1];
                                }
                                else{
                                    newYPoint = Math.sqrt((rPrime*rPrime) - (originBasedOdoX - DCoordsOriginBased[0][0])*(originBasedOdoX - DCoordsOriginBased[0][0])) - DCoordsOriginBased[0][1];
                                }
                            }
                            if((originBasedOdoX < 0) && (originBasedOdoY > 0)){  // quad 2
                                slopeForGoingAlongTinyCircle = (originBasedOdoX/newYPoint);  // we are changing the slope based on the new x value which will change cuz we are moving
                            }
                            else{
                                slopeForGoingAlongTinyCircle = (-originBasedOdoX/newYPoint); // not sure if this negative is needed or not
                            }                        
                        
                            Driver_Controller.SwerveCommandEncoderValue = slopeForGoingAlongTinyCircle;

                                
                        }
            
                    }
            }
                    