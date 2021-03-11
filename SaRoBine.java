package src;
import robocode.*;
import robocode.util.Utils;
import java.awt.*;

public class SaRoBine extends AdvancedRobot {
	int count = 0; 				// Keeps track of how long we've been searching for our target
    double gunTurnAmt = 10; 	// How much to turn our gun when searching
    String trackName = null; 	// Name of the robot we're currently tracking
    double e_heading;
    double e_bearing;
    double e_dist;
    double e_energy = 3.0;
    int move_dir = 1;
    int wall_margin = 80; // 60
    
    public void run() {
        setBodyColor(new Color(0, 0, 0));
        setGunColor(new Color(0, 0, 0));
        setRadarColor(new Color(0, 0, 0));
        this.addCustomEvent(new Condition("walls_trigger") {
            public boolean test() {
                return (getX() <= wall_margin || getX() >= getBattleFieldWidth() - wall_margin || getY() <= wall_margin
                        || getY() >= getBattleFieldHeight() - wall_margin);
            }
        });
        setAdjustGunForRobotTurn(true); // Gun movement and body movement apart
        if (trackName == null) { turnGunRight(360); }
        movement();
    }
    public void movement() {
        while (true) {
            // setTurnGunRight(gunTurnAmt);  OLD
            count++;
            if (count > 2) 					// 2 turns, look left
                gunTurnAmt = -10;
            if (count > 5) 					// 5 turns, look right
                gunTurnAmt = 10;
            if (count > 11) { 				// after 10 turns, turn 360
                trackName = null;
                turnGunRight(360);
            }
            
            setTurnGunRight(gunTurnAmt);
            if (getVelocity() == 0) {
                move_dir *= -1;
                ahead(250 * move_dir);
            }
        }
    }
    public void onCustomEvent(CustomEvent e) {
        if (e.getCondition().getName().equals("walls_trigger")) {
            // Calculate bearing --> Math.atan2 --> Represent the bearing in radians
            // (Direction)
            double middle_angle = Math.atan2(getBattleFieldWidth() / 2 - getX(), getBattleFieldHeight() / 2 - getY());
            // Move to direction (relative angle between the robot and the middle_angle)
            setTurnRightRadians(Utils.normalRelativeAngle(middle_angle - getHeadingRadians()));
            ahead(100);
        }
    }
    public void onScannedRobot(ScannedRobotEvent e) {
        reflex(e.getEnergy());      // Reflex
        e_dist = e.getDistance();
        e_bearing = e.getBearing();
        e_heading = e.getHeading();
        
        if (trackName == null) { trackName = e.getName(); }
        count = 0;
        
        setTurnRight(e_bearing + 90 - (10 * move_dir)); // Aim
        gunTurnAmt = normalRelativeAngle(e.getBearing() + (getHeading() - getRadarHeading()));  // Current angle the robot is facing --> (getHeading() - getRadarHeading())
        setTurnGunRight(gunTurnAmt);                                                            // Turn gun to enemy based on facing angle --> e.getBearing() + (getHeading() - getRadarHeading())
        
        var fire_power = Math.min((400 / e_dist), 3);   // Fire
        if (getGunHeat() == 0 && getEnergy() >= 0.75) {
            setFire(fire_power);
        }
        
        if (e.getDistance() < 100) {    // Our target is too close! Back up.
            if (e.getBearing() > -90 && e.getBearing() <= 90)
                setBack(40);
            else
                setAhead(40);
        }
        // else { relfex(e.getEnergy()); }
    }
    
    public void reflex(double energy) {
        if (energy < e_energy) {
            setAhead(60 * move_dir); // ahead(100 * move_dir);
        }
        e_energy = energy;
    }
    
    /**
     * normalRelativeAngle: returns angle such that -180<angle<=180
     */
    public double normalRelativeAngle(double angle) {
        if (angle > -180 && angle <= 180)
            return angle;
        double fixedAngle = angle;
        while (fixedAngle <= -180)
            fixedAngle += 360;
        while (fixedAngle > 180)
            fixedAngle -= 360;
        return fixedAngle;
    }
}
