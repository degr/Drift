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
package org.jbox2d.dynamics.joints;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.SolverData;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Position;
import org.jbox2d.dynamics.contacts.Velocity;

public class ConstantVolumeJoint extends Joint {

  private final Body[] bodies;
  private double[] targetLengths;
  private double targetVolume;

  private Vec2[] normals;
  private double m_impulse = 0.0f;

  private World world;

  private DistanceJoint[] distanceJoints;

  public Body[] getBodies() {
    return bodies;
  }

  public DistanceJoint[] getJoints() {
    return distanceJoints;
  }

  public void inflate(double factor) {
    targetVolume *= factor;
  }

  public ConstantVolumeJoint(World argWorld, ConstantVolumeJointDef def) {
    super(argWorld.getPool(), def);
    world = argWorld;
    if (def.bodies.size() <= 2) {
      throw new IllegalArgumentException(
          "You cannot create a constant volume joint with less than three bodies.");
    }
    bodies = def.bodies.toArray(new Body[0]);

    targetLengths = new double[bodies.length];
    for (int i = 0; i < targetLengths.length; ++i) {
      final int next = (i == targetLengths.length - 1) ? 0 : i + 1;
      double dist = bodies[i].getWorldCenter().sub(bodies[next].getWorldCenter()).length();
      targetLengths[i] = dist;
    }
    targetVolume = getBodyArea();

    if (def.joints != null && def.joints.size() != def.bodies.size()) {
      throw new IllegalArgumentException(
          "Incorrect joint definition.  Joints have to correspond to the bodies");
    }
    if (def.joints == null) {
      final DistanceJointDef djd = new DistanceJointDef();
      distanceJoints = new DistanceJoint[bodies.length];
      for (int i = 0; i < targetLengths.length; ++i) {
        final int next = (i == targetLengths.length - 1) ? 0 : i + 1;
        djd.frequencyHz = def.frequencyHz;// 20.0f;
        djd.dampingRatio = def.dampingRatio;// 50.0f;
        djd.collideConnected = def.collideConnected;
        djd.initialize(bodies[i], bodies[next], bodies[i].getWorldCenter(),
            bodies[next].getWorldCenter());
        distanceJoints[i] = (DistanceJoint) world.createJoint(djd);
      }
    } else {
      distanceJoints = def.joints.toArray(new DistanceJoint[0]);
    }

    normals = new Vec2[bodies.length];
    for (int i = 0; i < normals.length; ++i) {
      normals[i] = new Vec2();
    }
  }

  @Override
  public void destructor() {
    for (int i = 0; i < distanceJoints.length; ++i) {
      world.destroyJoint(distanceJoints[i]);
    }
  }

  private double getBodyArea() {
    double area = 0.0f;
    for (int i = 0; i < bodies.length; ++i) {
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      area +=
          bodies[i].getWorldCenter().x * bodies[next].getWorldCenter().y
              - bodies[next].getWorldCenter().x * bodies[i].getWorldCenter().y;
    }
    area *= .5f;
    return area;
  }

  private double getSolverArea(Position[] positions) {
    double area = 0.0f;
    for (int i = 0; i < bodies.length; ++i) {
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      area +=
          positions[bodies[i].m_islandIndex].c.x * positions[bodies[next].m_islandIndex].c.y
              - positions[bodies[next].m_islandIndex].c.x * positions[bodies[i].m_islandIndex].c.y;
    }
    area *= .5f;
    return area;
  }

