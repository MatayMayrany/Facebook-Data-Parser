package Constants;

public enum DanceTypes {
   BACHATA("bachata"),
   KIZOMBA("kizomba"),
   LINDY("lindy"),
   ROCK4("rock4"),
   ROCK6("rock6"),
   RUEDA_BACHATA("rueda - bachata"),
   SALSA_LA("salsa la"),
   SALSA_LA_BACHATA("salsa la - bachata"),
   TANGO("tango"),
   NO_LABEL("NO_LABEL");

   private String value;

   DanceTypes(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }
}
