package com.msservices.geopolitik.functions.combatFuctions;

/*
RADAR ACTION ----> ATTACK ------> ANTIAIRCRAFT SYSTEMS ------> RESULTS
 */

import java.util.Random;

public class aerialActions {

    private static Random r;
    //Fighters
    public static void destroyObjetive(int objetive, int quantity, int  terrainDefense, double radarBonus, int airDefenses){
//        if(radarAction(radarBonus))
//        return ;
    }

    public static void destroyRandomObjetives(int quantity, int terrainDefensebonus ){

        //int[] objetives = ;

    }


    //Only fighters can be used to counterstrike
    public static void bombing(int quantity, int objetive, double defenses, int fighters){

    }

    //Helicopters are inmune to radar detection
    public static void airRaid(int quantity, int objetive, double defenses, int defensesQuantity){

    }

    /*
    DEFENSIVE ACTIONS ONLY FOR THIS CLASS
     */
    private static boolean radarAction(double bonus){

        return true;
    }

    //Quantity determine enemy's casualties
    //Bonus determine succes percentage after radar action
    //Radar only determines inital strike succes
//    private static int antiAircraftDefensiveAction(int quantity, double bonus, double radarBonus){
//        int antiAircraftCasualties = (quantity * (r.nextDouble(bonus)+1)) - (quantity * (r.nextDouble(bonus)-1));
//
//        return antiAircraftCasualties;
//    }

    private static int casualties(int quantity, int ratio){
        return (quantity * (r.nextInt(ratio)+1)) - (quantity * (r.nextInt(ratio)-1));
    }

}
