class HappinessBar{

  int x_pos;
  int y_pos;
  int bar_width;
  int bar_height;
  PImage img;
  float mean_value;
  
  public HappinessBar(int xpos, int ypos, int bwidth, int bheight){
    
    this.x_pos = xpos;
    this.y_pos = ypos;
    this.bar_width = bwidth;
    this.bar_height = bheight;
    this.img = loadImage("RIF.jpg");
    this.mean_value = 0;
    
  }
  
  public void drawLine(float val, boolean visible){
    
    drawImg();
    
    if(visible){
    
      float n = map(val,1,10,1,bar_width);
    
      strokeWeight(4);
      stroke(0);
      line(x_pos+int(n),y_pos-4,x_pos+int(n),y_pos+bar_height+8);
      noStroke();
    }
    
  }
  
  public void drawImg(){
    
    imageMode(CORNER);
    image(img,995,180,185,70);
    
    float n = map(this.mean_value,1,10,1,bar_width);
    
    strokeWeight(4);
    stroke(#0021F9);
    line(x_pos+int(n),y_pos-5,x_pos+int(n),y_pos+bar_height+12);
    noStroke();
    
  }

}
