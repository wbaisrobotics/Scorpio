package org.usfirst.frc.team4338.robot.autoPrograms.center;

import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.SensorDrive;
import org.usfirst.frc.team4338.robot.autoPrograms.AutonomousProgram;

public class CenterSwitchRight implements AutonomousProgram {
	
	private SensorDrive drive;
	private Elevator elevator;
	private Fork fork;
	
	private int n = 0;
	
	private static enum State {
		INITIAL_STRAIGHT, FIRST_TURN, SECOND_STRAIGHT, SECOND_TURN, FINAL_STRAIGHT, ENDED;
	}
	private State currentState;

	public CenterSwitchRight(SensorDrive drive, Fork fork, Elevator elevator) {
		this.drive = drive;
		this.fork = fork;
		this.elevator = elevator;
	}

	@Override
	public void initialize() {
		drive.resetGyro();
		this.currentState = State.INITIAL_STRAIGHT;
	}

	@Override
	public void update() {
		if (n%10 == 0) {
		System.out.println("CenterSwitch: " + currentState.toString());
		}
		n++;
		
		switch (currentState) {
		case INITIAL_STRAIGHT:
			
			drive.gyroStraight(0.5);
			
			if (drive.getAvgDistance() > 0.5) {
				currentState = State.FIRST_TURN;
				drive.resetGyro();
			}
			
			break;
		case FIRST_TURN:
			
			drive.gyroTurn(0, 65);
			
			if (drive.getGyroAngle() > 55) {
				currentState = State.SECOND_STRAIGHT;
				drive.resetEncoders();
				drive.resetGyro();
			}
			
			break;
		case SECOND_STRAIGHT:

			drive.gyroStraight(0.5);
			
			if (drive.getAvgDistance() > 1.0) {
				currentState = State.SECOND_TURN;
				drive.resetGyro();
			}
			
			break;
		case SECOND_TURN:
			
			drive.gyroTurn(0, -65);
			
			if (drive.getGyroAngle() < -55) {
				currentState = State.FINAL_STRAIGHT;
				drive.resetEncoders();
				drive.resetGyro();
			}
			
			break;
		case FINAL_STRAIGHT:
			
			drive.gyroStraight(0.5);
			
			if (drive.getAvgDistance() > 1.0) {
				currentState = State.ENDED;
			}
			
			break;
			
		case ENDED:	
			stop();
			break;
		}
		
	}

	@Override
	public void stop() {
	}

}
