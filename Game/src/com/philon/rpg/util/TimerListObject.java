package com.philon.rpg.util;

import com.philon.rpg.mo.AbstractMapObj;

public class TimerListObject {
	public int timerValue;
	public AbstractMapObj mo;

	//----------
	
	public TimerListObject( AbstractMapObj newMo, int newTimerValue ) {
		mo = newMo;
		timerValue = newTimerValue;
	}

	//----------

}