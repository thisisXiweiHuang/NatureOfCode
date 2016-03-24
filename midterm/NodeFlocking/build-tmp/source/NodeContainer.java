import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class NodeContainer extends PApplet {













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

public void setup() {
  
  
  physics=new VerletPhysics();
  physics.setDrag(0.0000005f);
  birthPlace = new Vec3D(0, 0, 0);

  sphereDetail(3);

  ns = new NodeSystem(birthPlace, 600, 100);
  flock = new Flock(1, birthPlace);
  fs = new FlockSystem(birthPlace, 600, 100);

  cam = new PeasyCam(this, width/2-(ns.side-ns.distBetween)/4, height/2-(ns.side-ns.distBetween)/4, 0, 2000); //initialise the peasycam object
  cam.setMinimumDistance(400); //set minimum zoom distance
  cam.setMaximumDistance(1500); //set maximum zoom distance
  camera();
}

public void draw() {
  background(20);

  lights();
  blendMode(ADD);

  pushMatrix();
  translate(width/2-(ns.side-ns.distBetween)/2, height/2-(ns.side-ns.distBetween)/2, -100);
  //cam.rotateX(0.01);
  cam.rotateY(-0.01f);
  //cam.rotateZ(-0.005);

  ns.run();
  flock.run();
  fs.run();
  fs.addFlock();

  popMatrix();

  physics.update();
}

public void keyPressed() {
  println("yeah");
}
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


  public void flock(ArrayList boids){
    Vec3D sep = separate(boids);   // Separation
    Vec3D ali = align(boids);      // Alignment
    Vec3D coh = cohesion(boids);   // Cohesion
    // Arbitrarily weight these forces
    sep.scaleSelf(25.0f);
    ali.scaleSelf(25.0f);
    coh.scaleSelf(25.0f);
    // Add the force vectors to acceleration
    acc.addSelf(sep);
    acc.addSelf(ali);
    acc.addSelf(coh);
  }

   public void updateflock() {
    // Update velocity
    vel.addSelf(acc);
    // Limit speed
    vel.limit(maxspeed);

    pos.addSelf(vel);
    // Reset accelertion to 0 each cycle
    acc.clear();
  }

  public void seek(Vec3D target) {
    acc.addSelf(steer(target, false));
  }

  public void arrive(Vec3D target) {
    acc.addSelf(steer(target, true));
  }

  public Vec3D steer(Vec3D target, boolean slowdown) {
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
  public Vec3D separate (ArrayList boids) {
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
          diff.normalizeTo(1.0f/d);
          steer.addSelf(diff);
          count++;
        }
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      steer.scaleSelf(1.0f/count);
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
  public Vec3D align (ArrayList boids) {
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
      steer.scaleSelf(1.0f/count);
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
  public Vec3D cohesion (ArrayList boids) {
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
      sum.scaleSelf(1.0f/count);
      return steer(sum, false);  // Steer towards the location
    }
    return sum;
  }

  public void render() {
    fill(255);
    stroke(200);
    strokeWeight(1);
    sphereDetail(3);
    pushMatrix();
    point(pos.x, pos.y, pos.z);
    //translate(pos.x, pos.y, pos.z);
    //sphere(.2);
    popMatrix();
  }

  public void run(ArrayList boids){
    flock(boids);
    updateflock();
    render();
  }

  public void createSpring(Boid birthPlace, float distBetween) {
    spring = new VerletSpring(this, birthPlace, distBetween, 0.002f);
    physics.addSpring(spring);
  }

  public void updateSpring(float d) {
  spring.setRestLength(map(noise(t), 0, 1, d-d/2, d+d/4));
  t += ut;
  }
}
class Flock {
  ArrayList <Boid> boids; // An arraylist for all the boids
  Vec3D origin;

  Flock(int num, Vec3D v) {
    boids = new ArrayList<Boid>(); // Initialize the arraylist
    origin = v.copy();//not get
    for (int i =0; i< num; i ++) {
      boids.add(new Boid(origin, 2, 0.05f, NEIGHBOR_DIST, SEPARATION));
    }
  }

  public void run() {
    for (int i = boids.size()-1; i >= 0; i--) {
      Boid b = (Boid) boids.get(i);  
      b.run(boids);  // Passing the entire list of boids to each boid individually
    }
  }

  public void addBoid(Boid b) {
    boids.add(b);
  }
}
class FlockSystem {
  Vec3D birthPlace;
  ArrayList<Flock> flocks;
  int side;
  int distBetween;
  int numUnits;

  FlockSystem(Vec3D _birthPlace, int _side, int _distBetween) {
    flocks = new ArrayList<Flock>();
    birthPlace= new Vec3D(_birthPlace);

    side = _side;
    distBetween = _distBetween;
    numUnits = side/distBetween;

    for (int x = 0; x < numUnits; x++) {
      for (int y = 0; y < numUnits; y++) {
        for (int z = 0; z < numUnits; z++) {
          Vec3D pos = new Vec3D(x*distBetween, y*distBetween, z*distBetween);
          //nodes[x][y][z] = new Node(new Vec3D(pos.x, pos.y, pos.z));
          flocks.add(new Flock(1, new Vec3D(pos.x, pos.y, pos.z)));
        }
      }
    } 
    println(flocks.size());
  }

  public void run() {
    for (Flock f : flocks) {
      f.run();
    }
  }

  public void addFlock() {
    if (frameCount < 100) {
      for (int x = 0; x < numUnits; x++) {
        for (int y = 0; y < numUnits; y++) {
          for (int z = 0; z < numUnits; z++) {
            Vec3D pos = new Vec3D(x*distBetween, y*distBetween, z*distBetween);
            flock.addBoid(new Boid(new Vec3D(pos.x, pos.y, pos.z), 2, 0.05f, NEIGHBOR_DIST, SEPARATION));
          }
        }
      }
    }
  }
}
class Node extends VerletParticle {
  Vec3D pos;
  AttractionBehavior behavior;
  float t, ut;
  VerletSpring spring;

  Node (Vec3D loc) {
    super(loc);
    loc = pos;
    physics.addParticle(this);
    t= random(0, 200);
    ut = random(0, 0.01f);//update
    AttractionBehavior behavior = new AttractionBehavior(this, 20, -0.01f);//repel
    Vec3D pos = new Vec3D(width/2, height/2, -200);
    pos = new Vec3D (this.x, this.y, this.z);
  }

  public void run(float d) {
    render();
    updateSpring(d);
  }

  public void render() {
    fill(255);
    stroke(200);
    strokeWeight(.5f);
    sphereDetail(3);
    pushMatrix();
    //point(this.x, this.y, this.z);
    translate(this.x, this.y, this.z);
    sphere(1);
    popMatrix();
  }

  public void createSpring(Node birthPlace, float distBetween) {
    spring = new VerletSpring(this, birthPlace, distBetween, 0.002f);
    physics.addSpring(spring);
  }

  public void updateSpring(float d) {
    spring.setRestLength(map(noise(t), 0, 1, d-d/2, d+d/4));
    t += ut;
  }
}
class NodeSystem {
  Node birthPlace;
  ArrayList <Node> nodes;
  int side;
  int distBetween;
  int numUnits;

  NodeSystem(Vec3D _birthPlace, int _side, int _distBetween) {
    nodes = new ArrayList<Node>();
    birthPlace= new Node(_birthPlace);

    side = _side;
    distBetween = _distBetween;
    numUnits = side/distBetween;

    for (int x = 0; x < numUnits; x++) {
      for (int y = 0; y < numUnits; y++) {
        for (int z = 0; z < numUnits; z++) {
          Vec3D pos = new Vec3D(x*distBetween, y*distBetween, z*distBetween);
          //nodes[x][y][z] = new Node(new Vec3D(pos.x, pos.y, pos.z));
          nodes.add(new Node(new Vec3D(pos.x, pos.y, pos.z)));
        }
      }
    }
    for (Node n : nodes) {
    n.createSpring(birthPlace, distBetween);
    }

    println(nodes.size());

    //Node node = new Node(new Vec3D(pos.x, pos.y, pos.z));
    //node.x = x;
    //node.y = y;
    //node.z = z;
    //nodes.add(node);
    //Vec3D pos = new Vec3D(x,y,z);

    //if (x < numUnits - 1) {
    // Node rightNeighbor = nodes[x+1][y][z];
    //  nodes.pos.z= z;
    //}

    //if (y < numUnits - 1) {
    //Node downNeighbor = nodes[x][y+1][z];
    //downNeighbor.createSpring(downNeighbor, distBetween);


    //if (z < numUnits - 1) {
    //  Node frontNeighbor =nodes[x][y][z+1];
    //  nodes[x][y][z].createSpring(frontNeighbor, distBetween);
    //}

    //if (x < numUnits - 1) {

    //  //Node rightNeighbor = nodes.get(x * numUnits).get(y).get(z); 
    //  //  nodes[x]; // array
    //  //  nodes.get(i);
    //  // Node = arrayList
    //nodes[x][y][z].createSpring(rightNeighbor, distBetween);
    //}

    //if (y < numUnits - 1) {
    //       Node downNeighbor = nodes[x][y+1][z];
    //nodes[x][y][z].createSpring(downNeighbor, distBetween);
    //}
    //if (z < numUnits - 1) {
    //Node frontNeighbor =nodes[x][y][z+1];
    //nodes[x][y][z].createSpring(frontNeighbor, distBetween);
    //}
  }


  public void run() {
    for (Node n : nodes) {
      n.run(distBetween);
    }
    drawConnection();
  }

  //void addNode(){
  //Node n;
  //n= new Node(birthPlace);
  //}

  public void drawConnection() {
    for (Node n : nodes) {
      for (Node nOther : nodes) {
        float d= n.distanceTo(nOther);
        if (d< distBetween) {
          stroke(200); 
          strokeWeight(.1f); 
          line(n.x, n.y, n.z, nOther.x, nOther.y, nOther.z);
        }
      }
    }
  }
}
  public void settings() {  size(1280, 720, OPENGL);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "NodeContainer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
