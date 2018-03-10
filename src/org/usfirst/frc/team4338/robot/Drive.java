package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {
	
	private DifferentialDrive drive;

	private DoubleSolenoid pistons;

	private boolean inverted = false;

	public Drive(WPI_TalonSRX leftFirstMotor, WPI_TalonSRX leftSecondMotor, WPI_TalonSRX rightFirstMotor, WPI_TalonSRX rightSecondMotor,
			int pistonAPort, int pistonBPort) {

		drive = new DifferentialDrive (new SpeedControllerGroup (leftFirstMotor, leftSecondMotor), new SpeedControllerGroup (rightFirstMotor, rightSecondMotor));

		// Disable brake mode
		leftFirstMotor.setNeutralMode(NeutralMode.Coast);
		leftSecondMotor.setNeutralMode(NeutralMode.Coast);
		rightFirstMotor.setNeutralMode(NeutralMode.Coast);
		rightSecondMotor.setNeutralMode(NeutralMode.Coast);

		// Initialize the piston
		this.pistons = new DoubleSolenoid (pistonAPort, pistonBPort);

	}
	
	public void stop() {
		tankDrive (0,0);
	}
	
	public void tankDrive (double left, double right) {
		drive.tankDrive(left, right);
	}

	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		drive.arcadeDrive(isInverted()?-xSpeed:xSpeed, -zRotation, squaredInputs);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
		drive.curvatureDrive(isInverted()?-xSpeed:xSpeed, zRotation, isQuickTurn);
	}

	public void shiftLowGear () {
		pistons.set(Value.kReverse);
	}

	public void shiftHighGear () {
		pistons.set(Value.kForward);
	}

	public boolean isHighGear () {
		return pistons.get() == Value.kForward;
	}

	public void toggleGearSpeed () {
		if(isHighGear()) {
			shiftLowGear();
		}
		else {
			shiftHighGear();
		}
	}
	
	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

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
