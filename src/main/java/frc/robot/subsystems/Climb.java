package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.Servo;
import com.ctre.phoenix6.hardware.TalonFX;

public class Climb {
	public static boolean extendLastPressed;
	static int closedPosition = -10, openPosition = 60, servoPosition = closedPosition, timer = 0;
	public static Servo servo = new Servo(0);
	public static TalonFX climb = new TalonFX(Constants.climbID); 
	public static Double inPosition = Double.parseDouble(climb.getRotorPosition().toString().substring(0, 10));

	public static void letsClimb(){
		if (climb.getReverseLimit().getValue().toString() == "ClosedToGround") inPosition = Double.parseDouble(climb.getRotorPosition().toString().substring(0, 10));
		++timer;
		if (Driver_Controller.buttonExtendClimb() && extendLastPressed == false){
			timer = 0;
		}
		Double power = 0.0;
		if(Driver_Controller.buttonExtendClimb() && System.currentTimeMillis() > 1000){
			if(timer > 25 && inPosition-Double.parseDouble(climb.getRotorPosition().toString().substring(0, 10)) > -100.0){
				power = 0.5;
			}
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
		if(Driver_Controller.buttonExtendClimb() || Driver_Controller.buttonRetractClimb()) {
			servoPosition = closedPosition;
		}
		else {
			servoPosition = openPosition;
		}
		servo.setAngle(servoPosition);
		climb.set(power);
		extendLastPressed = Driver_Controller.buttonExtendClimb();
	}
}