package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Intake {

	private WPI_TalonSRX leftMotor;
	private WPI_TalonSRX rightMotor;
	
	private DoubleSolenoid piston;
	
	//private DigitalInput limitSwitch;
	
	public Intake (int leftMotorPort, int rightMotorPort, int pistonAPort, int pistonBPort) {
		leftMotor = new WPI_TalonSRX (leftMotorPort);
		rightMotor = new WPI_TalonSRX (rightMotorPort);
		piston = new DoubleSolenoid (pistonAPort, pistonBPort);
	}

	public boolean getRetractionState() {
		return piston.get()==Value.kForward;
	}

	public void armsIn() {
		piston.set(Value.kForward);
		stopWheels();
	}
	
	public void armsOut() {
		piston.set(Value.kReverse);
	}
	
	public void toggleArms() {
		if (getRetractionState()) {
			armsOut();
		}
		else {
			armsIn();
		}
	}
	
	public void cubeIn() {
		leftMotor.set(-0.6);
		rightMotor.set(0.6);
	}
	
	public void cubeOut() {
		leftMotor.set(0.6);
		rightMotor.set(-0.6);
	}
	
	public void stopWheels() {
		leftMotor.set(0.0);
		rightMotor.set(0.0);
	}
	
	public boolean wheelsRunning() {
		return (leftMotor.get()!=0 && rightMotor.get()!=0);
	}

}
