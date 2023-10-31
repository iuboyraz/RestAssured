package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    private String postcode;
    private String country;
    private String countryabbreviation;
    private List<Place> places;
    public String getPostcode() { //1. Getter/Setter, getter; _03_ApiPOJO class ýnda çaðýrmak için gerekli
        return postcode;
    }
    @JsonProperty("post code")
    // 3. API deki variable ismi "post code", Javada "postcode" olarak tanýmladýk.
    // Bu ikisini eþleþtirmek için @JsonProperty etiketi kullanýyoruz ve bu þekilde ismi parçalý olan tüm variable larýn tüm set lerini etiketlemek zorundayýz!
    public void setPostcode(String postcode) { //1. Getter/Setter, setter; dönüþüm (extract) yapmak için gerekli.
        // Ayrýca dönüþüm için dependency gerekli
        this.postcode = postcode; // dependency eklemeden testi çalýþtýrdýðýmýzda;
        // Cannot parse object because no JSON deserializer found in classpath.
        // Please put either Jackson (Databind) or Gson in the classpath mesajý gelecek.
        // 2. o yüzden pom.xml'e dependency eklendi.
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryabbreviation() {
        return countryabbreviation;
    }
    @JsonProperty("country abbreviation")
    public void setCountryabbreviation(String countryabbreviation) {
        this.countryabbreviation = countryabbreviation;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override // 4. yazdýrma yapmak için ekledik.
    public String toString() {
        return "Location{" +
                "postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryabbreviation='" + countryabbreviation + '\'' +
                ", places=" + places +
                '}';
    }
}
