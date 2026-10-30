public class DeliveryPartner {

    String partnerId;
    String name;
    String city;
    String status;

    int earnings = 0;
    int rejectionCount = 0;

    String orderId;

    public DeliveryPartner(String partnerId, String name, String city) {

        this.partnerId = partnerId;
        this.name = name;
        this.city = city;

        status = "Available";
    }

    public void assignOrder(String orderId) {

        this.orderId = orderId;
        status = "Busy";
    }

    public void rejectOrder() {

        rejectionCount++;
        orderId = null;
        status = "Available";
    }

    public void completeOrder() {

        earnings += 30;
        orderId = null;
        status = "Available";
    }
}
    

