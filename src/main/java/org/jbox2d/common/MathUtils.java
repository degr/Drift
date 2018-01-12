/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package org.jbox2d.common;

import java.util.Random;

/**
 * A few math methods that don't fit very well anywhere else.
 */
public class MathUtils extends PlatformMathUtils {
  public static final double PI = (double) Math.PI;
  public static final double TWOPI = (double) (Math.PI * 2);
  public static final double INV_PI = 1f / PI;
  public static final double HALF_PI = PI / 2;
  public static final double QUARTER_PI = PI / 4;
  public static final double THREE_HALVES_PI = TWOPI - HALF_PI;

  /**
   * Degrees to radians conversion factor
   */
  public static final double DEG2RAD = PI / 180;

  /**
   * Radians to degrees conversion factor
   */
  public static final double RAD2DEG = 180 / PI;

  public static final double[] sinLUT = new double[Settings.SINCOS_LUT_LENGTH];

  static {
    for (int i = 0; i < Settings.SINCOS_LUT_LENGTH; i++) {
      sinLUT[i] = (double) Math.sin(i * Settings.SINCOS_LUT_PRECISION);
    }
  }

  public static final double sin(double x) {
    if (Settings.SINCOS_LUT_ENABLED) {
      return sinLUT(x);
    } else {
      return (double) java.lang.StrictMath.sin(x);
    }
  }

  public static final double sinLUT(double x) {
    x %= TWOPI;

    if (x < 0) {
      x += TWOPI;
    }

    if (Settings.SINCOS_LUT_LERP) {

      x /= Settings.SINCOS_LUT_PRECISION;

      final int index = (int) x;

      if (index != 0) {
        x %= index;
      }

      // the next index is 0
      if (index == Settings.SINCOS_LUT_LENGTH - 1) {
        return ((1 - x) * sinLUT[index] + x * sinLUT[0]);
      } else {
        return ((1 - x) * sinLUT[index] + x * sinLUT[index + 1]);
      }

    } else {
      return sinLUT[MathUtils.round(x / Settings.SINCOS_LUT_PRECISION) % Settings.SINCOS_LUT_LENGTH];
    }
  }

  public static final double cos(double x) {
    if (Settings.SINCOS_LUT_ENABLED) {
      return sinLUT(HALF_PI - x);
    } else {
      return (double) Math.cos(x);
    }
  }

  public static final double abs(final double x) {
    if (Settings.FAST_ABS) {
      return x > 0 ? x : -x;
    } else {
      return Math.abs(x);
    }
  }

  public static final double fastAbs(final double x) {
    return x > 0 ? x : -x;
  }

  public static final int abs(int x) {
    int y = x >> 31;
    return (x ^ y) - y;
  }

  public static final int floor(final double x) {
    if (Settings.FAST_FLOOR) {
      return fastFloor(x);
    } else {
      return (int) java.lang.Math.floor(x);
    }
  }

  public static final int fastFloor(final double x) {
    int y = (int) x;
    if (x < y) {
      return y - 1;
    }
    return y;
  }

  public static final int ceil(final double x) {
    if (Settings.FAST_CEIL) {
      return fastCeil(x);
    } else {
      return (int) java.lang.Math.ceil(x);
    }
  }

  public static final int fastCeil(final double x) {
    int y = (int) x;
    if (x > y) {
      return y + 1;
    }
    return y;
  }

  public static final int round(final double x) {
    if (Settings.FAST_ROUND) {
      return floor(x + .5d);
    } else {
      return (int)java.lang.StrictMath.round(x);
    }
  }

  /**
   * Rounds up the value to the nearest higher power^2 value.
   * 
   * @param x
   * @return power^2 value
   */
  public static final int ceilPowerOf2(int x) {
    int pow2 = 1;
    while (pow2 < x) {
      pow2 <<= 1;
    }
    return pow2;
  }

  public final static double max(final double a, final double b) {
    return a > b ? a : b;
  }

  public final static int max(final int a, final int b) {
    return a > b ? a : b;
  }

