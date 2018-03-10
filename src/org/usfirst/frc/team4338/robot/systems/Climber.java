package org.usfirst.frc.team4338.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Represents the Climbing mechanism on the robot. Lifts and lowers the robot using two motors.
 * @author orianleitersdorf
 *
 */
public class Climber extends Subsystem{
	
	/** The left motor of the climber **/
	private SpeedController left;
	/** The right motor of the climber **/
	private SpeedController right;
	
	/**
	 * Initializes the Climber system with two SpeedControllers: one for left, and one for right.
	 * @param left
	 * @param right
	 */
	public Climber (SpeedController left, SpeedController right) {
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Lifts the robot up.
	 */
	public void up() {
		this.left.set(1.0);
		this.right.set(-1.0);
	}
	
	/**
	 * Lowers the robot.
	 */
	public void down() {
		this.left.set(-1.0);
		this.right.set(1.0);
	}
	
	/**
	 * Stops climbing.
	 */
	public void stop() {
		this.left.set(0);
		this.right.set(0);
	}

	/**
	 * No default command
	 */
	protected void initDefaultCommand() {}

}
