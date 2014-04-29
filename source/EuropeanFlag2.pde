class EuropeanFlag2{

  SectorCircular[] sectors;
  int win_x_center;
  int win_y_center;
  float fWidth;
  float fHeight;
  color flag_color;
  PImage img;
  
  public EuropeanFlag2(int x_pos, int y_pos, float fWidth, float fHeight, color fColor, PImage p){
    
    this.win_x_center = x_pos;
    this.win_y_center = y_pos;
    this.fWidth = fWidth;
    this.fHeight = fHeight;
    this.flag_color = fColor;
    this.img = p;
    
    sectors = new SectorCircular[12];
    
  }
  
  public void drawFlag(String s){
    
    noStroke();
    imageMode(CENTER);
    image(img,win_x_center,win_y_center,fWidth,fHeight);
    
    for (int i=0; i<12; i++){
     sectors[i] = new SectorCircular(win_x_center,win_y_center,12,(i)*TWO_PI/12,(i+1)*TWO_PI/12,(TWO_PI/12)/2,int((fHeight/3)),flag_color);
     //sectors[i].drawSector();
    }
    
    textAlign(CENTER);
    fill(0);
    text(s,win_x_center,win_y_center-(fHeight/2)-30);
    noFill();
     
  }
  
  public void drawScore(int score){
    
    score = int(map(score,1,10,1,12));
    
    for (int i=11; i>=score; i--){
     sectors[i].drawCircle(flag_color,15);
    }
    
    
  }
  
  public void drawAvg(float num){
      
    String s = nf(num,1,1);
    fill(255);
    text(s,win_x_center,win_y_center+10);
    noFill();
    
  }

}
