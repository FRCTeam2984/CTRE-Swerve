package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
//import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import frc.robot.Constants;

public class Intake {
    public static TalonSRX beltDrive = new TalonSRX(Constants.intakeBeltID);
    public static SparkMax topIntake = new SparkMax(Constants.intakeTopMotorID, MotorType.kBrushless);
    public static SparkMax bottomIntake = new SparkMax(Constants.intakeBottomMotorID, MotorType.kBrushless);
    public static SparkMax transportPivot = new SparkMax(Constants.intakeTransportPivotID, MotorType.kBrushless);
    public static SparkMax intakePivot = new SparkMax(Constants.intakePivotMotorID, MotorType.kBrushless);
    public static RelativeEncoder transportEncoder = transportPivot.getEncoder();
    public static RelativeEncoder intakeEncoder = intakePivot.getEncoder();
    private static char intakeLastUsed;
    public static String currentState = "none"; 
    private static int timer;
    public static Boolean retractNeeded = false, movingCoral = false;
    public static Double inPosition = intakeEncoder.getPosition(), transportInsidePosition = transportEncoder.getPosition(), climbPosition = 1.0, depositCoralPosition = 0.5, elevatorSideValue = transportInsidePosition-27;

    public static DigitalInput transportArmSensor = new DigitalInput(0);
    
    public static SparkLimitSwitch insideTransportSwitch = transportPivot.getForwardLimitSwitch();
    public static SparkLimitSwitch outsideTransportSwitch = transportPivot.getReverseLimitSwitch();

    public static SparkLimitSwitch insideSwitch = intakePivot.getForwardLimitSwitch();
    public static SparkLimitSwitch outsideSwitch = intakePivot.getReverseLimitSwitch();
    
    Intake(){
        transportEncoder.setPosition(0.0);
    }
    // clamp function (copy-pasted from elevator section)
    public static Double clamp(Double minimum, Double maximum, Double input){
        if (input < minimum)
            return minimum;
        if (input > maximum)
            return maximum;
        return input;
    }

    // calculate gravity comp for the intake
	// change the .1, define inPosition
    public static Double intakeGravity(){
        Double rotations = intakeEncoder.getPosition()-inPosition;
        return Math.sin(360.0*(Math.PI/180.0)*(rotations-7/*stable position*/)/105) * -0.05;
    }

    // function for retracting the intake for algae and coral
	// change gravityComp and power limits/scaling
	public static Boolean retractIntake(){
        Double maxPower = 0.5, minPower = -0.5, power, position = intakeEncoder.getPosition(), desiredPosition = inPosition;
        Boolean outside = true;
          
        // using limit switches
        if (insideSwitch.isPressed()){
            maxPower = 0.0;
        }
        if (outsideSwitch.isPressed()){
            minPower = 0.0;
            maxPower = 0.0;
            outside = false;
            movingCoral = true;
        }
        // using or not using the bottom intake motor depending on after algae or coral
        spinRollers(0.0);/*  
        if (intakeLastUsed == 'A' && !insideSwitch.isPressed())
            bottomIntake.set(0.1);
        }

        if (intakeLastUsed == 'C'){
            //desiredPosition = depositCoralPosition;
        }*/

        power = intakeGravity()+(desiredPosition-position)/105-0.05; // pivot power based linearly on error + gravity comp
        intakePivot.set(Intake.clamp(minPower, maxPower, power));
        if (outside = false && intakeLastUsed == 'C')
            currentState = "start";
        return outside;
    }
/* 
    //function to bring intake to a position
    //constants prob not right
    public static void intakeTo(String destination){
        // processing the input string to find the correct destination
        Double desiredPosition = 0.0;
        movingCoral = true;
        if (destination == "intakeAlgae")
            intakeLastUsed = 'A';
            desiredPosition = inPosition + 0.1;
            if (bottomIntake.getOutputCurrent() > 2.5)
                bottomIntake.set(0.1);
            else
                bottomIntake.set(0.5);
        if (destination == "stationIntakeCoral")
            intakeLastUsed = 'C';
            desiredPosition = inPosition + 0.07;
        if (destination == "climbPosition")
            intakeLastUsed = 'D';
            desiredPosition = inPosition + climbPosition;
        if (destination == "l1Outtake")
            intakeLastUsed = 'C';
            desiredPosition = inPosition + 0.085;
        Double position, maxPower = 0.5, minPower = -0.5, power;
        position = intakeEncoder.getPosition();
        
        // using limit switches
        if (insideSwitch.isPressed())
            maxPower = 0.0;
        if (outsideSwitch.isPressed())
            minPower = 0.0;
            
        // setting power of the pivot motor
        power = desiredPosition-position+intakeGravity(); // pivot power based linearly on error + gravity comp
        intakePivot.set(clamp(minPower, maxPower, power));
    }*/

