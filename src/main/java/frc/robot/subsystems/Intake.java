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
    public static SparkMax bottomIntake = new SparkMax(Constants.intakeBottomMotorID, MotorType.kBrushless);
    public static SparkMax intakePivot = new SparkMax(Constants.intakePivotMotorID, MotorType.kBrushless);
    public static RelativeEncoder intakeEncoder = intakePivot.getEncoder();
    public static SparkLimitSwitch insideSwitch = intakePivot.getReverseLimitSwitch();
    public static SparkLimitSwitch outsideSwitch = intakePivot.getForwardLimitSwitch();

    public static Boolean retractedSwitchPressed, extendedSwitchPressed;
    public static String intakeState;
    public static Double currentPosition, intakeGravity;

    public static void intakePeriodic(){
        retractedSwitchPressed = outsideSwitch.isPressed(); extendedSwitchPressed = insideSwitch.isPressed();
        if (outsideSwitch.isPressed()){intakeEncoder.setPosition(0.0);}
        else if (insideSwitch.isPressed()){intakeEncoder.setPosition(48.64);}
        currentPosition = -intakeEncoder.getPosition();
        intakeGravity = Math.sin(360.0*(Math.PI/180.0)*(currentPosition-6)/105) * -0.04;
        switch(intakeState){
            case "reset":
                if (outsideSwitch.isPressed()) intakePivot.set(0);
                else intakePivot.set(-0.3);
                break;
            case "intake":
                intakeAlgae();
                break;
            case "retract":
                retract();
                break;
        }
    }
    
    // clamp function (copy-pasted from elevator section)
    public static Double clamp(Double minimum, Double maximum, Double input){
        if (input < minimum)
            return minimum;
        if (input > maximum)
            return maximum;
        return input;
    }

    // function for retracting the intake for algae and coral
	// change gravityComp and power limits/scaling
	public static void retract(){
        Double maxPower = 0.5, minPower = -0.5;

        Double power = intakeGravity+currentPosition/75;
        // using limit switches
        if (insideSwitch.isPressed()){
            maxPower = 0.0;
        }
        if (outsideSwitch.isPressed()){
            power = 0.0;
        }if(Math.abs(currentPosition) < 2){
            power = intakeGravity;
        }

        power = intakeGravity+currentPosition/75;
        if (outsideSwitch.isPressed() == false && currentPosition < 5) power -= 0.2;
        
        intakePivot.set(clamp(minPower, maxPower, power));
    }

    //function to bring intake to a position
    //constants prob not right
    public static void intakeAlgae(){
        // processing the input string to find the correct destination
        Double desiredPosition = 22.0, error = desiredPosition+currentPosition;
        Double maxPower = 0.5, minPower = -0.5;
        Double power = (error)/10+intakeGravity;

        // using limit switches
        if (insideSwitch.isPressed())
            maxPower = 0.0;
        if (outsideSwitch.isPressed())
            minPower = 0.0;
            
        //power += (error > 0?0.1:-0.1);
        if (Math.abs(error) < 1) power = intakeGravity;
        intakePivot.set(clamp(minPower, maxPower, power));
    }
    /*
    // function for intaking coral
    // change constants!!!!
    public static void intakeCoral(Boolean reversing){
        Double position = intakeEncoder.getPosition();
        Double maxPower = 0.7, minPower = -0.7, power, rollerPower = 0.35, desiredPosition = inPosition+45;

        intakeLastUsed = 'C';
		movingCoral = true;

        // handle limit switches
        if (outsideSwitch.isPressed())
            minPower = 0.0;
        if (insideSwitch.isPressed() || (desiredPosition-position < 2)){
            minPower = 0.0;  // Kevin, talk to Dad about keeping gravity compensation here
            maxPower = 0.0;
        }

        // handling reverse intake button
        if (reversing)
            rollerPower *= -1;

        // set roller and pivot motor speeds
        spinRollers(rollerPower);
        power = (desiredPosition-position)/105+intakeGravity()+0.1; // pivot power based linearly on error + gravity comp
        intakePivot.set(clamp(minPower, maxPower, power)); 
    }

    // function for literally just spinning the rollers
    public static void spinRollers(Double speed){
        // limiting power if it is using a lot of power
        // if (bottomIntake.getOutputCurrent() > 2.5)
            // speed = 0.1;
        bottomIntake.set(speed);
        topIntake.set(-speed);
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
            switch (currentState){
		        case ("start"): // start
		    	    timer = 0;
		        	currentState = "run belt";
	        		break;
	            case ("run belt"): // run belt
                    ++timer;
	    		    beltDrive.set(ControlMode.PercentOutput, -0.5);
	    		    if (timer >= 50*2 || transportArmSensor.get() == false){ // 3 seconds
	    			    currentState = "use transport arm";
                        timer = 0;
                    }
			        break;
		        case ("use transport arm"): // use transport arm
                    ++timer;
			        if (insideTransportSwitch.isPressed()){
				        maxPower = 0.0;
			        }
			        if (/*outsideTransportSwitch.isPressed() || timer > 50*1){
				        transportPivot.set(0);
				        currentState = "return transport arm";
                        break;
                    }
                    //if (retractIntake() == false && Elevator.elevatorTo(0.0)){
			            transportPivot.set(clamp(minPower, maxPower, transportGravity-(transportEncoder.getPosition()-elevatorSideValue)/66-0.02));
                    //}
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
			        transportPivot.set(clamp(minPower, maxPower, transportGravity+(transportInsidePosition-transportEncoder.getPosition())/66+0.05));
                    break;
                case ("retract intake"):
                    if(retractIntake() == false) currentState = "none";
                    break;
	        }
        else{
            if (insideTransportSwitch.isPressed()){
                maxPower = 0.0;
            }
            if (outsideTransportSwitch.isPressed()){
                minPower = 0.0;
            }
            transportPivot.set(clamp(minPower, maxPower, transportGravity+transportEncoder.getPosition()));
        }
    }*/
}
