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
PeasyCam cam;
Vec3D birthPlace;
NodeSystem ns;
//ArrayList<NodeSystem> ss;

void setup() {
  smooth();
  size(1280, 720, P3D);
  physics=new VerletPhysics();
  physics.setDrag(0.0000005);
  birthPlace = new Vec3D(0, 0, 0);

  ns = new NodeSystem(birthPlace, 600, 100);
  //ss = new ArrayList<NodeSystem>();
  cam = new PeasyCam(this, width/2-(ns.side-ns.distBetween)/4, height/2-(ns.side-ns.distBetween)/4, 0, 700); //initialise the peasycam object
  cam.setMinimumDistance(200); //set minimum zoom distance
  cam.setMaximumDistance(2000); //set maximum zoom distance
  camera();
}

void draw() {
  lights();
  background(255, 133, 51);
  //blendMode(ADD);
  
  pushMatrix();
  translate(width/2-(ns.side-ns.distBetween)/2, height/2-(ns.side-ns.distBetween)/2,-100);
  //cam.rotateX(0.01);
  //cam.rotateY(-0.01);
  //cam.rotateZ(-0.005);
  
  ns.run();
  popMatrix();
 
  physics.update();
}

void keyPressed(){
 //ss.add(new NodeSystem(new Vec3D(0, 0, 0),300,50));
 println("yeah");
}