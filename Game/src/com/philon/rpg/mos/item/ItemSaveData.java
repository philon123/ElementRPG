package com.philon.rpg.mos.item;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObjSaveData;
import com.philon.rpg.stat.presuf.PrefixSuffixSaveData;

public class ItemSaveData {
	public Class<? extends AbstractItem> itemClass;
	public Vector pos;
	public boolean isIdentified;
	public int iEffType;
	public int occSlot;

	public EffectsObjSaveData baseEffects;
	public PrefixSuffixSaveData prefix;
	public PrefixSuffixSaveData suffix;

	//----------

	public ItemSaveData( AbstractItem it ) {
		iEffType = it.iEffType;

		itemClass = it.getClass();
		if (it.pos!=null) pos = it.pos.copy();
		isIdentified = it.isIdentified;

		baseEffects = it.baseEffects.save();
		if (it.prefix!=null) prefix = it.prefix.save();
		if (it.suffix!=null) suffix = it.suffix.save();
	}

	//----------

}
