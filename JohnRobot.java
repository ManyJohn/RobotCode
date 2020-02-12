package johnrobot;

import robocode.*;
import java.util.Random; 
import static robocode.util.Utils.normalRelativeAngleDegrees;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * JohnRobot - a robot by (your name here)
 */
public class JohnRobot extends Robot {
	boolean movingForward;
	int counter;
	int roundCounter=0 ;
	double turnGunAmt = 20;

	/**
	 * run: JohnRobot's default behavior
	 */
	//setAdjustGunForRobotTurn(true);
	
	public void run() {
		setAdjustGunForRobotTurn(true);

				
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while (true) {
			roundCounter += 1;
			turnGunRight(turnGunAmt);	
			if  (roundCounter >=10 ) {
				turnGunAmt *= -1;
				roundCounter= 0;
			}
			
			movingForward = true;	
			moveRandomStep(movingForward);
			rightTrunRandomAngle();
			//turnGunRight(30);
			
		}
	}
	

	public void moveRandomStep ( boolean isForward){
		Random rand = new Random();
		double distance = rand.nextDouble() *5*100 ; 
		if (isForward){
			ahead(distance);
		}else{
			back(distance);
		}
	
		
	}
	
	public void rightTrunRandomAngle (){
		Random rand = new Random();
		double angleConstant = rand.nextDouble() *4 ; 
		double angle = rand.nextDouble() * 45 ; 
		turnRight(angle);
	}
	
	/**
	 * reverseDirection: Switch from ahead to back & vice versa
	 */
	public void reverseDirection() {
		if (movingForward) {
			movingForward = false;
			moveRandomStep(movingForward);
			
		} else {
			movingForward = true;
			moveRandomStep(movingForward);
		}
	}
	
	public void getTarget(double bearing){
		double absoluteBearing = getHeading() + bearing;
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		getTarget(e.getBearing());
		smartFire(e.getDistance());
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		getTarget(e.getBearing());
		//rightTrunRandomAngle();
		reverseDirection();
	}
	
	public void onHitRobot(HitRobotEvent e){
		getTarget(e.getBearing());
		smartFire(30);
		//rightTrunRandomAngle();
		reverseDirection();
	}
	

	public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			fire(1);
		} else if (robotDistance > 50) {
			fire(2);
		} else {
			fire(3);
		}
	}
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		rightTrunRandomAngle();
		reverseDirection();
	}
}
