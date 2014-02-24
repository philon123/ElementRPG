package com.philon.rpg.util;

import com.philon.rpg.mo.RpgMapObj;

public class TimerListObject {
	public int timerValue;
	public RpgMapObj mo;

	//----------
	
	public TimerListObject( RpgMapObj newMo, int newTimerValue ) {
		mo = newMo;
		timerValue = newTimerValue;
	}

	//----------

}