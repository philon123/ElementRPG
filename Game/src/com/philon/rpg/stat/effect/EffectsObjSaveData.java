package com.philon.rpg.stat.effect;
import java.util.TreeMap;

import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;

public class EffectsObjSaveData {
	public TreeMap<Class<? extends AbstractEffect>, AbstractEffect> effects;

	//----------

	@SuppressWarnings("unchecked")
  public static EffectsObjSaveData create( EffectsObj eo ) {
		EffectsObjSaveData esd = new EffectsObjSaveData();

		esd.effects = (TreeMap<Class<? extends AbstractEffect>, AbstractEffect>) eo.effects.clone();

		return esd;
	}

	//----------

}
