package com.philon.rpg.spell;

import java.lang.reflect.InvocationTargetException;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgData;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spells.SpellArrow;
import com.philon.rpg.spells.SpellFireBolt;
import com.philon.rpg.spells.SpellFireWall;
import com.philon.rpg.spells.SpellHealing;
import com.philon.rpg.spells.SpellIdentify;
import com.philon.rpg.spells.SpellKugelBlitz;
import com.philon.rpg.spells.SpellMelee;
import com.philon.rpg.spells.SpellNone;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatFireDamage;
import com.philon.rpg.stat.StatsObj.StatIceDamage;
import com.philon.rpg.stat.StatsObj.StatLightningDamage;
import com.philon.rpg.stat.StatsObj.StatMagic;
import com.philon.rpg.stat.StatsObj.StatNormalDamage;

public class SpellData {
	public static final int MAX_SPELL_LVL = 20;

	public static int numSpells;

	public static final int EMPTY                      =  0;
	public static final int MELEE                      =  1;
	public static final int ARROW                      =  2;
	public static final int IDENTIFY                   =  3;
	public static final int HEALING                    =  4;
	public static final int FIRE_BOLT                  =  5;
	public static final int FIRE_BOLT_SEEKING          =  6;
	public static final int LARGE_FIRE_BOLT            =  7;
	public static final int LARGE_FIRE_BOLT_SEEKING    =  8;
	public static final int ICE_BOLT                   =  9;
	public static final int ICE_BOLT_SEEKING           = 10;
	public static final int LARGE_ICE_BOLT             = 11;
	public static final int LARGE_ICE_BOLT_SEEKING     = 12;
	public static final int LIGHTNING                  = 13;
	public static final int THUNDERBOLT                = 14;
	public static final int INSTANT_LIGHTNING          = 15;
	public static final int LIGHTNING_BEAM             = 16;
	public static final int FIRE_BREATH                = 17;
	public static final int FIRE_WALL                  = 18;
	public static final int FIRE_WAVE                  = 19;
	public static final int FIRE_BALL                  = 20;
	public static final int ICE_BREATH                 = 21;
	public static final int ICE_NOVA                   = 22;
	public static final int FROST_WAVE                 = 23;
	public static final int ICE_BALL                   = 24;
	public static final int KUGELBLITZ                 = 25;
	public static final int CHAIN_LIGHTNING_1          = 26;
	public static final int GROUND_LIGHTNING           = 27;
	public static final int CHAIN_LIGHTNING_2          = 28;
	public static final int FEVER_BREATH               = 29;
	public static final int ELEMENT_BALL               = 30;
	public static final int LARGE_ELEMENT_BALL         = 31;
	public static final int ELEMENT_STORM              = 32;
	public static final int FREEZE_KUGELBLITZ          = 33;
	public static final int SHOCKFROST                 = 34;
	public static final int FROST_STORM                = 35;
	public static final int CHAIN_FREEZERAY            = 36;
	public static final int FIRE_KUGELBLITZ            = 37;
	public static final int SHOCKWAVE                  = 38;
	public static final int ELECTRO_MAGNETIC_FIREBALL  = 39;
	public static final int ELECTRO_MAGNETIC_FIRESTORM = 40;

	public static String[]  displayText;
	public static float[]   speed;
	public static float[]   castTime;
	public static int[]     souPrepare;
	public static int[]     souCast;
	public static int[]     souLife;
	public static int[]     iconImg;
	public static int[]     lifeTime;
	public static int[]     factLifeTime;
	public static boolean[] passthrough;

	public static int[][]      manaCost;
	public static StatsObj[][] reqs;
	public static StatsObj[][] stats;

	public static Class<? extends AbstractSpell>[] spellClasses;

	//----------

	public static void loadMedia() {
    Object[][] result = RpgData.db.execQuery("SELECT * FROM spell ORDER BY id ASC");
    execInitArrays(result.length);

    for (int i=0; i<result.length; i++) {
      execLoadRow((Integer)(result[i][0]), result[i]);
    }
  }

