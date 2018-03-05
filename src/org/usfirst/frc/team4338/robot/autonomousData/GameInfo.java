package org.usfirst.frc.team4338.robot.autonomousData;

public class GameInfo {
	
	private boolean m_ourSwitchLeft;
	private boolean m_scaleLeft;
	private boolean m_theirSwitchLeft;

	public GameInfo(boolean ourSwitchLeft, boolean scaleLeft, boolean theirSwitchLeft) {
		this.m_ourSwitchLeft = ourSwitchLeft;
		this.m_scaleLeft = scaleLeft;
		this.m_theirSwitchLeft = theirSwitchLeft;
	}
	
	public boolean isOurSwitchLeft () {
		return this.m_ourSwitchLeft;
	}
	
	public boolean isScaleLeft () {
		return this.m_scaleLeft;
	}
	
	public boolean isTheirSwitchLeft () {
		return this.m_theirSwitchLeft;
	}
	
	public String toString () {
		return "Our Switch: " + (isOurSwitchLeft()?"Left":"Right") + ", Scale: " + (isScaleLeft()?"Left":"Right") + ", Their Switch: " + (isTheirSwitchLeft()?"Left":"Right");
	}
	
	public static GameInfo fromGameMessage (String message) {
		boolean isOurSwitchLeft = message.substring(0, 1).equalsIgnoreCase("L");
		boolean isScaleLeft = message.substring(1, 2).equalsIgnoreCase("L");
		boolean isTheirSwitchLeft = message.substring(2, 3).equalsIgnoreCase("L");
		return new GameInfo (isOurSwitchLeft, isScaleLeft, isTheirSwitchLeft);
	}
	
	public boolean isAllignedWithSwitch (StartingPosition pos) {
		return isOurSwitchLeft() == (pos == StartingPosition.LEFT);
	}
	
	public boolean isAllignedWithScale (StartingPosition pos) {
		return isScaleLeft() == (pos == StartingPosition.LEFT);
	}

}