  private boolean constrainEdges(Position[] positions) {
    double perimeter = 0.0f;
    for (int i = 0; i < bodies.length; ++i) {
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      double dx = positions[bodies[next].m_islandIndex].c.x - positions[bodies[i].m_islandIndex].c.x;
      double dy = positions[bodies[next].m_islandIndex].c.y - positions[bodies[i].m_islandIndex].c.y;
      double dist = MathUtils.sqrt(dx * dx + dy * dy);
      if (dist < Settings.EPSILON) {
        dist = 1.0f;
      }
      normals[i].x = dy / dist;
      normals[i].y = -dx / dist;
      perimeter += dist;
    }

    final Vec2 delta = pool.popVec2();

    double deltaArea = targetVolume - getSolverArea(positions);
    double toExtrude = 0.5f * deltaArea / perimeter; // *relaxationFactor
    // double sumdeltax = 0.0f;
    boolean done = true;
    for (int i = 0; i < bodies.length; ++i) {
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      delta.set(toExtrude * (normals[i].x + normals[next].x), toExtrude
          * (normals[i].y + normals[next].y));
      // sumdeltax += dx;
      double normSqrd = delta.lengthSquared();
      if (normSqrd > Settings.maxLinearCorrection * Settings.maxLinearCorrection) {
        delta.mulLocal(Settings.maxLinearCorrection / MathUtils.sqrt(normSqrd));
      }
      if (normSqrd > Settings.linearSlop * Settings.linearSlop) {
        done = false;
      }
      positions[bodies[next].m_islandIndex].c.x += delta.x;
      positions[bodies[next].m_islandIndex].c.y += delta.y;
      // bodies[next].m_linearVelocity.x += delta.x * step.inv_dt;
      // bodies[next].m_linearVelocity.y += delta.y * step.inv_dt;
    }

    pool.pushVec2(1);
    // System.out.println(sumdeltax);
    return done;
  }

  @Override
  public void initVelocityConstraints(final SolverData step) {
    Velocity[] velocities = step.velocities;
    Position[] positions = step.positions;
    final Vec2[] d = pool.getVec2Array(bodies.length);

    for (int i = 0; i < bodies.length; ++i) {
      final int prev = (i == 0) ? bodies.length - 1 : i - 1;
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      d[i].set(positions[bodies[next].m_islandIndex].c);
      d[i].subLocal(positions[bodies[prev].m_islandIndex].c);
    }

    if (step.step.warmStarting) {
      m_impulse *= step.step.dtRatio;
      // double lambda = -2.0f * crossMassSum / dotMassSum;
      // System.out.println(crossMassSum + " " +dotMassSum);
      // lambda = MathUtils.clamp(lambda, -Settings.maxLinearCorrection,
      // Settings.maxLinearCorrection);
      // m_impulse = lambda;
      for (int i = 0; i < bodies.length; ++i) {
        velocities[bodies[i].m_islandIndex].v.x += bodies[i].m_invMass * d[i].y * .5f * m_impulse;
        velocities[bodies[i].m_islandIndex].v.y += bodies[i].m_invMass * -d[i].x * .5f * m_impulse;
      }
    } else {
      m_impulse = 0.0f;
    }
  }

  @Override
  public boolean solvePositionConstraints(SolverData step) {
    return constrainEdges(step.positions);
  }

  @Override
  public void solveVelocityConstraints(final SolverData step) {
    double crossMassSum = 0.0f;
    double dotMassSum = 0.0f;

    Velocity[] velocities = step.velocities;
    Position[] positions = step.positions;
    final Vec2 d[] = pool.getVec2Array(bodies.length);

    for (int i = 0; i < bodies.length; ++i) {
      final int prev = (i == 0) ? bodies.length - 1 : i - 1;
      final int next = (i == bodies.length - 1) ? 0 : i + 1;
      d[i].set(positions[bodies[next].m_islandIndex].c);
      d[i].subLocal(positions[bodies[prev].m_islandIndex].c);
      dotMassSum += (d[i].lengthSquared()) / bodies[i].getMass();
      crossMassSum += Vec2.cross(velocities[bodies[i].m_islandIndex].v, d[i]);
    }
    double lambda = -2.0f * crossMassSum / dotMassSum;
    // System.out.println(crossMassSum + " " +dotMassSum);
    // lambda = MathUtils.clamp(lambda, -Settings.maxLinearCorrection,
    // Settings.maxLinearCorrection);
    m_impulse += lambda;
    // System.out.println(m_impulse);
    for (int i = 0; i < bodies.length; ++i) {
      velocities[bodies[i].m_islandIndex].v.x += bodies[i].m_invMass * d[i].y * .5f * lambda;
      velocities[bodies[i].m_islandIndex].v.y += bodies[i].m_invMass * -d[i].x * .5f * lambda;
    }
  }

  /** No-op */
  @Override
  public void getAnchorA(Vec2 argOut) {}

  /** No-op */
  @Override
  public void getAnchorB(Vec2 argOut) {}

  /** No-op */
  @Override
  public void getReactionForce(double inv_dt, Vec2 argOut) {}

  /** No-op */
  @Override
  public double getReactionTorque(double inv_dt) {
    return 0;
  }
}
