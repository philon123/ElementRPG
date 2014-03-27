package com.philon.rpg.map.mo;

import java.util.HashMap;
import java.util.LinkedList;

import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Path;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.AbstractMapObjState;
import com.philon.rpg.util.RpgUtil;

public abstract class UpdateMapObj extends RpgMapObj {
  HashMap<Class<? extends AbstractMapObjState>, Class<? extends AbstractMapObjState>> stateMap =
      new HashMap<Class<? extends AbstractMapObjState>, Class<? extends AbstractMapObjState>>();

	public AbstractMapObjState currState = null;

	public float v=0;
	public float tilesPerSecond=0;
	public Vector lastOffset=new Vector();

	public RpgMapObj currTarget;
	public Vector currTargetPos;
	public Vector currTargetDelta;
	public float currTargetDist;
	public Path currPath;
	public int currPathNode;

	public int dieCooldown;
	public int pathfindCooldown;

	public CombatMapObj killedBy=null;

	public UpdateMapObj() {
	  super();

		loadStates();

		tilesPerSecond = getTilesPerSecond();
		changeState(getDefaultState());
	}

	public abstract float getTilesPerSecond();
	public abstract int getImgIdle();
  public abstract int getImgMoving();
  public abstract int getImgDying();
  public abstract int getSouDie();

  public Class<? extends AbstractMapObjState> getDefaultState() {
    return StateIdle.class;
  }

  public int getDieCooldown() {
    return (int) (PhilonGame.fps/3);
  }

