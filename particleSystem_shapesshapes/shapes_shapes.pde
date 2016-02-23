ParticleSystem ps;

void setup() {
  size(600, 400, P3D);
  ps = new ParticleSystem();
  frameRate(30);
  //noStroke();
}


void draw() {
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
    sphere = new Sphere(new PVector (location.x, location.y, location.z));
  }

  void display() {
    pushMatrix();
    translate(location.x, location.y, -boxSize);
    rotateY(theta);
    theta += 0.01;
    fill(220);
    stroke(180);
    strokeWeight(0.1);
    box(boxSize);
    sphere.display(); 
    popMatrix();
    
  }
}

class Sphere {
  PVector location;
  float r = 7;
  float speed =10;
  float angle = 0.0;
  float size = 3;
  
  Sphere (PVector pos) {
    location = pos;
  }

  void display () {
    location.x = 2*sin(radians(angle)) * r;
    location.y = 2*cos(radians(angle)) * r;
    
    pushMatrix();
    translate(location.x, location.y, 0);
    fill(120);
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

  void addMore() {
    for (int i = 0; i < 25; i++) {
      for (int j = 0; j < 18; j++) {
        boxes.add(new Box((new PVector(i * gridScale, j * gridScale, -gridScale))));
      }
    }
  }

  void run() {
    for (Box b : boxes) {
      b.display();
    }
  }
}