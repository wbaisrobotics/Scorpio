package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

public class Ramp {
	
	private WPI_TalonSRX rampLeft;
	private WPI_TalonSRX rampRight;
	
	private WPI_TalonSRX teammateLifterLeft;
	private DigitalInput teammateLifterLeftSW;
	private WPI_TalonSRX teammateLifterRight;
	private DigitalInput teammateLifterRightSW;

	public Ramp(int rampLeftMotor, int rampRightMotor, int teammateLifterLeftMotor, 
			int teammateLifterRightMotor, int teammateLifterLeftSW,
			int teammateLifterRightSW) {
		this.rampLeft = new WPI_TalonSRX(rampLeftMotor);
		this.rampRight = new WPI_TalonSRX(rampRightMotor);
		this.teammateLifterLeft = new WPI_TalonSRX(teammateLifterLeftMotor);
		this.teammateLifterRight = new WPI_TalonSRX(teammateLifterRightMotor);
		this.teammateLifterLeftSW = new DigitalInput(teammateLifterLeftSW);
		this.teammateLifterRightSW = new DigitalInput(teammateLifterRightSW);
	}
	
	public void lowerRamp() {
		this.rampLeft.set(0.3);
		this.rampRight.set(-0.3);
	}
	
	public void stopRamp() {
		this.rampLeft.set(0);
		this.rampRight.set(0);
	}
	
	public void liftRamp() {
		this.rampLeft.set(-0.6);
		this.rampRight.set(0.6);
	}

	/**
	 * Lifts another robot
	 */
	public void liftTeammate() {
		if(!teammateLifterLeftSW.get()) {
			this.teammateLifterLeft.set(-1.0);
		}
		else {
			this.teammateLifterLeft.set(0);
		}
		
		if(!teammateLifterRightSW.get()) {
			this.teammateLifterRight.set(1.0);
		}
		else {
			this.teammateLifterRight.set(0);
		}
	}
	
	public void lowerTeammate() {
		this.teammateLifterLeft.set(1.0);
		this.teammateLifterRight.set(-1.0);
	}
	
	public void stopTeammate() {
		this.teammateLifterLeft.set(0);
		this.teammateLifterRight.set(0);
	}

}
