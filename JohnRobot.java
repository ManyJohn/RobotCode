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
	int roundCounter=1 ;
	double turnGunAmt = 60;
	boolean foundTarget = false;
	

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	


	/**
	 * run: JohnRobot's default behavior
	 */
	
	public void run() {
		setAdjustGunForRobotTurn(true);

			moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
			peek = false;
			turnLeft(getHeading() % 90);
			ahead(moveAmount);
			// Turn the gun to turn right 90 degrees.
			peek = true;
			turnGunRight(90);
			turnRight(90);

		while (true) {
						roundCounter += 1;
			turnGunRight(turnGunAmt);	
			if  (roundCounter >=6 ) {
				turnGunAmt *= -1;
				roundCounter= 0;
				foundTarget = false; 
			}
			
			roundCounter += 1;
			turnGunRight(turnGunAmt);	
			if  (roundCounter >=6 ) {
				turnGunAmt *= -1;
				roundCounter= 0;
				foundTarget = false; 
			}
			
			movingForward = true;
			if ( foundTarget && roundCounter ==0  )	{
				// I found a Target just now
				// do not move
			}else{
				moveRandomStep(movingForward);
			}
			
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
		
		roundCounter = 0;		
		foundTarget = true;

		getTarget(e.getBearing());	
		
		
		fire(2.6);
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
		reverseDirection();
		turnGunRight(turnGunAmt);
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
		rightTrunRandomAngle();
		reverseDirection();
	}
	
	public void onBulletMissed(BulletMissedEvent event) {
		foundTarget = false;
	}
}