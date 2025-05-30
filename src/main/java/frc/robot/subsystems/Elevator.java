package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import frc.robot.subsystems.Driver_Controller;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.Constants;
import frc.robot.drivetrain;

public class Elevator{
  public static TalonFX elevatorMotor = new TalonFX(Constants.elevatorMotorID);
  public static SparkMax armMotor = new SparkMax(Constants.elevatorArmMotorID, MotorType.kBrushless);
  public static Double currentPosition,
                armTimer = 0.0;
  public static Boolean bottomSwitchPressed;
  public static int currentLevel = 0;
  public static Double[] removeAlgaeH = {20.0, 30.0}, levelPosition = {0.0, 0.0, 77.0, 123.0, 188.25}; // change l4
  
  public static void elevatorPeriodic(){
    bottomSwitchPressed = elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround";
    currentPosition = Double.parseDouble(elevatorMotor.getRotorPosition().toString().substring(0, 10));
    if (bottomSwitchPressed){elevatorMotor.setPosition(0.0);}
    if (currentLevel == 0){
      if (currentPosition > 3 && Driver_Controller.buttonResetElevator()) elevatorTo(-99999.0);
      else if (!bottomSwitchPressed) elevatorMotor.set(-0.3);
      else elevatorMotor.set(0.0);
    }else if (currentLevel > 1)elevatorTo(levelPosition[currentLevel]);
    else elevatorMotor.set(0.0);
  }

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
    Double error = destination - currentPosition;
    Double minPower = -0.9, maxPower = 0.9, power = 0.0;
		Integer maxError = 5;
			
		// set motor power based on error or set it to keep position
    power = error/30;
    if (Math.abs(error) < maxError){
	    elevatorMotor.set(0.0);
	    return true;
    }
    // Clamp power and use limit switches
    if (bottomSwitchPressed) minPower = 0.0;
    power = clamp(minPower, maxPower, power);
    elevatorMotor.set(power);
    return false;
  }

  // extend or retract the small arm on the elevator
  /*public static Boolean moveElevatorArm(String extendOrRetract){
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
    armMotor.set(power);
    lastExtendOrRetract = extendOrRetract;
    return done;
    
    Boolean done = false;
    Double power = 0.3;
    power *= (extendOrRetract == "extend"?1:-1);
    if ((extendOrRetract == "extend" && outtakePosition > 20.0) || (extendOrRetract == "retract" && outtakePosition < 1.0)){
      power = 0.0;
      done = true;
      extendedOrRetracted = (extendOrRetract == "extend"?"extended":"retracted");
    }else extendedOrRetracted = "moving";
    armMotor.set(power);
    return done;
  }*/

  // function to be constantly called and remove algae
  /* public void removeAlgae(){
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