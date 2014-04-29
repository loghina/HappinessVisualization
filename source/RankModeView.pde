class RankModeView{

  int win_x_center;
  int win_y_center;
  
  SectorCircular[] sectors;
  SectorCircular[] sectorsBack;
  Question[][] data;
  HashMap<String,CountryInfo> cmap;
  EuropeanFlag2 flag;
 
  int currentQuestion;
  int num_countries;
  int lastViewed;
  boolean visible;
  
  public RankModeView(int win_x, int win_y, Question[][] d,HashMap<String,CountryInfo> cmap,EuropeanFlag2 flag){
  
    this.win_x_center = win_x;
    this.win_y_center = win_y;
    this.data = d;
    this.cmap = cmap;
    
    this.currentQuestion = 0;
    this.num_countries = 0;
    
    this.lastViewed = 0;
    this.visible = false;
    
    this.flag = flag;
    
  }
  
  public void show(int question_index, int gender, HappinessBar hbar){
    
    this.currentQuestion = question_index;
    this.num_countries = data[question_index][0].nb_countries;
    
    int nc = this.num_countries;
    int nb = 2;
    sectors = new SectorCircular[nc];
    sectorsBack = new SectorCircular[nb];
    
    float f, aggr_value = 0;
    float mean_value = 0;
    float mean_mapped;
    int stroke_size;
    
    color col;
  
    for (int j=0; j<nb; j++){
      sectorsBack[j] = new SectorCircular(win_x_center,win_y_center,800,(j)*TWO_PI/nb,(j+1)*TWO_PI/nb,(TWO_PI/nb)/2,300,250);
      sectorsBack[j].drawSector();
    }
    
    for (int i=0; i<nc; i++){
      
     aggr_value = 0;
   
     switch(gender){
       case BOTH_GENDERS: aggr_value = data[question_index][i].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[question_index][i].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[question_index][i].getMaleGenderValue();
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
       case BOTH_GENDERS: aggr_value = data[question_index][i].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[question_index][i].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[question_index][i].getMaleGenderValue();
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
   
     String s = nf(aggr_value,1,1);
     sectors[i].drawText(data[question_index][i].country);
     noFill();

    }
    
    if (visible){
      String s1 = data[question_index][lastViewed].country;
      CountryInfo c = cmap.get(s1);
      sectors[lastViewed].drawCountryFlag(c.img);
      switch(gender){
       case BOTH_GENDERS: aggr_value = data[currentQuestion][lastViewed].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[currentQuestion][lastViewed].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[currentQuestion][lastViewed].getMaleGenderValue();
                       break;
     
       default: aggr_value = 0;
      }
     String s = nf(aggr_value,1,1);
     sectors[lastViewed].drawInfo(c.countryName+" "+s+" /10",25);
     
   }
    hbar.mean_value = mean_value;
    hbar.drawLine(aggr_value,visible);
    
    textSize(24);
    
    flag.drawFlag("Average European value");
    int score = int(mean_value);
   
   flag.drawScore(score);
   flag.drawAvg(mean_value);
   noFill();
    
   fill(0);
   String s = data[question_index][0].name;
   text(s,win_x_center,30);
   noFill();
   
   //hbar.drawImg();
    
    
  }
  
  public void mouseOver(int gender, HappinessBar hbar){
    
    int nc = this.num_countries;
    float aggr_value = 0;
    
    for(int i = 0; i < nc; i++){
    
      if(sectors[i].contains(mouseX,mouseY)){
        
       String s1 = data[this.currentQuestion][i].country;
       CountryInfo c = cmap.get(s1);
       sectors[i].drawCountryFlag(c.img);
       
       switch(gender){
       case BOTH_GENDERS: aggr_value = data[currentQuestion][i].getBothGenderValue();
                        break;
                        
       case FEMALE_GENDER: aggr_value = data[currentQuestion][i].getFemaleGenderValue();
                         break;
     
       case MALE_GENDER: aggr_value = data[currentQuestion][i].getMaleGenderValue();
                       break;
     
       default: aggr_value = 0;
       }
       String s = nf(aggr_value,1,1);
       sectors[i].drawInfo(c.countryName+" "+s+" /10",25);
       
       
       lastViewed = i;
       visible = true;
       
       break;     
      }
      else visible = false;
  
    }
    
    hbar.drawLine(aggr_value,visible);
    
  }
  
public int mouseClick(){
    
    int nc = this.num_countries;
  
    for(int i = 0; i < nc; i++){
      
      if(sectors[i].contains(mouseX,mouseY)){
        
        return i;
        
      }
    
    }
    
    return -1;
  }
  

}
