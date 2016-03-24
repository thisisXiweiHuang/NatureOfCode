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
    println(flocks.size());
  }

  void run() {
    for (Flock f : flocks) {
      f.run();
    }
  }

  void addFlock() {
    if (frameCount < 60) {
      for (int x = 0; x < numUnits; x++) {
        for (int y = 0; y < numUnits; y++) {
          for (int z = 0; z < numUnits; z++) {
            Vec3D pos = new Vec3D(x*distBetween, y*distBetween, z*distBetween);
            flock.addBoid(new Boid(new Vec3D(pos.x, pos.y, pos.z), 2, 0.05, NEIGHBOR_DIST, SEPARATION));
          }
        }
      }
    }
  }
}