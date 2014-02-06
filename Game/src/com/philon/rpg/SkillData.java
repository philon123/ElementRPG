package com.philon.rpg;

import com.philon.rpg.spell.SpellData;


public abstract class SkillData {
	public static final int numSkills = 10;

	public static final int NONE    = 0;
	public static final int FIR_SIN = 1;
	public static final int FIR_SPL = 2;
	public static final int FIR_ICE = 3;
	public static final int ICE_SIN = 4;
	public static final int ICE_SPL = 5;
	public static final int ICE_LIG = 6;
	public static final int LIG_SIN = 7;
	public static final int LIG_SPL = 8;
	public static final int LIG_FIR = 9;

	public static int spellsForSkill[][];

	//----------

	public static void loadMedia() {
		spellsForSkill = new int[numSkills][];

		for( int i = 0; i <= numSkills-1; i++ ) {
			spellsForSkill[i] = new int[4];
		}

		spellsForSkill[NONE]    = new int[]{ SpellData.EMPTY,             SpellData.EMPTY,             SpellData.EMPTY,                     SpellData.EMPTY                      };
		spellsForSkill[FIR_SIN] = new int[]{ SpellData.FIRE_BOLT,         SpellData.FIRE_BOLT_SEEKING, SpellData.LARGE_FIRE_BOLT,           SpellData.LARGE_FIRE_BOLT_SEEKING    };
		spellsForSkill[FIR_SPL] = new int[]{ SpellData.FIRE_BREATH,       SpellData.FIRE_WALL,         SpellData.FIRE_WAVE,                 SpellData.FIRE_BALL                  };
		spellsForSkill[ICE_SIN] = new int[]{ SpellData.ICE_BOLT,          SpellData.ICE_BOLT_SEEKING,  SpellData.LARGE_ICE_BOLT,            SpellData.LARGE_ICE_BOLT_SEEKING     };
		spellsForSkill[ICE_SPL] = new int[]{ SpellData.ICE_BREATH,        SpellData.ICE_NOVA,          SpellData.FROST_WAVE,                SpellData.ICE_BALL                   };
		spellsForSkill[LIG_SIN] = new int[]{ SpellData.LIGHTNING,         SpellData.THUNDERBOLT,       SpellData.INSTANT_LIGHTNING,         SpellData.LIGHTNING_BEAM             };
		spellsForSkill[LIG_SPL] = new int[]{ SpellData.KUGELBLITZ,        SpellData.CHAIN_LIGHTNING_1, SpellData.GROUND_LIGHTNING,          SpellData.CHAIN_LIGHTNING_2          };
		spellsForSkill[FIR_ICE] = new int[]{ SpellData.FEVER_BREATH,      SpellData.ELEMENT_BALL,      SpellData.LARGE_ELEMENT_BALL,        SpellData.ELEMENT_STORM              };
		spellsForSkill[ICE_LIG] = new int[]{ SpellData.FREEZE_KUGELBLITZ, SpellData.SHOCKFROST,        SpellData.FROST_STORM,               SpellData.CHAIN_FREEZERAY            };
		spellsForSkill[LIG_FIR] = new int[]{ SpellData.FIRE_KUGELBLITZ,   SpellData.SHOCKWAVE,         SpellData.ELECTRO_MAGNETIC_FIREBALL, SpellData.ELECTRO_MAGNETIC_FIRESTORM };
	}

	//----------

}
