package com.chartmultiplexaxes.chartmultiplexaxes.service;
import com.chartmultiplexaxes.chartmultiplexaxes.dto.DataDTO;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;



@Service
public class ChartDataService {

    public DataDTO getData(){

       Map<LocalTime,Float> values = new HashMap<>();
       Map<LocalTime,Float> values2 = new HashMap<>();
       Map<Integer,Float> intMap1 = new HashMap<>();
       Map<Integer,Float> intMap2 = new HashMap<>();

       Random rand = new Random();

        LocalTime time =  LocalTime.MIN;
        Integer counter = 0;
        while(time.isBefore(LocalTime.MIN.plusMinutes(360))){
//            int r1 = rand.nextInt(10);
//            int r2 = rand.nextInt(10);
//            values.put(time,r1);
//            values2.put(time, r2);
//            intMap1.put(counter, r1 );
//            intMap2.put(counter, r2 );
//            values.put(time,0f);
//            values2.put(time, 0f);
            intMap1.put(counter, (float) 9);
            intMap2.put(counter, (float) 9);
            time = time.plusMinutes(10);
            counter++;
        }

        int r3 = rand.nextInt(10);
        int r4 = rand.nextInt(10);

//        intMap1.put(r3, (float) r3);
//        intMap2.put(r4, (float) r4);

        intMap1.put(10, 0f);
        intMap1.put(11, 0f);
        intMap1.put(12, 0f);

        List<Float> map1Values = new ArrayList<>(intMap1.values());
        List<Float> map2Values = new ArrayList<>(intMap2.values());

        // REPLACE FIRST  BY AVERAGE IF NEXT IS NOT ZERO

        for(int i=0;i<2;i++){
            intMap1 = replaceWithAverage(map1Values, intMap1);
            map1Values = new ArrayList<>(intMap1.values());
        }

        intMap1.values().forEach(System.out::println);


        return new DataDTO(values, values2, intMap1, intMap2);

    }


    public Float average(Float a, Float b){
        return (a+b)/2;
    }

    public Map<Integer,Float> replaceWithAverage(List<Float> map1Values, Map<Integer, Float> intMap1){

        for(int i=0 ; i < map1Values.size()-1 ; i++){

            if(map1Values.get(i)==0 && map1Values.get(i+1)!=0){
                Float average = average(map1Values.get(i), map1Values.get(i+1));
                intMap1.put(i,average);
            }

            if(map1Values.get(i)!=0 && map1Values.get(i+1)==0){
                Float average = average(map1Values.get(i), map1Values.get(i+1));
                intMap1.put(i+1,average);
            }

        }
        return intMap1;
    }

    public DataDTO getSampleData(){

        Map<LocalTime,Float> values = new HashMap<>();
        Map<LocalTime,Float> values2 = new HashMap<>();
        Map<Integer,Float> intMap1 = new HashMap<>();
        Map<Integer,Float> intMap2 = new HashMap<>();

        for(int i=0;i<22;i++){
            intMap2.put(i, 15f);
        }

        intMap1.put(0, 21f);
        intMap1.put(1, 22f);
        intMap1.put(2, 23f);
        intMap1.put(3, 24f);
        intMap1.put(4, 0f);
        intMap1.put(5, 0f);
        intMap1.put(6, 0f);
        intMap1.put(7, 28f);
        intMap1.put(8, 29f);
        intMap1.put(9, 30f);
        intMap1.put(10, 31f);
        intMap1.put(11, 32f);
        intMap1.put(12, 0f);
        intMap1.put(13, 0f);
        intMap1.put(14, 0f);
        intMap1.put(15, 36f);
        intMap1.put(16, 37f);
        intMap1.put(17, 38f);
        intMap1.put(18, 0f);
        intMap1.put(19, 0f);
        intMap1.put(20, 0f);
        intMap1.put(21, 42f);

       intMap1 = fixGraph(intMap1);

        List<Float> map1Values = new ArrayList<>(intMap1.values());
        doRegression(map1Values);

        return new DataDTO(values, values2, intMap1, intMap2);
    }


