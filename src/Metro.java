


public class Metro {
    public static void main(String[] args) {
        ControllerLogic.printStations();
        ControllerLogic.chooseStation(); 
        //count   1
        ControllerLogic.printCount();
        //time    2
        ControllerLogic.printTime(); 
        //ticket  3
        ControllerLogic.printPriceOfTicket(); 
        //route   4
        ControllerLogic.printRouteB();
        //directions  5        
        System.out.println(ControllerLogic.getListForwardDirection());
        System.out.println(ControllerLogic.getListBackwardDirection());
    }
}
