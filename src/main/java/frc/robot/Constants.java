package frc.robot;

import com.pathplanner.lib.config.RobotConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

public class Constants {
    public static class OperatorConstants {
        public static final int kDriverControllerPort1 = 0;
        public static final int kDriverControllerPort2 = 1;
        public static final int kDriverControllerPort3 = 2;
        public static final int kDriverControllerPort4 = 3;
    }

    public static final int climbID = 17;
    public static final int intakeBeltID = 16;
    public static final int elevatorLaserSensorID = 22;
    public static final int intakeBottomMotorID = 21;
    public static final int intakePivotMotorID = 14; 
    public static final int intakeTransportPivotID = 15; 
    public static final int elevatorMotorID = 18; 
    public static final int elevatorArmMotorID = 19;    
    static int ID_ROTARY_CONTROLLER = 1;

    /* From PathPlanner */
    static int ID_OPERATOR_CONTROLLER = 0;

    // auto's
    public static final String kPassTheLine = "Pass the Line";
    public static final String kTestingPathAuto = "Testing Path";
    public static final String kV1Auto = "V1";
    public static final String kV2Auto = "V2";
    public static final String kV3Auto = "V3";
    public static final String kSoftAndSlowAuto = "SoftAndSlow";
}