package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

	private WPI_TalonSRX motor;
	private Encoder encoder;
	private DigitalInput bottomLimitSW;
	private boolean overrideEncoderBottom = false;
	
	public enum Stage {
		
		LOWEST (0), SWITCH (2000), SCALE_OUR_FAVOR (6000), SCALE_CENTER (7000), MAX_HEIGHT (8100);
		
		private double value;
		private Stage (double value) {
			this.value = value;
		}
	}
	
	public static final double MAX_HEIGHT = 8100; // in pulses 8744.25
	
	public Elevator(int talonPort, int limitSwitchPort, int encoderA, int encoderB){
		this.motor = new WPI_TalonSRX(talonPort);
		enableBrakeMode();
		this.encoder = new Encoder(encoderA, encoderB);
		this.encoder.setDistancePerPulse(-1);
		this.bottomLimitSW = new DigitalInput(limitSwitchPort);
	}
	
	public void resetEncoder () {
		encoder.reset();
	}

	public double getCurrentHeight() {
		return encoder.getDistance();
	}
	
//	private void resetCurrentHeight() {
//		encoder.reset();
//	}
	
	public boolean atBottom() {
		if (isOverrideEncoderBottom()) {
			return bottomLimitSW.get();
		}
		else {
			return bottomLimitSW.get() ||(getCurrentHeight()<=0);
		}
	}
	
//	public boolean atTop() {
//		
//	}
	
	/**
	 * Positive speed cooresponds to going up
	 * @param speed
	 */
	public void elevateUpDown(double speed) {
		if(atBottom() && (speed < 0)) {
			this.motor.set(0.0);
			//resetCurrentHeight();
		}
		else if((getCurrentHeight() >= MAX_HEIGHT) &&(speed > 0)) {
			this.motor.set(0.0);
		}
		else {
			this.motor.set(-speed);
		}
	}

	public void stop() {
		elevateUpDown(0);
	}

	
	public void enableBrakeMode() {
		this.motor.setNeutralMode(NeutralMode.Brake);
	}
	
	public void disableBrakeMode () {
		this.motor.setNeutralMode(NeutralMode.Coast);
	}

	public boolean isOverrideEncoderBottom() {
		return overrideEncoderBottom;
	}

	public void setOverrideEncoderBottom(boolean overrideEncoderBottom) {
		this.overrideEncoderBottom = overrideEncoderBottom;
	}
	
	public boolean isAboveStage (Stage stage) {
		return stage.value < getCurrentHeight();
	}
	
	/**
	 * Will try to reach stage, returns true if arrived
	 * @param stage
	 */
	public void elevateToStageFromBelow (Stage stage) {
		if (isAboveStage (stage)) {
			elevateUpDown (0);
		}
		else {
			elevateUpDown (0.5);
		}
		
	}

	@Override
	protected void initDefaultCommand() {}

}
