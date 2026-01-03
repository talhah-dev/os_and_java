

==============================================================


import java.util.*;

public class Main {
  public static void main(String[] args){
    int[] arr = {5,4,1,2,3};

    for (int i = 0; i < arr.length; i++) {
      System.out.println(arr[i]);
    }

    System.out.println("====================After=========================");

    for(int i=0; i<arr.length-1; i++){
      for(int j=0; j < arr.length-i-1; j++){
        if(arr[j] > arr[j+1]){
          int temp = arr[j];
          arr[j] = arr[j+1];
          arr[j+1] = temp;
        }
      }
    }

    for (int i = 0; i < arr.length; i++) {
      System.out.println(arr[i]);
    }

    System.out.println("=============================================");

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Number : ");

    int found = sc.nextInt();

    for(int i=0;i< arr.length; i++){
      if(arr[i] == found){
          System.out.println( found + " is found at " + i + " index");
          break;
      }
    }
  }
}


===========================================================

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    int[] num = {8,1,2,5,3,7,4,0,66};

    int min = num[0];
    int max = num[0];

    for(int i=0; i< num.length; i++){
      if(num[i] < min){
        min = num[i];
      }

      if(num[i] > max){
        max = num[i];
      }
    }

    System.out.println("Min value is : " + min);
    System.out.println("Max value is : " + max);

  }
}

===============================================================

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    int[] arr1 = {2,4,5};
    int[] arr2 = {2,4,5};

    int result;

   for(int i =0; i < arr1.length; i++){
     result = arr1[i] * arr2[i];
     System.out.println((i+1) + ") " + result);
   }

  }
}

===================================================================

import java.util.*;

public class Main {
  public static void AvailableSeats(int totalSeats, ArrayList<Integer> seats){
    System.out.println( "Total seats Available : " + Math.abs(totalSeats - seats.size() ));
  }

  public static void AddSeat(ArrayList<Integer> seats){
    Scanner sc = new Scanner(System.in);
    System.out.println("How many seats you want to reserve : ");
    int reserveSeat = sc.nextInt();

    int count = 0;
    while(count < reserveSeat){
      System.out.println("Enter " + (count+1) + " Seat no : ");
      int seatNum = sc.nextInt();
      seats.add(seatNum);
      count++;
    }
  }

  public static void DeleteSeat(ArrayList<Integer> seats){
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Which seat you want to delete : ");
    int deleteSeat = sc.nextInt();
    seats.remove(Integer.valueOf(deleteSeat));
  }

  public static void TotalBookSeats(ArrayList<Integer> seats){
    System.out.println("Total Book Seats are : " + seats.size());
  }

  public static void ShowBookSeats(ArrayList<Integer> seats){
    System.out.println("These Seats Are Already Booked");
    for(int i=0; i < seats.size(); i++){
      System.out.println(seats.get(i));
    }
  }

  public static void main(String[] args) {

    int totalSeats = 10;

    Scanner sc = new Scanner(System.in);

    ArrayList<Integer> seats = new ArrayList<>();

    while(true) {
      System.out.println("Which action you want to perform : ");
      System.out.println("1) Total Seats Available");
      System.out.println("2) Add Seat");
      System.out.println("3) Delete Seat");
      System.out.println("4) Total Book Seats");
      System.out.println("5) Show Book Seats");
      System.out.println("6) Overall Seats");
      System.out.println("7) Exit");

      System.out.println("======================================================");
      System.out.println("Enter Number : ");
      int action = sc.nextInt();
      switch (action) {
        case 1:
          AvailableSeats(totalSeats, seats);
          break;
        case 2:
          AddSeat(seats);
          break;
        case 3:
          DeleteSeat(seats);
          break;
        case 4:
          TotalBookSeats(seats);
          break;
        case 5:
          ShowBookSeats(seats);
          break;
        case 6:
          System.out.println("Overall Seats are  : " + totalSeats);
          break;
        case 7:
          System.out.println("Thanks You!!");
          return;
        default:
          System.out.println("Invalid choice, Plz try again");
      }
    }
  }
}


===================================================================


import java.util.*;

public class Main {
  public static void main(String[] args) {

    int[] A = {120, 150, 200, 250};
    int[] B = {100, 180, 300, 400};

    ArrayList<Integer> arr = new ArrayList<>();

    for(int i =0; i < A.length; i++){
      arr.add(A[i]);
    }

    for(int i =0; i < A.length; i++){
      arr.add(B[i]);
    }

    System.out.println(arr);

    for(int i =0; i < arr.size() - 1; i++){
      for(int j =0; j<arr.size() -i -1; j++ ){
        if (arr.get(j) > arr.get(j+1)){
          int temp = arr.get(j);
          arr.set(j, arr.get(j+1));
          arr.set(j+1,temp);
        }
      }
    }

    System.out.println(arr);

  }
}
