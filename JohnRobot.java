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
	int turnDirection = 1; // Clockwise or counterclockwise
	int counter;
	int roundCounter=1 ;
	double turnGunAmt = 60;
	boolean foundTarget = false;
	

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	


	/**
	 * run: JohnRobot's default behavior
	 */
	
	public void run() {
		//setAdjustGunForRobotTurn(true);

			moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
			peek = false;
			turnLeft(getHeading() % 90);
			ahead(moveAmount);
			// Turn the gun to turn right 90 degrees.
			peek = true;
			turnRight(90);

		while (true) {
			turnRight(360);
		}
	}
	

	public void moveRandomStep ( boolean isForward){
		Random rand = new Random();
		double distance = rand.nextDouble() *5*140 ; 
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
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}

		turnRight(e.getBearing());
		ahead(e.getDistance() + 20);
		scan(); // Might want to move ahead again!
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		
		turnRight(e.getBearing());
		//rightTrunRandomAngle();
		reverseDirection();
	}
	
	public void onHitRobot(HitRobotEvent e){
				if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

		// Determine a shot that won't kill the robot...
		// We want to ram him instead for bonus points
		fire(1.5);
		ahead(40); // Ram him again!
	}
	

	public void ramingFire (double enemyEnergy){
		if (enemyEnergy > 16) {
			fire(3);
		} else if (enemyEnergy > 10) {
			fire(2);
		} else if (enemyEnergy > 4) {
			fire(1);
		} else if (enemyEnergy > 2) {
			fire(.5);
		} else if (enemyEnergy > .4) {
			fire(.1);
		}
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
		
	}
	
	public void onBulletMissed(BulletMissedEvent event) {
		foundTarget = false;
	}
}