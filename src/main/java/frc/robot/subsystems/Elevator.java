package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

public class Elevator{
  public static LaserCan laserSensor = new LaserCan(Constants.elevatorLaserSensorID);
  public static TalonFX elevatorMotor = new TalonFX(Constants.elevatorMotorID);
  public static SparkMax outtakeMotor = new SparkMax(Constants.elevatorOuttakeMotorID, MotorType.kBrushless);
  public static SparkLimitSwitch lowerSensor = outtakeMotor.getReverseLimitSwitch();
  public static SparkLimitSwitch upperSensor = outtakeMotor.getForwardLimitSwitch();
  public static RelativeEncoder outtakeEncoder = outtakeMotor.getEncoder();
  public static Double currentPosition,
                armTimer = 0.0,
                gravity = 0.03,
                prevPower = 0.0;
  public static Boolean bottomSwitchPressed,
                useLaserSensor = true,
                moveCoral = false,
                enableOuttakeSensors = false;
  public static int currentLevel = 0, counter = 0, maxError = 5;
  public static Double[] removeAlgaeH = {20.0, 30.0}, levelPosition = {0.0, 13.001538, 68.5, 116.0, 188.1538}; // change l4 // Modified on other branch{0.0, 15.0, 62.0, 116.0, 186.0};
  public static void sensorInit(){
    try {
      laserSensor.setRangingMode(LaserCan.RangingMode.LONG);
      laserSensor.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
      laserSensor.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_20MS);
    } catch (ConfigurationFailedException e) {}
  }

  public static void elevatorPeriodic(){
    bottomSwitchPressed = elevatorMotor.getReverseLimit().getValue().toString() == "ClosedToGround";
    currentPosition = elevatorMotor.getRotorPosition().getValueAsDouble();
    laserOffsetCalc();
    if (bottomSwitchPressed){elevatorMotor.setPosition(0.0);}

    //calculate power
    Double power = 0.0;
    if (currentLevel == 0){
      if (currentPosition > 15 && Driver_Controller.buttonResetElevator()) power = elevatorTo(-99999.0);
      else if (!bottomSwitchPressed) power = -0.2;
      else power = 0.0;
    }else if (currentLevel >= 1){
      power = elevatorTo(levelPosition[currentLevel]);
    }else if (currentLevel == -2){
      power = 0.03;
    }
    power = clamp(-prevPower-0.03, 1.0, power);
    prevPower = Math.abs(power);
    elevatorMotor.set(power);
  }

  // function for keeping a variable between a lower and upper limit
  public static Double clamp(Double minimum, Double maximum, Double input){
    if (input < minimum)
      return minimum;
    if (input > maximum)
      return maximum;
    return input;
  }
  
  // function that returns the amount of power that should be used to move the elevator
  public static Double elevatorTo(Double destination){
    Double error = destination - currentPosition;
    Double minPower = -0.7, maxPower = 0.9, power = 0.0;
    
    if (Math.abs(error) < maxError){
	    return gravity;
    }

    //set motor power based on error or set it to keep position
    power = error/30+gravity;

    // Clamp power and use limit switches
    if (bottomSwitchPressed) minPower = 0.0;
    power = clamp(minPower, maxPower, power);
    return power;
  }

  public static Integer historyLength = 12;
  public static Double[] offset = new Double[historyLength];
  public static void laserOffsetCalc(){
    useLaserSensor=Driver_Controller.switchLaserCan();
    LaserCan.Measurement laserDist = laserSensor.getMeasurement();
    Double laserCanOffset = 0.0;
    if (laserDist != null && laserDist.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && laserDist.distance_mm<500){
      laserCanOffset=laserDist.distance_mm*0.1017127325+0.2260062079-currentPosition;
      if (Math.abs(laserCanOffset) > 300*0.1017127325) return;
      Double total = 0.0;
      for (int i = 0; i < historyLength-1; ++i){
        offset[i] = offset[i+1];
        if (offset[i] != null)total += offset[i];
      }
      offset[historyLength-1] = laserCanOffset;
      laserCanOffset = (total+laserCanOffset) / historyLength;
    }
    if (useLaserSensor)currentPosition+=laserCanOffset;
  }
}