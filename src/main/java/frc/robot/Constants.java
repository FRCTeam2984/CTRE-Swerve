package frc.robot;

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

    public static final int intakeBeltID = 1; // incorrect
    public static final int intakeTopMotorID = 2; // incorrect
    public static final int intakeBottomMotorID = 3; // incorrect
    public static final int intakePivotMotorID = 4; // incorrect
    public static final int intakeTransportPivotID = 5; // incorrect
    public static final int elevatorMotorID = 6; // incorrect
    public static final int elevatorArmMotorID = 7; // incorrect
    static int ID_ROTARY_CONTROLLER = 1;

    static int ID_OPERATOR_CONTROLLER = 0;
}