  public final static double min(final double a, final double b) {
    return a < b ? a : b;
  }

  public final static int min(final int a, final int b) {
    return a < b ? a : b;
  }

  public final static double map(final double val, final double fromMin, final double fromMax,
      final double toMin, final double toMax) {
    final double mult = (val - fromMin) / (fromMax - fromMin);
    final double res = toMin + mult * (toMax - toMin);
    return res;
  }

  /** Returns the closest value to 'a' that is in between 'low' and 'high' */
  public final static double clamp(final double a, final double low, final double high) {
    return max(low, min(a, high));
  }

  public final static Vec2 clamp(final Vec2 a, final Vec2 low, final Vec2 high) {
    final Vec2 min = new Vec2();
    min.x = a.x < high.x ? a.x : high.x;
    min.y = a.y < high.y ? a.y : high.y;
    min.x = low.x > min.x ? low.x : min.x;
    min.y = low.y > min.y ? low.y : min.y;
    return min;
  }

  public final static void clampToOut(final Vec2 a, final Vec2 low, final Vec2 high, final Vec2 dest) {
    dest.x = a.x < high.x ? a.x : high.x;
    dest.y = a.y < high.y ? a.y : high.y;
    dest.x = low.x > dest.x ? low.x : dest.x;
    dest.y = low.y > dest.y ? low.y : dest.y;
  }

  /**
   * Next Largest Power of 2: Given a binary integer value x, the next largest power of 2 can be
   * computed by a SWAR algorithm that recursively "folds" the upper bits into the lower bits. This
   * process yields a bit vector with the same most significant 1 as x, but all 1's below it. Adding
   * 1 to that value yields the next largest power of 2.
   */
  public final static int nextPowerOfTwo(int x) {
    x |= x >> 1;
    x |= x >> 2;
    x |= x >> 4;
    x |= x >> 8;
    x |= x >> 16;
    return x + 1;
  }

  public final static boolean isPowerOfTwo(final int x) {
    return x > 0 && (x & x - 1) == 0;
  }

  public static final double pow(double a, double b) {
    if (Settings.FAST_POW) {
      return fastPow(a, b);
    } else {
      return (double) java.lang.Math.pow(a, b);
    }
  }

  public static final double atan2(final double y, final double x) {
    if (Settings.FAST_ATAN2) {
      return fastAtan2(y, x);
    } else {
      return (double) java.lang.StrictMath.atan2(y, x);
    }
  }

  public static final double fastAtan2(double y, double x) {
    if (x == 0.0f) {
      if (y > 0.0f) return HALF_PI;
      if (y == 0.0f) return 0.0f;
      return -HALF_PI;
    }
    double atan;
    final double z = y / x;
    if (abs(z) < 1.0f) {
      atan = z / (1.0f + 0.28f * z * z);
      if (x < 0.0f) {
        if (y < 0.0f) return atan - PI;
        return atan + PI;
      }
    } else {
      atan = HALF_PI - z / (z * z + 0.28f);
      if (y < 0.0f) return atan - PI;
    }
    return atan;
  }

  public static final double reduceAngle(double theta) {
    theta %= TWOPI;
    if (abs(theta) > PI) {
      theta = theta - TWOPI;
    }
    if (abs(theta) > HALF_PI) {
      theta = PI - theta;
    }
    return theta;
  }

  public static final double randomFloat(double argLow, double argHigh) {
    return (double) Math.random() * (argHigh - argLow) + argLow;
  }

  public static final double randomFloat(Random r, double argLow, double argHigh) {
    return r.nextFloat() * (argHigh - argLow) + argLow;
  }

  public static final double sqrt(double x) {
    return (double) java.lang.StrictMath.sqrt(x);
  }

  public final static double distanceSquared(Vec2 v1, Vec2 v2) {
    double dx = (v1.x - v2.x);
    double dy = (v1.y - v2.y);
    return dx * dx + dy * dy;
  }

  public final static double distance(Vec2 v1, Vec2 v2) {
    return sqrt(distanceSquared(v1, v2));
  }
}