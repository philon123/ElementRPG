package com.philon.rpg;

import com.philon.engine.Database;
import com.philon.rpg.mos.enemy.EnemyData;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.player.CharData;
import com.philon.rpg.mos.wall.WallData;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.presuf.PrefixSuffixData;

public class RpgDatabase { //TODO Database: should have all data classes as members to be accessed like: Game.inst.gdb.enemies.[...]
	public static Database db;

	public static void loadAll() {
	   db = new Database();
	   
	   //with db
	   new ImageData().loadMedia();
     new SoundData().loadMedia();
     new SpellData().loadMedia(); //StatsObj has dependancy to this, load FIRST
     
	   //without db
	   WallData.loadMedia();
	   SkillData.loadMedia();
	   ItemData.loadMedia();
	   PrefixSuffixData.loadMedia();
	   EnemyData.loadMedia();
	   CharData.loadMedia();
	}
}
