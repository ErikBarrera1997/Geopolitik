package com.msservices.geopolitik.functions.combatFuctions;

import java.util.Random;

public class artilleryAttack {

    static Random rand = new Random();

    //blind attack
    //Probability = 1/3
     public static int[] launchAttackToMilitaryUnits(int quantity){
         Random r = new Random();
         int last = 0;
         int p;

         int Xo = r.nextInt(quantity);
         last = quantity - Xo;
         int X1 = r.nextInt(last);
         last = last - X1;
         int X2 = last;

         int[] objetives = new int[21];
         int destroyed = 0;

         while(X1 >= 0){
             p = r.nextInt(21);
             destroyed = objetives[p];
             objetives[p] = destroyed + 1;
             X1--;
         }

         while(X2 >= 0){
             p = r.nextInt(21);
             destroyed = objetives[p];
             objetives[p] = destroyed + 1;
             X2--;
         }

        return objetives;
     }

     public static int attackCivillians(int quantity, int civillians){
         int result = (int)(Math.round(rand.nextDouble(2))*quantity);
         return result;
     }

     //Buildings have 20% resistance to conventional artillery attacks
     public static int attackInfraestructure(int objetive, int stat, int quantity, int percent){
         double chance = (rand.nextInt(100) + 1);
         double resistance = (chance * 20)/100;

         chance = chance - resistance;

         if(chance <= percent && quantity >= 100){

         }
         return 0;
     }
}
