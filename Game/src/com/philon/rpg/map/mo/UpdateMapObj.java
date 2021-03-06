package com.philon.rpg.map.mo;

import java.util.HashMap;
import java.util.LinkedList;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.MapObjState;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.util.RpgUtil;

public abstract class UpdateMapObj extends RpgMapObj {
  @SuppressWarnings("rawtypes")
  HashMap<Class<? extends MapObjState>, Class<? extends MapObjState>> stateMap =
      new HashMap<Class<? extends MapObjState>, Class<? extends MapObjState>>();

	public MapObjState<?> currState = null;
	public AIState currAIState = null;

	public UpdateMapObj() {
	  super();
		loadStates();

		changeState(StateIdle.class, new StateParam());
	}

	public abstract float getTilesPerSecond();
	public abstract int getImgIdle();
  public abstract int getImgMoving();
  public abstract int getImgDying();
  public abstract int getSouDie();

  public AIState getAI() {
    if(currAIState==null) currAIState = getDefaultAI();
    return currAIState;
  }

  protected AIState getDefaultAI() {
    return new DefaultAI();
  }

  public float getDieCooldown() {
    return 1/3f;
  }

  @SuppressWarnings("rawtypes")
  public void loadStates() {
    for (Class<? extends MapObjState> currStateClass : Util.getInnerClassesOfType(MapObjState.class, getClass(), RpgMapObj.class)) {
      for(Class<? extends MapObjState> currStateSuperClass : Util.getClassHierarchy(currStateClass, MapObjState.class)) {
        stateMap.put(currStateSuperClass, currStateClass);
      }
    }
  }

	@SuppressWarnings({ "unchecked", "rawtypes" })
  public void changeState(Class<? extends MapObjState<?>> newStateClass, StateParam param) {
	  Class<? extends MapObjState> instClass = stateMap.get(newStateClass);
	  if(currState==null || currState.isStateChangeAllowed((Class<? extends MapObjState<?>>) instClass)) {
      MapObjState<?> newState = Util.instantiateClass(instClass, this, param);
  	  newState.execOnChange();
  	  currState = newState;
	  }
	}

	public void deathTrigger(CombatMapObj killedBy) {
		RpgGame.inst.playSoundFX( getSouDie() );
	}

	/**
	 * @returns false if object is to be deleted
	 */
	public boolean update(float deltaTime) {
	  getAI().update(deltaTime);

		if(!currState.execUpdate(deltaTime)) { //update failed, revert to default state
		  if(currState instanceof StateDying) return false;
		  currState = null;
		  changeState(StateIdle.class, new StateParam());
		}
		return true;
	}

	public void interact( RpgMapObj newTargetGO ) {
    newTargetGO.interactTrigger(this);
  }

	protected boolean changePosition(Vector newOffset) {
    if(!newOffset.equals(new Vector())) {
      turnToDirection(newOffset);
      setPosition(Vector.add(pos, newOffset));
      RpgUtil.cleanMapObj(this);
    }

    return true;
  }

	protected Vector getNewPositionOffset(Vector targetOffset) {
		Vector result = targetOffset.copy();

		LinkedList<RpgMapObj> potentialColls = getPotentialCollisions( result );
		if (potentialColls!=null) { //collision occured!
			//determine main movement axis and attempt to move in that direction instead
			Vector absDir = Vector.absolute(orientation);
			float newOffsetLength = targetOffset.getLength();
			Vector unitDirection = new Vector(Math.signum(orientation.x), Math.signum(orientation.y)).mulScalarInst(newOffsetLength);

			boolean xIsLarger=false;
			boolean yIsLarger=false;
			if( absDir.x > absDir.y ) {
				xIsLarger=true;
			} else if( absDir.y > absDir.x ) {
				yIsLarger=true;
			}
			if( xIsLarger || ! yIsLarger ) {
				result = new Vector(unitDirection.x, 0);
			} else if( yIsLarger ) {
				result = new Vector(0, unitDirection.y);
			}

			if( getPotentialCollisions(result)!=null ) {
				boolean isConstraintMet=false;
				if( xIsLarger ) {
					isConstraintMet = absDir.y>0 && (absDir.x/absDir.y > 0.05);
				} else if( yIsLarger ) {
					isConstraintMet = absDir.x>0 && (absDir.y/absDir.x > 0.05);
				} else { //equal
					isConstraintMet = true;
				}
				if( isConstraintMet ) {
					if( xIsLarger || ! yIsLarger ) {
						result = new Vector(0, unitDirection.y);
					} else if( yIsLarger ) {
						result = new Vector(unitDirection.x, 0);
					}
					if( getPotentialCollisions(result)!=null ) {
						return null; //obstacle in the way
					}
				} else {
					return null; //constraints not met
				}
			}
		}

		return result;
	}

