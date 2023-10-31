package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    private String postcode;
    private String country;
    private String countryabbreviation;
    private List<Place> places;
    public String getPostcode() { //1. Getter/Setter, getter; _03_ApiPOJO class �nda �a��rmak i�in gerekli
        return postcode;
    }
    @JsonProperty("post code")
    // 3. API deki variable ismi "post code", Javada "postcode" olarak tan�mlad�k.
    // Bu ikisini e�le�tirmek i�in @JsonProperty etiketi kullan�yoruz ve bu �ekilde ismi par�al� olan t�m variable lar�n t�m set lerini etiketlemek zorunday�z!
    public void setPostcode(String postcode) { //1. Getter/Setter, setter; d�n���m (extract) yapmak i�in gerekli.
        // Ayr�ca d�n���m i�in dependency gerekli
        this.postcode = postcode; // dependency eklemeden testi �al��t�rd���m�zda;
        // Cannot parse object because no JSON deserializer found in classpath.
        // Please put either Jackson (Databind) or Gson in the classpath mesaj� gelecek.
        // 2. o y�zden pom.xml'e dependency eklendi.
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

    @Override // 4. yazd�rma yapmak i�in ekledik.
    public String toString() {
        return "Location{" +
                "postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryabbreviation='" + countryabbreviation + '\'' +
                ", places=" + places +
                '}';
    }
}
