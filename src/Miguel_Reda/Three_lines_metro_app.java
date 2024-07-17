package Miguel_Reda;

import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
public class Three_lines_metro_app {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<String> line1 = Arrays.asList(
                "HELWAN", "AIN HELWAN", "HELWAN UNIVERSITY", "WADI HOF",
                "HADAYEK HELWAN", "EL-MAASARA", "TORA EL-ASMANT", "KOZZIKA",
                "TORA EL-BALAD", "SAKANAT EL-MAADI", "MAADI", "HADAYEK EL-MAADI",
                "DAR EL-SALAM", "EL-ZAHRAA", "MAR GIRGIS", "EL-MALEK EL-SALEH",
                "AL-SAYEDA ZEINAB", "SAAD ZAGHLOUL", "SADAT", "GAMAL ABDEL NASSER",
                "ORABI", "AL SHOHADAA", "GHAMRA", "EL-DEMERDASH", "MANSHIET EL-SADR",
                "KOBRI EL-QOBBA", "HAMMAMAT EL-QOBBA", "SARAY EL-QOBBA",
                "HADAYEK EL-ZAITOUN", "HELMEYET EL-ZAITOUN", "EL-MATAREYYA",
                "AIN SHAMS", "EZBET EL-NAKHL", "EL MARG"
        );
        List<String> line2 = Arrays.asList(
                "SHUBRA EL-KHEIMA", "KOLLEYET EL-ZERAA", "MEZALLAT", "KHALAFAWY",
                "ST. TERESA", "ROD EL-FARAG", "MASARRA", "AL SHOHADAA", "ATTABA",
                "SADAT", "OPERA", "DOKKI", "EL BOHOTH", "CAIRO UNIVERSITY", "FAISAL",
                "GIZA", "OMM EL-MASRYEEN", "SAKIAT MEKKI", "EL-MOUNIB"
        );
        List<String> line3 = Arrays.asList(
                "ADLY MANSOUR", "EL HAYKESTEP", "OMAR IBN EL-KHATTAB", "QOBAA",
                "HESHAM BARAKAT", "EL-NOZHA", "NADI EL-SHAMS", "ALF MASKAN",
                "HELIOPOLIS SQUARE", "HAROUN", "AL-AHRAM", "KOLLEYET EL-BANAT",
                "CAIRO STADIUM", "FAIR ZONE", "ABBASIA", "ABDOU PASHA", "EL GEISH",
                "BAB EL SHAARIA", "ATTABA", "NASSER", "MASPERO", "ZAMALEK", "KIT KAT"
        );

        List<String>[] metroLines = new List[3];
        metroLines[0] = line1;
        metroLines[1] = line2;
        metroLines[2] = line3;

        System.out.println("Enter start station:");
        String start_station = s.nextLine().toUpperCase();
        System.out.println("Enter end station:");
        String end_station = s.nextLine().toUpperCase();

        int start_line = -1;
        int end_line = -1;
        int start_index = -1;
        int end_index = -1;

        for (int i = 0; i < metroLines.length; i++) {
            if (start_line == -1) {
                start_index = metroLines[i].indexOf(start_station);
                if (start_index != -1) {
                    start_line = i;
                }
            }
            if (end_line == -1) {
                end_index = metroLines[i].indexOf(end_station);
                if (end_index != -1) {
                    end_line = i;
                }
            }
            if (start_line != -1 && end_line != -1) {
                break;
            }
        }

        if (start_line == -1 || end_line == -1) {
            System.out.println("Invalid station entered.");
            return;
        }

