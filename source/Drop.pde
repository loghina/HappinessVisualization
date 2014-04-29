class Drop{

  int country_1;
  int country_2;
  int selected;
  int value=0;
  Country cDropped;
  CountryInfo cDrop1,cDrop2,cDrop;
  boolean parOverview=false;
  int flagsel=0;
  
  public Drop(int country_1, int country_2, int selected, Country cDropped, CountryInfo cDrop1, CountryInfo cDrop2, CountryInfo cDrop){
    
    this.country_1 = country_1;
    this.country_2 = country_2;
    this.selected = selected;
    this.cDropped = cDropped;
    this.cDrop1 = cDrop1;
    this.cDrop2 = cDrop2;
    this.cDrop = cDrop;
  
  }
 
void drawDrop() {
  if(this.selected==1){
    if(parOverview){
      if(flagsel==1)
      image(d1.cDrop1.img,mouseX,mouseY,40,40);
      else
      image(d1.cDrop2.img,mouseX,mouseY,40,40);
    } else {
  fill(50);
   ellipse(mouseX,mouseY,10,10);
    }
  }
}
/*
  void mouseDragged() {
    if(value==0){
      if(mouseX<662 && mouseX>509 && mouseY<546 && mouseY>356){
        selected=1;
        value++;    
      }
    }
    value=value+5;
  }

void mouseDragged() {
  this.selected=1;
  value = value + 5;
  if (value > 255) {
    value = 0;
  }
  redraw();
}
*/

public boolean InACircle(int x, int y, int x_center,int y_center,int radius){
float r;
  r=0;
  r=((x-x_center)*(x-x_center)+(y-y_center)*(y-y_center));
  if(r<(radius*radius)){
  return true;
  } else {
return false;
  }
}

}


