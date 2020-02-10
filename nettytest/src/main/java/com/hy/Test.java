package com.hy;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;

public class Test {
    public static void test(int i,int j){
        int z = 0;
        int sum = 0;
        for(int s = 0;s < j;s++){
            z =z*10+i;
            sum+=z;
        }
        System.err.println(sum);
    }
public static void demo(int s){
        int i = 100;
        while(i < s){
            int g = i % 10;
            int shi = (i/10)%10;
            int b = i / 100;
            int shui = g*g*g+shi*shi*shi+b*b*b;
            if(shui == i){
                System.out.println(shui);
            }
            ++i;
        }
}

public static void hen(){
        for(int i=0;i<=100;i++){
            for(int j =0;j<=100;j++){
                int g = i*5;
                int m = j*3;
                int x = 100-i-j;
                int xiaoji = x/3;
                int z = g+m+xiaoji;
                if(z == 100 && i+j+x == 100){
                    System.out.println("gj"+i);
                    System.out.println("mj"+j);
                    System.out.println("xj"+x);
                }
            }
        }
}

public static void chonfu(){
        int[] arr = {100,25,45,78,69,58,15,35,45,45,78,100};
        for(int i = 0;i< arr.length-1;i++){
            for(int j = i+1;j<arr.length;j++){
                if(arr[i] == arr[j]){
                    System.out.println(i+"and"+j);
                }
            }
        }
}

public static void test(int i){
    for (int j = 1; j < i; j++) {
        for (int k =0; k < i-j; k++) {
            System.out.print(" ");
        }
        for (int k =0; k < (2*j)-1; k++) {
            System.out.print("*");
        }
        System.out.println("'");
    }
    for (int j = 0; j < i; j++) {
        for (int k = 0; k < j; k++) {
            System.out.print(" ");
        }
        for (int k = 0; k < (i-j)*2-1; k++) {
            System.out.print("*");
        }
        System.out.println(".");
    }
}


public static void lin(int i){
    for (int j = 1; j <= i; j++) {
        for (int k = 1; k <= i-j; k++) {
            System.out.print(" ");
        }
        for (int k = 1; k <= (2*j)-1; k++) {
            System.out.print("*");
        }
        System.out.println();
    }
    for (int j = 1; j <= i-1; j++) {
        for (int k = 1; k <= j; k++) {
            System.out.print(" ");
        }
        for (int k = 1; k <= (i-j)*2-1 ; k++) {
            System.out.print("*");
        }
        System.out.println();
    }
}



public static void maopao(){
        int[] arr = {12,45,1,65,100,2,3,45};
    for (int i = 0; i < arr.length-1; i++) {
        for (int j = i+1; j < arr.length; j++) {
            if(arr[i]<arr[j]){
                int tem = arr[i];
                arr[i] = arr[j];
                arr[j] = tem;
            }
        }
    }
    for (int a:arr) {
        System.out.println(a);
    }
}

public static void erfen(int i){
        int[] arr={12,23,45,78,89,99,102,147};
        int start = 0;
        int end = arr.length;
        while (start <= end){
            int z = (end + start)/2;
            if(arr[z]>i){
                end = z+1;
            }else if(arr[z]<i){
                start = z-1;
            }else{
                System.out.println(z);
                break;
            }
           // System.out.println("meizhaodao");
        }

}
    public static void main(String[] args) {
     //test(2,5);
        //demo(1000);
        //test(5);
        //erfen(78);
        //lin(3);
        System.out.println("你好");

    }

public static void readd(){
    try {
        FileReader read = new FileReader(new File("E://d.txt"));
        char[] c = new char[1024];
        int i=read.read(c);
        while (i!=-1){
            String s = new String(c,0,i);
            i=read.read(c);
            System.out.println(s);
        }


    } catch (IOException e) {
        e.printStackTrace();
    }

}
}
