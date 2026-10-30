import java.util.ArrayList;

public class DeliveryAssignment {

    ArrayList<deliverypartner> partners = new ArrayList<>();

    // Add Partner

    public void addPartner(deliverypartner partner) {

        partners.add(partner);

        System.out.println("Partner Added Successfully");
    }

    // Assign Partner

    public void assignPartner(String orderId, String city) {

        for (deliverypartner p : partners) {

            if (p.city.equalsIgnoreCase(city)
                    && p.status.equals("Available")) {

                p.assignOrder(orderId);

                System.out.println("Order ID: " + orderId);
                System.out.println("Partner: " + p.name);
                System.out.println("Status: Busy");

                return;
            }
        }

        System.out.println("No Partner Available");
    }

    // Reject Assignment

    public void rejectAssignment(String partnerId) {

        for (deliverypartner p : partners) {

            if (p.partnerId.equals(partnerId)) {

                if (!p.status.equals("Busy")) {
                    System.out.println("No active order");
                    return;
                }

                if (p.rejectionCount >= 3) {
                    System.out.println("Rejection Limit Reached");
                    return;
                }

                p.rejectOrder();

                System.out.println("Assignment Rejected");

                return;
            }
        }
    }

    // Complete Delivery

    public void completeDelivery(String partnerId) {

        for (deliverypartner p : partners) {

            if (p.partnerId.equals(partnerId)
                    && p.status.equals("Busy")) {

                p.completeOrder();

                System.out.println("Delivery Completed");
                System.out.println("Earnings: Rs.30");

                return;
            }
        }
    }

    // Display Partners

    public void displayPartners() {

        for (deliverypartner p : partners) {

            System.out.println("\nPartner ID: " + p.partnerId);
            System.out.println("Name: " + p.name);
            System.out.println("City: " + p.city);
            System.out.println("Status: " + p.status);
            System.out.println("Earnings: Rs." + p.earnings);
        }
    }
}