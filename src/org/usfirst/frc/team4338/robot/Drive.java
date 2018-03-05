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
	
	public static final double WHEEL_RADIUS = 0.0785; // in meters
	public static final double WHEEL_CIRCUMFERENCE = Math.PI * 2 * WHEEL_RADIUS;
	public static final double WHEEL_ROTATIONS_IN_METER = 1/WHEEL_CIRCUMFERENCE;
	public static final double ENCODER_PULSES_PER_REVOLUTION = 20; // ( 20 pulses per CIMCoder revolution )
	//public static final double GEAR_RATIO = (1/26.04);
	public static final double GEAR_RATIO = (1/2.5);
	public static final double PULSES_IN_METER = ENCODER_PULSES_PER_REVOLUTION * WHEEL_ROTATIONS_IN_METER / GEAR_RATIO;
	public static final double DISTANCE_PER_PULSE = WHEEL_CIRCUMFERENCE * GEAR_RATIO / ENCODER_PULSES_PER_REVOLUTION;

	private DoubleSolenoid pistons;
	private GyroBase gyro;
	public static final double GYRO_STRAIGHT_KP = 0.03;
	
	private PIDController leftController;
	private PIDController rightController;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;

	private boolean inverted = false;

	public Drive(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, int pistonAPort, int pistonBPort, Encoder leftEncoder, Encoder rightEncoder) {

		super(leftMotor, rightMotor);

		// Disable brake mode
		leftMotor.setNeutralMode(NeutralMode.Coast);
		rightMotor.setNeutralMode(NeutralMode.Coast);
		
		this.leftEncoder = leftEncoder;
		this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		this.rightEncoder = rightEncoder;
		this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		
		this.leftController = new PIDController (0, 0, 0, leftEncoder, leftMotor);
		SmartDashboard.putData(leftController);
		this.rightController = new PIDController (0, 0, 0, rightEncoder, rightMotor);
		SmartDashboard.putData(rightController);

		// Initialize the piston
		this.pistons = new DoubleSolenoid (pistonAPort, pistonBPort);

		gyro = new ADXRS450_Gyro();

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
		arcadeDrive (xSpeed, -getGyroAngle()*GYRO_STRAIGHT_KP, false);
	}
	
	/**
	 * Returns true if finished turning
	 * @param degrees
	 * @return
	 */
	public boolean turn (double degrees) {
		if (getGyroAngle() < degrees) {
			arcadeDrive (0, (Math.signum(degrees)*0.3), false);
			return false;
		}
		else {
			arcadeDrive (0,0,false);
			return true;
		}
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
