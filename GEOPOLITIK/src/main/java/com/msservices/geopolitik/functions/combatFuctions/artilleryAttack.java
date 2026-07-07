package com.msservices.geopolitik.functions.combatFuctions;

import java.util.Random;

public class artilleryAttack {

    static Random rand = new Random();

    //blind attack
     public static void launchAttackToMilitaryUnits(int stat, int quantity, double percent){
         int[] toDestroy = new int[19];


         double chance = rand.nextInt(100) + 1;
         System.out.println("Chance: " + chance);
         for(int i = 0; i < toDestroy.length; i++){
             if(chance <= percent){
                 chance++;
                 //toDestroy[i] = rand.nextInt(quantity);
                 int result = (int)(Math.round(rand.nextDouble(stat))*quantity);
                 System.out.println(i + " - " + result);
             }
         }

         //return toDestroy;
     }

     public static int attackCivillians(int stat, int quantity, int civillians){
         int result = (int)(Math.round(rand.nextDouble(stat))*quantity);

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
