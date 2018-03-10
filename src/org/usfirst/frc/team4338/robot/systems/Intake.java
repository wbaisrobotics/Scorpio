package org.usfirst.frc.team4338.robot.systems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Represents an intake mechanism which can grab cubes (using two motors: one for left, one for right)
 * and has a piston(s) which can open and close the arms with the wheels.
 * @author orianleitersdorf
 *
 */
public class Intake extends Subsystem{

	/** Represents the left motor of the intake **/
	private SpeedController leftMotor;
	/** Represents the right motor of the intale **/
	private SpeedController rightMotor;
	/** Represents the piston in control of opening and closing the arms **/
	private DoubleSolenoid piston;
	
	/**
	 * Initializes the intake mechanism with a motor for left, a motor for right, and a piston
	 * for the arms (respectively)
	 * @param leftMotor
	 * @param rightMotor
	 * @param piston
	 */
	public Intake (SpeedController leftMotor, SpeedController rightMotor, DoubleSolenoid piston) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.piston = piston;
	}

	/**
	 * Closes the arms using the piston
	 */
	public void closeArms() {
		piston.set(Value.kForward);
	}
	
	/**
	 * Opens the arms using the piston
	 */
	public void openArms() {
		piston.set(Value.kReverse);
	}
	
	/**
	 * Toggles the arm's position (if closed becomes open, if open becomes closed)
	 */
	public void toggleArms() {
		piston.set((piston.get()==Value.kForward)?Value.kReverse:Value.kForward);
	}
	
	/**
	 * Rotates the motors to intake a cube
	 */
	public void cubeIn() {
		leftMotor.set(-0.8);
		rightMotor.set(0.8);
	}
	
	/**
	 * Rotates the motors to kick a cube out
	 */
	public void cubeOut() {
		leftMotor.set(0.8);
		rightMotor.set(-0.8);
	}
	
	/**
	 * Rotates the motors to kick a cube out in full power
	 */
	public void cubeOutFullPower () {
		leftMotor.set(1.0);
		rightMotor.set(-1.0);
	}
	
	/**
	 * Stops the intaking wheels
	 */
	public void stop() {
		leftMotor.set(0.0);
		rightMotor.set(0.0);
	}
	
	/**
	 * Returns wether or not the intake wheels are runnign
	 * @return
	 */
	public boolean wheelsRunning() {
		return (leftMotor.get()!=0 && rightMotor.get()!=0);
	}

	@Override
	protected void initDefaultCommand() {}

}
