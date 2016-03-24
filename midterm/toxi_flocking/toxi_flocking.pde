import peasy.*;
import peasy.org.apache.commons.math.*;
import peasy.org.apache.commons.math.geometry.*;
import peasy.test.*;

import toxi.physics.constraints.*;
import toxi.physics2d.constraints.*;
import toxi.physics2d.behaviors.*;
import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.physics2d.*;

import toxi.processing.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;

int numBoids = 500;
int DIM = 100;
int NUM = 500;
int NEIGHBOR_DIST = 50;
int SEPARATION = 15;

Flock flock;
VerletPhysics physics;
PeasyCam cam;

void setup() {
  size(800, 800, P3D);
  physics = new VerletPhysics();
  physics.setWorldBounds(new AABB(new Vec3D(0, 0, 0), 200));
  flock = new Flock();
  sphereDetail(3);
  smooth();
  background(220);

  cam = new PeasyCam(this, 0, 0, 0, 1500); //initialise the peasycam object
  cam.setMinimumDistance(150); //set minimum zoom distance
  cam.setMaximumDistance(1500); //set maximum zoom distance
  camera();
}
void draw() {
  background(20, 20);
  if (frameCount < 1000) {
    flock.addBoid(new Boid(new Vec3D(), 2, 0.05, NEIGHBOR_DIST, SEPARATION));
  }
  //blur();
  lights();
  pushMatrix();
  translate(width/2, height/2, 0);
  noFill();
  flock.run();
  popMatrix();
  println(flock.boids.size());
}