    // function for intaking coral
    // change constants!!!!
    public static void intakeCoral(Boolean reversing){
        Double position = intakeEncoder.getPosition();
        Double maxPower = 0.3, minPower = -0.3, power, rollerPower = 0.0, desiredPosition = inPosition+45;

        intakeLastUsed = 'C';
		movingCoral = true;

        // handle limit switches
        if (outsideSwitch.isPressed())
            minPower = 0.0;
        if (insideSwitch.isPressed() || desiredPosition-position > 43){
            minPower = 0.0;
            maxPower = 0.0;
            rollerPower = 0.5;
        }

        // handling reverse intake button
        if (reversing)
            rollerPower *= -1;

        // set roller and pivot motor speeds
        spinRollers(rollerPower);
        power = (desiredPosition-position)/105+intakeGravity(); // pivot power based linearly on error + gravity comp
        System.out.println(power);
        intakePivot.set(clamp(minPower, maxPower, power)); 
    }

    // function for literally just spinning the rollers
    public static void spinRollers(Double speed){
        // limiting power if it is using a lot of power
        if (bottomIntake.getOutputCurrent() > 2.5)
            speed = 0.1;
        bottomIntake.set(speed);
        topIntake.set(speed);
    }

    // function for putting the coral in the elevator
    public static void moveCoral(){
        if (currentState != "run belt")
            beltDrive.set(ControlMode.PercentOutput, 0);
        if (currentState != "use transport arm" && currentState != "return transport arm"){
            transportPivot.set(0);
        }
	    Double minPower = -0.2, maxPower = 0.2;// error = transportEncoder.getPosition()-0.3;
        Double transportGravity = 0.0;//*Math.sin(Math.toRadians(error)) / Math.pow(transportPivot.getOutputCurrent(), 2) * 0.1;
	    //if (Elevator.elevatorTo(Elevator.bottomPosition) && transportEncoder.getPosition() <= 0.05){
            switch (currentState){
		        case ("start"): // start
		    	    timer = 0;
		        	currentState = "run belt";
	        		break;
	            case ("run belt"): // run belt
                    ++timer;
	    		    beltDrive.set(ControlMode.PercentOutput, 0.5);
	    		    if (timer >= 50*3 || transportArmSensor.get() == false) // 3 seconds
	    			    currentState = "use transport arm";
			        break;
		        case ("use transport arm"): // use transport arm
			        if (insideTransportSwitch.isPressed()){
				        maxPower = 0.0;
			        }
			        if (outsideTransportSwitch.isPressed()){
				        transportPivot.set(0);
				        currentState = "return transport arm";
                        break;
                    }
			        transportPivot.set(clamp(minPower, maxPower, transportGravity-(transportEncoder.getPosition()-elevatorSideValue)/30));
                    System.out.println(clamp(minPower, maxPower, transportGravity-(transportEncoder.getPosition()-elevatorSideValue)/30));
                    System.out.println(transportEncoder.getPosition()-elevatorSideValue);
			        break;
		        case ("return transport arm"): // return transport arm
			        if (insideTransportSwitch.isPressed()){
				        transportPivot.set(0);
                        intakeLastUsed = 'D';
				        currentState = "retract intake";
                        break;
			        }
			        if (outsideTransportSwitch.isPressed()){
				        minPower = 0.0;
			        }
			        transportPivot.set(clamp(minPower, maxPower, transportGravity+(transportInsidePosition-transportEncoder.getPosition())/30+0.1));
                    System.out.println(clamp(minPower, maxPower, transportGravity+(transportInsidePosition-transportEncoder.getPosition())/30+0.1));
			        break;
                case ("retract intake"):
                    //if(retractIntake()) currentState = "none";
                    break;
	        }
        /* }else{
            if (insideTransportSwitch.isPressed()){
                maxPower = 0.0;
            }
            if (outsideTransportSwitch.isPressed()){
                minPower = 0.0;
            }
            transportPivot.set(clamp(minPower, maxPower, transportGravity+transportEncoder.getPosition()));
        }*/
    }
}
