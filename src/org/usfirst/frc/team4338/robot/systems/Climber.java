package org.usfirst.frc.team4338.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem{
	
	private SpeedController left;
	private SpeedController right;
	
	public Climber (SpeedController left, SpeedController right) {
		this.left = left;
		this.right = right;
	}
	
	public void up() {
		this.left.set(1.0);
		this.right.set(-1.0);
	}
	
	public void down() {
		this.left.set(-1.0);
		this.right.set(1.0);
	}
	
	public void stop() {
		this.left.set(0);
		this.right.set(0);
	}

	protected void initDefaultCommand() {}

}
