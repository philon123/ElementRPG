package com.philon.rpg.stat.presuf;

import com.philon.rpg.stat.effect.EffectsObjSaveData;

public class PrefixSuffixSaveData {
	public Class<? extends AbstractPrefixSuffix> presufClass;
	public int level;
	public EffectsObjSaveData effects;

	//----------

	public static PrefixSuffixSaveData create( AbstractPrefixSuffix ps ) {
		PrefixSuffixSaveData psd = new PrefixSuffixSaveData();

		psd.presufClass = ps.getClass();
		psd.level = ps.level;
		psd.effects = ps.effects.save();

		return psd;
	}

	//----------

}
