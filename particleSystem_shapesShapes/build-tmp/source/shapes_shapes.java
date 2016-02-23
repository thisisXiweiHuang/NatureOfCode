import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class shapes_shapes extends PApplet {

ParticleSystem ps;

public void setup() {
  
  ps = new ParticleSystem();
  frameRate(30);
  noStroke();
}


public void draw() {
  background(255);
  lights();
  smooth();
  
  ps.run();
}


class Box {
  float theta = 0;
  PVector location;
  int boxSize =15;
  Sphere sphere;  

  Box(PVector pos) {
    location = pos;
    sphere = new Sphere(new PVector (location.x, location.y, -boxSize));
  }

  public void display() {
    pushMatrix();
    translate(location.x, location.y, -boxSize);
    rotateY(theta);
    theta += 0.01f;
    fill(120);
    box(boxSize);
    sphere.display(); 
    popMatrix();
    
  }
}

class Sphere {
  PVector location;
  float r = 7;
  float speed =10;
  float angle = 0.0f;
  float size = 5;
  
  Sphere (PVector pos) {
    location = pos;
  }

  public void display () {
    location.x = (sin(radians(angle)) * r)+5;
    location.y = cos(radians(angle)) * r;
    
    pushMatrix();
    translate(location.x, location.y, 0);
    fill(220);
    noStroke();
    sphere(size);
    popMatrix();
    
    angle += speed;
  }
}

class ParticleSystem {
  ArrayList<Box> boxes;
  int gridScale=30;

  ParticleSystem() {
    boxes= new ArrayList<Box>();
    addMore();
  }

  public void addMore() {
    for (int i = 0; i < 30; i++) {
      for (int j = 0; j < 20; j++) {
        boxes.add(new Box((new PVector(i * gridScale, j * gridScale, -gridScale))));
      }
    }
  }

  public void run() {
    for (Box b : boxes) {
      b.display();
    }
  }
}
  public void settings() {  size(600, 400, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "shapes_shapes" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
