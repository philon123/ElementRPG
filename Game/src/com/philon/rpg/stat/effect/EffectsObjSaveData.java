package com.philon.rpg.stat.effect;
import java.util.LinkedList;

import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;

public class EffectsObjSaveData {
	public LinkedList<AbstractEffect> effects;

	//----------

	public static EffectsObjSaveData create( EffectsObj eo ) {
		EffectsObjSaveData esd = new EffectsObjSaveData();

		esd.effects = (LinkedList<AbstractEffect>) eo.effects.clone();

		return esd;
	}

	//----------

}
