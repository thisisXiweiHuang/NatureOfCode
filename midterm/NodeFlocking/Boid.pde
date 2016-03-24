class Boid extends Node {
  Vec3D pos;
  Vec3D vel;
  Vec3D acc;

  float maxforce; 
  float maxspeed;

  float neighborDist;
  float desiredSeperation;

  Boid(Vec3D loc, float ms, float mf, float nd, float sep) {
    super(loc);
    pos = loc;
    acc = new Vec3D();
    vel = Vec3D.randomVector();//randomize velocity 
    maxspeed = ms;
    maxforce = mf;
    neighborDist = sq(nd);
    desiredSeperation = sep;
    
  }


  void flock(ArrayList boids){
    Vec3D sep = separate(boids);   // Separation
    Vec3D ali = align(boids);      // Alignment
    Vec3D coh = cohesion(boids);   // Cohesion
    // Arbitrarily weight these forces
    sep.scaleSelf(25.0);
    ali.scaleSelf(25.0);
    coh.scaleSelf(25.0);
    // Add the force vectors to acceleration
    acc.addSelf(sep);
    acc.addSelf(ali);
    acc.addSelf(coh);
  }

   void updateflock() {
    // Update velocity
    vel.addSelf(acc);
    // Limit speed
    vel.limit(maxspeed);

    pos.addSelf(vel);
    // Reset accelertion to 0 each cycle
    acc.clear();
  }

  void seek(Vec3D target) {
    acc.addSelf(steer(target, false));
  }

  void arrive(Vec3D target) {
    acc.addSelf(steer(target, true));
  }

  Vec3D steer(Vec3D target, boolean slowdown) {
    Vec3D steer;  // The steering vector
    Vec3D desired = target.sub(pos);  // A vector pointing from the location to the target
    float d = desired.magnitude(); // Distance from the target is the magnitude of the vector
    // If the distance is greater than 0, calc steering (otherwise return zero vector)
    if (d > 0) {
      // Normalize desired
      desired.normalize();
      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
      if ((slowdown) && (d < 100.0f)) desired.scaleSelf(maxspeed*(d/100.0f)); // This damping is somewhat arbitrary
      else desired.scaleSelf(maxspeed);
      // Steering = Desired minus Velocity
      steer = desired.sub(vel).limit(maxforce);  // Limit to maximum steering force
    } else {
      steer = new Vec3D();
    }
    return steer;
  }


  // Separation
  // Method checks for nearby boids and steers away
  Vec3D separate (ArrayList boids) {
    Vec3D steer = new Vec3D();
    int count = 0;
    // For every boid in the system, check if it's too close
    for (int i = boids.size ()-1; i >= 0; i--) {
      Boid other = (Boid) boids.get(i);
      if (this != other) {
        float d = pos.distanceTo(other.pos);
        // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
        if (d < desiredSeperation) {
          // Calculate vector pointing away from neighbor
          Vec3D diff = pos.sub(other.pos);
          diff.normalizeTo(1.0/d);
          steer.addSelf(diff);
          count++;
        }
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      steer.scaleSelf(1.0/count);
    }

    // As long as the vector is greater than 0
    if (steer.magSquared() > 0) {
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalizeTo(maxspeed);
      steer.subSelf(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Alignment
  // For every nearby Boidin the system, calculate the average velocity
  Vec3D align (ArrayList boids) {
    Vec3D steer = new Vec3D();
    int count = 0;
    for (int i = boids.size ()-1; i >= 0; i--) {
      Boid other = (Boid) boids.get(i);
      if (this != other) {
        if (pos.distanceToSquared(other.pos) < neighborDist) {
          steer.addSelf(other.vel);
          count++;
        }
      }
    }
    if (count > 0) {
      steer.scaleSelf(1.0/count);
    }

    // As long as the vector is greater than 0
    if (steer.magSquared() > 0) {

      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalizeTo(maxspeed);
      steer.subSelf(vel);
      steer.limit(maxforce);
    }
    return steer;
  }

  // Cohesion
  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
  Vec3D cohesion (ArrayList boids) {
    Vec3D sum = new Vec3D();   // Start with empty vector to accumulate all locations
    int count = 0;
    for (int i = boids.size ()-1; i >= 0; i--) {
      Boid other = (Boid) boids.get(i);
      if (this != other) {
        if (pos.distanceToSquared(other.pos) < neighborDist) {
          sum.addSelf(other.pos); // Add location
          count++;
        }
      }
    }
    if (count > 0) {
      sum.scaleSelf(1.0/count);
      return steer(sum, false);  // Steer towards the location
    }
    return sum;
  }

  void render() {
    //fill(0);
    stroke(0,70);
    strokeWeight(1.5);
    //sphereDetail(3);
    pushMatrix();
    point(pos.x, pos.y, pos.z);
    //translate(pos.x, pos.y, pos.z);
    //sphere(.5);
    popMatrix();
  }

  void run(ArrayList boids){
    flock(boids);
    updateflock();
    render();
  }

  //void createSpring(Boid birthPlace, float distBetween) {
  //  spring = new VerletSpring(this, birthPlace, distBetween, 0.002);
  //  physics.addSpring(spring);
  //}

  //void updateSpring(float d) {
  //spring.setRestLength(map(noise(t), 0, 1, d-d/2, d+d/4));
  //t += ut;
  //}
}