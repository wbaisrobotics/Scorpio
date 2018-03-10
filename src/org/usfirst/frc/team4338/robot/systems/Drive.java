package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Represents a {@link DifferentialDrive} system which can reverse it's front and also shift gears using a {@link DoubleSolenoid}
 * @author orianleitersdorf
 *
 */
public class Drive extends Subsystem {
	
	/** The raw drive system which outputs to the motor **/
	private DifferentialDrive drive;

	/** The gear shifter used to change between low and high gear **/
	private DoubleSolenoid gearShifter;

	/** Whether or not the face of the bot is inverted **/
	private boolean inverted = false;

	/**
	 * Initializes the Drive system with four motor drive, and a double solenoid for a gear shifter
	 * @param leftFirstMotor
	 * @param leftSecondMotor
	 * @param rightFirstMotor
	 * @param rightSecondMotor
	 * @param gearShifter
	 */
	public Drive(WPI_TalonSRX leftFirstMotor, WPI_TalonSRX leftSecondMotor, WPI_TalonSRX rightFirstMotor, WPI_TalonSRX rightSecondMotor,
			DoubleSolenoid gearShifter) {

		drive = new DifferentialDrive (new SpeedControllerGroup (leftFirstMotor, leftSecondMotor), new SpeedControllerGroup (rightFirstMotor, rightSecondMotor));

		// Disable brake mode
		leftFirstMotor.setNeutralMode(NeutralMode.Coast);
		leftSecondMotor.setNeutralMode(NeutralMode.Coast);
		rightFirstMotor.setNeutralMode(NeutralMode.Coast);
		rightSecondMotor.setNeutralMode(NeutralMode.Coast);

		// Initialize the piston
		this.gearShifter = gearShifter;

	}
	
	/**
	 * Stops driving
	 */
	public void stop() {
		tankDrive (0,0);
	}
	
	/**
	 * Raw input to motors for tankDrive
	 * @param left
	 * @param right
	 */
	public void tankDrive (double left, double right) {
		drive.tankDrive(left, right);
	}

	/** 
	 * Arcade drive (affected by the current face of the bot)
	 * @param xSpeed
	 * @param zRotation
	 * @param squaredInputs
	 */
	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		drive.arcadeDrive(isInverted()?-xSpeed:xSpeed, -zRotation, squaredInputs);
	}

	/**
	 * Curvature drive (affected by the current face of the bot)
	 * @param xSpeed
	 * @param zRotation
	 * @param isQuickTurn
	 */
	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
		drive.curvatureDrive(isInverted()?-xSpeed:xSpeed, zRotation, isQuickTurn);
	}

	/**
	 * Uses the piston to enter low gear (slower but more torque)
	 */
	public void shiftLowGear () {
		gearShifter.set(Value.kReverse);
	}

	/**
	 * Uses the piston to enter high gear (faster but less torque)
	 */
	public void shiftHighGear () {
		gearShifter.set(Value.kForward);
	}

	/**
	 * Returns true if currently in high gear
	 * @return
	 */
	public boolean isHighGear () {
		return gearShifter.get() == Value.kForward;
	}

	/**
	 * Toggles the current gear speed (if currently low becomes high, if currently high becomes low)
	 */
	public void toggleGearSpeed () {
		gearShifter.set((gearShifter.get()==Value.kForward)?Value.kReverse:Value.kForward);
	}
	
	/**
	 * Returns whether or not the face is currently inverted
	 * @return
	 */
	public boolean isInverted() {
		return inverted;
	}

	/**
	 * Sets whether or not the face of the bot is inverted (xSpeed negates if inverted is true)
	 * @param inverted
	 */
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	/**
	 * Toggles whether or not the face of the bot is inverted (xSpeed negates if inverted is true)
	 */
	public void toggleInverted() {
		setInverted (!isInverted());
	}

	// -------------- Aaron's Driving Code --------------

	/**
	 * Gets an x value from a given y value on a logistic function.
	 * Limits turning when moving forward faster.
	 *
	 * @param y	the y value of a controller joystick
	 */
	private double getXScale(double y){
		//Logistic function to limit turning speed based on forward speed
		return 1 - 0.5f / (1 + Math.pow(Math.E, -10 * (Math.abs(y) - 0.5f)));
	}

	/**
	 * Arcade Drive + Logistic Function in high gear
	 * @param x
	 * @param y
	 */
	public void aaronDrive (double x, double y) {

		y *= isInverted() ? 1 : -1;

		if(isHighGear()){ //High gear driving
			x *= getXScale(y);
		} else{ //Low gear driving
			x *= 0.7;
			y *= 0.8;
		}
		tankDrive(y - x, y + x);
	}

	@Override
	protected void initDefaultCommand() {}

}
