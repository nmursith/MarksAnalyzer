package Marks;

/**
 * Created by ROSE on 11/30/2016.
 */
public class Test {
    public static void main(String[]args){
        String track = "no, %B4216890200522445^KARUNASINGHE/NALIN D^1710221190460000000000394000000?";
        track = track.replace("no, ", "");

        String [] details = track.split("\\^");
        details[0] = details[0].replace("%B","");
        details[1] = details[1].replace("/"," ");
        details[2] = details[2].substring(0,4);
        String cardNo = details[0].substring(0,4)+ " **** **** ****";
        String eYear = details[2].substring(0,2);
        String eMonth = details[2].substring(2);
        System.out.println(cardNo);
        System.out.println(eMonth);
        System.out.println(eYear);

        for(String det:details){
            System.out.println(det);
        }


    }
}
