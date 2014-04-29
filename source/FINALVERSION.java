import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.geom.*; 
import java.lang.Enum.*; 
import java.util.Arrays; 
import java.util.Comparator; 
import java.util.Map; 
import java.util.ArrayList; 
import java.util.regex.Pattern; 
import java.util.regex.Matcher; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class FINALVERSION extends PApplet {

/* Happiness project - visualize and analyse data coming from the Eurofound survey 2013 on happiness 
see http://www.eurofound.europa.eu/surveys/smt/eqls/results.htm for more details
Authors: Alexandru Loghin, Francesco Santoro, Chiara Prencipe, Cyril Sabbagh
*/






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


public void setup() {
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
  flag2 = new EuropeanFlag2(140,250,150,100,0xff0055A5,img);
  
  PImage bg = loadImage("pnggender/mf.png");
  PImage fg = loadImage("pnggender/female.png");
  PImage mg = loadImage("pnggender/male.png");
  
  both = new GenderSymbol(62,480,40,85,0xff0055A5,bg);
  female = new GenderSymbol(137,480,40,85,0xff0055A5,fg);
  male = new GenderSymbol(212,480,40,85,0xff0055A5,mg);
 
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
 
public void draw(){
  background(255);
  switch(map){
    case 0:  
      scale(w*0.55f/ (float)mapBounds.getWidth(), -h*0.85f/(float)mapBounds.getHeight());
      translate(50, -80);  //global
    break;  
    case 1:
      scale(w*1.1f/ (float)mapBounds.getWidth(), -1.4f*h /(float)mapBounds.getHeight());  
      translate(25, -75);  //NW
    break;
    case 2:  
      scale(1.1f*w/ (float)mapBounds.getWidth(), -1.4f*h /(float)mapBounds.getHeight());  
      translate(28, -60);  //SW
    break;
    case 3:  
      scale(1.1f*w/ (float)mapBounds.getWidth(), -1.4f*h /(float)mapBounds.getHeight());  
      translate(8, -77);  //NE
    break;
    case 4:
      scale(1.1f*w/ (float)mapBounds.getWidth(), -1.4f*h /(float)mapBounds.getHeight());  
      translate(6, -63);  //SE
    break;
  }
  
  // Set the same visual attributes for all countries
  fill(color(0, 0, 255, 20));
  stroke(0);
  strokeWeight(0.1f);
  
  // Draw all countries
  for (int i = 0; i < countries.length; i++) {
    countries[i].draw();
  }
      
  //undo changes
  switch(map){
    case 0:
      translate(-50, 80);  //global  
      scale((float)mapBounds.getWidth()/(w*0.55f), -(float)mapBounds.getHeight()/(h*0.85f));
      
    break;  
    case 1:
      translate(-25, 75);  //NW
      scale((float)mapBounds.getWidth()/(w*1.1f), -(float)mapBounds.getHeight()/(h*1.4f));  
      //translate(-20, 70);  //NW
    break;
    case 2:
      translate(-28, 60);  //SW  
      scale((float)mapBounds.getWidth()/(w*1.1f), -(float)mapBounds.getHeight()/(h*1.4f));  
      //translate(-20, 55);  //SW
    break;
    case 3:
      translate(-8, 77);  //NE  
      scale((float)mapBounds.getWidth()/(w*1.1f), -(float)mapBounds.getHeight()/(h*1.4f));  
      //translate(5, 70);  //NE
    break;
    case 4:
      translate(-6, 63);  //SE
      scale((float)mapBounds.getWidth()/(w*1.1f), -(float)mapBounds.getHeight()/(h*1.4f));  
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

public void drawSmallMap(){
  
  scale(0.2f*w / (float)mapBounds.getWidth(), -0.2f*h /(float)mapBounds.getHeight());
  //translate(25,-225);
  translate(25,-243);

  // Set the same visual attributes for all countries
  fill(color(0, 0, 255, 20));
  stroke(0);
  strokeWeight(0.1f);
    
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

public void mouseClicked(){
 
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
public void mouseMoved() {
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
 
public void mouseDragged(){
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

public void mouseReleased() {
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
class CountryInfo{

  
  PImage img;
  String countryName;
  String initials;
  int countryIndex;
  
  public CountryInfo(String filename, String country_name, String initials, int countryIndex){
    
    this.img = loadImage(filename);
    this.countryName = country_name;
    this.countryIndex = countryIndex;
    this.initials = initials;
    
  }
  
  

}
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
    
    int col;
    
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
   
     //questo serve per scalare il valore di felicit\u00e0 su un'insieme pi\u00f9 grande x dare lo spessore ai settori
     f = map(aggr_value,1,10,10,60);
     mean_mapped = map(mean_value,1,10,10,60);
     
     stroke_size = PApplet.parseInt(f);
   
     if (f >= mean_mapped) col = 0xffA0F9BE;
     else col = 0xffF97070;
   
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
   
   int score = PApplet.parseInt(mean_value);
   
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
 
  public void draw() {
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
  
  public Rectangle2D getBounds() {
    return path.getBounds2D();
  }
  
  public boolean contains(float x, float y) {
    return path.contains(x, y);
  }
  
  //-------------------------------------------
  public void select(boolean s){
    this.selected=s;  
  }
  
  /*static void setMapBounds(Rectangle2D map, int w2, int h2){
    this.w=w2;
    this.h=h2;
    this.mapB=map;
  }*/
  
  
}
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
 
public void drawDrop() {
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


class EuropeanFlag2{

  SectorCircular[] sectors;
  int win_x_center;
  int win_y_center;
  float fWidth;
  float fHeight;
  int flag_color;
  PImage img;
  
  public EuropeanFlag2(int x_pos, int y_pos, float fWidth, float fHeight, int fColor, PImage p){
    
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
     sectors[i] = new SectorCircular(win_x_center,win_y_center,12,(i)*TWO_PI/12,(i+1)*TWO_PI/12,(TWO_PI/12)/2,PApplet.parseInt((fHeight/3)),flag_color);
     //sectors[i].drawSector();
    }
    
    textAlign(CENTER);
    fill(0);
    text(s,win_x_center,win_y_center-(fHeight/2)-30);
    noFill();
     
  }
  
  public void drawScore(int score){
    
    score = PApplet.parseInt(map(score,1,10,1,12));
    
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
class GenderSymbol{

  float win_x_center;
  float win_y_center;
  float fWidth;
  float fHeight;
  PImage img;
  int bcolor;
  
  public GenderSymbol(float x_pos, float y_pos, float fWidth, float fHeight, int c, PImage p){
  
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
      line(x_pos+PApplet.parseInt(n),y_pos-4,x_pos+PApplet.parseInt(n),y_pos+bar_height+8);
      noStroke();
    }
    
  }
  
  public void drawImg(){
    
    imageMode(CORNER);
    image(img,995,180,185,70);
    
    float n = map(this.mean_value,1,10,1,bar_width);
    
    strokeWeight(4);
    stroke(0xff0021F9);
    line(x_pos+PApplet.parseInt(n),y_pos-5,x_pos+PApplet.parseInt(n),y_pos+bar_height+12);
    noStroke();
    
  }

}
class ParallelCoordinates{
  
//matrix with (x,y) coord for all countries
//[question, country, coord]
float[][][] countries;  
boolean display=false;
float x_mouse,y_mouse;
PFont font;
int picked=-1;
int x_axis, y_axis,end_x,end_y;
int country_to_view=-1, question_to_view=-1;
boolean c_visible=false,q_visible=false;
//***************************
boolean[] visible_country;   
boolean compareMode=false;
int c=0;
int ind1,ind2;
//***************************

Question[][] data;

public ParallelCoordinates(Question[][] d){
  this.data=d;
  font=loadFont("ChalkboardSE-Light-48.vlw");
  textFont(font, 24);
  
   //********************************************
  visible_country=new boolean[34];
  viewAllCountries();
  //********************************************
  
}

public void draw(int f){
  textAlign(CENTER,BOTTOM);
  x_axis=920;
  y_axis=665;
  end_x=1230;
  end_y=550;
  fill(color(200,150));
  rectMode(CORNER);
  rect(x_axis-20,end_y-30,end_x-x_axis+30,y_axis-end_y+50);  
  drawAxis();
  drawGraph();
  
  if ((display)&&(q_visible==true)){  
      //show the selected question, with a rectangle behind it 
      stroke(color(0xffF70C10,200));
      strokeWeight(2);
      line(x_mouse,y_axis,x_mouse,end_y);
      noStroke();
      strokeWeight(1);
    } 
   
   if ((display) && (picked >= 0) && (picked<data.length)){        
        //println("flag2 ",f);
        String s=data[picked][0].name; 
        float len=textWidth(s);
        fill(0xffF7E15F,127);        
        if (x_mouse-len/2-5<=0){
          textAlign(LEFT,BOTTOM);
          rect(x_mouse,y_axis-24,len+10,24);        
        }else if (x_mouse+len/2+5>=width){
          textAlign(RIGHT,BOTTOM);
          rect(x_mouse-len-5,y_axis-24,len+10,24);
        }else{  
          textAlign(CENTER,BOTTOM);
          rect(x_mouse-len/2-5,y_axis-24,len+10,24);
        }        
        fill(0);     
        text(s,x_mouse,y_axis);  
        stroke(color(0xffF70C10,200));
        strokeWeight(2);
        line(x_mouse,y_axis,x_mouse,end_y);
        noStroke();
        strokeWeight(1);       
        noFill();
   }    
}

public void drawAxis(){
  stroke(color(0,60));
  strokeWeight(1);
  fill(color(50,200));
  textSize(14);
  line(x_axis,y_axis,end_x,y_axis);
  line(x_axis,y_axis,x_axis,end_y-20);
  
  int x=x_axis;
  for (int i=0;i<data.length;i++){
    //text(questions[i],x,y_axis+20);
    line(x,y_axis-2,x,y_axis+2);
    fill(color(50,50));
    ellipse(x,y_axis,6,6);
    x+=(end_x-x_axis)/data.length;
    fill(color(50,200));
  
  }
  int y=y_axis;
  for (int i=0;i<=10;i+=2){
    text(i,x_axis-10,y);
    line(x_axis-2,y,x_axis+2,y);
    y-=2*(y_axis-end_y)/10;
  }  
  
  line(end_x-10,y_axis-5,end_x,y_axis);
  line(end_x-10,y_axis+5,end_x,y_axis);
  text("Questions",end_x-20,y_axis+20);
  line(x_axis-5,end_y-10,x_axis,end_y-20);
  line(x_axis+5,end_y-10,x_axis,end_y-20);
  text("Values",x_axis+25,end_y-10);
  textSize(18);
}

public void drawGraph(){
  countries=new float[21][34][2]; 
  //country2=new float[20][2];  
  float x=x_axis;  
  float y;
  
        
  for (int i=0;i<data.length;i++){
    for (int j=0;j<data[i].length;j++){
      y=(10-data[i][j].getBothGenderValue())*(y_axis-end_y)/10+end_y;
      countries[i][j][0]=x;
      countries[i][j][1]=y;
    }
    for (int j=0;j<data[i].length;j++){
      if (i>0){
        if ((j==country_to_view)&&(c_visible==true)){
          stroke(color(20,0,255,200));
          strokeWeight(2);
        }
        else{
          stroke(color(20,0,255,100));
          strokeWeight(0.3f);
        }
         //***********************************
        if (visible_country[j]==true){
          if (compareMode==true){
            textAlign(LEFT,BOTTOM);
            strokeWeight(2);
            stroke(color(20,0,255));
            line(x_axis+100,end_y-15,x_axis+120,end_y-15);
            fill(color(20,0,255));
            textSize(15);
            text(data[0][ind1].country,x_axis+130,end_y-10);
            stroke(color(255,0,30));
            line(x_axis+170,end_y-15,x_axis+190,end_y-15);
            fill(color(255,0,30));
            text(data[0][ind2].country,x_axis+200,end_y-10);
            textSize(18); 
            c++;
            if (c==1){
            stroke(color(20,0,255,200));
            strokeWeight(2);
            }else if (c==2){
            stroke(color(255,0,30,200));
            strokeWeight(2);  
            c=0;          
            }
          }
        //**********************************
        line(countries[i-1][j][0],countries[i-1][j][1],countries[i][j][0],countries[i][j][1]);
        //noStroke();
        }
      }
    }
    x+=(end_x-x_axis)/data.length;    
    
  }
}

public void mouseMoved(){
  fill(0);
  //check if mouse over one of the ellipses in the graphic
  //display=false;
  picked=-1; 
  
  int i=0;
  if ((mouseY>=y_axis-3)&&(mouseY<=y_axis+3)){
    for (int x=x_axis;x<end_x;x+=(end_x-x_axis)/data.length){
      if ((mouseX>=x-3)&&(mouseX<=x+3)){        
        display=true;
        x_mouse=x;
        picked=i;        
        return;
      }
     i++; 
    }   
  }
  
  //picked = -1;
  //display = false;
}

public void selectCountry(int index,boolean vis){
  country_to_view=index;      
  c_visible=vis;
  redraw();
}

public void selectQuestion(int index,boolean vis){
  question_to_view=index;      
  q_visible=vis;
  x_mouse=x_axis+(end_x-x_axis)/data.length*question_to_view;
  display=true;  
  redraw();
}

//*****************************************
public void compareCountries(int i1,int i2){
  for (int i=0;i<visible_country.length;i++)
        visible_country[i]=false;  
  visible_country[i1]=true;
  visible_country[i2]=true;
  compareMode=true;
  c=0;
  ind1=i1;
  ind2=i2;
}

public void viewAllCountries(){
  for (int i=0;i<visible_country.length;i++)
        visible_country[i]=true;  
  compareMode=false;
  c=0;
}
//*****************************************
}



class Question {
  String name;
  String country;
  int nb_reponse;
  int nb_countries;
  
  ArrayList<Float> gender_both = new ArrayList<Float>();
  ArrayList<Float> gender_female = new ArrayList<Float>();
  ArrayList<Float> gender_male = new ArrayList<Float>();
  
  float gender_both_value;
  float gender_female_value;
  float gender_male_value;
  
  Question(Table table, int row) {
    
    nb_countries = row;
    country = table.get(0,row);
    name = table.get(1, row);
    nb_reponse = (table.getColumnCount() - 2)/3;
    
    nb_countries = table.getRowCount();
    
    //this.nb_reponse = nb_rep;
    
    for(int i=2;i < nb_reponse + 2;i++){
      gender_both.add(table.getFloat(i, row));
      gender_female.add(table.getFloat(i+nb_reponse, row));
      gender_male.add(table.getFloat(i+2*nb_reponse, row));
    }
    
    gender_both_value = 0;
    gender_female_value = 0;
    gender_male_value = 0;
    
    
    calculateAggregateValue();
    
  }
  
  public void calculateAggregateValue(){
    
    if (nb_reponse == 1){
      
      gender_both_value = gender_both.get(0).floatValue();
      gender_female_value = gender_female.get(0).floatValue();
      gender_male_value = gender_male.get(0).floatValue();
      return;
    }
    
    for (int i=1; i <= nb_reponse; i++){
      
      float multiplier = (float)i/nb_reponse;
      
      gender_both_value += gender_both.get(i-1).floatValue() * multiplier;
      gender_female_value += gender_female.get(i-1).floatValue() * multiplier;
      gender_male_value += gender_male.get(i-1).floatValue() * multiplier;
      
    }
    
    gender_both_value = map(gender_both_value,0,100,1,10);
    gender_female_value = map(gender_female_value,0,100,1,10);
    gender_male_value = map(gender_male_value,0,100,1,10);
  
  }
  
  public float getBothGenderValue(){
    return gender_both_value;
  }
  
  public int getNbCountries(){
    return nb_countries;
  }
  
  public float getFemaleGenderValue(){
    return gender_female_value;
  }
  
  public float getMaleGenderValue(){
    return gender_male_value;
  }
  
}
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
    
    int col;
  
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
   
     //questo serve per scalare il valore di felicit\u00e0 su un'insieme pi\u00f9 grande x dare lo spessore ai settori
     f = map(aggr_value,1,10,10,60);
     mean_mapped = map(mean_value,1,10,10,60);
     stroke_size = PApplet.parseInt(f);
     
     if (f >= mean_mapped) col = 0xffA0F9BE;
     else col = 0xffF97070;
   
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
    int score = PApplet.parseInt(mean_value);
   
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
  int fill_color;
  boolean data_visibility;
  float current_value;
  
  
  public SectorCircular(int win_x_center, int win_y_center, float sSize, float angle_beg, float angle_end, float offset, int radius, int fill_color){
    
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
      
      x = x_info + 40 + PApplet.parseInt(offset/2);
      y = y_info;
      
    }
    else if ( x_info > win_x_center ){
      
      x = x_info - 40 - PApplet.parseInt(offset/2);
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
      
      x = x_info + distanceFromSector +  PApplet.parseInt(offset/2);
      y = y_info;
      
    }
    else if ( x_info > win_x_center ){
      
      x = x_info - distanceFromSector - PApplet.parseInt(offset/2);
      y = y_info;
      
    }
    
    fill(0xffDCDCDC);
    rect(x,y,offset,32);
    
    fill(0);
    text(v,x,y);
    
    noFill();
    
  }
  
  public void drawCircle(int col, float Rradius){
    
    ellipseMode(CENTER);
    noStroke();
    fill(col);
    ellipse(x_center,y_center,Rradius,Rradius);
    
  }

}
//
// Loads a csv or tsv file into a table structure.
//
// Code taken from Ben Fry's "Visualizing Data" book, example ch03-usmap
// and extended by Pierre Dragicevic. Uses regex parsing code from Nathan Spears.
//
// @author Ben Fry, Pierre Dragicevic, Nathan Spears.
//




class Table {
  int rowCount = 0;
  int colCount = 0;
  String[][] data = null;
      
  // Creates a Table from a csv or tsv file.
  //
  // The separator between rows in the input file has to be a carriage return.
  // The separator between columns is provided as a constructor argument and can be anything like "\t", ",", ";", etc.
  // Double quote delimiters are properly handled, i.e., formats like 1000,"100,000",10.
  // All empty lines and lines starting with # are silently skipped. The third argument speficies additional lines to
  // ignore (line counting starts from 1) and can be set to null. The first unskipped line in the input file needs to
  // contain column names.
  Table(String filename, String separator, int[] linesToIgnore) {
    
    print("Loading file " + filename + "... ");

    String[] lines = loadStrings(filename);
    
    println("Done.");
    print("Creating table... ");

    data = new String[lines.length][];
    csvPattern = Pattern.compile("\"([^\"]*)\"|(?<=" + separator + "|^)([^" + separator + "]*)(?:" + separator + "|$)");
    
    for (int i = 0; i < lines.length; i++) {
      
      // skip empty rows
      if (trim(lines[i]).length() == 0)
        continue; 
      
      // skip comment lines
      if (lines[i].startsWith("#"))
        continue;
      
      // skip lines to ignore
      if (linesToIgnore != null && contains(linesToIgnore, i+1))
        continue;
      
      // split the line on the separator
      String line = lines[i];
      String[] pieces = parseCsvLine(line);
      
      // if first row (labels), count the number of columns
      if (rowCount == 0)
        colCount = pieces.length;
      else if (pieces.length != colCount) {
        // otherwise, check for consistency
        println("WARNING - line " + (i+1) + " has been ignored since it contains " + pieces.length + " cells instead of " + colCount + ". ");
        continue;
      }
      
      // copy to the table array
      data[rowCount] = pieces;
      rowCount++;
    }
       
    // resize the 'data' array as necessary
    data = (String[][]) subset(data, 0, rowCount);
   
    rowCount--;
 
    println("Done. Created " + colCount + " columns and " + rowCount + " rows.");

  }
  
  // Copies the table
  Table(Table table) {
      this.csvPattern = table.csvPattern;
      this.rowCount = table.rowCount;
      this.colCount = table.colCount;
      
      data = new String[table.data.length][];
      for (int idx = 0; idx < table.data.length; ++idx)
          data[idx] = table.data[idx].clone();
  }
  
  // Returns the number of rows, not counting the first row with column names
  public int getRowCount() {
    return rowCount;
  }

  // Returns the number of columns
  public int getColumnCount() {
    return colCount;
  }
  
  // Find a column by its name, returns -1 if no column found
  public int getColumnIndex(String name) {
    for (int i = 0; i < colCount; i++) {
      if (data[0][i].equals(name)) {
        return i;
      }
    }
    println("No column named '" + name + "' was found");
    return -1;
  }
  
  // Returns the name of the column of given index
  public String getColumnName(int column) {
    return data[0][column];
  }

  // Reads a cell value. Rows and column indices start from zero.
  public String get(int column, int row) {
    return data[row+1][column]; // do not count column names
  }

  // Reads a cell value as an int. Rows and column indices start from zero.
  // This method will try to parse dirty numerical formats by removing spaces and commas.
  // If the cell cannot be parsed as an integer, returns Integer.MAX_VALUE.
  // Avoid calling this method multiples times, as it is rather slow. See getColumnAsInts()
  // for an alternative.
  public int getInt(int column, int row) {
    return parseInt(get(column, row));
  }

  // Reads a cell value as a float. Rows and column indices start from zero.
  // This method will try to parse dirty numerical formats by removing spaces and commas.
  // If the cell cannot be parsed as an integer, returns NaN.
  // Avoid calling this method multiples times, as it is rather slow. See getColumnAsFloats()
  // for an alternative.
  public float getFloat(int column, int row) {
    return parseFloat(get(column, row));
  }
  
  // Extracts a column as an array of strings.
  public String[] getColumn(int column) {
    String[] col = new String[rowCount];
    for (int i=0; i < rowCount; i++)
      col[i] = get(column, i);
    return col;
  }
  
  // Extracts a column as a numerical array for a more optimized processing in case
  // numerical values have to be read multiple times.
  // See getInt() for how non-numerical cell values are handled.
  public int[] getColumnAsInts(int column) {
    int[] col = new int[rowCount];
    for (int i=0; i < rowCount; i++)
      col[i] = smartParseInt(get(column, i));
    return col;
  }

  // Extracts a column as a numerical array for a more optimized processing in case
  // numerical values have to be read multiple times.
  // See getFloat() for how non-numerical cell values are handled.
  public float[] getColumnAsFloats(int column) {
    float[] col = new float[rowCount];
    for (int i=0; i < rowCount; i++)
      col[i] = smartParseFloat(get(column, i));
    return col;
  }
    
  // Modifies a cell. Rows and column indices start from zero.
  public void set(int column, int row, String value) {
    data[row+1][column] = value;
  }
   
  // Saves the table to a csv file
  public void save(String filename, String separator) {
    PrintWriter output = createWriter(filename);
    for (int c = 0; c < colCount; c++) {
      String cell = getColumnName(c);
      if (cell.indexOf(separator) != -1)
        cell = "\"" + cell + "\"";
      output.print(cell + ((c < colCount - 1) ? separator : "\n"));
    }
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c++) {
        String cell = get(c, r);
        if (cell.indexOf(separator) != -1)
          cell = "\"" + cell + "\"";
        output.print(cell + ((c < colCount - 1) ? separator : "\n"));
      }
    }
    output.flush();
    output.close();
  }
  
  ///////////////////////////////////////////////////////////////////////////////////////////////////
  // utility functions
  ///////////////////////////////////////////////////////////////////////////////////////////////////
  
  private boolean contains(int[] arr, int value) {
    for (int i=0; i<arr.length; i++)
      if (arr[i] == value)
        return true;
    return false;
  }
  
  public int smartParseInt(String s) {
    // We use isInt here because contrary to the specification, parseInt does not return NaN if the string is not parseable.
    if (isInt(s))
      return parseInt(s);
    else {
      s = s.replaceAll(" ", ""); // remove spaces
      s = s.replaceAll(",", ""); // remove commas
      if (isInt(s)) {
        return parseInt(s);
      }
    }
    return Integer.MAX_VALUE;
  }
  
  public float smartParseFloat(String s) {
    float value = parseFloat(s);
    if (Float.isNaN(value)) {
      s = s.replaceAll(" ", ""); // remove spaces
      s = s.replaceAll(",", ""); // remove commas
      value = parseFloat(s);
    }
    return value;
  }
  
  public boolean isInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch(NumberFormatException e) {
      return false;
    }
  }

  // The following code replaces split() since it's not capable of handling cell values occasionally surrounded by double quotes.
  // Code taken from Nathan Spears, http://stackoverflow.com/questions/1441556/parsing-csv-input-with-a-regex-in-java
  
  private final Pattern csvPattern;  
  private ArrayList<String> allMatches = new ArrayList<String>();        
  private Matcher matcher = null;
  private String match = null;
  private int size;

  public String[] parseCsvLine(String csvLine) {
      Matcher matcher = csvPattern.matcher(csvLine);
      allMatches.clear();
      while (matcher.find()) {
              match = matcher.group(1);
              if (match!=null) {
                      allMatches.add(match);
              }
              else {
                      allMatches.add(matcher.group(2));
              }
      }

      size = allMatches.size();               
      if (size > 0) {
              return allMatches.toArray(new String[size]);
      }
      else {
              return new String[0];
      }                       
  }  
 
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "FINALVERSION" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
