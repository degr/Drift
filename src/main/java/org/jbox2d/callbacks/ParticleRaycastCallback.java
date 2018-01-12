package org.jbox2d.callbacks;

import org.jbox2d.common.Vec2;

public interface ParticleRaycastCallback {
  /**
   * Called for each particle found in the query. See
   * {@link RayCastCallback#reportFixture(org.jbox2d.dynamics.Fixture, Vec2, Vec2, double)} for
   * argument info.
   * 
   * @param index
   * @param point
   * @param normal
   * @param fraction
   * @return
   */
  double reportParticle(int index, Vec2 point, Vec2 normal, double fraction);

}
