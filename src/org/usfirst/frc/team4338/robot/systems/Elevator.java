package org.usfirst.frc.team4338.robot.systems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Represents the Elevator on the robot. Uses a motor, an encoder, and a limit switch at the bottom, to move the elevator to different locations.
 * Locations are specified in the {@link Stage} enumeration, and the elevator can be elevated to a certain stage.
 * The elevator cannot go any lower than where it was when the program was started (unless override used), and cannot go any higher than {@link MAX_HEIGHT} above
 * the initial height.
 * @author orianleitersdorf
 *
 */
public class Elevator extends Subsystem {

	/** The motor used to move the elevator **/
	private WPI_TalonSRX motor;
	/** The encoder connected to the elevator: used to track elevator height **/
	private Encoder encoder;
	/** The limit switch at the bottom of the elevator **/
	private DigitalInput bottomSW;
	/** If set, the elevator will be able to go lower than where it was when the program started **/
	private boolean overrideEncoderBottom = false;
	
	/**
	 * Represents the different Stages (heights) that the elevator can be in.
	 * @author orianleitersdorf
	 *
	 */
	public enum Stage {
		
		LOWEST (0), SWITCH (2000), SCALE_OUR_FAVOR (6000), SCALE_CENTER (7000), MAX_HEIGHT (8100);
		
		private double value;
		private Stage (double value) {
			this.value = value;
		}
	}
	
	/**
	 * Initializes the elevator with a motor, an encoder, and an input for the bottom limit switch
	 * @param motor
	 * @param encoder
	 * @param bottomSW
	 */
	public Elevator(WPI_TalonSRX motor, Encoder encoder, DigitalInput bottomSW){
		this.motor = motor;
		this.encoder = encoder;
		this.bottomSW = bottomSW;
		
		this.encoder.setDistancePerPulse(-1);
		
		enableBrakeMode();
	}
	
	/**
	 * Resets the encoder
	 */
	public void resetCurrentHeight () {
		encoder.reset();
	}

	/**
	 * Returns the current height according to the encoder
	 * @return
	 */
	private double getCurrentHeight() {
		return encoder.getDistance();
	}
	
	/**
	 * Returns whether or not the elevator is at it's bottom. If the limit switch is pressed,
	 * always returns true. Else, if override is off, returns if the current height is negative,
	 * and if the override is on, returns false.
	 * @return
	 */
	public boolean atBottom() {
		if (isOverrideEncoderBottom()) {
			return bottomSW.get();
		}
		else {
			return bottomSW.get() || (getCurrentHeight()<=0);
		}
	}
	
	/**
	 * Returns whether or not the elevator is at it's maximum height (according to the encoder).
	 * This requires that the program starts at the zero position, so that the relative location
	 * measured (using the encoder) can represent the physical maximum height.
	 * @return
	 */
	public boolean atTop() {
		return isAboveStage (Stage.MAX_HEIGHT);
	}
	
	/**
	 * Positive speed cooresponds to going up
	 * @param speed
	 */
	public void elevate(double speed) {
		// If currently at the bottom, and trying to go down: stop
		if(atBottom() && (speed < 0)) {
			stop();
		}
		
		// Else if, current at the top, and trying to go up: stop
		else if(atTop() &&(speed > 0)) {
			stop();
		}
		
		// Else, set the motor to the given speed (all tests are passed)
		else {
			this.motor.set(-speed);
		}
	}

	/**
	 * Stops the elevator motor (actions taken by the TalonSRX
	 * depend on the {@link NuetralMode} chosen, which can be set
	 * using enableBrakeMode() and disableBrakeMode().
	 */
	public void stop() {
		this.motor.set(0.0);
	}

	/**
	 * Enables brake mode on the TalonSRX programatically
	 */
	public void enableBrakeMode() {
		setBrakeMode(true);
	}
	
	/**
	 * Disables brake mode on the TalonSRX programatically
	 */
	public void disableBrakeMode() {
		setBrakeMode(false);
	}
	
	/**
	 * Sets the brake mode of the TalonSRX
	 * @param brakeMode - true cooresponds to enabling brake mode
	 */
	public void setBrakeMode (boolean brakeMode) {
		this.motor.setNeutralMode(brakeMode?NeutralMode.Brake:NeutralMode.Coast);
	}

	/**
	 * Returns whether or not the elevator bottom override is activated
	 * @return
	 */
	public boolean isOverrideEncoderBottom() {
		return overrideEncoderBottom;
	}

	/**
	 * Sets the override for the elevator's encoder lower limit. If set to true,
	 * the elevator will be able to go below the height at which the program was
	 * started, but not any lower than the limit switch.
	 * @param overrideEncoderBottom
	 */
	public void setOverrideEncoderBottom(boolean overrideEncoderBottom) {
		this.overrideEncoderBottom = overrideEncoderBottom;
	}
	
	/**
	 * Returns whether or not the elevator is currently above a given stage
	 * @param stage
	 * @return
	 */
	public boolean isAboveStage (Stage stage) {
		return stage.value < getCurrentHeight();
	}
	
	/**
	 * Tries to elevate to the given stage from below, if currently above the given stage
	 * does nothing.
	 * @param stage
	 */
	public void elevateToStageFromBelow (Stage stage) {
		if (isAboveStage (stage)) {
			elevate (0);
		}
		else {
			elevate (0.5);
		}
		
	}

	@Override
	protected void initDefaultCommand() {}

}
