package Constants;

public enum FacebookValues {
   LIFETIME_POST_TOTAL_IMPRESSIONS("Lifetime Post Total Impressions"),
   LIFETIME_POST_TOTAL_REACH("Lifetime Post Total Reach"),
   LIFETIME_TOTAL_VIDEO_VIEWS("Lifetime Total Video Views"),
   LIFETIME_UNIQUE_VIDEO_VIEWS("Lifetime Unique Video Views"),
   LIFETIME_TOTAL_30_SECOND_VIEWS("Lifetime Total 30-second Views"),
   LIFETIME_UNIQUE_30_SECOND_VIEWS("Lifetime Unique 30-second Views"),
   LIFETIME_TOTAL_WATCHES_AT_95("Lifetime Total watches at 95%"),
   LIFETIME_UNIQUE_WATCHES_AT_95("Lifetime Unique watches at 95%"),
   LIFETIME_TOTAL_60_SECOND_VIDEO_VIEWS("Lifetime Total 60-second video views"),
   LIFETIME_UNIQUE_60_SECOND_VIDEO_VIEWS("Lifetime Unique 60-second video views"),
   LIFETIME_AUTO_PLAYED_VIDEO_VIEWS("Lifetime Auto-played Video Views"),
   LIFETIME_CLICK_PLAYED_VIDEO_VIEWS("Lifetime Clicked-to-play Video Views"),
   LIFETIME_AUTO_PLAYED_30_SECOND_VIEWS("Lifetime Auto-played 30-second Views"),
   LIFETIME_CLICK_PLAYED_30_SECOND_VIEWS("Lifetime Clicked-to-play 30-second Views"),
   LIFETIME_AUTO_PLAYED_WATCHES_95("Lifetime Auto-played watches at 95%"),
   LIFETIME_CLICK_PLAYED_WATCHES_95("Lifetime Click-to-play watches at 95%"),
   LIFETIME_POST_CONSUMPTION_CLICKS_TO_PLAY("Lifetime Post Consumptions by type - clicks to play"),
   LIFETIME_POST_CONSUMPTION_OTHER_CLICKS("Lifetime Post Consumptions by type - other clicks"),
   LIFETIME_POST_CONSUMERS_CLICKS_TO_PLAY("Lifetime Post consumers by type - clicks to play"),
   LIFETIME_POST_CONSUMERS_OTHER_CLICKS("Lifetime Post consumers by type - other clicks"),
   LIFETIME_POST_STORIES_LIKE("Lifetime Post stories by action type - like"),
   LIFETIME_POST_STORIES_SHARE("Lifetime Post stories by action type - share"),
   LIFETIME_TALKING_ABOUT_THIS_POST_COMMENT("Lifetime Talking About This (Post) by action type - comment"),
   LIFETIME_TALKING_ABOUT_THIS_POST_LIKE("Lifetime Talking About This (Post) by action type - like"),
   LIFETIME_TALKING_ABOUT_THIS_POST_SHARE("Lifetime Talking About This (Post) by action type - share");


   private String value;

   FacebookValues(String value) {
       this.value = value;
   }

   public String getValue() {
      return value;
   }

}
