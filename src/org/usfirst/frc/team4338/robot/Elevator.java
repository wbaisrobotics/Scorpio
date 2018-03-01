package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Elevator {

	private WPI_TalonSRX motor;
	private Encoder encoder;
	private DigitalInput bottomLimitSW;
	
	public static final double MAX_HEIGHT = 1000; // in pulses
	
	public Elevator (int talonPort, int limitSwitchPort, int encoderA, int encoderB){
		bottomLimitSW = new DigitalInput (limitSwitchPort);
		motor = new WPI_TalonSRX (talonPort);
		encoder = new Encoder (encoderA, encoderB);
	}

	public double getCurrentHeight() {
		return encoder.getDistance();
	}
	
//	private void resetCurrentHeight() {
//		encoder.reset();
//	}
	
	public boolean atBottom() {
		// True reading repesents the bottom limit switch being pressed
		return bottomLimitSW.get() || (getCurrentHeight()<=0);
	}
	
	public void elevateUpDown (double speed) {
		if (atBottom() && (speed > 0)) {
			this.motor.stopMotor();
			//resetCurrentHeight();
		}
		else if ((getCurrentHeight() >= MAX_HEIGHT) && (speed < 0)) {
			this.motor.stopMotor();
		}
		else {
			this.motor.set(speed);
		}
	}

	public void stop() {
		elevateUpDown (0);
	}

}
