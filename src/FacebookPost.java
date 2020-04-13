package src;

import java.util.HashMap;
import java.util.Map;

class FacebookPost {

   private String danceType;
   private Map<String, Double> data = new HashMap<>();
   private boolean isLive;

   FacebookPost(String danceType, boolean isLive) {
      this.danceType = danceType == null || danceType.isBlank() ? "NO_LABEL" : danceType;
      this.isLive = isLive;
   }

   void addData(String title, Double value) {
      data.put(title, value);
   }

   Double getData(String title) {
      return data.get(title);
   }

   String getDanceType() {
      return danceType;
   }

   public boolean isLive() {
      return isLive;
   }
}
