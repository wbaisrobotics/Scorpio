package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorDrive extends Drive{
	
	public static enum EncoderConstants {
		A_BOT (0.0785, 20, (1/26.04), 0, 0, 0), B_BOT (0.0785, 20, (1/7.5), 0.5, 0, 0);
		
		private double wheelRadius; // meters
		private int encoderPPR; // for encoder revolution
		private double gearRatio; // encoder : wheel ratio
		
		private double kP;
		private double kI;
		private double kD;
		private EncoderConstants(double wheelRadius, int encoderPPR, double gearRatio, double kP, double kI, double kD) {
			this.wheelRadius = wheelRadius;
			this.encoderPPR = encoderPPR;
			this.gearRatio = gearRatio;
			this.kP = kP;
			this.kI = kI;
			this.kD = kD;
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
		A_BOT (0.03, 0.015, 0, 0), B_BOT (0.03, 0.011, 0.00001, 0.0136);
		
		private double straightKp;
		private double turnKp;
		private double turnKi;
		private double turnKd;
		private GyroConstants(double straightKp, double turnKp, double turnKi, double turnKd) {
			this.straightKp = straightKp;
			this.turnKp = turnKp;
			this.turnKi = turnKi;
			this.turnKd = turnKd;
		}
	}
	
	private GyroBase gyro;
	private PIDController gyroTurnPID;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private PIDController encoderDrivePID;

	public SensorDrive(WPI_TalonSRX leftFirstMotor, WPI_TalonSRX leftSecondMotor, WPI_TalonSRX rightFirstMotor, WPI_TalonSRX rightSecondMotor
			, int pistonAPort, int pistonBPort, Encoder leftEncoder, Encoder rightEncoder) {
		
		super (leftFirstMotor, leftSecondMotor, rightFirstMotor, rightSecondMotor, pistonAPort, pistonBPort);
		
		this.leftEncoder = leftEncoder;
		this.leftEncoder.setDistancePerPulse(EncoderConstants.B_BOT.distancePerPulse());
		this.rightEncoder = rightEncoder;
		this.rightEncoder.setDistancePerPulse(EncoderConstants.B_BOT.distancePerPulse());

		gyro = new ADXRS450_Gyro();
		
		gyroTurnPID = new PIDController (GyroConstants.B_BOT.turnKp, GyroConstants.B_BOT.turnKi, GyroConstants.B_BOT.turnKd, gyro, new PIDOutput() {
			public void pidWrite(double output) {}
		});
		gyroTurnPID.setOutputRange(-0.7, 0.7);
		
		SmartDashboard.putData("Gyro Turn PID Controller" , gyroTurnPID);
		
		encoderDrivePID = new PIDController (EncoderConstants.B_BOT.kP, EncoderConstants.B_BOT.kI, EncoderConstants.B_BOT.kD, leftEncoder, new PIDOutput() {
			public void pidWrite(double output) {}
		});
		//encoderDrivePID.setOutputRange(-0.7,  0.7);
		
		SmartDashboard.putData("Encoder Straight PID Controller", encoderDrivePID);
		
	}
	
	public void gyroStraight (double xSpeed) {
		arcadeDrive (xSpeed, -getGyroAngle()*GyroConstants.B_BOT.straightKp, false);
	}
	
	// Gyro PID Turning
	public void initGyroTurn (double desiredAngle) {
		resetGyro();
		gyroTurnPID.reset();
		gyroTurnPID.setSetpoint(desiredAngle);
		gyroTurnPID.enable();
	}
	
	public void executeGyroTurn () {
		executeGyroTurn (0);
	}
	
	public void executeGyroTurn (double magnitude) {
		
		double turn = gyroTurnPID.get();
		
		if (turn < 0 && turn > -0.3) {
			turn = -0.3;
		}
		else if (turn > 0 && turn < 0.3) {
			turn = 0.3;
		}
		arcadeDrive (magnitude, turn, false);
	}
	
	public double getGyroTurnError () {
		return gyroTurnPID.getError();
	}
	
	
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public void resetGyro() {
		gyro.reset();
	}
	
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
	// Encoder (+ Gyro) PID straight distance
	public void initEncoderStraight (double desiredDistance) {
		resetGyro();
		resetEncoders();
		encoderDrivePID.reset();
		encoderDrivePID.setSetpoint(desiredDistance);
		encoderDrivePID.enable();
	}
	
	public void executeEncoderStraight () {
		
		double magnitude = encoderDrivePID.get();
		
		if (magnitude < 0 && magnitude > -0.4) {
			magnitude = -0.4;
		}
		else if (magnitude > 0 && magnitude < 0.4) {
			magnitude = 0.4;
		}
		
		gyroStraight (magnitude);
		
	}
	
	public double getEncoderStraightError () {
		return encoderDrivePID.getError();
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
