class Flock {
  ArrayList <Boid> boids; // An arraylist for all the boids
  Vec3D origin;

  Flock(int num, Vec3D v) {
    boids = new ArrayList<Boid>(); // Initialize the arraylist
    origin = v.copy();//instead of get(), copy() for Vec3D;
  }

  void run() {
    for (int i = boids.size()-1; i >= 0; i--) {
      Boid b = (Boid) boids.get(i);  
      b.run(boids);  
    }
  }

  void addBoid(Boid b) {
    boids.add(b);
  }
}