  @SuppressWarnings("unchecked")
  public void loadStates() {
    for (Class<? extends RpgMapObj> currentClass : getClassHierarchy(getClass(), RpgMapObj.class) ) {
      for (Class<?> newClass : currentClass.getDeclaredClasses()) {
        if ( !AbstractMapObjState.class.isAssignableFrom(newClass) ) continue;
        for(Class<? extends AbstractMapObjState> currClass : getClassHierarchy(newClass, AbstractMapObjState.class)) {
          stateMap.put(currClass, (Class<? extends AbstractMapObjState>)newClass);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> LinkedList<Class<? extends T>> getClassHierarchy(Class<?> forClass, Class<T> boundClass) {
    LinkedList<Class<? extends T>> result = new LinkedList<Class<? extends T>>();

    Class<?> currClass = forClass;
    do {
      if(boundClass.isAssignableFrom(currClass) && currClass!=boundClass) result.addFirst((Class<? extends T>)currClass);
      currClass = currClass.getSuperclass();
    } while(currClass!=null);

    return result;
  }

	public void changeState( Class<? extends AbstractMapObjState> newStateClass ) {
	  AbstractMapObjState newState = createState(newStateClass);

	  if (currState!=null && newStateClass==currState.getClass()) return;
	  newState.execOnChange();
	  currState = newState;
	}

	@SuppressWarnings("unchecked")
  public <T> T createState(Class<? extends T> newStateClass) {
	  return (T) Util.instantiateClass(stateMap.get(newStateClass), this);
	}

	public void deathTrigger(CombatMapObj killedBy) {
		RpgGame.playSoundFX( getSouDie() );
	}

	public void update() {
		//cooldowns
		updateCooldowns();

		//target
		updateTarget();

		//state update callback
		if (currState == null) new IllegalArgumentException("currState is not initialized at obj: " + getClass().getSimpleName());
		if( !currState.execUpdate() ) { //update failed, revert to default state
		  if( !(currState instanceof StateDying) ) {
		    changeState(StateIdle.class);
		  }
		}
	}

	//----------

	public void updateTarget() {
		if( currTargetPos!=null ) {
			currTargetDelta = Vector.sub( currTargetPos, pos );
			currTargetDist = currTargetDelta.getLength();
		}
	}

	//----------

	public void updateCooldowns() {
		//pathfind cooldown
		if( pathfindCooldown>0 ) {
			pathfindCooldown -= 1;
		}
	}

	public void interact( RpgMapObj newTargetGO ) {
    newTargetGO.interactTrigger(this);
  }

  public boolean changePosition( Vector newOffset ) {
    if(        newOffset.x==0 && lastOffset.x==0 && Math.signum(newOffset.y) == -Math.signum(lastOffset.y) ) {
      return false; //180 degree turns forbidden
    } else if( newOffset.y==0 && lastOffset.y==0 && Math.signum(newOffset.x) == -Math.signum(lastOffset.x) ) {
      return false; //180 degree turns forbidden
    }

    lastOffset=newOffset.copy();
    turnToDirection(newOffset);
    setPosition(Vector.add(pos, newOffset));

    return true;
  }

	public Vector getNewPositionOffset( Vector targetOffset ) {
		Vector result = targetOffset.copy();

		LinkedList<RpgMapObj> potentialColls = getPotentialCollisions( result );
		if (potentialColls!=null) { //collision occured!
			//determine main movement axis and attempt to move in that direction instead
			Vector absDir = Vector.absolute(direction);
			Vector unitDirection = new Vector(Math.signum(direction.x)*v, Math.signum(direction.y)*v);

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
		LinkedList<RpgMapObj> result = RpgGame.inst.gMap.getRectColls(Vector.add(pos, newOffset), collRect);
		result =  RpgUtil.filterList( result, true, true, false, false, true, true );
		if (result==null) return null;
		result.remove(this);
		if (result.isEmpty()) return null;

		return result;
	}

	//----------

	public void setTarget( RpgMapObj newTarget, Vector newTargetPos ) {
		currTarget = newTarget;
		currTargetPos = newTargetPos;
		if( currTargetPos==null && currTarget!=null ) {
			currTargetPos = ((RpgMapObj)currTarget).pos.copy();
		}
		turnToTarget( currTargetPos );
		updateTarget();
	}

	//----------

	public void setTarget( RpgMapObj newTarget) {
		setTarget(newTarget, null);
	}

	//####################################

	public class StateIdle extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getImgIdle()], (int)(PhilonGame.fps/3), false));
	    v=0;
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

	//----------

	public class StateDying extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getImgDying()], getDieCooldown(), false));
	    dieCooldown = getDieCooldown();
	    v=0;
	    deathTrigger(killedBy);
	  }

	  @Override
	  public boolean execUpdate() {
      if (dieCooldown==0) {
        deleteObject();
      } else {
        dieCooldown--;
      }
	    return true;
	  }

	}

	//----------

	public class StateMovingStraight extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getImgMoving()], (int)(PhilonGame.fps/3), false));
	  }

	  @Override
	  public boolean execUpdate() {
	    v = tilesPerSecond / PhilonGame.fps;
	    Vector newOffset = Vector.mulScalar(direction, v);
	    newOffset = getNewPositionOffset(newOffset);
	    if (newOffset==null) return false;

	    return changePosition( newOffset );
	  }

	}

	//----------

	public class StateMovingTarget extends AbstractMapObjState {
	  StateMovingStraight m_movingStraight;

    protected StateMovingStraight getStateMovingStraight() {
      return m_movingStraight!=null ? m_movingStraight : createState(StateMovingStraight.class);
    }

    @Override
    public void execOnChange() {
      getStateMovingStraight().execOnChange();
    }

    @Override
    public boolean execUpdate() {
      float tmpDist = Vector.getDistance(pos, currTargetPos);
      float maxDist = collRect.getLength() + 0.2f;
      if (tmpDist<maxDist) return false;
      direction = Vector.sub(currTargetPos, pos).normalizeInst();

      return getStateMovingStraight().execUpdate();
    }

  }

  //----------

	public class StateMovingSmart extends AbstractMapObjState {
    StateMovingStraight m_movingStraight;

    protected StateMovingStraight getStateMovingStraight() {
      return m_movingStraight!=null ? m_movingStraight : createState(StateMovingStraight.class);
    }

	  @Override
	  public void execOnChange() {
	    getStateMovingStraight().execOnChange();
	    pathfindCooldown=0;
	  }

	  @Override
	  public boolean execUpdate() {
	  //get/update path
	    if( (currPath==null && pathfindCooldown==0) || pathfindCooldown==0 ) {
	      currPath = RpgGame.inst.gMap.getAStarPath( pos, currTargetPos );
	      currPathNode=0;
	      pathfindCooldown=60;
	    }
	    if( currPath==null ) {
	      return false; //failed to find path
	    }

	    //move along path
	    Vector nextNodeDelta = currPath.nodes[currPathNode].pos.copy().subInst(pos);

	    direction = nextNodeDelta.copy().normalizeInst();
	    if (!getStateMovingStraight().execUpdate()) {
	      return false; //obstacle on the way
	    }

	    if (nextNodeDelta.getLength() < 0.1) { //reached next node
	      if( currPathNode==currPath.nodes.length-1 ) {
	        currPath=null;
	        pathfindCooldown=0;
	        return false; //reached last pathNode
	      } else { //go to next pathNode
	        currPathNode += 1;
	      }
	    }

	    return true;
	  }

	}

	//----------


}
