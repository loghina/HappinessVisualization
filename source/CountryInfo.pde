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
