import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ControllerLogic {
    public static String typeStation1;
    public static String typeStation2;
    public static List<String> routB;
    private static List<String> listBackwardDirection;
    private static List<String> listForwardDirection;

  

    public static void printStations() {
        System.out.println("names of stations");
        for (String station : StationsModel.metroStations1) {
            System.out.println(station);
        }
        for (String station : StationsModel.metroStations2) {
            System.out.println(station);
        }
    }

    public static void chooseStation() {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose your first station=>>>>>> ");
        typeStation1 = s.nextLine();
        System.out.println("Choose your second station=>>>>> ");
        typeStation2 = s.nextLine();
        routB = routBetweenTwoStations(typeStation1, typeStation2);
    }

    public static List<String> routBetweenTwoStations(String station1, String station2) {
        List<String> stationsList1 = Arrays.asList(StationsModel.metroStations1);
        List<String> stationsList2 = Arrays.asList(StationsModel.metroStations2);

        if (stationsList1.contains(station1) && stationsList1.contains(station2)) {
            return getRoute(stationsList1, station1, station2, "Helwan", "El-Marg");
        } else if (stationsList2.contains(station1) && stationsList2.contains(station2)) {
            return getRoute(stationsList2, station1, station2, "El Monib", "Shobra El-Kheima");
        } else if (stationsList2.contains(station1) && stationsList1.contains(station2)) {
            List<String> route = new ArrayList<>();
            route.addAll(getRoute(stationsList2, station1, "Al-Shohadaa (Ramses)", "El Monib", "Shobra El-Kheima"));
            route.addAll(getRoute(stationsList1, "Al-Shohadaa (Ramses)", station2, "Helwan", "El-Marg").subList(1, getRoute(stationsList1, "Al-Shohadaa (Ramses)", station2, "Helwan", "El-Marg").size()));
            return route;
        } else if (stationsList1.contains(station1) && stationsList2.contains(station2)) {
            List<String> route = new ArrayList<>();
            route.addAll(getRoute(stationsList1, station1, "Al-Shohadaa (Ramses)", "Helwan", "El-Marg"));
            route.addAll(getRoute(stationsList2, "Al-Shohadaa (Ramses)", station2, "El Monib", "Shobra El-Kheima").subList(1, getRoute(stationsList2, "Al-Shohadaa (Ramses)", station2, "El Monib", "Shobra El-Kheima").size()));
            return route;
        } else {
            System.out.println("Invalid station name");
            return Collections.emptyList();
        }
    }

    public static List<String> getRoute(List<String> stationsList, String station1, String station2, String forwardDirection, String backwardDirection) {
        int startIndex = stationsList.indexOf(station1);
        int endIndex = stationsList.indexOf(station2);
        if (startIndex <= endIndex) {
        listForwardDirection = Arrays.asList(forwardDirection);
            setListForwardDirection(listForwardDirection);
            return new ArrayList<>(stationsList.subList(startIndex, endIndex + 1));
        } else {
        listBackwardDirection = Arrays.asList(backwardDirection);
            setListBackwardDirection(listBackwardDirection);
            List<String> subList = new ArrayList<>(stationsList.subList(endIndex, startIndex + 1));
            Collections.reverse(subList);
            return subList;
        }
    }

    public static void printRouteB() {
        System.out.println("route Stations between =>>>>>> " + typeStation1 + " and " + typeStation2 + " : ");
        System.out.println(routB);
    }

    public static void printCount() {
        System.out.println("count of Stations=>>>>> " + routB.size());
    }

    public static void printTime() {
        System.out.println("time of trip=>>>>> " + ((routB.size() * 2) / 60) + " hours " + ((routB.size() * 2) % 60) + " minutes");
    }

    public static void printPriceOfTicket() {
        System.out.println("price of ticket=>>>>> " + (routB.size() * 0.5) + "EG");
    }

    public static List<String> getListForwardDirection() {
        return listForwardDirection;
    }

    public static void setListForwardDirection(List<String> listForwardDirection) {
        ControllerLogic.listForwardDirection = listForwardDirection;
    }

    public static List<String> getListBackwardDirection() {
        return listBackwardDirection;
    }

    public static void setListBackwardDirection(List<String> listBackwardDirection) {
        ControllerLogic.listBackwardDirection = listBackwardDirection;
    }
}