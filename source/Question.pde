import java.util.ArrayList;

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
