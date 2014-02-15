package com.philon.rpg.util;

import com.philon.rpg.mo.GameMapObj;

public class TimerListObject {
	public int timerValue;
	public GameMapObj mo;

	//----------
	
	public TimerListObject( GameMapObj newMo, int newTimerValue ) {
		mo = newMo;
		timerValue = newTimerValue;
	}

	//----------

}