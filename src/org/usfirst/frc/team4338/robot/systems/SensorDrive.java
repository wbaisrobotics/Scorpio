package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Adds sensor functionality to {@link Drive} by using encoders and a gyro. Uses PID to turn
 * a given angle with the gyro, and PID to drive a certain distance with the encoders.
 * @author orianleitersdorf
 *
 */
public class SensorDrive extends Drive{
	
	/**
	 * The Constants for the encoder (both gear ratio and PID constants
	 * @author orianleitersdorf
	 *
	 */
	public static enum EncoderConstants {
		A_BOT (0.0785, 20, (1/26.04), 0.65, 0, 0), B_BOT (0.0785, 20, (1/7.5), 0.65, 0, 0);
		
		/** The radius of the wheel (in meters) **/
		private double wheelRadius;
		/** The pulses per revolution of the encoder **/
		private int encoderPPR;
		/** The gear ratio between the encoder and the wheels **/
		private double gearRatio;
		
		/** The P term in the PID controller for driving a certain distance **/
		private double kP;
		/** The I term in the PID controller for driving a certain distance **/
		private double kI;
		/** The D term in the PID controller for driving a certain distance **/
		private double kD;
		
		private EncoderConstants(double wheelRadius, int encoderPPR, double gearRatio, double kP, double kI, double kD) {
			this.wheelRadius = wheelRadius;
			this.encoderPPR = encoderPPR;
			this.gearRatio = gearRatio;
			this.kP = kP;
			this.kI = kI;
			this.kD = kD;
		}
		
		/**
		 * Returns the wheel radius
		 * @return
		 */
		public double wheelRadius () {
			return this.wheelRadius;
		}
		
		/**
		 * Returns the wheel circumference
		 * @return
		 */
		public double wheelCircumference () {
			return Math.PI * 2 * wheelRadius();
		}
		
		/**
		 * Returns the rotations of the wheels for a meter displacement
		 * @return
		 */
		public double wheelRotationsForMeterDisplacement () {
			return 1 / wheelCircumference();
		}
		
		/**
		 * Returns the encoder's pulses per revolution
		 * @return
		 */
		public int encoderPPR () {
			return this.encoderPPR;
		}
		
		/**
		 * Returns the gear ratio between the encoder and the wheels
		 * @return
		 */
		public double gearRatio () {
			return this.gearRatio;
		}
		
		/**
		 * Returns the pulses of the encoder for a meter displacement
		 * @return
		 */
		public double pulsesInMeter () {
			return encoderPPR() * wheelRotationsForMeterDisplacement() / gearRatio();
		}
		
		/**
		 * Returns the displacement (in meters) for every pulse of the encoder
		 * @return
		 */
		public double distancePerPulse () {
			return  wheelCircumference() * gearRatio() / encoderPPR();
		}
	}
	
	/**
	 * The PID constants for the gyro 
	 * @author orianleitersdorf
	 *
	 */
	public static enum GyroConstants {
		A_BOT (0.03, 0.011, 0.00001, 0.0136), B_BOT (0.03, 0.011, 0.00001, 0.0136);
		
		/** The P term used in a PID controller for driving straight **/
		private double straightKp;
		/** The P term used in a PID controller for turning **/
		private double turnKp;
		/** The I term used in a PID controller for turning **/
		private double turnKi;
		/** The D term used in a PID controller for turning **/
		private double turnKd;
		
		private GyroConstants(double straightKp, double turnKp, double turnKi, double turnKd) {
			this.straightKp = straightKp;
			this.turnKp = turnKp;
			this.turnKi = turnKi;
			this.turnKd = turnKd;
		}
	}
	
	/** The gyro used to turn and drive straight **/
	private GyroBase gyro;
	/** The PID controller used with the gyro for turning **/
	private PIDController gyroTurnPID;
	
	/** The encoder on the left side of drive **/
	private Encoder leftEncoder;
	/** The encoder on the right side of drive **/
	private Encoder rightEncoder;
	/** The PID controller for driving a given distance with encoders **/
	private PIDController encoderDrivePID;

