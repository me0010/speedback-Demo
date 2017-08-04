/*
 Copyright 2017 Christopher Francis

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import java.util.HashMap;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
class DataScraper{
  public static void main(String[] args){
    if(args.length != 2){
      System.out.println("Usage: <Input File> <OutputFile>");
      System.exit(-1);
    }
    HashMap<Integer,LinkedList<Sign>> speedData = new HashMap<Integer,LinkedList<Sign>>();
    try{
      File file = new File(args[0]);
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      while((line = bufferedReader.readLine())!= null){
        String[] parts = line.split(",");
        Sign sign = new Sign();
        sign.speedSignId = parts[0];
        sign.location = parts[1];
        sign.speedLimit = parts[2];
        sign.latitude = parts[3];
        sign.longitude = parts[4];

        int key = calculateKey(parts[3], parts[4]);
        if(!speedData.containsKey(key)){
          LinkedList<Sign> signList = new LinkedList<Sign>();
          signList.add(sign);
          speedData.put(key, signList);
        } else {
          LinkedList<Sign> signList = speedData.get(key);
          signList.add(sign);
        }
      }
    } catch(Exception e){
      e.printStackTrace();
    }
    Gson gson = new Gson();
    //String json = gson.toJson(speedData);
    //System.out.println(json);
    try(FileWriter writer = new FileWriter(args[1])){
      gson.toJson(speedData,writer);
    } catch(IOException e){
      e.printStackTrace();
    }
  }
  private static int calculateKey(String lat, String lng){
    double latDouble = new Double(lat)*100;
    int latInt = (int)latDouble;
    double lngDouble = new Double(lng)*100;
    int lngInt = (int)lngDouble;
    return latInt*2*100000+lngInt*2;
  }
  private static class SignSector{
    public LinkedList<Sign> SignList;
    public SignSector(){
      SignList = new LinkedList<Sign>();
    }
  }
  private static class Sign{
    public String speedSignId;
    public String location;
    public String speedLimit;
    public String latitude;
    public String longitude;
  }
}
