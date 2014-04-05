package com.philon.engine.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Util {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(value=ElementType.TYPE)
  public  @interface Order {
    int value();
  }

  public static int getOrderForClass(Class<?> clazz) {
    Iterator<Class<?>> descIter = getClassHierarchy(clazz, Object.class).descendingIterator();
    while(descIter.hasNext()) {
      Order a = descIter.next().getAnnotation(Order.class);
      if(a!=null) return a.value();
    }
    return -1;
  }

  public static class OrderComparator implements Comparator<Class<?>> {
    public int compare(Class<?> o1, Class<?> o2) {
      int order1 = getOrderForClass(o1);
      int order2 = getOrderForClass(o2);
      return ((Integer)order1).compareTo(order2);
    }
  }

	public static int round( float f ) {
		return (int) Math.floor(f+0.5f);
	}

	//----------

	public static int[][] mirrorMatrix( int[][] matrix, Vector mirrorAxes ) {
		Vector newSize = new Vector(matrix[0].length, matrix.length);
		int tmpX;
		int tmpY;

		int result[][] = new int[(int) newSize.y][];
		for( int y = 0; y <= newSize.y-1; y++ ) {
			result[y] = new int[(int) newSize.x];
		}

		for( int y = 0; y <= newSize.y-1; y++ ) {
			for( int x = 0; x <= newSize.x-1; x++ ) {
				tmpX=x;
				tmpY=y;
				if(mirrorAxes.y==1) tmpX=(int) (newSize.x-1 - x);
				if(mirrorAxes.x==1) tmpY=(int) (newSize.y-1 - y);
				result[y][x] = matrix[tmpY][tmpX];
			}
		}

		return result;
	}

	//----------

	public static void printMatrix( int matrix[][] ) {
		String line;
		for( int y = 0; y <= matrix.length-1; y++ ) {
			line = "[";
			for( int x = 0; x <= matrix[0].length-1; x++ ) {
				if (x>0) line += ", ";
				line += matrix[y][x];
			}
			System.out.println(line + "]");
		}
	}

	//----------

	//untested
	public static Vector isRectLineColliding( Vector newRectPos, Vector newRectSize, Vector newLineP1, Vector newLineP2 ) {
		Vector tmp1;
		Vector tmp2;
		Vector collPoint;

		if( newLineP1.x<newLineP2.x ) {
			tmp1=newRectPos.copy();
			tmp2=tmp1.copy();
			tmp2.y += newRectSize.y;

			collPoint = isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
			if (collPoint!=null) return collPoint;

			if( newLineP1.y<newLineP2.y ) {
				tmp1=newRectPos.copy();
				tmp2=tmp1.copy();
				tmp2.x += newRectSize.x;

				collPoint=isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
				if (collPoint!=null) return collPoint;
			} else {
				tmp1=newRectPos.copy();
				tmp1.y += newRectSize.y;
				tmp2=tmp1.copy();
				tmp2.x += newRectSize.x;

				collPoint=isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
				if (collPoint!=null) return collPoint;
			}
		} else {
			tmp1=newRectPos.copy();
			tmp1.x += newRectSize.x;
			tmp2=tmp1.copy();
			tmp2.y += newRectSize.y;

			collPoint=isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
			if (collPoint!=null) return collPoint;

			if( newLineP1.y<newLineP2.y ) {
				tmp1=newRectPos.copy();
				tmp2=tmp1.copy();
				tmp2.x += newRectSize.x;

				collPoint=isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
				if (collPoint!=null) return collPoint;
			} else {
				tmp1=newRectPos.copy();
				tmp1.y += newRectSize.y;
				tmp2=tmp1.copy();
				tmp2.x += newRectSize.x;

				collPoint=isLineLineColliding(newLineP1, newLineP2, tmp1, tmp2 );
				if (collPoint!=null) return collPoint;
			}
		}
		return null;
	}

	//----------

	public static Vector rectsColliding( Vector pos1, Vector size1, Vector pos2, Vector size2 ) {
		Vector deltaPos = Vector.sub( pos2, pos1 );
		Vector penDepth = Vector.add( Vector.mulScalar(size1, 0.5f), Vector.mulScalar(size2, 0.5f) ).subInst( Vector.absolute(deltaPos) );

		if (penDepth.isAllLOE(new Vector())) { //collision!
			//project in x
			if (deltaPos.x < 0) { //project To the Left
				penDepth.x *= -1;
			}
			//project in y
			if (deltaPos.y < 0) { //project up
				penDepth.y *= -1;
			}
			return penDepth;

			/**
			 * If penDepth.x < penDepth.y Then
				 * 'project in x
				 * If deltaPos.x < 0 Then
					 * 'project To the Left
					 * penDepth.x :* -1
					 * penDepth.y = 0
				 * Else
					 * 'proj To Right
					 * penDepth.y = 0
				 * EndIf
			 * Else
				 * 'project in y
				 * If deltaPos.y < 0 Then
					 * 'project up
					 * penDepth.x = 0
					 * penDepth.y :* -1
				 * Else
					 * 'project down
					 * penDepth.x = 0
				 * EndIf
			 * EndIf
			 * Return penDepth
			 */

		}

		return null;
	}

	//----------

	//untested
	public static Vector isLineLineColliding( Vector a1, Vector a2, Vector b1, Vector b2 ) {
		    Vector b = Vector.sub(a2, a1);
		    Vector d = Vector.sub(b2, b1);
		    float bDotDPerp = b.x * d.y - b.y * d.x;

			//if b dot d == 0, it means the lines are parallel so have infinite intersection points
		    if (bDotDPerp==0) return null;

		    Vector c = Vector.sub(b1, a1);
		    float t = (c.x * d.y - c.y * d.x) / bDotDPerp;
		    if (t < 0 || t > 1) return null;

		    float u = (c.x * b.y - c.y * b.x) / bDotDPerp;
		    if (u < 0 || u > 1) return null;

		    Vector intersection = Vector.add(a1, b.mulScalarInst(t));
		    return intersection;
	}

	//----------

	public static float random(float minValue, float maxValue) {
		return (float) (minValue + Math.random()*(maxValue-minValue));
	}

	public static int rand(int minValue, int maxValue) {
	  return (int) Math.floor(random(minValue, maxValue) + 0.5f);
	}

	//----------

	public static String getSignedPercentageString( float fromValue ) {
    String sign = "";
    if( Math.signum(fromValue) == 1 ) {
      sign = "+";
    }
    return sign + (int)(fromValue*100) + "%";
  }

	public static String getSignedIntString(int fromValue) {
	  String sign = "";
    if( Math.signum(fromValue) == 1 ) {
      sign = "+";
    }
    return sign + fromValue;
	}

	public static String getSignedFloatString(float fromValue) {
	  String sign = "";
    if( Math.signum(fromValue) == 1 ) {
      sign = "+";
    }
    StringBuilder valueString = new StringBuilder(String.valueOf((int)(fromValue*100)));
    valueString.insert(valueString.length()-1-2, ',');
    return sign + valueString;
  }

	/**
	 * Be careful: Make sure newClass has only ONE constructor. In either case, the first constructor is called.
	 */
	@SuppressWarnings("unchecked")
  public static <T> T instantiateClass(Class<T> newClass, Object... args) {
	  try {
      return (T)newClass.getConstructors()[0].newInstance(args);
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
    return null;
	}

	/**
	 * Be careful: This will fail horribly if newClass doesn't have a default contructor...
	 */
	public static <T> T instantiateClass(Class<T> newClass) {
	  return instantiateClass(newClass, new Object[]{});
	}

	/**
	 * Get Hierarchy for any class. The order of elements is from top to bottom; classes closer to Object will come first. If you don't want the full hierarchy down to Object, pass a boundClass.
	 * @param forClass Topmost class to include, usually getClass()
	 * @param boundClass If set, boundClass and all superclasses of boundClass will be ignored.
	 * @return List of superclasses of forClass
	 */
  public static <T> LinkedList<Class<? extends T>> getClassHierarchy(Class<? extends T> forClass, Class<T> boundClass) {
    LinkedList<Class<? extends T>> result = new LinkedList<Class<? extends T>>();

    Class<?> currClass = forClass;
    do {
      if(currClass==boundClass) break;
      Class<? extends T> tmpClass = currClass.asSubclass(boundClass);
      result.addFirst(tmpClass);
      currClass = currClass.getSuperclass();
    } while(currClass!=null);

    return result;
  }

  public static <MEMBER> LinkedList<Class<? extends MEMBER>> getInnerClassesOfType( Class<?> containerClass, Class<MEMBER> memberClass ) {
    LinkedList<Class<? extends MEMBER>> result = new LinkedList<Class<? extends MEMBER>>();

    for (Class<?> newClass : containerClass.getDeclaredClasses()) {
      if(newClass.isInterface() || Modifier.isAbstract(newClass.getModifiers())) continue;
      Class<? extends MEMBER> tmpMemberClass = newClass.asSubclass(memberClass);
      result.addLast( tmpMemberClass );
    }

    return result;
  }

  /**
   * Get all superclasses of memberClass that appear as inner classes within the class hierarchy from hierarchyThreshold (exclusive) to containerClass (inclusive).
   * Ignores abstract classes.
   * @param hierarchyThreshold - usually the class you call this with; the "bottom most" class in the hierarchy you wish to EXCLUDE from the search
   * @param containerClass - usually getClass(), returning the "upper most" class in the hierarchy. Search will end here (inclusive).
   * @param memberClass - the type of inner class you want to find.
   * @param args - the arguments passed to the instantiation function
   * @return HashMap< class of member, instance of member >
   */
  public static <CONTAINER, MEMBER> LinkedHashMap<Class<? extends MEMBER>, MEMBER> createInnerClasses(
      Class<CONTAINER> hierarchyThreshold,
      Class<? extends CONTAINER> containerClass,
      Class<MEMBER> memberClass,
      Object[] args
  ) {
    LinkedHashMap<Class<? extends MEMBER>, MEMBER> result
    = new LinkedHashMap<Class<? extends MEMBER>, MEMBER>();

    for (Class<? extends CONTAINER> currClass : getClassHierarchy(containerClass, hierarchyThreshold) ) {
      for(Class<? extends MEMBER> currMemberClass : getInnerClassesOfType(currClass, memberClass)) {
        MEMBER newMember = instantiateClass(currMemberClass, args);
        for(Class<? extends MEMBER> currMemberHierarchyClass : Util.getClassHierarchy(currMemberClass, memberClass)) {
          result.put( currMemberHierarchyClass, newMember );
        }
      }
    }

    return result;
  }

}






