package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

//import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import frc.robot.Constants;

public class Intake {
    public static TalonSRX bottomIntake = new TalonSRX(Constants.intakeBottomMotorID);
    //public static SparkMax bottomIntake = new SparkMax(Constants.intakeBottomMotorID, MotorType.kBrushless);
    public static SparkMax intakePivot = new SparkMax(Constants.intakePivotMotorID, MotorType.kBrushless);
    public static RelativeEncoder intakeEncoder = intakePivot.getEncoder();
    //public static RelativeEncoder rollerEncoder = bottomIntake.getEncoder();
    public static DigitalInput insideSwitch = new DigitalInput(1);//extended
    public static DigitalInput outsideSwitch = new DigitalInput(2);//retracted
    public static DutyCycleEncoder armEncoder = new DutyCycleEncoder(0);

    public static Integer historyLength = 12;
    public static Double[] rollerSpeed = new Double[historyLength], rollerCurrent = new Double[historyLength];
    public static Boolean retractedSwitchPressed, extendedSwitchPressed;
    public static String intakeState = "retract";
    public static Double currentPosition, intakeGravity, desiredPosition, powerFactor = 1.0, upAdjust = 0.0;

    public static void intakePeriodic(){
        // updating values
        retractedSwitchPressed = outsideSwitch.get(); extendedSwitchPressed = insideSwitch.get();
        // if (retractedSwitchPressed){intakeEncoder.setPosition(0.0);}
        // else if (extendedSwitchPressed){intakeEncoder.setPosition(42.0);}
        // currentPosition = intakeEncoder.getPosition();
        // if (currentPosition < 0)intakeEncoder.setPosition(0.0);
        // currentPosition = intakeEncoder.getPosition();
        currentPosition = ((10.4-armEncoder.get())%1+0.304-0.4)*42/(.17+.304);
        intakeGravity = Math.sin(360.0*(Math.PI/180.0)*(currentPosition-4)/105) * -0.02;
        // moving the arm based on inputs
        Double motorPower = 0.0;
        switch(intakeState){
            case "remove":
                desiredPosition = 10.0;
                motorPower = moveIntake();
                break;
            case "reset":
                if (retractedSwitchPressed) motorPower = 0.0;
                else motorPower = -0.3;
                break;
            case "intake":
                // 22;
                desiredPosition = Math.max(0, 21.0-upAdjust);
                motorPower = moveIntake();
                System.out.println(motorPower);
                break;
            case "retract":
                motorPower = retract();
                break;
            case "hold":
                if(intakeEncoder.getPosition() > 3){
                    motorPower = intakeGravity;
                }else motorPower = 0.0;
                break;
        }
        //System.out.println(motorPower);
        //intakePivot.set(motorPower);
        controlRoller();
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
	public static Double retract(){
        Double maxPower = 0.5, minPower = -0.5;

        // calculate power, gravity compensation + more power for how far away
        Double power = intakeGravity-(currentPosition/25)-0.1;
        // using limit switches
        if (extendedSwitchPressed){
            maxPower = 0.0;
        }
        // increase power if far from inside
        if (currentPosition > 5) power -= 0.1;
        if (retractedSwitchPressed){
            power = 0.0; // sets power to 0 if close to inside limit switch
        }
        return(clamp(minPower, maxPower, power));
    }

    //function to bring intake to a position
    //constants prob not right
    public static Double moveIntake(){
        // processing the input string to find the correct destination
        Double error = (desiredPosition-currentPosition);
        Double maxPower = 0.5, minPower = -0.5;
        Double power = (error)/25+intakeGravity;

        // using limit switches
        if (extendedSwitchPressed){
            maxPower = 0.0;
        }
        if (retractedSwitchPressed){
            minPower = 0.0;
        }
            
        //power += (error > 0?0.1:-0.1);
        if (Math.abs(error) < 0.5) power = intakeGravity;
        return(clamp(minPower, maxPower, power));
    }

    public static void controlRoller(){
        Double averageCurrent = 0.0;//, averageSpeed = 0.0;
        for (int i = historyLength-2; i >= 0; --i){
            rollerCurrent[i+1] = rollerCurrent[i];
            //rollerSpeed[i+1] = rollerSpeed[i];
            if (rollerSpeed[i] < 0){
                averageCurrent += rollerCurrent[i];
            }
            //averageSpeed += rollerSpeed[i];
        }
        rollerCurrent[0] = bottomIntake.getStatorCurrent();
        //rollerSpeed[0] = -rollerEncoder.getVelocity();
        averageCurrent += rollerCurrent[0];
        //averageSpeed += rollerSpeed[0];
        averageCurrent /= (double) historyLength;
        //averageSpeed /= (double) historyLength;

        averageCurrent = Math.abs(averageCurrent);
        //averageSpeed = Math.abs(averageSpeed);
        Double maxRPM = 6000.0, maxCurrent = 1.5;
        if (averageCurrent > maxCurrent){
            powerFactor = 0.05;
        }
        //Double allowedCurrent = 2+(averageSpeed/maxRPM)*(maxCurrent-2);
        //powerFactor = clamp(0.1, 1.0, 2.0-(2.0*averageCurrent/allowedCurrent));
        /*System.out.print("outputs ");
        System.out.print(averageCurrent);
        System.out.print(' ');
        System.out.print((int)(1*averageSpeed));
        System.out.print(' ');
        //if (powerFactor < 0.99)
        System.out.println((int) (100*powerFactor));*/
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
