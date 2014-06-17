package com.philon.engine.util;

import java.lang.Math;
import java.math.BigDecimal;

import org.jbox2d.common.Vec2;

public class Vector {
	public float x;
	public float y;

	//Vec2 interface
	public Vector(Vec2 vec2) {
	  this(vec2.x, vec2.y);
	}


	//----------

	public Vector( float newXValue, float newYValue ) {
		this.x = newXValue;
		this.y = newYValue;
	}

	//----------

	public Vector() {
		this(0, 0);
	}

	//----------

	public Vector(float sclar) {
		this(sclar, sclar);
	}

	//----------

	public Vector copy() {
		return new Vector( x, y );
	}

	//----------

	public float getDim( int dim ) {
		if( dim == 0 ) {
			return this.x;
		}
		return this.y;
	}

	//----------

	public void setDim( int dim, float newVal ) {
		if( dim == 0 ) {
			this.x = newVal;
			return;
		}
		this.y = newVal;
	}

	//----------

	public String toString() {
	  BigDecimal a = new BigDecimal(this.x);
	  BigDecimal b = new BigDecimal(this.y);
	  a = a.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP);
    b = b.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP);
	 	return "(" + a.toPlainString() + ", " + b.toPlainString() + ")";
	}

	public String toStringIntRounded() {
	  return "(" + Util.round(x) + ", " + Util.round(y) + ")";
	}

	public String toStringIntRange() {
	  String sx = String.valueOf((int)Math.floor(x));
	  String sy = String.valueOf((int)Math.floor(y));
	  return sx + "-" + sy;
	}

	//----------

	public String toCsv() {
	 	return this.x + "," + this.y;
	}

	//----------

	public float[] toArray() {
	 	return new float[]{ this.x, this.y };
	}

	//----------

	public int[] toIntArray() {
	 	return new int[]{ (int)this.x , (int)this.y };
	}

	//----------

	public static Vector add( Vector v1, Vector v2 ) {
		return new Vector( v1.x + v2.x, v1.y + v2.y );
	}

	//----------

	public Vector addInst( Vector otherVec ) {
		x += otherVec.x;
		y += otherVec.y;
		return this;
	}

	//----------

	public static Vector sub( Vector v1, Vector v2 ) {
		return new Vector( v1.x - v2.x, v1.y - v2.y );
	}

	//----------

	public Vector subInst( Vector otherVec ) {
		x -= otherVec.x;
		y -= otherVec.y;
		return this;
	}

	//----------

	public static Vector mul( Vector v1, Vector v2 ) {
		return new Vector( v1.x * v2.x, v1.y * v2.y );
	}

	//----------

	public static Vector mulScalar( Vector v1, float scalar ) {
		return new Vector( v1.x * scalar, v1.y * scalar );
	}

	//----------

	public Vector mulInst( Vector otherVec ) {
		x *= otherVec.x;
		y *= otherVec.y;
		return this;
	}

	//----------

	public Vector mulScalarInst( float scalar ) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	//----------

	public static Vector div( Vector v1, Vector v2 ) {
		return new Vector( v1.x / v2.x, v1.y / v2.y );
	}

	//----------

	public static Vector divScalar( Vector v1, float scalar ) {
		return new Vector( v1.x / scalar, v1.y / scalar );
	}

	//----------

	public Vector divInst( Vector otherVec ) {
		x /= otherVec.x;
		y /= otherVec.y;
		return this;
	}

	//----------

	public Vector divScalarInst( float scalar ) {
		x /= scalar;
		y /= scalar;
		return this;
	}

	//----------

	public static Vector divInt( Vector v1, Vector v2 ) {
		return new Vector( (float)((int)v1.x / (int)v2.x), (float)((int)v1.y / (int)v2.y) );
	}

	//----------

	public static float getDistance( Vector a, Vector b ) {
		Vector tmp = Vector.sub( b, a );
		tmp = Vector.mul( tmp, tmp );
		return (float) Math.sqrt( tmp.x + tmp.y );
	}

	//----------

	public float getLength() {
		return getDistance( this, new Vector() );
	}

	//----------

	public static float getRelRotation( Vector a, Vector b ) {
		return (float) ((Math.atan2( b.y-a.y , b.x-a.x ) + 360 ) % 360);
	}

	//----------

	public float getRotationDegInst() {
		return (float) ((Math.toDegrees(Math.atan2(y , x)) + 360 ) % 360);
	}

	//----------

	public Vector rotate90() {
		return new Vector(y, -x);
	}

	//----------

	public static Vector getRelScale( Vector sizeA, Vector sizeB ) {
		Vector rs = new Vector();
		int i;
		for( i = 0; i <= 1; i++ ) {
			rs.setDim( i, (float)((sizeB.getDim(i) * 1.0) / sizeA.getDim(i)) );
		}

		return rs;
	}

	//----------

	public boolean isZeroVector() {
	  return isAllEqual(new Vector());
	}

	//----------

	public boolean isAllSmaller( Vector otherVec ) {
		if (x<otherVec.x && y<otherVec.y) return true;
		return false;
	}

	//----------

	public boolean isAllSOE( Vector otherVec ) {
		if (x<=otherVec.x && y<=otherVec.y) return true;
		return false;
	}

	//----------

	public boolean isEitherSmaller( Vector otherVec ) {
		if (x<otherVec.x || y<otherVec.y) return true;
		return false;
	}

	public boolean isEitherSOE( Vector otherVec ) {
    if (x<=otherVec.x || y<=otherVec.y) return true;
    return false;
  }

	//----------

	public boolean isAllLarger( Vector otherVec ) {
		if (x>otherVec.x && y>otherVec.y) return true;
		return false;
	}

	//----------

	public boolean isAllLOE( Vector otherVec ) {
		if (x>=otherVec.x && y>=otherVec.y) return true;
		return false;
	}

	//----------

	public boolean isEitherLarger( Vector otherVec ) {
		if (x>otherVec.x || y>otherVec.y) return true;
		return false;
	}

	public boolean isEitherLOE( Vector otherVec ) {
    if (x>=otherVec.x || y>=otherVec.y) return true;
    return false;
  }

	//----------

	public boolean isAllEqual( Vector otherVec ) {
		if (x==otherVec.x && y==otherVec.y) return true;
		return false;
	}

	//----------

	public boolean isEitherEqual( Vector otherVec ) {
		if (x==otherVec.x || y==otherVec.y) return true;
		return false;
	}

	//----------

	public static Vector floorAll( Vector newVec ) {
		return new Vector( (float)Math.floor(newVec.x), (float)Math.floor(newVec.y) );
	}

	//----------

	public Vector floorAllInst() {
		x = (float)Math.floor(x);
		y = (float)Math.floor(y);
		return this;
	}

	//----------

	public static Vector ceilAll( Vector newVec ) {
		return new Vector(  (float)Math.ceil(newVec.x), (float)Math.ceil(newVec.y) );
	}

	//----------

	public Vector ceilAllInst() {
		x = (float)Math.ceil(x);
		y = (float)Math.ceil(y);
		return this;
	}

	//----------

	public Vector roundAllInst() {
		x = (float)Math.floor(x+0.5);
		y = (float)Math.floor(y+0.5);
		return this;
	}

	//----------

	public static Vector rotateDeg( Vector newVec, float angleDeg ) {
		double angleRad = ( ((angleDeg+360) % 360)/360d ) * Math.PI*2;
		float sn = (float)Math.sin(angleRad);
		float cs = (float)Math.cos(angleRad);
		return new Vector(  newVec.x*cs - newVec.y*sn,
								newVec.y*cs + newVec.x*sn );
	}

	//----------

	public Vector rotateDegInst( float angle ) {
		Vector newVec = rotateDeg( this, angle );
		x = newVec.x;
		y = newVec.y;
		return this;
	}

	//----------

	public static Vector normalize( Vector newVector ) {
		float d = newVector.getLength();
		return new Vector( newVector.x/d, newVector.y/d );
	}

	//----------

	public Vector normalizeInst() {
		float d = getLength();
		if (d==0) return this;
		x /= d;
		y /= d;
		return this;
	}

	//----------

	public Vector getNormal() {
		return new Vector( -1*y, x );
	}

	//----------

	public static float dotProduct( Vector v1, Vector v2 ) {
		return v1.x*v2.x + v1.y*v2.y; //zero for (anti)parrallel vectors
	}

	//----------

	public static Vector project( Vector v1, Vector v2 ) { //projects v1 onto v2 { v2 must be normalized
		float dotProd = dotProduct( v1, v2 );
		return new Vector( dotProd*v2.x, dotProd*v2.y );
	}

	//----------

	public static Vector absolute( Vector v1 ) {
		return new Vector( Math.abs(v1.x), Math.abs(v1.y) );
	}

	//----------

	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector)obj;
			if (v==null) return super.equals(obj);

			if( x==v.x && y==v.y ) {
				return true; //vectors are equal
			} else {
				return super.equals(obj);
			}
	}

	//----------

	public float getRandomFloatValue() {
	  return Util.random(x, y);
	}

	public int getRandomIntValue() {
	  return Util.round( Util.random(x, y) );
	}

	public static Vector applyBounds(Vector in, Vector lower, Vector upper) {
	  return new Vector(
	      in.x<lower.x ? lower.x : in.x>upper.x ? upper.x : in.x,
	      in.y<lower.y ? lower.y : in.y>upper.y ? upper.y : in.y
	 );
	}

	public Vector applyBoundsInst(Vector lower, Vector upper) {
	  x = x<lower.x ? lower.x : x>upper.x ? upper.x : x;
	  y = y<lower.y ? lower.y : y>upper.y ? upper.y : y;
	  return this;
	}


}
