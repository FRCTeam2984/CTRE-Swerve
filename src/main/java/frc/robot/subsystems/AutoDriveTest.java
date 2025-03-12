package frc.robot.subsystems;

import frc.robot.Robot;

public class AutoDriveTest{   
    public static double odox = 7;// the x axis in m given by odometry;
    public static double odoy = 6;// y axis m value given by odometry;
    public static double odoangle = 0;// degrees given by odometry, will be impt for turning and driving
    // if lost refer to team home and or desmos graph called "autodrive math"

    //statemachine stages
        private static int stage;
        private static int coralNumber = 0;
        private static int farRight1 = 1;
        private static int farRight2 = 2;
        private static int right1 = 3;
        private static int right2 = 4;
        private static int close1 = 5;
        /* close 2
        left 1
        left 2
        far left 1
        far left 2
        far 1
        far 2 */
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
        double inequCoords[][] = {  // coordinates for inequ coords
                        {4.23,2.51}  // close 1
                        };
    
    
        static Double bigCircleR=2.032;   // 80 in
        static Double centerToSmallCirc = 64.5/39.37; // 64.5 in from center of reef to final straight position   1.64     A on the diagram
    
        static double xValueRSide = 0.15;  // will change with testing put in number just to have one
        static double xValueLSide = 0.001 ;  // will change with testing put in number just to have one
        static Double BRMinusDist = (bigCircleR*bigCircleR) - (centerToSmallCirc * centerToSmallCirc); //4.13 - 2.7 =1.43         radius^2 - A^2
        static double rPrime = (BRMinusDist-(xValueRSide * xValueRSide))/(bigCircleR*2 + (2*xValueRSide));  // = .12 m
        static Double rDblePrime = (BRMinusDist-(xValueLSide * xValueLSide))/(bigCircleR*2 + (2*xValueLSide)); //   =.35 m
        static double dCircleDist =  rPrime * rPrime;
        static double ECircleDist = rDblePrime *rDblePrime;
    
    
        static Double bigCircleDist = bigCircleR * bigCircleR;
            
        static double dFReef =(Math.pow((odox - 4.5), 2) + Math.pow((odoy - 4), 2));  // dFReef = distance from reef, checks if you are in big circle or not
        static double circleDCheck =Math.pow((odox-DCoords[0][0]), 2) + Math.pow((odoy - DCoords[0][1]),2);  //checks if robot is in circle d
        static double circleECheck = Math.pow((odox - ECoords[0][0]), 2) + Math.pow((odoy - ECoords[0][1]),2);
            
        static double DInequality = ((scorePos[0][0]-DCoords[0][0])/(scorePos[0][1]-DCoords[0][1]))*((odoy-scorePos[0][1]) + odox);
        static double EInequality = (-(scorePos[0][0]-ECoords[0][0])/(scorePos[0][1]-ECoords[0][1])*(odoy-scorePos[0][1]) + odox);  
        // these lines go through the center and point and d circle (black on desmos) and E circle (red on desmos)
        static double reciprocalDInequ =  (-(scorePos[0][1]-DCoords[0][1])/(scorePos[0][0]-DCoords[0][0])*((odoy-scorePos[0][1]) + odox));
        static double reciprocalEInequ = ((scorePos[0][1]-ECoords[0][1])/(scorePos[0][0]-ECoords[0][0])*(odoy-scorePos[0][1]) + odox);  

        static String areaName;

                                        // the blue and red lines perpendicular to d and e inequal
        public AutoDriveTest(double odox, double odoy, double odoangle){
            // do i need parameters????????????????????????
            //AutoDrivetest math = new AutoDriveTest();
            AutoDriveTest.determineArea(odox, odoy, odoangle);
            AutoDriveTest.driveStraightToCircle();
        }
        public static void determineArea(double odox, double odoy, double odoangle){
            /* Optional<Alliance> ally = DriverStation.getAlliance();
            Int dF
            if (ally.isPresent()) {
            if (ally.get() == Alliance.Red) { */
            stage = close1; // will delete later
            System.out.println("works!!");
            if(stage == close1){
                if(bigCircleDist >= dFReef){
                    areaName = "big circle";
                    System.out.println("big circle");
                    if(circleDCheck <= dCircleDist){
                        areaName = "circle d";
                        System.out.println("circle d");
                    }
                    else if(circleECheck <= ECircleDist){
                        areaName = "circle e";
                        System.out.println("circle e");
                    }
                    else if(odox <=  DInequality && odox<  EInequality){ 
                    // checks red inequality and black in equality   checks whether u are in the c, cprime, or score
                        if(odox<= DCoords[0][0]){
                            if(odoy <= scorePos[1][1]){
                                areaName = "area c";
                                System.out.println("area c");
                                // drive then return bool if it is in right position
                            }
                            else {
                                areaName = "area c prime";
                                System.out.println("area c prime");
                            }
                        }
                        else{
                            areaName = "score automatically";
                            System.out.println("score automatically");
                        }
                    }
                    else if(odoy <= scorePos[0][1]){
                        areaName = "area a";
                        System.out.println("area a");
                    }
                    else{
                        areaName = "area a prime";
                        System.out.println("a prime");
                    }
                                                
                }
                                                
                                            
                                                
                else{
                                                    
                    if(odox >= reciprocalDInequ && odox >= reciprocalEInequ ){
                        if(odoy>scorePos[1][1]){  // means ur in B
                            areaName = "area b";
                            System.out.println("area b");
                        }
                        else{
                            areaName = "area b prime";
                            System.out.println("area b prime");
                        }
                                                
                    }
                }
                    System.exit(0);  // may need to delete
            }
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
            double pointX = ((bigCircleDist*bigCircleDist)/originBasedOdoX) - ((originBasedOdoY*(pointY))/originBasedOdoX);
            double pointXPrime = ((bigCircleDist*bigCircleDist)/originBasedOdoX) - ((originBasedOdoY*(pointYPrime))/originBasedOdoX);
<<<<<<< Updated upstream
            // double distBetweenPointAndCircleE = (Math.pow((odox - ECoords[0][0]), 2) + Math.pow((odoy- ECoords[1][0]), 2));
=======
            double distBetweenPointAndCircleE = (Math.pow((odox - ECoords[0][0]), 2) + Math.pow((odoy- ECoords[1][0]), 2));
>>>>>>> Stashed changes
            double distBetweenPointPrimeAndCircleE = (Math.pow((odox - ECoords[0][0]), 2) + Math.pow((odoy- ECoords[1][0]), 2));   // NOT SQUARE ROOTED NEED TO TAKE INTO ACCOUNT WHEN DOING MATH DONT FORGET PLS
            
            System.out.println("("+pointX + "," + pointY+")");
            System.out.println("("+pointXPrime + "," + pointYPrime+")");

            //if(areaName.equals("area b")){

            //}
        }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
