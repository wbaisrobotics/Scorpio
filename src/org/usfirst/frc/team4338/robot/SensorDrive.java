package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;

public class SensorDrive extends Drive{
	
	public static enum EncoderConstants {
		A_BOT (0.0785, 20, (1/26.04)), B_BOT (0.0785, 20, (1/7.5));
		
		private double wheelRadius; // meters
		private int encoderPPR; // for encoder revolution
		private double gearRatio; // encoder : wheel ratio
		private EncoderConstants(double wheelRadius, int encoderPPR, double gearRatio) {
			this.wheelRadius = wheelRadius;
			this.encoderPPR = encoderPPR;
			this.gearRatio = gearRatio;
		}
		
		public double wheelRadius () {
			return this.wheelRadius;
		}
		
		public double wheelCircumference () {
			return Math.PI * 2 * wheelRadius();
		}
		
		public double wheelRotationsForMeterDisplacement () {
			return 1 / wheelCircumference();
		}
		
		public int encoderPPR () {
			return this.encoderPPR;
		}
		
		public double gearRatio () {
			return this.gearRatio;
		}
		
		public double pulsesInMeter () {
			return encoderPPR() * wheelRotationsForMeterDisplacement() / gearRatio();
		}
		
		public double distancePerPulse () {
			return  wheelCircumference() * gearRatio() / encoderPPR();
		}
	}
	
	public static enum GyroConstants {
		A_BOT (0.03, 0.015), B_BOT (0.03, 0.015);
		
		private double straightKp;
		private double turnKp;
		private GyroConstants(double straightKp, double turnKp) {
			this.straightKp = straightKp;
			this.turnKp = turnKp;
		}
	}
	
	private GyroBase gyro;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;

	public SensorDrive(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, int pistonAPort, int pistonBPort, Encoder leftEncoder, Encoder rightEncoder) {
		
		super (leftMotor, rightMotor, pistonAPort, pistonBPort);
		
		this.leftEncoder = leftEncoder;
		this.leftEncoder.setDistancePerPulse(EncoderConstants.B_BOT.distancePerPulse());
		this.rightEncoder = rightEncoder;
		this.rightEncoder.setDistancePerPulse(EncoderConstants.B_BOT.distancePerPulse());

		gyro = new ADXRS450_Gyro();
		
	}
	
	public void gyroStraight (double xSpeed) {
		arcadeDrive (xSpeed, -getGyroAngle()*GyroConstants.B_BOT.straightKp, false);
	}
	
	public void gyroTurn (double desiredAngle) {
		gyroTurn (0, desiredAngle);
	}
	
	public void gyroTurn (double magnitude, double desiredAngle) {
		arcadeDrive (magnitude, -(getGyroAngle() - desiredAngle)*GyroConstants.B_BOT.turnKp, false);
	}
	
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void resetGyro() {
		gyro.reset();
	}
	
	public void resetEncoders () {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	public double getLeftDistance () {
		return leftEncoder.getDistance();
	}
	
	public double getRightDistance () {
		return rightEncoder.getDistance();
	}
	
	public double getAvgDistance () {
		return (getLeftDistance() + getRightDistance())/2;
	}

}