    public Float doRegression(List<Float> list){
        SimpleRegression regression = new SimpleRegression();
        for(int i=0;i<21;i++){
            System.out.println("LIST " + list.get(i));
            regression.addData(i, list.get(i));
        }

        System.out.println("REGRESSION INTERCEPT" + regression.getIntercept());
// displays intercept of regression line

        System.out.println("REGRESSION SLOPE" + regression.getSlope());
// displays slope of regression line

        System.out.println("REGRESSION STDERROR" + regression.getSlopeStdErr());

        System.out.println("REGRESSION prediction" + regression.predict(4));

        return (float)regression.getSlope();
    }

    public Map<Integer, Float> fixGraph(Map<Integer, Float> map){
       List<Float> map1Values = new ArrayList<>(map.values());
       doRegression(map1Values);
       map1Values =removeZeroEnds(map1Values);

       List<Integer> gaps = getGaps(map1Values);
       for(int i=0;i<gaps.size();i+=2){
           int start = gaps.get(i);
           int end = gaps.get(i+1) + 1;
           Float slope = getSlope(start, end, map);
           map = addSlope(start, end, map1Values, map, slope);
       }
        return map;
    }

    public List<Float> removeZeroEnds(List<Float> list){
        if(list.get(list.size()-1)==0){
            if(getAverageForList(list)>5){
                list.set(list.size()-1, addSlopeIncrease(list.size()-1,0f, list.get(0)));
            }
        }

        if(list.get(0)==0){
            if(getAverageForList(list)>5){
                list.set(0, addSlopeIncrease(0,0f, getAverageForList(list)));
            }
        }

        return list;
    }

    public Float getAverageForList(List<Float> list){
        Float avg = list.stream().reduce((a,b)->a+b).get()/list.size();
        System.out.println("LIST AVERAGE : " + avg);
        return avg;
    }

    public Float percentChange(Float a , Float b){
        return Math.abs((b-a)/a * 100);
    }


    // gives the MAP KEY WHERE DIP STARTS NON-ZERO AND AFTER DIP ENDS NON-ZERO - DEFINED AS START AND END EX:  2 TO 6 MEANS 3, 4, 5 ARE ZERO
    public List<Integer> getGaps(List<Float> values){
        List<Integer>gaps = new ArrayList<>();
        for(int i=0 ; i < values.size()-1 ; i++){
            if(percentChange(values.get(i), values.get(i+1))>50){
                gaps.add(i);
            }
        }

        System.out.println("GAPS ---------------");
        gaps.forEach(System.out::println);

        return gaps;
    }



    public Float getSlope (Integer a, Integer b, Map<Integer, Float> map){
        Float deltaY =  (map.get(b) - map.get(0));
        Float deltaX =  (float) (b - 0);
        System.out.println(" a :  " + a + " b :  " + b + " delta y :  " + deltaY + " delta x :  " + deltaX);
        System.out.println(" SLOPE : " + deltaY/deltaX);
        return  deltaY/deltaX;
    }


    // GETTING THE NEXT Y COORDINATE BASED ON THE FORMULA Y = MX + C WHERE C IS MAP START KEY  , M IS SLOPE AND X IS THE REQUIRED X COORDINATE WHERE Y IS TO BE FOUND

    public Float addSlopeIncrease(Integer x, Float slope, Float yIntercept){
        System.out.println("Y INTERCEPT : " +  yIntercept);
        System.out.println(" X  : " + x );
        Float result = yIntercept + slope*x;
        System.out.println("Y = " +   result);
        return result;
    }



    public Map<Integer, Float> addSlope(Integer start, Integer end, List<Float> list,  Map<Integer, Float> map, Float slope) {

        System.out.println(" START VALUE BEFORE DIP " + map.get(start));

        for(int i=start;i<end; i++){
            list.set(i+1, map.get(i));
            Float increasedValue = addSlopeIncrease(i+1, slope, map.get(0));
            System.out.println(" BEFORE " + map.get(i+1));
            map.put(i+1, increasedValue);
            System.out.println(" AFTER  " + map.get(i+1));

            }

    return map;

    }

}
