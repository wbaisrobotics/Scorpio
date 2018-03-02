package org.usfirst.frc.team4338.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {

	private WPI_TalonSRX leftMotor;
	private WPI_TalonSRX rightMotor;
	
	private Solenoid piston;
	
	//private DigitalInput limitSwitch;
	
	public Intake (int leftMotorPort, int rightMotorPort, int pistonBPort) {
		leftMotor = new WPI_TalonSRX (leftMotorPort);
		rightMotor = new WPI_TalonSRX (rightMotorPort);
		piston = new Solenoid (pistonBPort);
	}

	public boolean getRetractionState() {
		return piston.get();
	}

	public void armsIn() {
		piston.set(true);
		stopWheels();
	}
	
	public void armsOut() {
		piston.set(false);
	}
	
	public void toggleArms() {
		if (piston.get()) {
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
