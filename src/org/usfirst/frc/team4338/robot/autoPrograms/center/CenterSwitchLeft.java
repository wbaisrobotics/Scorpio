package org.usfirst.frc.team4338.robot.autoPrograms.center;

import org.usfirst.frc.team4338.robot.Elevator;
import org.usfirst.frc.team4338.robot.Fork;
import org.usfirst.frc.team4338.robot.SensorDrive;
import org.usfirst.frc.team4338.robot.autoPrograms.AutonomousProgram;

public class CenterSwitchLeft implements AutonomousProgram {
	
	private SensorDrive drive;
	private Elevator elevator;
	private Fork fork;
	
	private static enum State {
		INITIAL_STRAIGHT, FIRST_TURN, SECOND_STRAIGHT, SECOND_TURN, FINAL_STRAIGHT, LIFT_ELEVATOR, EXTEND_FORK, RELEASE_CUBE, ENDED;
	}
	private State currentState;

	public CenterSwitchLeft(SensorDrive drive, Fork fork, Elevator elevator) {
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
		
		switch (currentState) {
		case INITIAL_STRAIGHT:
			
			drive.gyroStraight(0.5);
			
			if (drive.getAvgDistance() > 0.5) {
				currentState = State.FIRST_TURN;
				drive.resetGyro();
			}
			
			break;
		case FIRST_TURN:
			
			drive.gyroTurn(0, -75);
			
			if (drive.getGyroAngle() < -65) {
				currentState = State.SECOND_STRAIGHT;
				drive.resetEncoders();
				drive.resetGyro();
			}
			
			break;
		case SECOND_STRAIGHT:

			drive.gyroStraight(0.5);
			
			if (drive.getAvgDistance() > 2.0) {
				currentState = State.SECOND_TURN;
				drive.resetGyro();
			}
			
			break;
		case SECOND_TURN:
			
			drive.gyroTurn(0, 75);
			
			if (drive.getGyroAngle() > 65) {
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
		case LIFT_ELEVATOR:
			
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