	public LinkedList<RpgMapObj> getPotentialCollisions( Vector newOffset ) {
		LinkedList<RpgMapObj> result = RpgUtil.getRectColls(Vector.add(pos, newOffset), collRect);
		result =  RpgUtil.filterList( result, true, true, false, false, true, true );
		if (result==null) return null;
		result.remove(this);
		if (result.isEmpty()) return null;

		return result;
	}

	//####################################

	public class StateIdle extends MapObjState<StateParam> {
	  public StateIdle(StateParam param) {
      super(param);
    }
    @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getImgIdle()), 1/2f, false));
	  }
	  @Override
	  public boolean execUpdate(float deltaTime) {
	    return true;
	  }
	}

	public class StateDying extends MapObjState<StateDyingParam> {
    private float dieCooldown;
    private CombatMapObj killedBy;
	  public StateDying(StateDyingParam param) {
	    super(param);
	    killedBy = param.killedBy;
	  }
	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getImgDying()), getDieCooldown(), false));
	    dieCooldown = getDieCooldown();
	    deathTrigger(killedBy);
	  }
	  @Override
	  public boolean execUpdate(float deltaTime) {
      if (dieCooldown<0) {
        deleteObject();
        return false;
      } else {
        dieCooldown -= deltaTime;
      }
	    return true;
	  }
	  @Override
	  public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClazz) {
	    return false;
	  }
	}
  public static class StateDyingParam extends StateParam {
    public CombatMapObj killedBy;
    public StateDyingParam(CombatMapObj newKilledBy) {
      killedBy = newKilledBy;
    }
  }

	public class StateMovingStraight extends MapObjState<StateMovingParam> {
	  private Vector direction;
	  public StateMovingStraight(StateMovingParam param) {
      super(param);
      direction = param.direction;
    }
    @Override
	  public void execOnChange() {
      changeDirection(direction);
	    setAnimation(new FrameAnimation(Data.textures.get(getImgMoving()), 1/2f, false));
	  }
	  @Override
	  public boolean execUpdate(float deltaTime) {
	    float tilesThisFrame = getTilesPerSecond() * deltaTime;
	    Vector newOffset = Vector.mulScalar(direction, tilesThisFrame);
	    newOffset = getNewPositionOffset(newOffset);
	    if (newOffset==null) return false;

	    return changePosition(newOffset);
	  }
	  public void changeDirection(Vector newDirection) {
	    direction = newDirection.copy().normalizeInst();
	    turnToDirection(direction);
	  }
	  public void turn(float degrees) {
	    changeDirection(direction.copy().rotateDegInst(degrees));
	  }
	}
  public static class StateMovingParam extends StateParam {
    public Vector direction;
    public StateMovingParam(Vector newDirection) {
      direction = newDirection.copy();
    }
  }

  public static interface AIState {
    void update(float deltaTime);
  }

  public class DefaultAI implements AIState {
    protected float aiUpdateCooldown = Util.rnd(0, (getConfiguredAIUpdateCooldown()));
    @Override
    public void update(float deltaTime) {
      if(aiUpdateCooldown>0) {
        aiUpdateCooldown -= deltaTime;
      } else {
        updateTimed();
        aiUpdateCooldown = getConfiguredAIUpdateCooldown();
      }
    }
    protected void updateTimed() {
    }
    protected float getConfiguredAIUpdateCooldown() {
      return 0.5f;
    }
  }

  public class MoveToTargetAI extends DefaultAI {
    private Vector m_targetPos;
    public MoveToTargetAI(Vector newTargetPos) {
      setTargetPos(newTargetPos);
    }
    @Override
    public void updateTimed() {
      if(currState instanceof StateMovingStraight) {
        ((StateMovingStraight)currState).changeDirection(Vector.sub(m_targetPos, pos));
      } else {
        changeState(StateMovingStraight.class, new StateMovingParam(Vector.sub(m_targetPos, pos)));
      }
    }
    public void setTargetPos(Vector newTargetPos) {
      m_targetPos = newTargetPos.copy();
    }
  }

}
