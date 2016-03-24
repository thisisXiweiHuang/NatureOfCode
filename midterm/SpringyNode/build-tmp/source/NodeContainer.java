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
PeasyCam cam;
Vec3D birthPlace;
NodeSystem ns;
//ArrayList<NodeSystem> ss;

public void setup() {
  
  
  physics=new VerletPhysics();
  physics.setDrag(0.0000005f);
  birthPlace = new Vec3D(0, 0, 0);

  ns = new NodeSystem(birthPlace, 600, 100);
  //ss = new ArrayList<NodeSystem>();
  cam = new PeasyCam(this, width/2-(ns.side-ns.distBetween)/4, height/2-(ns.side-ns.distBetween)/4, 0, 5000); //initialise the peasycam object
  cam.setMinimumDistance(400); //set minimum zoom distance
  cam.setMaximumDistance(1000); //set maximum zoom distance
  camera();
}

public void draw() {
  lights();
  //for(NodeSystem ns: ss){
  //  ns.run();
  //  ns.addNode();
  //}
  background(20);
  blendMode(ADD);
  
  pushMatrix();
  translate(width/2-(ns.side-ns.distBetween)/2, height/2-(ns.side-ns.distBetween)/2,-100);
  //cam.rotateX(0.01);
  cam.rotateY(-0.01f);
  //cam.rotateZ(-0.005);
  
  ns.run();
  popMatrix();
 
  physics.update();
}

public void keyPressed(){
 //ss.add(new NodeSystem(new Vec3D(0, 0, 0),300,50));
 println("yeah");
}
class Node extends VerletParticle {
  Vec3D pos;
  AttractionBehavior behavior;
  float t, ut;
  VerletSpring spring;
 
  Node (Vec3D loc) {
    super(loc);
    physics.addParticle(this);
    t= random(0, 200);
    ut = random(0, 0.01f);//update
    //AttractionBehavior behavior = new AttractionBehavior(this, 20, -0.01);//repel
    //Vec3D pos = new Vec3D(width/2, height/2,-200);
    pos = new Vec3D (this.x, this.y, this.z);
  }

  public void run(float d) {
    render();
    //this.updateSpring(d);
  }

  public void render() {
    fill(255);
    stroke(200);
    strokeWeight(.5f);
    sphereDetail(10);
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

  //void updateSpring(float d) {
  // spring.setRestLength(map(noise(t), 0, 1, d-d/2, d+d/4));
  // t += ut;
  //}
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

    //for (Node n : nodes) {
    // n.createSpring(birthPlace, distBetween);
    //}

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
    for (Node n : this.nodes) {
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
        if (d< 150) {
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
