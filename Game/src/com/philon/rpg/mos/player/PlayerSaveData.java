package com.philon.rpg.mos.player;

import com.philon.rpg.mos.player.inventory.InventorySaveData;
import com.philon.rpg.stat.effect.EffectsObjSaveData;

public class PlayerSaveData {
	public Class<? extends AbstractChar> charClass;
	public int xp;
	public EffectsObjSaveData baseEffects;
	public InventorySaveData inv;
	public int skills[];

	//----------

	public PlayerSaveData( AbstractChar p ) {
		charClass = p.getClass();
		xp = p.xp;

		baseEffects = p.baseEffects.save();
		inv = p.inv.save();

		skills = new int[p.skills.length];
		for( int i = 0; i <= skills.length-1; i++ ) {
			skills[i] = p.skills[i];
		}
	}

	//----------

}
