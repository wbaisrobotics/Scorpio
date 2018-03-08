package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends DifferentialDrive {

	private DoubleSolenoid pistons;

	private boolean inverted = false;

	public Drive(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, int pistonAPort, int pistonBPort) {

		super(leftMotor, rightMotor);

		// Disable brake mode
		leftMotor.setNeutralMode(NeutralMode.Coast);
		rightMotor.setNeutralMode(NeutralMode.Coast);

		// Initialize the piston
		this.pistons = new DoubleSolenoid (pistonAPort, pistonBPort);

	}
	
	public void enablePID () {
		leftController.enable();
		rightController.enable();
	}
	
	public void disablePID() {
		leftController.disable();
		rightController.disable();
	}
	
	/**
	 * 
	 * @param distance (meters)
	 */
	public void driveEncoderStraight (double distance) {
		leftController.setSetpoint(leftEncoder.getDistance() + distance);
		rightController.setSetpoint(rightEncoder.getDistance() + distance);
		leftController.enable();
		rightController.enable();
	}

	public void driveGyroStraight(double xSpeed) {
		arcadeDrive (xSpeed, -getGyroAngle()*GYRO_KP, false);
	}

	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		super.arcadeDrive(isInverted()?-xSpeed:xSpeed, -zRotation, squaredInputs);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
		super.curvatureDrive(isInverted()?-xSpeed:xSpeed, zRotation, isQuickTurn);
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

	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void resetGyro() {
		gyro.reset();
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

	//	public void calibrateGyro() {
	//		gyro.calibrate();
	//	}

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

}
