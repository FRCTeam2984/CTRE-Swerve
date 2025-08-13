
// Based on the 2023 implimentation of rotary encoder using the wood controller
// Can be found in python at: https://github.com/FRCTeam2984/ChargedUp2023/blob/main/src/subsystems/rotary_controller.py

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;



public class Rotary_Controller {
    static double angle_offset = 0.0;
        // private static double generate_angle_midmax(double mid, double _min, double _max) {
        //     double midmax = ((mid - _min) / (_max - _min));
        //     return midmax;
        // }
        public void reset_angle(double angle, Joystick xboxController){
            angle_offset = 0.0;
            angle_offset = angle - RotaryJoystick(xboxController);
            System.out.println("Angle reset called");
        }
        public static double RotaryJoystick(Joystick xboxController){
            int fineAxis = (int)((1-xboxController.getRawAxis(5))*100), coarseAxis = 2400+(int)((1-xboxController.getRawAxis(2))*1200);
            int coarseInterval = coarseAxis/200, closest = 10000;
            for (int i = -1; i <= 1; ++i){
                int newAngle = ((coarseInterval+i)*200+fineAxis);
                if (Math.abs(newAngle-coarseAxis) < Math.abs(closest-coarseAxis))
                    closest = newAngle;
            }
            double angle = (((closest%2400)*3.0/20.0)-180);
            return angle;
            /*
            double rawAxis1 = xboxController.getRawAxis(2), rawAxis2 = xboxController.getRawAxis(3); // this is the wrong axis
            double coarseResult = (3-rawAxis1)*180;
            double fineAngle = rawAxis2*15+180.0; // might need to be inverted
            double closest = 0.0;
            for (closest = 0.0; fineAngle < 900.0; fineAngle += 30.0){
                closest = Math.min(closest, Math.abs(coarseResult - fineAngle));
            }
            double angle = coarseResult;
            if (Math.abs(closest-angle) < 5.0){
                angle = closest;
            }
            return ((angle%360)-180);*/
    }

}