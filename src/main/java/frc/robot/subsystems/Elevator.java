package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.subsystems.Driver_Controller;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.Constants;
import frc.robot.drivetrain;

public class Elevator{
  public static TalonFX elevatorMotor = new TalonFX(Constants.elevatorMotorID);
  public static TalonSRX armMotor = new TalonSRX(Constants.elevatorArmMotorID);
  public static Double bottomPosition = Double.parseDouble(elevatorMotor.getRotorPosition().toString().substring(0, 10)), armTimer = 0.0, l2Position = 70.5, l3Position = 117.8, l4Position = 188.25;
  public static Boolean removeButtonLastPressed = false;
  public static String state = "idle", extendedOrRetracted = "retracted", lastExtendOrRetract = "";
  public static int recentLevel = 2;
  public static Double[] removeAlgaeH = {20.0, 30.0};
  
  // function for keeping a variable between a lower and upper limit
  public static Double clamp(Double minimum, Double maximum, Double input){
    if (input < minimum)
      return minimum;
    if (input > maximum)
      return maximum;
    return input;
  }
  
  // function for moving elevator (you can change function name, i don’t care)
  // change the math for the units of distance, power at different positions, gravity compensation
  public static Boolean elevatorTo(Double destination){
    String rawInput = elevatorMotor.getRotorPosition().toString();
    Double position = Double.parseDouble(rawInput.substring(0, 10)) - bottomPosition;
     
		// convert destination from input units to encoder rotations
    Double minPower = -0.6, maxPower = 0.6, error = destination - position, power = 0.0;
		Integer maxError = 5; // change gravityComp
		Boolean closeEnough = false;
			
		// set motor power based on error or set it to keep position
    //if (error > 0) power = 1.0;
    //if (error < 0) power = -1.0;
    power = error/30;
    if (Math.abs(error) < maxError){
	    power = 0.0;
	    closeEnough = true;
    }

    // Clamp power and use limit switches
    if (elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround")
      power = Elevator.clamp(0.0, maxPower, power);
    /*else if (elevatorMotor.getForwardLimit().getValue().toString() == "ClosedToGround")
      power = Elevator.clamp(minPower, 0.0, power);*/
    else
      power = Elevator.clamp(minPower, maxPower, power);
    // set motor power and return whether it is close enough
    elevatorMotor.set(power);
    return closeEnough;
  }

  // extend or retract the small arm on the elevator
  public static Boolean moveElevatorArm(String extendOrRetract){
    lastExtendOrRetract = extendOrRetract;
    Boolean done = false;
    Double power;
    if (extendOrRetract == "extend") power = 0.3; else power = -0.3;
    armTimer += 0.02; // adding 20 millisecons per call
    if (armTimer >= 0.25/Math.abs(power) || extendOrRetract.substring(0, 3) == extendedOrRetracted.substring(0, 3)){
      if (armTimer >= 0.15/Math.abs(power) || extendedOrRetracted == "moving") power /= 3;
      done = true;
      power = 0.0;
      armTimer = 0.0;
      if (extendOrRetract == "extend") extendedOrRetracted = "extended"; else extendedOrRetracted = "retracted";
    }else extendedOrRetracted = "moving";
    armMotor.set(ControlMode.PercentOutput, power);
    return done;
  }

  // function to be constantly called and remove algae
  /*public void removeAlgae(){
    if (Driver_Controller.buttonL2()) recentLevel = 2;
    if (Driver_Controller.buttonL3()) recentLevel = 3;
    if (Driver_Controller.buttonRemoveAlgae()){
      switch(state){
        case "idle":
          if (removeButtonLastPressed == false){
            state = "preparing";
          }
          break;
        case "preparing":
          if (elevatorTo(15.0) && moveElevatorArm("extend")){ // making the elevator prepared to move up
            state = "positioning";
          }
          break;
        case "positioning":
          if (AutoDriveTest.autoDrive()){ // automatically driving to the correct position
            state = "removing";
          }
          break;
        case "removing":
          if (elevatorTo(removeAlgaeH[recentLevel-2])){ // raising elevator to knock off algae
            state = "backing up";
          }
          break;
        case "backing up":
          drivetrain.back up; // back up idk how  SIENA FIND OUT
          if (finished){
            state = "finished";
          }
          break;
        case "finished":
          if (elevatorTo(bottomPosition) && moveElevatorArm("retract")){
            state = "idle";
          }
          break;

      }
    }else state = "idle";
    removeButtonLastPressed = Driver_Controller.buttonRemoveAlgae();
  }*/ 
}