package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Represents the fork subsystem for extending and retracting a lead screw connected to a carriage.
 * Connects to two {@link DigitalInput} representing the limit switches in the extended and
 * retracted position of the fork.
 * @author orianleitersdorf
 *
 */
public class Fork extends Subsystem{
	
	/** The motor in control of extending and retracting the fork (holding the intake mechanism) **/
	private WPI_TalonSRX motor;
	/** The limit switch that is triggered when the fork is in the fully extended position **/
	private DigitalInput extendedLimitSW;
	/** The limit switch that is triggered when the fork is in the fully retracted position **/
	private DigitalInput retractedLimitSW;
	
	/**
	 * Initializes the Fork extension mechanism with a motor used for output, and two digital inputs
	 * representing limit switches for the extended and retracted positions (respectively).
	 * @param motor
	 * @param extendedSW
	 * @param retractedSW
	 */
	public Fork(WPI_TalonSRX motor, DigitalInput extendedSW, DigitalInput retractedSW) {
		this.motor = motor;
		this.extendedLimitSW = extendedSW;
		this.retractedLimitSW = retractedSW;
		
		this.motor.setNeutralMode(NeutralMode.Brake);
	}

	/**
	 * Returns whether or not the fork can extend (according to the limit switches)
	 * @return
	 */
	public boolean canExtend() {
		return !extendedLimitSW.get();
	}

	/**
	 * Returns whether or not the fork can retract (according to the limit switches)
	 * @return
	 */
	public boolean canRetract() {
		return !retractedLimitSW.get();
	}

	/**
	 * Extends the fork (only if {@link canExtend()} returns true, if not calls {@link stop()})
	 */
	public void extend() {
		if(canExtend()) {
			this.motor.set(1.0);
		}
		else {
			stop();
		}
	}

	/**
	 * Retracts the fork (only if {@link canRetract()} returns true, if not calls {@link stop()})
	 */
	public void retract() {
		if(canRetract()) {
			this.motor.set(-1.0);
		}
		else {
			stop();
		}
	}

	/**
	 * Stops the motor in control of the fork
	 */
	public void stop() {
		this.motor.set(0.0);
	}
	
	/**
	 * No default command
	 */
	protected void initDefaultCommand() {}

}
