class SectorCircular{
  
  int maxStrokeSize;
  
  float x_center;
  float y_center;
  float x_info;
  float y_info;
  int win_x_center;
  int win_y_center;
  float strokeSize;
  float angle_rad_beg;
  float angle_rad_end;
  float angle_offset;
  int radius;
  color fill_color;
  boolean data_visibility;
  float current_value;
  
  
  public SectorCircular(int win_x_center, int win_y_center, float sSize, float angle_beg, float angle_end, float offset, int radius, color fill_color){
    
    this.win_x_center = win_x_center;
    this.win_y_center = win_y_center;
    this.strokeSize = sSize;
    this.angle_rad_beg = angle_beg;
    this.angle_rad_end = angle_end;
    this.angle_offset = offset;
    this.radius = radius;
    this.fill_color = fill_color;
    this.maxStrokeSize = 60;
    this.data_visibility = false;
    this.current_value = 0;
    
    calculateXYCenter();
    calculateXYInfo(50);
  
  }


  public void drawSector(){
    
    float t = (maxStrokeSize - strokeSize)/2;
    strokeWeight(strokeSize);
    stroke(fill_color);
    arc(win_x_center, win_y_center, (radius-t)*2, (radius-t)*2, angle_rad_beg+angle_offset, angle_rad_end+angle_offset);
  }
  
  public boolean contains(float x, float y){
    
    float distance = sqrt((x-this.x_center)*(x-this.x_center) + (y-this.y_center)*(y-this.y_center));
      
    if (distance <= 52) return true;
    else return false;
    
  }
  
  public void calculateXYCenter(){
    
   float x, y;
   
   x = win_x_center + cos(angle_rad_beg+(angle_offset*2)) * radius;
   y = win_y_center + sin(angle_rad_beg+(angle_offset*2)) * radius;
  
   this.x_center = x;
   this.y_center = y;
  
  
  }
  
  public void calculateXYInfo(int t){
  
    float x, y;
    
    x = win_x_center + cos(angle_rad_beg+(angle_offset*2)) * (radius - t);
    y = win_y_center + sin(angle_rad_beg+(angle_offset*2)) * (radius - t);
  
   this.x_info = x;
   this.y_info = y;
    
  }
  
  public void drawText(String s){
      
    textAlign(CENTER, CENTER);
    fill(0);
    text(s,x_center,y_center);
    
  }
  
  public void drawCountry(String v){
    
    textAlign(CENTER, CENTER);
    fill(0);
    float x, y;
    float offset;
    
    x=0;
    y=0;
    
    offset = textWidth(v);
    
    if ( x_info <= win_x_center ){
      
      x = x_info + 40 + int(offset/2);
      y = y_info;
      
    }
    else if ( x_info > win_x_center ){
      
      x = x_info - 40 - int(offset/2);
      y = y_info;
      
    }
    
    
    text(v,x,y);
    
  }
  
  public void drawCountryFlag(PImage img){
      
    imageMode(CENTER);
    image(img,x_info,y_info,40,40);

  }
  
  public void drawInfo(String v, int distanceFromSector){
    
    textAlign(CENTER, CENTER);
    rectMode(CENTER);
    
    
    float offset;
    float x, y;
    
    x=0;
    y=0;
    
    offset = textWidth(v);
    
    if ( x_info <= win_x_center ){
      
      x = x_info + distanceFromSector +  int(offset/2);
      y = y_info;
      
    }
    else if ( x_info > win_x_center ){
      
      x = x_info - distanceFromSector - int(offset/2);
      y = y_info;
      
    }
    
    fill(#DCDCDC);
    rect(x,y,offset,32);
    
    fill(0);
    text(v,x,y);
    
    noFill();
    
  }
  
  public void drawCircle(color col, float Rradius){
    
    ellipseMode(CENTER);
    noStroke();
    fill(col);
    ellipse(x_center,y_center,Rradius,Rradius);
    
  }

}
