package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends DifferentialDrive {
	
	private DoubleSolenoid pistons;
	private GyroBase gyro;
	
	private boolean inverted = false;

	public Drive(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, int pistonAPort, int pistonBPort) {
		
		super(leftMotor, rightMotor);
		
		// Disable brake mode
		leftMotor.setNeutralMode(NeutralMode.Coast);
		rightMotor.setNeutralMode(NeutralMode.Coast);
		
		// Initialize the piston
		this.pistons = new DoubleSolenoid (pistonAPort, pistonBPort);
		
		gyro = new ADXRS450_Gyro();
		
	}
	
	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		super.arcadeDrive(isInverted()?-xSpeed:xSpeed, -zRotation, squaredInputs);
	}
	
	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
		super.curvatureDrive(isInverted()?-xSpeed:xSpeed, -zRotation, isQuickTurn);
	}
	
	public void shiftLowGear () {
		pistons.set(Value.kReverse);
	}
	
	public void shiftHighGear () {
		pistons.set(Value.kForward);
	}
	
	public void toggleGearSpeed () {
		pistons.set((pistons.get()==Value.kForward)?Value.kReverse:Value.kForward);
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

}
