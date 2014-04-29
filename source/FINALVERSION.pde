/* Happiness project - visualize and analyse data coming from the Eurofound survey 2013 on happiness 
see http://www.eurofound.europa.eu/surveys/smt/eqls/results.htm for more details
Authors: Alexandru Loghin, Francesco Santoro, Chiara Prencipe, Cyril Sabbagh
*/
import java.awt.geom.*;
import java.lang.Enum.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

int currentQuestionIndex;
int currentCountryIndex;
int currentGender;

CountryShape[] countries; 
int w = displayWidth, h = displayHeight; // window size
int skipVertices = 0; // If set to a value greater than 0 (up to 5), will skip vertices to speed up display
Rectangle2D mapBounds = null; // Map bounds
String s="",c="";
boolean country1=false,country2=false;
//parOverview=false;

int small_x,small_y,small_w,small_h;
int map=0;
int ellipse_x, ellipse_y;
CountryShape lastSelected=null;
int lasti=-1;
int value=0, selected=0, f=0, ind=(-1);//selected is just an exaple
PFont labelFont;

//******Alex added*********
ParallelCoordinates p;  
boolean ParCoordOn=false;

static final int BOTH_GENDERS = 0;
static final int FEMALE_GENDER = 1;
static final int MALE_GENDER = 2;

Drop d1;
SectorCircular[] sectors;
SectorCircular[] sectorsBack;

GenderSymbol both;
GenderSymbol female;
GenderSymbol male;

HappinessBar hbar;

Question[][] data;
RankModeView r_view;
CountryModeView c_view;
HashMap<String,CountryInfo> cmap = new HashMap<String,CountryInfo>();
HashMap<String,Integer> qmap = new HashMap<String,Integer>();

//cyril
//Countries Lists
Country[] Cwide;
Country[] Cz1;
Country[] Cz2;
Country[] Cz3;
Country[] Cz4;

//Countries Selectors Absolute Positions
String countryName[]={"IS","ME","MK","RS","TR","XK","AT","BE","BG",null,"CZ","DE","DK","EE","EL","ES","FI","FR","HR","HU","IE","IT","LT","LU","LV","MT","NL","PL","PT","RO","SE","SI","SK","UK"};
float countryX[]={482,648,673,660,754,664,605,513,705,0,608,560,554,713,672,437,714,493,623,645,395,583,690,527,710,604,521,649,396,701,613,606,646,454};
float countryY[]={210,510,526,488,565,514,444,402,514,0,415,393,328,291,552,539,238,448,469,452,367,507,340,416,317,606,380,383,535,468,237,466,428,378};

String countryNameZ1[]={"IS",null,null,null,null,null,null,"BE",null,null,"CZ","DE","DK",null,null,null,null,"FR",null,null,null,null,null,"LU",null,null,"NL","PL",null,null,"SE",null,null,"UK"};
float countryXZ1[]={501,0,0,0,0,0,0,556,0,0,750,660,640,0,0,0,0,519,0,0,0,0,0,584,0,0,572,810,0,0,753,0,0,440};
float countryYZ1[]={232,0,0,0,0,0,0,550,0,0,569,542,429,0,0,0,0,618,0,0,0,0,0,571,0,0,513,507,0,0,293,0,0,504};

String countryNameZ2[]={null,null,null,null,null,null,"AT","BE",null,null,null,"DE",null,null,null,"ES",null,"FR","HR",null,null,"IT",null,"LU",null,"MT","NL",null,"PT",null,null,"SI",null,"UK"};
float countryXZ2[]={0,0,0,0,0,0,798,613,0,0,0,721,0,0,0,458,0,573,834,0,0,756,0,640,0,795,629,0,370,0,0,801,0,492};
float countryYZ2[]={0,0,0,0,0,0,281,212,0,0,0,191,0,0,0,449,0,300,322,0,0,385,0,233,0,546,175,0,460,0,0,314,0,168};

