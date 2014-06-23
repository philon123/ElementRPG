package com.philon.rpg.util;

import com.philon.rpg.map.mo.RpgMapObj;

public class TimerListObject {
	public float timerValue;
	public RpgMapObj mo;

	public TimerListObject( RpgMapObj newMo, float newTimerValue ) {
		mo = newMo;
		timerValue = newTimerValue;
	}

}