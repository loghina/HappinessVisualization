class Country {
  String name;
  float x;
  float y;
  int radius=8;
  boolean highlight = false;
  boolean visible = true;

  public Country(String n,float a,float b){
    name = n;
    x = a;
    y = b;
  }
  public void drawCountrySphere(){
    fill(255);
    noStroke();
    lights();
    translate(x, y, 0);
    if(highlight){
      fill(249,25,25);}
    sphere(radius);
  }
   public void drawCountryCircle(){
    fill(0);
    noStroke();
    //lights();
    //translate(x, y, 0);
    if(highlight){
      fill(249,25,25);}
    ellipse(x,y,10,10);
  }
  
}