String countryNameZ3[]={null,null,null,null,null,null,null,null,null,null,null,null,null,"EE",null,null,"FI",null,null,null,null,null,"LT",null,"LV",null,null,"PL",null,null,"SE",null,null,null};
float countryXZ3[]={0,0,0,0,0,0,0,0,0,0,0,0,0,636,0,0,658,0,0,0,0,0,589,0,632,0,0,517,0,0,444,0,0,0};
float countryYZ3[]={0,0,0,0,0,0,0,0,0,0,0,0,0,412,0,0,299,0,0,0,0,0,491,0,455,0,0,562,0,0,312,0,0,0};

String countryNameZ4[]={null,"ME","MK","RS","TR","XK","AT",null,"BG","CY","CZ",null,null,null,"EL",null,null,null,"HR","HU",null,null,"LT",null,"LV",null,null,"PL",null,"RO",null,"SI","SK",null};
float countryXZ4[]={0,471,520,506,747,502,388,0,583,641,395,0,0,0,520,0,0,0,422,469,0,0,555,0,600,0,0,476,0,578,0,388,476,0};
float countryYZ4[]={0,456,482,434,544,462,352,0,460,631,298,0,0,0,532,0,0,0,390,365,0,0,176,0,139,0,0,246,0,390,0,382,322,0};


