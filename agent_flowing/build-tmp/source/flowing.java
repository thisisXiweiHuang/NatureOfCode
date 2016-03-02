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

public class flowing extends PApplet {

NodeSystem ns;
FlowField ff;

float t=0;


public void setup() {
  
  background(200, 80);  

  ff = new FlowField();
  ns = new NodeSystem();
  ns.addNodes();
}

public void draw() {
  ff.appear(t);
  ns.run(ff);
  t += 0.00003f;
}

class FlowField {
  PVector[][]field;
  int cols, rows;
  int resolution;


  FlowField() {
    resolution =5;
    cols = width/resolution;
    rows = height/resolution;
    field= new PVector[cols][rows];
  }

  public void appear(float t) {
    float xoff = 0;
    for (int i =0; i < cols; i++) {
      float yoff = 0;
      for (int j = 0; j< rows; j++) {
        float theta = noise(xoff, yoff, t);
        theta= map(theta, 0, 1, 0, TWO_PI);
        field[i][j] = new PVector(cos(theta), sin(theta));//this is wave shape;
        yoff +=0.1f;//increment distance
      }
      xoff +=0.05f;
    }
  }

  public PVector lookup(PVector lookup) {
    int colum = PApplet.parseInt (lookup.x/resolution);
    colum = constrain(colum, 0, cols-1);
    
    int row = PApplet.parseInt(lookup.y/resolution);
    row = constrain(row, 0, rows-1);
    return field[colum][row].get();
  }
}

class Node {
  PVector loc;
  PVector vel;
  PVector acc;
  float maxSpeed; 
  float maxForce; 

  Node(PVector l) {
    loc= l.get();
    vel= new PVector(0, 0);
    acc= new PVector(0, 0);
    maxSpeed =.5f;
    maxForce =0.01f;
  }

  //make sure the wave starts over
  public void checkEdge() {
    if ( loc.x<0) {
      loc.x = width;
    } else if (loc.x>width) {
      loc.x = 0;
    }

    if (loc.y<0) {
      loc.y=height;
    } else if (loc.y>height) {
      loc.y=0;
    }
  }

  //follow behavior
  public void follow(FlowField flow) {
    PVector desired = flow.lookup(loc);
    desired.limit(maxSpeed);//desired velocity with a direction

    PVector steer = PVector.sub(desired, vel);
    steer.limit(maxForce);//times mag
    applyForce(steer);
  }

  public void applyForce (PVector force) {
    acc.add(force);
  }

  public void display() {
  stroke(50,10);
  point(loc.x, loc.y);
  }

  public void update() {
    vel.add(acc);
    vel.limit(maxSpeed);
    loc.add(vel);
    acc.mult(0);
  }
}

class NodeSystem {

  ArrayList <Node> nodes;

  NodeSystem() {
    nodes= new ArrayList<Node>();
  }

  public void addNodes() {
    for (int i=0; i<100; i++) {
      PVector l= new PVector(random(width), random(height));
      nodes.add(new Node(l));
    }
  }

  public void run(FlowField flow) {
    for (Node n : nodes) {
      n.follow(flow);
      n.checkEdge();
      n.update();
      n.display();
    }
  }
}
  public void settings() {  size(800, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "flowing" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