  public static void execInitArrays(int rowCount) {
    numSpells = rowCount;

    displayText = new String  [rowCount];
    speed       = new float   [rowCount];
    castTime    = new float   [rowCount];
    souPrepare  = new int     [rowCount];
    souCast     = new int     [rowCount];
    souLife     = new int     [rowCount];
    iconImg     = new int     [rowCount];
    lifeTime    = new int     [rowCount];
    factLifeTime= new int     [rowCount];
    passthrough = new boolean [rowCount];

    reqs        = new StatsObj[rowCount][];
    stats       = new StatsObj[rowCount][];
    manaCost    = new int     [rowCount][];

    spellClasses = new Class[rowCount];
    spellClasses[EMPTY] = SpellNone.class;
    spellClasses[MELEE] = SpellMelee.class;
    spellClasses[ARROW] = SpellArrow.class;
    spellClasses[IDENTIFY] = SpellIdentify.class;
    spellClasses[HEALING] = SpellHealing.class;
    spellClasses[FIRE_BOLT] = SpellFireBolt.class;
//    spellClasses[FIRE_BOLT_SEEKING] = .class;
//    spellClasses[LARGE_FIRE_BOLT] = .class;
//    spellClasses[LARGE_FIRE_BOLT_SEEKING] = .class;
//    spellClasses[ICE_BOLT] = .class;
//    spellClasses[ICE_BOLT_SEEKING] = .class;
//    spellClasses[LARGE_ICE_BOLT] = .class;
//    spellClasses[LARGE_ICE_BOLT_SEEKING] = .class;
//    spellClasses[LIGHTNING] = .class;
//    spellClasses[THUNDERBOLT] = .class;
//    spellClasses[INSTANT_LIGHTNING] = .class;
//    spellClasses[LIGHTNING_BEAM] = .class;
//    spellClasses[FIRE_BREATH] = .class;
    spellClasses[FIRE_WALL] = SpellFireWall.class;
//    spellClasses[FIRE_WAVE] = .class;
//    spellClasses[FIRE_BALL] = .class;
//    spellClasses[ICE_BREATH] = .class;
//    spellClasses[ICE_NOVA] = .class;
//    spellClasses[FROST_WAVE] = .class;
//    spellClasses[ICE_BALL] = .class;
    spellClasses[KUGELBLITZ] = SpellKugelBlitz.class;
//    spellClasses[CHAIN_LIGHTNING_1] = .class;
//    spellClasses[GROUND_LIGHTNING] = .class;
//    spellClasses[CHAIN_LIGHTNING_2] = .class;
//    spellClasses[FEVER_BREATH] = .class;
//    spellClasses[ELEMENT_BALL] = .class;
//    spellClasses[LARGE_ELEMENT_BALL] = .class;
//    spellClasses[ELEMENT_STORM] = .class;
//    spellClasses[FREEZE_KUGELBLITZ] = .class;
//    spellClasses[SHOCKFROST] = .class;
//    spellClasses[FROST_STORM] = .class;
//    spellClasses[CHAIN_FREEZERAY] = .class;
//    spellClasses[FIRE_KUGELBLITZ] = .class;
//    spellClasses[SHOCKWAVE] = .class;
//    spellClasses[ELECTRO_MAGNETIC_FIREBALL] = .class;
//    spellClasses[ELECTRO_MAGNETIC_FIRESTORM] = .class;


  }

  //----------

  public static AbstractSpell createSpell( CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, RpgMapObj newTarget ) {
    AbstractSpell s=null;
    try {
      s = spellClasses[newSType].
          getDeclaredConstructor(CombatMapObj.class, int.class, int.class, Vector.class, RpgMapObj.class).
          newInstance( newOwnerMO, newSType, newSLvl, newTarPos, newTarget );

    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }

    return s;
  }

  //----------

  public static void execLoadRow(int rowNum, Object[] row) {
    displayText [rowNum] = (String) row[1];
    speed       [rowNum] = new Float((Double)row[2]);
    castTime    [rowNum] = new Float((Double)row[3]);
    souPrepare  [rowNum] = (Integer)row[4];
    souCast     [rowNum] = (Integer)row[5];
    souLife     [rowNum] = (Integer)row[6];
    iconImg     [rowNum] = (Integer)row[7];
    lifeTime    [rowNum] = (Integer)row[8];
    factLifeTime[rowNum] = (Integer)row[9];
    passthrough [rowNum] = (Integer)row[10]!=null && (Integer)row[10]==1;

    reqs       [rowNum] = new StatsObj[MAX_SPELL_LVL];
    stats      [rowNum] = new StatsObj[MAX_SPELL_LVL];
    manaCost   [rowNum] = new int[MAX_SPELL_LVL];

    for( int j = 0; j <= MAX_SPELL_LVL - 1; j++ ) {
      manaCost[rowNum][j] = (int) (new Float((Integer)row[11]) + j*new Float((Double)row[12]));
      reqs [rowNum][j] = new StatsObj();
        reqs[rowNum][j].addOrCreateStat( StatMagic.class, (Integer)row[13] +j*(Integer)row[14] );
      stats[rowNum][j] = new StatsObj();
        stats[rowNum][j].addOrCreateStat(StatNormalDamage.class, new Vector(
            new Float((Double)row[15]) + j*new Float((Double)row[16]),
            new Float((Double)row[17]) + j*new Float((Double)row[18]) )
            );
        stats[rowNum][j].addOrCreateStat(StatFireDamage.class, new Vector(
            new Float((Double)row[19]) + j*new Float((Double)row[20]),
            new Float((Double)row[21]) + j*new Float((Double)row[22]) )
            );
        stats[rowNum][j].addOrCreateStat(StatLightningDamage.class, new Vector(
            new Float((Double)row[23]) + j*new Float((Double)row[24]),
            new Float((Double)row[25]) + j*new Float((Double)row[26]) )
            );
        stats[rowNum][j].addOrCreateStat(StatIceDamage.class, new Vector(
            new Float((Double)row[27]) + j*new Float((Double)row[28]),
            new Float((Double)row[29]) + j*new Float((Double)row[30]) )
            );
    }
  }

  public static Vector getTotalDmg( AbstractChar forChar, int newSpellID, int newSpellLvl ) {
    if( newSpellID==MELEE || newSpellID==ARROW ) {
      return forChar.stats.getTotalDamage();
    } else {
      if (newSpellID==EMPTY || newSpellLvl==0) return new Vector();
      return stats[newSpellID][newSpellLvl-1].getTotalDamage();
    }
  }

}