        if (start_line == end_line) {
            List<String> journeyStations;
            String direction;
            int stops;
            int time;

            if (start_index > end_index) {
                journeyStations = new ArrayList<>(metroLines[start_line].subList(end_index, start_index + 1));
                Collections.reverse(journeyStations);
                direction = switch (start_line) {
                    case 0 -> "El Marg";
                    case 1 -> "Shubra El-Kheima";
                    case 2 -> "Adly Mansour";
                    default -> throw new IllegalStateException("Unexpected value: " + start_line);
                };
                stops = start_index - end_index;
            } else {
                journeyStations = new ArrayList<>(metroLines[start_line].subList(start_index, end_index + 1));
                direction = switch (start_line) {
                    case 0 -> "Helwan";
                    case 1 -> "El-Mounib";
                    case 2 -> "Kit Kat";
                    default -> throw new IllegalStateException("Unexpected value: " + start_line);
                };
                stops = end_index - start_index;
            }

            time = stops * 2;
            System.out.println("You will pass through the following stations:");
            System.out.println(journeyStations);
            System.out.println("Direction: " + direction);
            System.out.println("Estimated time: " + time + " minutes");
            System.out.println("Number of stops: " + stops);
            int length = journeyStations.size();
            if (length < 9) {
                System.out.println("Ticket price: 6 EGP");
            } else if (length < 16) {
                System.out.println("Ticket price: 8 EGP");
            } else if (length < 24) {
                System.out.println("Ticket price: 12 EGP");
            } else {
                System.out.println("Ticket price: 15 EGP");
            }
        } else {
            List<String> switchStations = new ArrayList<>(metroLines[start_line]);
            switchStations.retainAll(metroLines[end_line]);
            List<String>[] routes = new List[switchStations.size()];
            int shortestRouteIndex = 0;

            for (int i = 0; i < switchStations.size(); i++) {
                String switchStation = switchStations.get(i);
                int switchStartIndex = metroLines[start_line].indexOf(switchStation);
                int switchEndIndex = metroLines[end_line].indexOf(switchStation);
                List<String> firstJourney, secondJourney;

                if (switchStartIndex > start_index) {
                    firstJourney = new ArrayList<>(metroLines[start_line].subList(start_index, switchStartIndex + 1));
                } else {
                    firstJourney = new ArrayList<>(metroLines[start_line].subList(switchStartIndex, start_index + 1));
                    Collections.reverse(firstJourney);
                }

                if (switchEndIndex > end_index) {
                    secondJourney = new ArrayList<>(metroLines[end_line].subList(end_index, switchEndIndex + 1));
                } else {
                    secondJourney = new ArrayList<>(metroLines[end_line].subList(switchEndIndex, end_index + 1));
                }

                secondJourney.remove(0); // Remove switch station
                firstJourney.addAll(secondJourney);
                routes[i] = firstJourney;

                if (routes[shortestRouteIndex] == null || firstJourney.size() < routes[shortestRouteIndex].size()) {
                    shortestRouteIndex = i;
                }
            }

            System.out.println("This is the shortest route:");
            List<String> shortestRoute = routes[shortestRouteIndex];
            System.out.println("You will pass through the following stations:");
            System.out.println(shortestRoute);
            System.out.println("Estimated time: " + (shortestRoute.size() - 1) * 2 + " minutes");
            System.out.println("Number of stops: " + (shortestRoute.size() - 1));

            int length = shortestRoute.size();
            if (length < 9) {
                System.out.println("Ticket price: 6 EGP");
            } else if (length < 16) {
                System.out.println("Ticket price: 8 EGP");
            } else if (length < 24) {
                System.out.println("Ticket price: 12 EGP");
            } else {
                System.out.println("Ticket price: 15 EGP");
            }

            for (int i = 0; i < routes.length; i++) {
                if (i != shortestRouteIndex) {
                    System.out.println("Alternative route:");
                    System.out.println(routes[i]);
                    System.out.println("Estimated time: " + (routes[i].size() - 1) * 2 + " minutes");
                    System.out.println("Number of stops: " + (routes[i].size() - 1));
                    int altLength = routes[i].size();
                    if (altLength < 9) {
                        System.out.println("Ticket price: 6 EGP");
                    } else if (altLength < 16) {
                        System.out.println("Ticket price: 8 EGP");
                    } else if (altLength < 24) {
                        System.out.println("Ticket price: 12 EGP");
                    } else {
                        System.out.println("Ticket price: 15 EGP");
                    }
                }
            }
        }
    }
}
