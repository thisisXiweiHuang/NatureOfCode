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
          nodes.add(new Node(new Vec3D(pos.x, pos.y, pos.z)));
        }
      }
    }

   //for (Node n : nodes) {
   //n.createSpring(birthPlace, distBetween);
   //}

    println(nodes.size());
  }
  
  //void mousePressed(){
  // for (Node n : nodes) {
  // n.createSpring(birthPlace, distBetween);
  // }
  //}


  void run() {
    for (Node n : nodes) {
      n.run(distBetween);
    }
    //drawConnection();//line between each node depends on the constraint distance
  }

  void drawConnection() {
    stroke(0,20); 
    strokeWeight(.2); 
    for (Node n : nodes) {
      for (Node nOther : nodes) {
        float d= n.distanceTo(nOther);
        if (d< 1000/*constraint distance */){
          line(n.x, n.y, n.z, nOther.x, nOther.y, nOther.z);
        }
      }
    }
  }
}