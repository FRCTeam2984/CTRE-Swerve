package frc.robot.subsystems;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.subsystems.Driver_Controller;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix6.hardware.TalonFX;

public class Climb {/*
	int openPosition = 0, closedPosition = 90, servoPosition = closedPosition;
	static Servo servo = new Servo(0);
	static TalonFX climb = new TalonFX(1);

	//public static void Climb(TalonFX(device ID), DigitalInput(number), Encoder(number))		// not right
	//DigitalInput limitUnwindedForward = new DigitalInput(number);
	//DigitalInput limitUnwindedBackward = new DigitalInput(number);
	// extendButton creation
	// retractButton creation

	public void letsClimb(){// add something
		Double power = 0.0;
		if(Driver_Controller.buttonExtendClimb() && System.currentTimeMillis() > 1000){ // right way to calc time?
			//Thread.sleep(500);  // change with testing
			if(Double.parseDouble(climb.getRotorPosition().toString().substring(0, 10)) < 0)
				power = 0.01; // climb motor unwind, idk what direction is wind and unwind (invert?)
			if(climb.getForwardLimit().getValue().toString() == "ClosedToGround") // limit switches, use kyle’s limits
				power = 0.0; // not sure what will happen, put in as a guess
			// find out led code -> LED light flashes or solid a specific color
		}
		if(Driver_Controller.buttonExtendClimb()){
			power = -0.01; // climb motor wind to climb, idk what direction is wind and unwind (invert?)
			if(climb.getReverseLimit().getValue().toString() == "ClosedToGround") // limit switches, use kyle’s limits
				power = 0.0;//climbMotor.set(ControlMode.PercentOutput, 0);  // not sure what will happen, put in as a guess
		}
		if(Driver_Controller.buttonExtendClimb()) {
			servoPosition = openPosition;
		}
		else {
			servoPosition = closedPosition;
		}
		servo.setAngle(servoPosition);
		climb.set(power);
		
	} */
}