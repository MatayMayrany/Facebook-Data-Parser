package src;

import Constants.DanceTypes;
import Constants.FacebookValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

   private static Map<String, Integer> valueToIndexMap = new HashMap<>();
   private static List<FacebookPost> posts = new ArrayList<>();

   private static Map<String, Map<String, Double>> averageValuesPerDanceType = new HashMap<>();
   private static Map<String, Map<String, Double>> maxValuesPerDanceType = new HashMap<>();
   private static Map<String, Map<String, Double>> minValuesPerDanceType = new HashMap<>();

   private static Map<String, Double> averageLiveValues = new HashMap<>();
   private static Map<String, Double> maxLiveValues = new HashMap<>();
   private static Map<String, Double> minLiveValues = new HashMap<>();

   private static Map<String, Double> averageRecordedValues = new HashMap<>();
   private static Map<String, Double> maxRecordedValues = new HashMap<>();
   private static Map<String, Double> minRecordedValues = new HashMap<>();

   public static void main(String[] args) throws IOException {
//      fixData();
      //converting data to facebook post objects
      convertDataToFacebookPosts();
      //comparing posts based on dance type
      buildDataPerDanceType();
      writeDataPerDanceType();
      //comparing posts based on whether they are live streams or not
      buildDataBasedOnLiveStatus();
      writeDataBasedOnLiveStatus();
   }

   private static void convertDataToFacebookPosts() throws IOException {
      BufferedReader csvReader = new BufferedReader(
         new FileReader("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/rawFacebookData/rawdata.csv"));

      //first row is the titles
      String row = csvReader.readLine();
      buildValueToIndexMap(row);

      //skipping explanations
      csvReader.readLine();

      //building objects for each post and collecting them
      while ((row = csvReader.readLine()) != null) {
         String[] currentRowData = row.split(",");
         //create a new post object with the dance type
         FacebookPost facebookPost = new FacebookPost(
            currentRowData[valueToIndexMap.getOrDefault("Custom labels", 9)],
            currentRowData[valueToIndexMap.getOrDefault("Is broadcast", 10)].equals("1"));
         //Collecting data for this individual post
         for (FacebookValues value : FacebookValues.values()) {
            if (currentRowData[valueToIndexMap.get(value.getValue())].matches("-?\\d+")) {
               Double currentValue = Double.parseDouble(currentRowData[valueToIndexMap.get(value.getValue())]);
               facebookPost.addData(value.getValue(), currentValue);
            } else {
               facebookPost.addData(value.getValue() + " - MISSING", 0.0);
            }
         }
         posts.add(facebookPost);
      }

      csvReader.close();
   }

   private static void writeDataPerDanceType() throws FileNotFoundException {
      // Average
      writeFile("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/perDanceType/avg.csv", averageValuesPerDanceType);
      //Max
      writeFile("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/perDanceType/max.csv", maxValuesPerDanceType);
      //Min
      writeFile("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/perDanceType/min.csv", minValuesPerDanceType);
   }

   private static void writeFile(String filePath, Map<String, Map<String, Double>> fileContent) throws FileNotFoundException {
      File csvOutputFile = new File(filePath);
      try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
         getDataLines(fileContent).forEach(pw::println);
      }
      assert (csvOutputFile.exists());
   }

   private static List<String> getDataLines(Map<String, Map<String, Double>> map) {
      List<String> dataLines = new ArrayList<>();
      // write dance titles
      String titles = "," + String.join(",", map.keySet());
      dataLines.add(titles);
      for (FacebookValues facebookValue : FacebookValues.values()) {
         StringBuilder lineBuilder = new StringBuilder();
         lineBuilder.append(facebookValue.getValue()).append(",");
         for (String danceType : map.keySet()) {
            lineBuilder.append(map.get(danceType).get(facebookValue.getValue())).append(",");
         }
         lineBuilder.replace(lineBuilder.lastIndexOf(","), lineBuilder.lastIndexOf(",") + 1, "\n");
         dataLines.add(lineBuilder.toString());
      }
      return dataLines;
   }

   private static void writeDataBasedOnLiveStatus() throws FileNotFoundException {
      writePair("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/live/avgLive.csv", averageLiveValues);
      writePair("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/live/maxLive.csv", maxLiveValues);
      writePair("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/live/minLive.csv", minLiveValues);
      writePair(
         "/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/recorded/avgRecorded.csv",
         averageRecordedValues);
      writePair(
         "/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/recorded/maxRecorded.csv",
         maxRecordedValues);
      writePair(
         "/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/recorded/minRecorded.csv",
         minRecordedValues);
   }

   private static void writePair(String filePath, Map<String, Double> fileContent) throws FileNotFoundException {
      File csvOutputFile = new File(filePath);
      try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
         fileContent.forEach((key, value) -> {
            pw.println(key + "," + value);
         });
      }
      assert (csvOutputFile.exists());
   }

   private static void buildValueToIndexMap(String row) {
      int index = 0;
      for (String value : row.split(",")) {
         valueToIndexMap.put(value.replaceAll("^\"|\"$", ""), index++);
      }
   }

   private static void buildDataPerDanceType() {
      for (DanceTypes danceType : DanceTypes.values()) {
         Map<String, Double> averageMap = new HashMap<>();
         Map<String, Double> maximumMap = new HashMap<>();
         Map<String, Double> minimumMap = new HashMap<>();

         for (FacebookValues facebookValue : FacebookValues.values()) {
            List<Double> allForThisValueAndDanceType = posts.stream()
               .filter(post -> post.getDanceType().equals(danceType.getValue()))
               .map(post -> post.getData(facebookValue.getValue()))
               .collect(Collectors.toList());

            OptionalDouble avgForThisValueAndDanceType = allForThisValueAndDanceType.stream()
               .filter(Objects::nonNull)
               .mapToDouble(a -> a)
               .average();
            OptionalDouble maxForThisValueAndDanceType = allForThisValueAndDanceType.stream()
               .filter(Objects::nonNull)
               .mapToDouble(a -> a)
               .max();
            OptionalDouble minForThisValueAndDanceType = allForThisValueAndDanceType.stream()
               .filter(Objects::nonNull)
               .mapToDouble(a -> a)
               .min();

            averageMap.put(
               facebookValue.getValue(),
               avgForThisValueAndDanceType.isPresent() ? avgForThisValueAndDanceType.getAsDouble() : 0.0
            );

            maximumMap.put(
               facebookValue.getValue(),
               maxForThisValueAndDanceType.isPresent() ? maxForThisValueAndDanceType.getAsDouble() : 0.0
            );

            minimumMap.put(
               facebookValue.getValue(),
               minForThisValueAndDanceType.isPresent() ? minForThisValueAndDanceType.getAsDouble() : 0.0);

         }
         averageValuesPerDanceType.put(danceType.getValue(), averageMap);
         maxValuesPerDanceType.put(danceType.getValue(), maximumMap);
         minValuesPerDanceType.put(danceType.getValue(), minimumMap);
      }
   }

   private static void buildDataBasedOnLiveStatus() {
      for (FacebookValues facebookValue : FacebookValues.values()) {

         List<Double> allLiveForThisValue = posts.stream()
            .filter(FacebookPost::isLive)
            .map(post -> post.getData(facebookValue.getValue()))
            .collect(Collectors.toList());

         List<Double> allRecordedForThisPost = posts.stream()
            .filter(facebookPost -> !facebookPost.isLive())
            .map(post -> post.getData(facebookValue.getValue()))
            .collect(Collectors.toList());

         OptionalDouble avgLive = allLiveForThisValue.stream().filter(Objects::nonNull).mapToDouble(a -> a).average();
         OptionalDouble maxLive = allLiveForThisValue.stream().filter(Objects::nonNull).mapToDouble(a -> a).max();
         OptionalDouble minLive = allLiveForThisValue.stream().filter(Objects::nonNull).mapToDouble(a -> a).min();

         averageLiveValues.put(facebookValue.getValue(), avgLive.isPresent() ? avgLive.getAsDouble() : 0.0);
         maxLiveValues.put(facebookValue.getValue(), maxLive.isPresent() ? maxLive.getAsDouble() : 0.0);
         minLiveValues.put(facebookValue.getValue(), minLive.isPresent() ? minLive.getAsDouble() : 0.0);

         OptionalDouble avgRecorded = allRecordedForThisPost.stream().filter(Objects::nonNull).mapToDouble(a -> a).average();
         OptionalDouble maxRecorded = allRecordedForThisPost.stream().filter(Objects::nonNull).mapToDouble(a -> a).max();
         OptionalDouble minRecorded = allRecordedForThisPost.stream().filter(Objects::nonNull).mapToDouble(a -> a).min();

         averageRecordedValues.put(facebookValue.getValue(), avgRecorded.isPresent() ? avgRecorded.getAsDouble() : 0.0);
         maxRecordedValues.put(facebookValue.getValue(), maxRecorded.isPresent() ? maxRecorded.getAsDouble() : 0.0);
         minRecordedValues.put(facebookValue.getValue(), minRecorded.isPresent() ? minRecorded.getAsDouble() : 0.0);
      }

   }

   private static void fixData() throws IOException {
      BufferedReader csvReader = new BufferedReader(new FileReader(
         "/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/rawFacebookData/rawData.csv"));

      StringBuilder builder = new StringBuilder();
      Stream<String> data = csvReader.lines();
      data.forEach(line -> {
         builder.append(line
            .replace(",,,,", ", , , ,")
            .replace(",,,", ", , ,")
            .replace(",,", ", ,")
            .trim())
            .append(line.charAt(line.length() - 1) == ',' ? " \n" : "\n");
      });

      File testOutputfile = new File("/Users/mataymayrany/Desktop/kth/FacebookDataParser/resources/rawData.csv");
      try (PrintWriter pw = new PrintWriter(testOutputfile)) {
         builder.toString().lines().forEach(pw::println);
      }
   }

}

