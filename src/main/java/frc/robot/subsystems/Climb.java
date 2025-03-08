package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.Servo;
import com.ctre.phoenix6.hardware.TalonFX;

public class Climb {
	static int openPosition = 0, closedPosition = 47, servoPosition = 130;//closedPosition;
	public static Servo servo = new Servo(0);
	public static TalonFX climb = new TalonFX(Constants.climbID); 
	//DigitalInput limitUnwindedForward = new DigitalInput(number);
	//DigitalInput limitUnwindedBackward = new DigitalInput(number);

	public static void letsClimb(){// add something
		Double power = 0.0;
		if(Driver_Controller.buttonExtendClimb() && System.currentTimeMillis() > 1000){
			//if(climb.getForwardLimit().getValue().toString() != "ClosedToGround") // limit switches, use kyle’s limits
				power = 0.5;
			// find out led code -> LED light flashes or solid a specific color
		}
		if(Driver_Controller.buttonRetractClimb()){
			//if (Elevator.elevatorTo(Elevator.bottomPosition) && Elevator.extendedOrRetracted == "retracted" && Math.abs(Intake.intakeEncoder.getPosition()-(Intake.climbPosition+Intake.inPosition)) < 0.05 && Intake.transportEncoder.getPosition() < 0.5){
				
			if(climb.getReverseLimit().getValue().toString() != "ClosedToGround") // limit switch
				power = -0.5; // retract climb
			/* }else{
				Intake.intakeTo("climbPosition");
				if (Elevator.extendedOrRetracted != "extended"){
					if(Elevator.lastExtendOrRetract == "extend"){
						Elevator.armTimer = 0.0;
					}
					Elevator.moveElevatorArm("retract");
				}
			}*/
		}
		if(Driver_Controller.buttonExtendClimb()) {
			servoPosition = openPosition;
			//++servoPosition;
			//System.out.println(servoPosition);
		}
		else {
			servoPosition = closedPosition;
		}
		// if(Driver_Controller.buttonRetractClimb()){
		// 	--servoPosition;
		// 	System.out.println(servoPosition);
		// } 
		servo.setAngle(servoPosition);
		//climb.set(power);
		
	}
}