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
    //n.createSpring(birthPlace, distBetween);
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


  void run() {
    for (Node n : this.nodes) {
      n.run(distBetween);
    }
    drawConnection();
  }

  void drawConnection() {
    for (Node n : nodes) {
      for (Node nOther : nodes) {
        float d= n.distanceTo(nOther);
        if (d< 220) {
          stroke(200); 
          strokeWeight(.1); 
          line(n.x, n.y, n.z, nOther.x, nOther.y, nOther.z);
        }
      }
    }
  }
}