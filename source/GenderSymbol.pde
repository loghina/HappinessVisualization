class GenderSymbol{

  float win_x_center;
  float win_y_center;
  float fWidth;
  float fHeight;
  PImage img;
  color bcolor;
  
  public GenderSymbol(float x_pos, float y_pos, float fWidth, float fHeight, color c, PImage p){
  
    this.win_x_center = x_pos;
    this.win_y_center = y_pos;
    this.fWidth = fWidth;
    this.fHeight = fHeight;
    this.img = p;
    this.bcolor = c;
  
  }
  
  public void drawGenderSymbol(){
  
    rectMode(CENTER);
    fill(bcolor);
    rect(win_x_center,win_y_center,fWidth + 4, fHeight + 4);
    noFill();
    
    imageMode(CENTER);
    image(img,win_x_center,win_y_center,fWidth,fHeight);
    
  }
  
  public boolean contains(float x, float y){
  
    if (x > (win_x_center - fWidth/2) && x < (win_x_center + fWidth/2) && y > (win_y_center - fHeight/2) && y < (win_y_center + fHeight/2)) return true;
    else return false;
    
  }
  
  
}
