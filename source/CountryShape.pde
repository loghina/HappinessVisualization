//
// @author Pierre Dragicevic. 
//modified by Alexandru Loghin
//
 
class CountryShape {

  String name;
  float[][] xpoints, ypoints; // keep the list of points for drawing
  GeneralPath path; // maintain this Java object for hit testing and other things

  //added
  boolean selected=false; 
  int x_center, y_center, radius;
  float p1,p2;
  int newx,newy;
  Rectangle2D mapB; // Map bounds
  int w,h;
  
  // Builds a new country given a string specification in the form x,y;x,y;x,y x,y;x,y ...
  // If skipVertices is set to a value greater than 0 (up to 4), will skip vertices to speed up display
  public CountryShape(String name, String pointList, int skipVertices) {
    println(name);
    String[] polygonStrings = split(pointList, " ");
    float[][] xpoints = new float[polygonStrings.length][];
    float[][] ypoints = new float[polygonStrings.length][];
    for (int p = 0; p < polygonStrings.length; p++) {
      String[] pointStrings = split(polygonStrings[p], ";");
      int npoints = pointStrings.length / (skipVertices + 1);
      xpoints[p] = new float[npoints];
      ypoints[p] = new float[npoints];
      for (int v = 0; v < npoints; v++) {
        String[] coordStrings = split(pointStrings[v * (skipVertices + 1)], ",");
        xpoints[p][v] = parseFloat(coordStrings[0]);
        ypoints[p][v] = parseFloat(coordStrings[1]);
        //println(v, xpoints[p][v],ypoints[p][v]);
        
      }
    }
    this.name = name;
    this.xpoints = xpoints;
    this.ypoints = ypoints;
    buildPath();
    
    //----------------------
    //this.x_center=x;
    //this.y_center=y;
    //this.radius=r;
  }
  
  public CountryShape(String name, float[][] xpoints, float[][] ypoints) {
    this.name = name;
    this.xpoints = xpoints;
    this.ypoints = ypoints;
    buildPath();
  }
  
  private void buildPath() {
    path = new GeneralPath();
    for (int i = 0; i < xpoints.length; i++) {
      for (int j = 0; j < xpoints[i].length; j++) {
        if (j == 0)
          path.moveTo(xpoints[i][j], ypoints[i][j]);
        else
          path.lineTo(xpoints[i][j], ypoints[i][j]);
      }
      path.closePath();
    }
  }
 
  void draw() {
    if (this.name.compareTo("Iceland")==0){
      translate(20,0);  
    }
    
    if (this.name.compareTo("Cyprus")==0){
      translate(-5,0);  
    }
    
    if (this.selected){
      fill(100);
    }else{
      fill(color(0, 0, 255, 20));
    }
    for (int i = 0; i < xpoints.length; i++) {
      beginShape();      
      for (int j = 0; j < xpoints[i].length; j++){
        //calculate the distance and check it's inside the circle
           vertex(xpoints[i][j], ypoints[i][j]);   
      }
      endShape(CLOSE);
      
    }
    if (this.name.compareTo("Iceland")==0){
      translate(-20,0);  
    }
    
    if (this.name.compareTo("Cyprus")==0){
      translate(5,0);  
    }
    
  }
  
  Rectangle2D getBounds() {
    return path.getBounds2D();
  }
  
  boolean contains(float x, float y) {
    return path.contains(x, y);
  }
  
  //-------------------------------------------
  void select(boolean s){
    this.selected=s;  
  }
  
  /*static void setMapBounds(Rectangle2D map, int w2, int h2){
    this.w=w2;
    this.h=h2;
    this.mapB=map;
  }*/
  
  
}
