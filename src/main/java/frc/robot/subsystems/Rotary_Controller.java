
// Based on the 2023 implimentation of rotary encoder using the wood controller
// Can be found in python at: https://github.com/FRCTeam2984/ChargedUp2023/blob/main/src/subsystems/rotary_controller.py

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;



public class Rotary_Controller {
    static double angle_offset = 0.0;
        private static double generate_angle_midmax(double mid, double _min, double _max) {
            double midmax = ((mid - _min) / (_max - _min));
            return midmax;
        }
        public void reset_angle(double angle, Joystick joystick){
            angle_offset = 0.0;
            angle_offset = angle - RotaryJoystick(joystick);
            System.out.println("Angle reset called");
        }
        public static double RotaryJoystick(Joystick joystick){
            
            double x = joystick.getRawAxis(0);
            double y = joystick.getRawAxis(1);
            double z = joystick.getRawAxis(2);
            //System.out.println(x);
            //System.out.println(y);
            //System.out.println(z);
            double _max = Math.max(x, Math.max(y, z));
            double _min = Math.min(x, Math.min(y, z));
    
            if ((_max - _min) == 0){
                System.out.println("Joystick is not connected or is misconfigured");
                return 0;
            }
            else{
                double mid = -1;
                double angle = -1;
                if ((x <= y) && (y <= z)){
                    mid = y;
                    angle = 60 - generate_angle_midmax(mid, _min, _max) * 60;
                }
                if ((y <= x) && (x <= z)){
                    mid = x;
                    angle = 60 + generate_angle_midmax(mid, _min, _max) * 60;
                }
                if ((y <= z) && (z <= x)){
                    mid = z;
                    angle = 180 - generate_angle_midmax(mid, _min, _max) * 60;
                }
                if ((z <= y) && (y <= x)){
                    mid = y;
                    angle = 180 + generate_angle_midmax(mid, _min, _max) * 60;
                }
                if ((z <= x) && (x <= y)){
                    mid = x;
                    angle = 300 - generate_angle_midmax(mid, _min, _max) * 60;
                }
                if ((x <= z) && (z <= y)){
                    mid = z;
                    angle = 300 + generate_angle_midmax(mid, _min, _max) * 60;
                }
                angle += angle_offset;
            return angle;
    }
}
}