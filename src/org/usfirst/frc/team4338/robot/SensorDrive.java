package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;

public class SensorDrive {
	
	public static enum EncoderConstants {
		A_BOT (0.0785, 20, (1/26.04)), B_BOT (0.0785, 20, (1/2.5));
		
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
		A_BOT (0.03, 0.01), B_BOT (0.03, 0.01);
		
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

	public SensorDrive(Encoder leftEncoder, Encoder rightEncoder) {
		
		this.leftEncoder = leftEncoder;
		this.leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		this.rightEncoder = rightEncoder;
		this.rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

		gyro = new ADXRS450_Gyro();
		
	}
	
	public void driveGyroStraight(double xSpeed) {
		arcadeDrive (xSpeed, -getGyroAngle()*GYRO_KP, false);
	}
	
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void resetGyro() {
		gyro.reset();
	}

}
