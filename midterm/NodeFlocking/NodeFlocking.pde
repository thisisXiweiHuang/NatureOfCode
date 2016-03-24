import java.util.*;
import toxi.physics.constraints.*;
import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.geom.*;
import processing.opengl.*;
import peasy.PeasyCam;
import peasy.test.*;
import peasy.org.apache.commons.math.*;
import peasy.*;
import peasy.org.apache.commons.math.geometry.*;

VerletPhysics physics;
Vec3D birthPlace;

Flock flock;
NodeSystem ns;
FlockSystem fs;

int numBoids = 500;
int DIM = 100;
int NUM = 500;
int NEIGHBOR_DIST = 50;
int SEPARATION = 15;

PeasyCam cam;

void setup() {
  smooth();
  size(1280, 720, P3D);
  physics=new VerletPhysics();
  physics.setDrag(0.00005);
  birthPlace = new Vec3D(0, 0, 0);

  sphereDetail(3);

  ns = new NodeSystem(birthPlace, 600, 150);
  flock = new Flock(1, birthPlace);
  fs = new FlockSystem(birthPlace, 600, 150);

  cam = new PeasyCam(this, width/2-(ns.side-ns.distBetween)/4, height/2-(ns.side-ns.distBetween)/4, 0, 1100); //initialise the peasycam object
  cam.setMinimumDistance(400); 
  cam.setMaximumDistance(1500); 
  camera();
}

void draw() {
  background(210);

  lights();
  //blendMode(ADD);

  pushMatrix();
  translate(width/2-(ns.side-ns.distBetween)/2, height/2-(ns.side-ns.distBetween)/2, -100);
  //cam.rotateX(0.01);
  cam.rotateY(-0.01);
  //cam.rotateZ(-0.005);

  ns.run();
  flock.run();
  fs.run();
  fs.addFlock();

  popMatrix();

  physics.update();
}

void keyPressed() {
  println("yeah");
}