void setup() {
  smooth();
  size(1250, 700);
  labelFont = loadFont("ChalkboardSE-Light-48.vlw");
  textFont(labelFont, 48);

  // Load the table with country names, codes and shapes
  Table table = new Table("euro.csv", "\t", null);
  
  // Build the array of country shapes based on data in the table.
  countries = new CountryShape[table.getRowCount()];
  for (int r = 0; r < table.getRowCount(); r++)
    countries[r] = new CountryShape(table.get(2, r), table.get(4, r), skipVertices);
  
  // Compute the bounding box of all countries.
  // In order to zoom in a particular region, set the variable mapBounds by hand.
  for (int r = 0; r < countries.length; r++) {
    if (mapBounds == null)
      mapBounds = countries[r].getBounds();
    else
      mapBounds = mapBounds.createUnion(countries[r].getBounds());
  }
  w=1250;
  h=700;
  small_x=25;
  small_y=h-155;
  small_w=220;
  small_h=140;
  ellipse_x=small_x+small_w/2;
  ellipse_y=small_y+small_h/2; 
  //CountryShape.setMapBounds(mapBounds,w,h);
  //----------------
  //smooth();
  //size(w, h);

/////////PARTE DI TABLEDEMO2 DI FRA____________________________________________________________________
noFill();
 
  //15 pixels thick
  strokeCap(SQUARE);
  
  EuropeanFlag2 flag2;
  int nbr_fichiers = 21; //Set Number of csv files to load
  int q_nbr=0;
  
  data = new Question[nbr_fichiers][34];
  
  while(q_nbr < nbr_fichiers){
    Table table1 = new Table("question_"+q_nbr+".csv", "," , new int[]{});
    for(int i=0;i<table1.getRowCount();i++){
      
      
      data[q_nbr][i] = new Question(table1,i);
      //Exemple of displaying both gender answers
      //println(""+q_nbr+""+data[q_nbr][i].nom+"\tBoth"+data[q_nbr][i].gender_both);
    }
    
    qmap.put(table1.get(1,0),q_nbr);
    
    
    q_nbr++;
  }
  
  PImage img = loadImage("flag_yellow_high.jpg");
  flag2 = new EuropeanFlag2(140,250,150,100,#0055A5,img);
  
  PImage bg = loadImage("pnggender/mf.png");
  PImage fg = loadImage("pnggender/female.png");
  PImage mg = loadImage("pnggender/male.png");
  
  both = new GenderSymbol(62,480,40,85,#0055A5,bg);
  female = new GenderSymbol(137,480,40,85,#0055A5,fg);
  male = new GenderSymbol(212,480,40,85,#0055A5,mg);
 
  Table tab = new Table("countries_info.csv",",",new int[]{});
  for(int i=0;i<tab.getRowCount();i++){
      
   String fname = tab.get(2,i);
   String cname = tab.get(1,i);
   String initials = tab.get(0, i);
   
   CountryInfo c = new CountryInfo("flags/"+fname,cname,initials,i);
   cmap.put(initials,c);    
   
  }
 
  r_view = new RankModeView(600,375,data,cmap,flag2);
  c_view = new CountryModeView(600,375,data,flag2,cmap);
  
 d1 = new Drop(-1,-1,0,null,null,null,null);
/////////____________________________________________________________________________________________
  //cyril
  // Initialize Countries Circles Positions
Cwide = new Country[data[0].length];
Cz1 = new Country[data[0].length];
Cz2 = new Country[data[0].length];
Cz3 = new Country[data[0].length];
Cz4 = new Country[data[0].length];
 for(int i=0;i<data[0].length;i++){
   if(countryName[i]!=null)
   Cwide[i]=new Country(countryName[i],countryX[i],countryY[i]);
   if(countryNameZ1[i]!=null)
   Cz1[i]=new Country(countryNameZ1[i],countryXZ1[i],countryYZ1[i]);
   if(countryNameZ2[i]!=null)
   Cz2[i]=new Country(countryNameZ2[i],countryXZ2[i],countryYZ2[i]);
   if(countryNameZ3[i]!=null)
   Cz3[i]=new Country(countryNameZ3[i],countryXZ3[i],countryYZ3[i]);
   if(countryNameZ4[i]!=null)
   Cz4[i]=new Country(countryNameZ4[i],countryXZ4[i],countryYZ4[i]);
 }
 
 
 currentQuestionIndex = 20;
 currentCountryIndex = 0;
 currentGender = BOTH_GENDERS;
 
 //****Alex added******
  p=new ParallelCoordinates(data);
  ParCoordOn=true;
  
  hbar = new HappinessBar(995,200,170,25);
}
 
void draw(){
  background(255);
  switch(map){
    case 0:  
      scale(w*0.55/ (float)mapBounds.getWidth(), -h*0.85/(float)mapBounds.getHeight());
      translate(50, -80);  //global
    break;  
    case 1:
      scale(w*1.1/ (float)mapBounds.getWidth(), -1.4*h /(float)mapBounds.getHeight());  
      translate(25, -75);  //NW
    break;
    case 2:  
      scale(1.1*w/ (float)mapBounds.getWidth(), -1.4*h /(float)mapBounds.getHeight());  
      translate(28, -60);  //SW
    break;
    case 3:  
      scale(1.1*w/ (float)mapBounds.getWidth(), -1.4*h /(float)mapBounds.getHeight());  
      translate(8, -77);  //NE
    break;
    case 4:
      scale(1.1*w/ (float)mapBounds.getWidth(), -1.4*h /(float)mapBounds.getHeight());  
      translate(6, -63);  //SE
    break;
  }
  
  // Set the same visual attributes for all countries
  fill(color(0, 0, 255, 20));
  stroke(0);
  strokeWeight(0.1);
  
  // Draw all countries
  for (int i = 0; i < countries.length; i++) {
    countries[i].draw();
  }
      
  //undo changes
  switch(map){
    case 0:
      translate(-50, 80);  //global  
      scale((float)mapBounds.getWidth()/(w*0.55), -(float)mapBounds.getHeight()/(h*0.85));
      
    break;  
    case 1:
      translate(-25, 75);  //NW
      scale((float)mapBounds.getWidth()/(w*1.1), -(float)mapBounds.getHeight()/(h*1.4));  
      //translate(-20, 70);  //NW
    break;
    case 2:
      translate(-28, 60);  //SW  
      scale((float)mapBounds.getWidth()/(w*1.1), -(float)mapBounds.getHeight()/(h*1.4));  
      //translate(-20, 55);  //SW
    break;
    case 3:
      translate(-8, 77);  //NE  
      scale((float)mapBounds.getWidth()/(w*1.1), -(float)mapBounds.getHeight()/(h*1.4));  
      //translate(5, 70);  //NE
    break;
    case 4:
      translate(-6, 63);  //SE
      scale((float)mapBounds.getWidth()/(w*1.1), -(float)mapBounds.getHeight()/(h*1.4));  
      //translate(5, 55);  //SE
    break;
  }
   //CIRCLE WITH SECTORS________
   noFill();
   textSize(18); 
   //Draw Countries Circles
 switch(map){
    case 0:
       for(int i=0;i<data[0].length;i++){
         if(Cwide[i]!=null && Cwide[i].visible){
         Cwide[i].drawCountryCircle();
         }
       }
    break;  
    case 1:
        for(int i=0;i<data[0].length;i++){
        if(Cz1[i]!=null && Cz1[i].visible)
         Cz1[i].drawCountryCircle();
        }
    break;
    case 2:
        for(int i=0;i<data[0].length;i++){
        if(Cz2[i]!=null && Cz2[i].visible)
         Cz2[i].drawCountryCircle();
        }
    break;
    case 3:
       for(int i=0;i<data[0].length;i++){
       if(Cz3[i]!=null && Cz3[i].visible)
         Cz3[i].drawCountryCircle();
       }
    break;
    case 4:
        for(int i=0;i<data[0].length;i++){
        if(Cz4[i]!=null && Cz4[i].visible)
         Cz4[i].drawCountryCircle();
        }
    break;
  }
  
   noFill();
   
   if(f==0){
     //drawRankMode(1,BOTH_GENDERS);
     r_view.show(currentQuestionIndex,currentGender,hbar);
   } else {
     //drawCountryMode(1,BOTH_GENDERS);
     c_view.show(currentCountryIndex,currentGender,hbar);
   }
   
   
   textSize(24);
   fill(50);
   text("Compare 2 countries",1080,310); 
   text("Drop them here!!",1080, 360);
   fill(50);
   ellipse(1040,415,40,40);
   ellipse(1120,415,40,40);
   
   imageMode(CENTER);
   if(d1.country_1!=(-1)){
     image(d1.cDrop1.img,1040,415,40,40);
   }
   if(d1.country_2!=(-1)){
     image(d1.cDrop2.img,1120,415,40,40);
   }
  
   d1.drawDrop();
   drawSmallMap();
   
   both.drawGenderSymbol();
   female.drawGenderSymbol();
   male.drawGenderSymbol();
   
   p.draw(f);
   
}

void drawSmallMap(){
  
  scale(0.2*w / (float)mapBounds.getWidth(), -0.2*h /(float)mapBounds.getHeight());
  //translate(25,-225);
  translate(25,-243);

  // Set the same visual attributes for all countries
  fill(color(0, 0, 255, 20));
  stroke(0);
  strokeWeight(0.1);
    
  // Draw all countries
  for (int i = 0; i < countries.length; i++) {
    countries[i].draw();
  }
  //undo the changes
  translate(-25,243);
  scale(5*(float)mapBounds.getWidth()/w, -5*(float)mapBounds.getHeight()/h);
  
  //draw the map rectangle
  rectMode(CORNER);
  rect(small_x,small_y,small_w,small_h);
  line(small_x+small_w/2,small_y,small_x+small_w/2,small_y+small_h);
  line(small_x,small_y+small_h/2,small_x+small_w,small_y+small_h/2);
  fill(color(0, 0, 255, 0));
  ellipse(ellipse_x,ellipse_y,20,20);
  
}

void mouseClicked(){
 
  //check the click about rank mode and country mode
  if (f == 0) {
    Country c = getCountry(mouseX,mouseY);  
  
    if (c != null){ 
      CountryInfo cinfo = cmap.get(c.name);
      println(c.name);
      if (cinfo != null){ 
        currentCountryIndex = cinfo.countryIndex;
        //***********draw the selected country with a different colour
        println(cinfo.countryName);
        for (int k=0;k<countries.length;k++){         
          if (countries[k].name.compareTo(cinfo.countryName)==0){    
            //println(countries.length,countries[k].name,k);
            if (lastSelected!=null)
              lastSelected.select(false);
            if (countries[k].name.compareTo("Luxembourg")!=0)    //problems with Luxembourg
                countries[k].select(true);
            lastSelected=countries[k];
          }
        }
      }
      f=1;
      
    }   
  }
  else{
    if (c_view.mouseClick() >= 0){ 
      currentQuestionIndex = c_view.mouseClick();
      f = 0;
    }
    Country c = getCountry(mouseX,mouseY);  
  
    if (c != null){ 
      CountryInfo cinfo = cmap.get(c.name);
      if (cinfo != null){
        currentCountryIndex = cinfo.countryIndex;
        //***********draw the selected country with a different colour
        for (int k=0;k<countries.length;k++){
          if (countries[k].name.compareTo(cinfo.countryName)==0){
            //println(countries[k].name);
            if (lastSelected!=null)
              lastSelected.select(false);
              if (countries[k].name.compareTo("Luxembourg")!=0)
                countries[k].select(true);
            lastSelected=countries[k];
          }
        }
      }
    }
  
  }
  
  //check the click about gender
  
  if (both.contains(mouseX,mouseY)) currentGender = BOTH_GENDERS;
  if (female.contains(mouseX,mouseY)) currentGender = FEMALE_GENDER;
  if (male.contains(mouseX,mouseY)) currentGender = MALE_GENDER;
  
  //check if click inside the map rectangle
  if (mouseX>small_x && mouseX<small_x+small_w){
    if (mouseY>small_y && mouseY<small_y+small_h){
      //println(mouseX,mouseY);
      //check which part of the map I clicked
       if (mouseX>small_x && mouseX<=small_x+small_w/2){
         if (mouseY>small_y && mouseY<=small_y+small_h/2){
           //NW
           println("NW");
           map=1;
         }else{
           //SW
           println("SW");
           map=2;
         }
       }else{
         if (mouseY>small_y && mouseY<=small_y+small_h/2){
           //NE
           println("NE");
           map=3;
         }else{
           //SE
           println("SE");
           map=4;
         }
       }
       //check if clicked on central ellipse
       float dist=sqrt(pow(mouseX-ellipse_x,2)+pow(mouseY-ellipse_y,2));
       if (dist<10){
         map=0;
       }
    }
  
    
    redraw();
  }
}

//to replace with the menu for selecting the country
/*void keyPressed(){
  if (keyCode==UP){
    if (lasti<countries.length){
      lasti++;
    }    
  }else{ 
    if(keyCode==DOWN){
      if (lasti>-1){
      lasti--;
      }  
    }
  }
  if (lasti>-1){
  if (lastSelected!=null)
        lastSelected.select(false);
  countries[lasti].select(true);
  lastSelected=countries[lasti];
  redraw();
  } 
}
*/
void mouseMoved() {
  s=mouseX+" "+mouseY;
  if (f == 0) {
    r_view.mouseOver(currentGender,hbar);
   //*********Alex*********
    p.selectCountry(r_view.lastViewed,r_view.visible);
  } else {
    c_view.mouseOver(hbar);
    //******Alex*******
    p.selectQuestion(c_view.lastViewed,c_view.visible);
  }
  
  
  //Hover over countries circles
  switch(map){
    case 0:
         for(int i=0;i<data[0].length;i++){
        if(Cwide[i]!=null){
        float dist=sqrt((Cwide[i].x-mouseX)*(Cwide[i].x-mouseX)+(Cwide[i].y-mouseY)*(Cwide[i].y-mouseY));
         if(dist<=Cwide[i].radius) Cwide[i].highlight=true;
         else Cwide[i].highlight=false;
        }
  }  
    break;  
    case 1:
        for(int i=0;i<data[0].length;i++){
        if(Cz1[i]!=null){
        float dist=sqrt((Cz1[i].x-mouseX)*(Cz1[i].x-mouseX)+(Cz1[i].y-mouseY)*(Cz1[i].y-mouseY));
         if(dist<=Cz1[i].radius) Cz1[i].highlight=true;
         else Cz1[i].highlight=false;
        }
     }
    break;
    case 2:
          for(int i=0;i<data[0].length;i++){
          if(Cz2[i]!=null){
            float dist=sqrt((Cz2[i].x-mouseX)*(Cz2[i].x-mouseX)+(Cz2[i].y-mouseY)*(Cz2[i].y-mouseY));
            if(dist<=Cz2[i].radius) Cz2[i].highlight=true;
            else Cz2[i].highlight=false;
          }
       }
    break;
    case 3:
          for(int i=0;i<data[0].length;i++){
          if(Cz3[i]!=null){
          float dist=sqrt((Cz3[i].x-mouseX)*(Cz3[i].x-mouseX)+(Cz3[i].y-mouseY)*(Cz3[i].y-mouseY));
           if(dist<=Cz3[i].radius) Cz3[i].highlight=true;
           else Cz3[i].highlight=false;
          }
       }
    break;
    case 4:
        for(int i=0;i<data[0].length;i++){
        if(Cz4[i]!=null){
          float dist=sqrt((Cz4[i].x-mouseX)*(Cz4[i].x-mouseX)+(Cz4[i].y-mouseY)*(Cz4[i].y-mouseY));
          if(dist<=Cz4[i].radius) Cz4[i].highlight=true;
          else Cz4[i].highlight=false;
        }  
     }
    break;
  }
  
  p.mouseMoved();
  noFill();
  redraw();
}
 
void mouseDragged(){
  if(value==0){
    
  if(d1.InACircle(mouseX,mouseY,1040,415,40)){
    if(country1 && country2){
      //println("ho cliccato nelle ellissi");
      d1.selected=1;
      d1.parOverview=true;
      d1.flagsel=1;
    }
  }
  if(d1.InACircle(mouseX,mouseY,1120,415,40)){
    if(country1 && country2){
      //println("ho cliccato nelle ellissi");
      d1.selected=1;
      d1.parOverview=true;  
      d1.flagsel=2;
    }
  }
    d1.cDropped = getCountry(mouseX,mouseY);
    fill(50);
    text(c,100,100);
    if(d1.cDropped!=null){ //here I have to replace with a for-cycle on the countries vector;
      d1.cDrop=cmap.get(d1.cDropped.name);
      //if(d1.cDrop.countryIndex<10){
      if(Cwide[d1.cDrop.countryIndex]!=null)
      Cwide[d1.cDrop.countryIndex].visible=false;
      if(Cz1[d1.cDrop.countryIndex]!=null)
      Cz1[d1.cDrop.countryIndex].visible=false;
      if(Cz2[d1.cDrop.countryIndex]!=null)
      Cz2[d1.cDrop.countryIndex].visible=false;
      if(Cz3[d1.cDrop.countryIndex]!=null)
      Cz3[d1.cDrop.countryIndex].visible=false;
      if(Cz4[d1.cDrop.countryIndex]!=null)
      Cz4[d1.cDrop.countryIndex].visible=false;
      ind=d1.cDrop.countryIndex;
      d1.selected=1;
      value=1;
    }    
}
    //redraw();
}

void mouseReleased() {
    if (value != 0) {//I'm dropping
      if(d1.InACircle(mouseX,mouseY,1040,415,40)){//I'm releasing the mouse inside the right ellipse
        country1=true;
        d1.cDrop1 = d1.cDrop;
        d1.country_1=d1.cDrop1.countryIndex;
        
      } else if (d1.InACircle(mouseX,mouseY,1120,415,40)){
        country2=true;
        d1.cDrop2 = d1.cDrop;
        d1.country_2=d1.cDrop2.countryIndex;
      }
      d1.selected=0;
    //if(d1.cDrop.countryIndex<10){
      if(Cwide[d1.cDrop.countryIndex]!=null)
      Cwide[d1.cDrop.countryIndex].visible=true;
      if(Cz1[d1.cDrop.countryIndex]!=null)
      Cz1[d1.cDrop.countryIndex].visible=true;
      if(Cz2[d1.cDrop.countryIndex]!=null)
      Cz2[d1.cDrop.countryIndex].visible=true;
      if(Cz3[d1.cDrop.countryIndex]!=null)
      Cz3[d1.cDrop.countryIndex].visible=true;
      if(Cz4[d1.cDrop.countryIndex]!=null)
      Cz4[d1.cDrop.countryIndex].visible=true;
      
     if(country1 && country2){
       p.compareCountries(d1.country_1,d1.country_2);
       //country1==false;
       //country2==false;
     } 
        
  } else { //I've released mouse outside the two ellipses so --> no comparison
    d1.selected=0;
    if(d1.cDrop!=null){
    if(Cwide[d1.cDrop.countryIndex]!=null)
      Cwide[d1.cDrop.countryIndex].visible=true;
      if(Cz1[d1.cDrop.countryIndex]!=null)
      Cz1[d1.cDrop.countryIndex].visible=true;
      if(Cz2[d1.cDrop.countryIndex]!=null)
      Cz2[d1.cDrop.countryIndex].visible=true;
      if(Cz3[d1.cDrop.countryIndex]!=null)
      Cz3[d1.cDrop.countryIndex].visible=true;
      if(Cz4[d1.cDrop.countryIndex]!=null)
      Cz4[d1.cDrop.countryIndex].visible=true;
      
      
    }
   
      if(d1.parOverview){
        d1.selected=0;
        p.viewAllCountries();
        d1.country_1=-1;
        d1.country_2=-1;
        d1.parOverview=false;
        country1=false;
        country2=false;
      }
   
  }
      value=0;
      redraw();
}

public Country getCountry(float _x,float _y){
      Country ct;
        switch(map){
          case 0:
          for(int i=0;i<data[0].length;i++){
          if(Cwide[i]!=null){
              float dist=sqrt((Cwide[i].x-mouseX)*(Cwide[i].x-mouseX)+(Cwide[i].y-mouseY)*(Cwide[i].y-mouseY));
              if(dist<=Cwide[i].radius) return Cwide[i];
          }
        }  
        break;  
        
        case 1:
        for(int i=0;i<data[0].length;i++){
        if(Cz1[i]!=null){
         float dist=sqrt((Cz1[i].x-mouseX)*(Cz1[i].x-mouseX)+(Cz1[i].y-mouseY)*(Cz1[i].y-mouseY));
         if(dist<=Cz1[i].radius)  return ct=Cz1[i];
        }
        }
        break;
        
        case 2:
        for(int i=0;i<data[0].length;i++){
        if(Cz2[i]!=null){
          float dist=sqrt((Cz2[i].x-mouseX)*(Cz2[i].x-mouseX)+(Cz2[i].y-mouseY)*(Cz2[i].y-mouseY));
          if(dist<=Cz2[i].radius) return ct=Cz2[i];
        }
        }
        break;
        
        case 3:
        for(int i=0;i<data[0].length;i++){
        if(Cz3[i]!=null){
          float dist=sqrt((Cz3[i].x-mouseX)*(Cz3[i].x-mouseX)+(Cz3[i].y-mouseY)*(Cz3[i].y-mouseY));
          if(dist<=Cz3[i].radius) return ct=Cz3[i];
        }
        }
        break;
    case 4:
        for(int i=0;i<data[0].length;i++){
        if(Cz4[i]!=null){
        float dist=sqrt((Cz4[i].x-mouseX)*(Cz4[i].x-mouseX)+(Cz4[i].y-mouseY)*(Cz4[i].y-mouseY));
        if(dist<=Cz4[i].radius) return ct=Cz4[i];
        }
        }
    break;
  }
  return null;
}
  


