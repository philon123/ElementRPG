package com.philon.rpg.mo;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.philon.engine.util.Path;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.state.AbstractMapObjState;
import com.philon.rpg.util.GameUtil;

public abstract class UpdateMapObj extends AbstractMapObj {
	public Class<? extends AbstractMapObjState> currState = null;
	public Class<? extends AbstractMapObjState> defaultState;
	public LinkedHashMap<Class<? extends AbstractMapObjState>, AbstractMapObjState> states 
	 = new LinkedHashMap<Class<? extends AbstractMapObjState>, AbstractMapObjState>();

	public float v=0;
	public float tilesPerSecond=0;
	
	public Selectable currTarget;
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
		defaultState = getDefaultState();
		changeState(defaultState);
		
		if (getIsAutoInsert()) RpgGame.inst.dynamicMapObjs.addLast(this);
	}
	
	public abstract float getTilesPerSecond();
	public abstract int getImgIdle();
  public abstract int getImgMoving();
  public abstract int getImgDying();
  public abstract int getSouDie();
  
  public Class<? extends AbstractMapObjState> getDefaultState() {
    return StateIdle.class;
  }
  
  public boolean getIsAutoInsert() {
    return true;
  }
  
  public int getDieCooldown() {
    return (int) (RpgGame.fps/3);
  }
	
	public void loadStates() {
	  LinkedList<Class<?>> hierarchy = new LinkedList<Class<?>>();
    Class<?> currClass = getClass();
    do {
      hierarchy.addFirst(currClass);
      currClass = currClass.getSuperclass();
    } while(currClass!=AbstractMapObj.class);
    
    for (Class<?> currentClass : hierarchy) {
      for (Class<?> tmpClass : currentClass.getDeclaredClasses()) {
        if (AbstractMapObjState.class.isAssignableFrom(tmpClass)) {
          addState(tmpClass.asSubclass(AbstractMapObjState.class));
        }
      }
    }
	}
	
	public void addState(Class<? extends AbstractMapObjState> newStateClass) {
	  addStateInternal(newStateClass, newStateClass);
	}
	
	public void addStateInternal(Class<? extends AbstractMapObjState> newKeyClass, Class<? extends AbstractMapObjState> newStateClass) {
	  try {
      AbstractMapObjState newState = (AbstractMapObjState) newStateClass.getConstructors()[0].newInstance(this);
      states.put(newKeyClass, newState);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
	}
	
	public void replaceState( Class<? extends AbstractMapObjState> oldStateClass, Class<? extends AbstractMapObjState> newStateClass ) {
	  addStateInternal(oldStateClass, newStateClass);
	}

	public void changeState( Class<? extends AbstractMapObjState> newState ) {
	  if (newState==currState) return;
	  states.get(newState).execOnChange();
	  currState = newState;
	}
	
	public void deleteObject() {
		RpgGame.inst.dynamicMapObjs.remove(this);

		super.deleteObject();
	}

	public void deathTrigger(CombatMapObj killedBy) {
		RpgGame.playSoundFX( getSouDie() );
	}

	public void update() {
		//animation
		if (currAnimStart>-1) updateAnimFrame();

		//cooldowns
		updateCooldowns();

		//target
		updateTarget();
	  
		//state update callback
		if (currState == null) new IllegalArgumentException("currState is not initialized at obj: " + getClass().getSimpleName());
		if( !states.get(currState).execUpdate() ) { //update failed, revert to default state
		  if(!(StateDying.class.isAssignableFrom(currState))) {
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
	
	public void interact( Selectable newTargetGO ) {
    newTargetGO.interactTrigger(this);
  }

	public Vector getNewPositionOffset( Vector targetOffset ) {
		Vector result = targetOffset.copy();

		LinkedList<AbstractMapObj> potentialColls = getPotentialCollisions( result );
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

	//----------

	public boolean changePosition( Vector newOffset ) {
		if(        newOffset.x==0 && lastOffset.x==0 && Math.signum(newOffset.y) == -Math.signum(lastOffset.y) ) {
			return false; //180 degree turns forbidden
		} else if( newOffset.y==0 && lastOffset.y==0 && Math.signum(newOffset.x) == -Math.signum(lastOffset.x) ) {
			return false; //180 degree turns forbidden
		}

	  lastOffset=newOffset.copy();
	  dir = GameUtil.getDir( newOffset );
		setPosition(Vector.add(pos, newOffset));

		return true;
	}
	
	//----------

	public LinkedList<AbstractMapObj> getPotentialCollisions( Vector newOffset ) {
		LinkedList<AbstractMapObj> result = RpgGame.inst.gMap.getRectColls(Vector.add(pos, newOffset), collRect);
		result =  AbstractMapObj.filterList( result, true, true, false, false, true, true );
		if (result==null) return null;
		result.remove(this);
		if (result.isEmpty()) return null;
		
		return result;
	}

	//----------
	
	public void setTarget( Selectable newTarget, Vector newTargetPos ) {
		currTarget = newTarget;
		currTargetPos = newTargetPos;
		if( currTargetPos==null && currTarget!=null ) {
			currTargetPos = ((AbstractMapObj)currTarget).pos.copy();
		}
		turnToTarget( currTargetPos );
		updateTarget();
	}

	//----------
	
	public void setTarget( Selectable newTarget) {
		setTarget(newTarget, null);
	}

	//####################################
	
	public class StateIdle extends AbstractMapObjState {
	  
	  @Override
	  public void execOnChange() {
	    setImage( getImgIdle() );
	    v=0;
	    startAnim( RpgGame.fps/3 );
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
	    setImage( getImgDying() );
	    v=0;
	    dieCooldown = getDieCooldown();
	    startAnim( getDieCooldown() );

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
	    setImage( getImgMoving() );
	    startAnim( RpgGame.fps/3 );
	  }
	  
	  @Override
	  public boolean execUpdate() {
	    v = tilesPerSecond / RpgGame.fps;
	    Vector newOffset = Vector.mulScalar(direction, v);
	    newOffset = getNewPositionOffset(newOffset);
	    if (newOffset==null) return false;

	    return changePosition( newOffset );
	  }
	  
	}
	
	//----------
	
	public class StateMovingTarget extends AbstractMapObjState {
    
    @Override
    public void execOnChange() {
      states.get(UpdateMapObj.StateMovingStraight.class).execOnChange();
    }
    
    @Override
    public boolean execUpdate() {
      float tmpDist = Vector.getDistance(pos, currTargetPos);
      float maxDist = collRect.getLength() + 0.2f;
      if (tmpDist<maxDist) return false;
      direction = Vector.sub(currTargetPos, pos).normalizeInst();
      return states.get(UpdateMapObj.StateMovingStraight.class).execUpdate();
    }
    
  }
  
  //----------
	
	public class StateMovingSmart extends AbstractMapObjState {
	  
	  @Override
	  public void execOnChange() {
	    states.get(UpdateMapObj.StateMovingStraight.class).execOnChange();
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
	    if (!states.get(UpdateMapObj.StateMovingStraight.class).execUpdate()) {
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
