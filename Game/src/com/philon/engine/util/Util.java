package com.philon.engine.util;

public class Util {

	//------------------------------

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

}






