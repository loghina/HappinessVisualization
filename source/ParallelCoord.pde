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

void draw(int f){
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
      stroke(color(#F70C10,200));
      strokeWeight(2);
      line(x_mouse,y_axis,x_mouse,end_y);
      noStroke();
      strokeWeight(1);
    } 
   
   if ((display) && (picked >= 0) && (picked<data.length)){        
        //println("flag2 ",f);
        String s=data[picked][0].name; 
        float len=textWidth(s);
        fill(#F7E15F,127);        
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
        stroke(color(#F70C10,200));
        strokeWeight(2);
        line(x_mouse,y_axis,x_mouse,end_y);
        noStroke();
        strokeWeight(1);       
        noFill();
   }    
}

void drawAxis(){
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

void drawGraph(){
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
          strokeWeight(0.3);
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

void mouseMoved(){
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

void selectCountry(int index,boolean vis){
  country_to_view=index;      
  c_visible=vis;
  redraw();
}

void selectQuestion(int index,boolean vis){
  question_to_view=index;      
  q_visible=vis;
  x_mouse=x_axis+(end_x-x_axis)/data.length*question_to_view;
  display=true;  
  redraw();
}

//*****************************************
void compareCountries(int i1,int i2){
  for (int i=0;i<visible_country.length;i++)
        visible_country[i]=false;  
  visible_country[i1]=true;
  visible_country[i2]=true;
  compareMode=true;
  c=0;
  ind1=i1;
  ind2=i2;
}

void viewAllCountries(){
  for (int i=0;i<visible_country.length;i++)
        visible_country[i]=true;  
  compareMode=false;
  c=0;
}
//*****************************************
}

