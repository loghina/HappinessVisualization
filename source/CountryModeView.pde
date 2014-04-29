class CountryModeView{
  
  int win_x_center;
  int win_y_center;
  
  SectorCircular[] sectors;
  SectorCircular[] sectorsBack;
  Question[][] data;
  EuropeanFlag2 flag;
  HashMap<String,CountryInfo> cmap;
 
  int currentCountry;
  int num_questions;
  int lastViewed;
  boolean visible;
  
  public CountryModeView(int win_x, int win_y, Question[][] d, EuropeanFlag2 flag,HashMap<String,CountryInfo> cmap){
  
    this.win_x_center = win_x;
    this.win_y_center = win_y;
    this.data = d;
    this.flag = flag;
    
    this.currentCountry = 0;
    this.num_questions = 21;
    
    this.cmap = cmap;
    
    this.lastViewed = 0;
    this.visible = false;
    
  }
  
  public void show(int country_index, int gender, HappinessBar hbar){
    
    this.currentCountry = country_index;
    this.num_questions = 21;
    
    int nc = this.num_questions;
    int nb = 6;
    sectors = new SectorCircular[nc];
    sectorsBack = new SectorCircular[nb];
    
    float f, aggr_value, mean_value = 0;
    float mean_mapped;
    int stroke_size;
  
    String c_initials = data[0][currentCountry].country;
    String c_name = "error";
    PImage img = null;
    
    color col;
    
    for (Map.Entry me : cmap.entrySet()) {
      
      CountryInfo c = (CountryInfo)me.getValue();
      
      if (c.initials.compareTo(c_initials) == 0){
        c_name = c.countryName;
        img = c.img;
        break;
      }
      
    }
  
    for (int j=0; j<nb; j++){
      sectorsBack[j] = new SectorCircular(win_x_center,win_y_center,800,(j)*TWO_PI/nb,(j+1)*TWO_PI/nb,(TWO_PI/nb)/2,300,250);
      sectorsBack[j].drawSector();
    }
    
    for (int i=0; i<nc; i++){
       aggr_value = 0;
   
       switch(gender){
       case BOTH_GENDERS: aggr_value = data[i][country_index].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[i][country_index].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[i][country_index].getMaleGenderValue();
                       break;
     
       default: return;
       }
   
       mean_value += aggr_value;
    }
    
    mean_value = mean_value/nc;
    
    for (int i=0; i<nc; i++){
   
     f=0;
     stroke_size = 0;
     aggr_value = 0;
   
     switch(gender){
       case BOTH_GENDERS: aggr_value = data[i][country_index].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[i][country_index].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[i][country_index].getMaleGenderValue();
                       break;
     
       default: return;
     }
   
     //mean_value += aggr_value;
   
     //questo serve per scalare il valore di felicità su un'insieme più grande x dare lo spessore ai settori
     f = map(aggr_value,1,10,10,60);
     mean_mapped = map(mean_value,1,10,10,60);
     
     stroke_size = int(f);
   
     if (f >= mean_mapped) col = #A0F9BE;
     else col = #F97070;
   
     //ho bisogno che stroke_size sia un numero pari
     if ((stroke_size % 2) != 0) stroke_size -= 1;
   
     sectors[i] = new SectorCircular(win_x_center,win_y_center,stroke_size,(i)*TWO_PI/nc,(i+1)*TWO_PI/nc,(TWO_PI/nc)/2,300,col);
     
     sectors[i].drawSector();
     noStroke();
   
     sectors[i].current_value = aggr_value;
     
     String s = nf(aggr_value,1,1);
     sectors[i].drawText(s);
     noFill();
   
   }
   
   if (visible){
     String s1 = data[lastViewed][this.currentCountry].name;
     sectors[lastViewed].drawInfo(s1,0);
     
   }
   
   hbar.mean_value = mean_value;
   hbar.drawLine(sectors[lastViewed].current_value,visible);
   
   textSize(24);
   flag.drawFlag("Average Country value");
   
   int score = int(mean_value);
   
   flag.drawScore(score);
   flag.drawAvg(mean_value);
   noFill(); 
    
   imageMode(CENTER);
   image(img,win_x_center-80,30,40,40); 
   fill(0);
   
   
   text(c_name,win_x_center+30,30);
   noFill(); 
   
   //hbar.drawImg();
    
  }
  
  public void mouseOver(HappinessBar hbar){
    
    int nc = this.num_questions;
  
    for(int i = 0; i < nc; i++){
    
      if(sectors[i].contains(mouseX,mouseY)){

       String s1 = data[lastViewed][this.currentCountry].name;
       sectors[i].drawInfo(s1,0);
       
       
       lastViewed = i;
       visible = true;
       
       break;     
      }
      else visible = false;
  
    }
    
    hbar.drawLine(sectors[lastViewed].current_value,visible);
    
  }
  
  public int mouseClick(){
    
    int nc = this.num_questions;
  
    for(int i = 0; i < nc; i++){
      
      if(sectors[i].contains(mouseX,mouseY)){
        
        return i;
        
      }
    
    }
    
    return -1;
  }
  

}