	/**
	 * Initializes the sensor drive with four motor drive, a piston for shifting gears, and two encoders.
	 * @param leftFirstMotor
	 * @param leftSecondMotor
	 * @param rightFirstMotor
	 * @param rightSecondMotor
	 * @param piston
	 * @param leftEncoder
	 * @param rightEncoder
	 */
	public SensorDrive(WPI_TalonSRX leftFirstMotor, WPI_TalonSRX leftSecondMotor, WPI_TalonSRX rightFirstMotor, WPI_TalonSRX rightSecondMotor
			, DoubleSolenoid piston, Encoder leftEncoder, Encoder rightEncoder) {
		
		super (leftFirstMotor, leftSecondMotor, rightFirstMotor, rightSecondMotor, piston);
		
		this.leftEncoder = leftEncoder;
		this.leftEncoder.setDistancePerPulse(EncoderConstants.A_BOT.distancePerPulse());
		this.rightEncoder = rightEncoder;
		this.rightEncoder.setDistancePerPulse(EncoderConstants.A_BOT.distancePerPulse());

		gyro = new ADXRS450_Gyro();
		
		gyroTurnPID = new PIDController (GyroConstants.A_BOT.turnKp, GyroConstants.A_BOT.turnKi, GyroConstants.A_BOT.turnKd, gyro, new PIDOutput() {
			public void pidWrite(double output) {}
		});
		gyroTurnPID.setOutputRange(-0.7, 0.7);
		
		SmartDashboard.putData("Gyro Turn PID Controller" , gyroTurnPID);
		
		encoderDrivePID = new PIDController (EncoderConstants.A_BOT.kP, EncoderConstants.A_BOT.kI, EncoderConstants.A_BOT.kD, new PIDSource() {
			
			public void setPIDSourceType(PIDSourceType pidSource) {}

			public PIDSourceType getPIDSourceType() {return PIDSourceType.kDisplacement;}

			public double pidGet() {return getAvgDistance();}
			
		}, new PIDOutput() {
			public void pidWrite(double output) {}
		});
		//encoderDrivePID.setOutputRange(-0.7,  0.7);
		
		SmartDashboard.putData("Encoder Straight PID Controller", encoderDrivePID);
		
	}
	
	/**
	 * Drives straight using the gyro to adjust turning, xSpeed represents the magnitude of driving straight (between -1 and 1)
	 * @param xSpeed
	 */
	public void gyroStraight (double xSpeed) {
		arcadeDrive (xSpeed, -getGyroAngle()*GyroConstants.A_BOT.straightKp, false);
	}
	
	/**
	 * Initializes the PID controllers before a turn for a given angle
	 * @param desiredAngle
	 */
	public void initGyroTurn (double desiredAngle) {
		resetGyro();
		gyroTurnPID.reset();
		gyroTurnPID.setSetpoint(desiredAngle);
		gyroTurnPID.enable();
	}
	
	/**
	 * Executes the output from the PID controller for turning (call initGyroTurn first, then call this repeatedly)
	 */
	public void executeGyroTurn () {
		executeGyroTurn (0);
	}
	
	/**
	 * Executes the output from the PID controller for turning (call initGyroTurn first, then call this repeatedly)
	 * @param magnitude - the magnitude to use for xSpeed while turning (allows for curving)
	 */
	public void executeGyroTurn (double magnitude) {
		
		double turn = gyroTurnPID.get();
		
		if (turn < 0 && turn > -0.35) {
			turn = -0.35;
		}
		else if (turn > 0 && turn < 0.35) {
			turn = 0.35;
		}
		arcadeDrive (magnitude, turn, false);
	}
	
	/**
	 * Returns the current error from the turning (desiredAngle - currentAngle)
	 * @return
	 */
	public double getGyroTurnError () {
		return gyroTurnPID.getError();
	}
	
	/**
	 * Returns the current gyro angle
	 * @return
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	/**
	 * Resets the gyro
	 */
	public void resetGyro() {
		gyro.reset();
	}
	
	/**
	 * Calibrates the gyro (important that the robot is not moving)
	 */
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
	/**
	 * Initializes the drive for a given distance using a PID controller
	 * @param desiredDistance
	 */
	public void initEncoderStraight (double desiredDistance) {
		resetGyro();
		resetEncoders();
		encoderDrivePID.reset();
		encoderDrivePID.setSetpoint(desiredDistance);
		encoderDrivePID.enable();
	}
	
	/**
	 * Executes the PID controller's output for driving a given distance (call initEncoderStraight first, then call this repeatedly)
	 */
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
	
	/**
	 * Returns the current error for driving a given distance (desiredDistance - currentDistance)
	 * @return
	 */
	public double getEncoderStraightError () {
		return encoderDrivePID.getError();
	}
	
	/**
	 * Resets the encoders for drive
	 */
	public void resetEncoders () {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	/**
	 * Returns the distance from the left encoder
	 * @return
	 */
	public double getLeftDistance () {
		return leftEncoder.getDistance();
	}
	
	/**
	 * Returns the distance from the right encoder
	 * @return
	 */
	public double getRightDistance () {
		return rightEncoder.getDistance();
	}
	
	/**
	 * Returns the average distance from both encoders
	 * @return
	 */
	public double getAvgDistance () {
		return (getLeftDistance() + getRightDistance())/2;
	}

}
