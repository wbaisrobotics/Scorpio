package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Fork extends Subsystem{

	// ----------- Extension -----------
	private WPI_TalonSRX motor;
	private DigitalInput extendedLimitSW;
	private DigitalInput retractedLimitSW;
	
	// ----------- Releasing fork -----------
	private DoubleSolenoid releaser;
	
	// ----------- Gripping cubes -----------
	private DoubleSolenoid gripper;
	
	private long time;
	
	// Constructor
	public Fork(int motorPort, int extendedLimitSwitchPort, int retractedLimitSwitchPort,
			int gripperPistonA, int gripperPistonB, int releasePistonA, int releasePistonB) {
		this.motor = new WPI_TalonSRX(motorPort);
		this.motor.setNeutralMode(NeutralMode.Brake);
		this.retractedLimitSW = new DigitalInput(retractedLimitSwitchPort);
		this.extendedLimitSW = new DigitalInput(extendedLimitSwitchPort);
		
		this.releaser = new DoubleSolenoid(releasePistonA, releasePistonB);
		
		this.gripper = new DoubleSolenoid(gripperPistonA, gripperPistonB);
		
	}

	public boolean canExtend() {
		return !extendedLimitSW.get();
	}

	public boolean canRetract() {
		return !retractedLimitSW.get();
	}

	public void extend() {
		if(canExtend()) {
			this.motor.set(1.0);
		}
		else {
			stop();
		}
	}

	public void retract() {
		if(canRetract()) {
			this.motor.set(-1.0);
		}
		else {
			stop();
		}
	}

	public void stop() {
		this.motor.set(0.0);
	}
	
	public void resetTimer () {
		time = Robot.timeSinceStart();
	}
	
	public long getTimerTime () {
		return Robot.timeSinceStart() - time;
	}
	
	public void extend(long milliseconds) {
		if (getTimerTime() < milliseconds) {
			stop();
		}
		else {
			extend();
		}
	}
	
	public void retract(long milliseconds) {
		if (getTimerTime() < milliseconds) {
			stop();
		}
		else {
			retract();
		}
	}
	
	// ----------- Releasing fork -----------

	public void releaseFork() {
		this.releaser.set(Value.kForward);
	}

	public void holdFork() {
		this.releaser.set(Value.kReverse);
	}
	
	public void toggleReleaseFork () {
		releaser.set((releaser.get()==Value.kForward)?Value.kReverse:Value.kForward);
	}

	// ----------- Gripping cubes -----------
	
	public void openGripper() {
		this.gripper.set(Value.kReverse);
	}

	public void closeGripper() {
		this.gripper.set(Value.kForward);
	}
	
	public void toggleGripper () {
		gripper.set((gripper.get()==Value.kForward)?Value.kReverse:Value.kForward);
	}

	@Override
	protected void initDefaultCommand() {}

}
