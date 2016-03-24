NodeSystem ns;
FlowField ff;

float t=0;

void setup() {
  size(800, 500);
  background(200, 80);

  ff = new FlowField();
  ns = new NodeSystem();
  ns.addNodes();

  frameRate(30);
}

void draw() {
  ff.appear(t);
  ns.run(ff);
  t += 0.0003;
 // saveGif();
}

void saveGif() {
  if (frameCount<120) {
    saveFrame("gif/image-####.gif");
  }
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

  void appear(float t) {
    float xoff = 0;
    for (int i =0; i < cols; i++) {
      float yoff = 0;
      for (int j = 0; j< rows; j++) {
        float theta = noise(xoff, yoff, t);
        theta= map(theta, 1, 0, 0, TWO_PI);
        field[i][j] = new PVector(cos(theta),sin(theta)*2);//this is wave shape;
        yoff +=0.1;//increment distance
      }
      xoff +=0.1;
    }
  }

  PVector lookup(PVector lookup) {
    int colum = int (lookup.x/resolution);
    colum = constrain(colum, 0, cols-1);

    int row = int(lookup.y/resolution);
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
    maxSpeed =0.8;
    maxForce =0.01;
  }

  //make sure the wave starts over
  void checkEdge() {
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
  void follow(FlowField flow) {
    PVector desired = flow.lookup(loc);
    desired.limit(maxSpeed);//desired velocity with a direction

    PVector steer = PVector.sub(desired, vel);
    steer.limit(maxForce);//times mag
    applyForce(steer);
  }

  void applyForce (PVector force) {
    acc.add(force);
  }

  void display() {
    stroke(25, 10);
    point(loc.x, loc.y);
  }

  void update() {
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

  void addNodes() {
    for (int i=0; i<300; i++) {
      PVector l= new PVector(random(width), random(height));
      nodes.add(new Node(l));
    }
  }

  void run(FlowField flow) {
    for (Node n : nodes) {
      n.follow(flow);
      n.checkEdge();
      n.update();
      n.display();
    }
  }
}