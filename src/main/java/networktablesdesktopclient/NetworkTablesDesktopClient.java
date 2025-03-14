package networktablesdesktopclient;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import networktablesdesktopclient.Vector2;



public class NetworkTablesDesktopClient {
    double OdoValues[][] = {  // coordinates for circle D
     // close 1
    };

    /* public static void main(String args) {
      new NetworkTablesDesktopClient().getRobotPosition(); 
    } */
  
    /* public void run() {
      NetworkTableInstance inst = NetworkTableInstance.getDefault();
      NetworkTable table = inst.getTable("datatable");
      NetworkTableEntry xEntry = table.getEntry("x");
      NetworkTableEntry yEntry = table.getEntry("y");
  
      // inst.startClientTeam(inst.startClient("hostname"));  // where TEAM=190, 294, etc, or use inst.startClient("hostname") or similar
      // inst.startDSClient();  // recommended if running on DS computer; this gets the robot IP from the DS
  
      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ex) {
          System.out.println("interrupted");
          return;
        }
  
         double x = xEntry.getDouble(0.0);
         double y = yEntry.getDouble(0.0);
        System.out.println("X: " + x + " Y: " + y);
      }
    } */

    public static Vector2 getRobotPosition(){
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
      NetworkTable table = inst.getTable("datatable");
      NetworkTableEntry xEntry = table.getEntry("x");
      NetworkTableEntry yEntry = table.getEntry("y");
  
      // inst.startClientTeam(inst.startClient("hostname"));  // where TEAM=190, 294, etc, or use inst.startClient("hostname") or similar
      // inst.startDSClient();  // recommended if running on DS computer; this gets the robot IP from the DS
  
      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ex) {
          System.out.println("interrupted");
          // return ;
        }
  
         /* double x = xEntry.getDouble(0.0);
         double y = yEntry.getDouble(0.0);
        System.out.println("X: " + x + " Y: " + y); */

        Vector2 robotPos = new Vector2();
        robotPos.x = xEntry.getDouble(0.0);
        robotPos.y=yEntry.getDouble(0.0);

        System.out.println(robotPos);
        return robotPos;
      }

    }

}