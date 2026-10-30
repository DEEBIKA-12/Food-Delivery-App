package fooddelivery;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final RestaurantService service = new RestaurantService();

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("     FOOD DELIVERY APP - MODULE 1");
        System.out.println("  Restaurant and Menu Management");
        System.out.println("======================================");

        Role role = chooseRole();

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> addRestaurant(role);
                    case 2 -> addMenuItem(role);
                    case 3 -> viewRestaurants();
                    case 4 -> activateRestaurant(role);
                    case 5 -> changeRestaurantStatus(role);
                    case 6 -> updateMenuPrice(role);
                    case 7 -> updateMenuAvailability(role);
                    case 8 -> updateTimings(role);
                    case 9 -> deleteRestaurant(role);
                    case 10 -> setActiveOrders(role);
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Thank you! Module 1 closed.");
    }

    private static Role chooseRole() {
        while (true) {
            System.out.println("\\nSelect your role:");
            System.out.println("1. Admin");
            System.out.println("2. Restaurant Owner");
            System.out.println("3. Customer");
            int choice = readInt("Choice: ");
            if (choice == 1) return Role.ADMIN;
            if (choice == 2) return Role.RESTAURANT_OWNER;
            if (choice == 3) return Role.CUSTOMER;
            System.out.println("Choose 1, 2 or 3.");
        }
    }

    private static void showMenu() {
        System.out.println("\\n----------- MAIN MENU -----------");
        System.out.println("1. Add Restaurant");
        System.out.println("2. Add Menu Item");
        System.out.println("3. View Restaurants and Menus");
        System.out.println("4. Activate Restaurant");
        System.out.println("5. Mark Restaurant Open / Closed");
        System.out.println("6. Update Menu Item Price");
        System.out.println("7. Change Menu Item Availability");
        System.out.println("8. Update Restaurant Timings");
        System.out.println("9. Delete Restaurant");
        System.out.println("10. Set Active Order Count (demo/testing)");
        System.out.println("0. Exit");
    }

    private static void addRestaurant(Role role) {
        String name = readText("Restaurant name: ");
        CuisineType cuisine = chooseCuisine();
        String city = readText("City: ");
        LocalTime opening = readTime("Opening time (HH:mm): ");
        LocalTime closing = readTime("Closing time (HH:mm): ");
        double minimum = readDouble("Minimum order value: ");

        Restaurant r = service.addRestaurant(role, name, cuisine, city,
                opening, closing, minimum);
        System.out.println("Restaurant added successfully. ID: " + r.getId());
    }

    private static void addMenuItem(Role role) {
        int restaurantId = readInt("Restaurant ID: ");
        String name = readText("Menu item name: ");
        double price = readDouble("Price: ");
        FoodCategory category = chooseCategory();
        boolean available = readYesNo("Is item available? (y/n): ");

        MenuItem item = service.addMenuItem(role, restaurantId, name,
                price, category, available);
        System.out.println("Menu item added. Item ID: " + item.getId());
    }

    private static void viewRestaurants() {
        List<Restaurant> restaurants = service.getAllRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants added yet.");
            return;
        }

        for (Restaurant r : restaurants) {
            System.out.println("\\n" + r);
            if (r.getMenuItems().isEmpty()) {
                System.out.println("  No menu items.");
            } else {
                for (MenuItem item : r.getMenuItems()) {
                    System.out.println("  " + item);
                }
            }
        }
    }

    private static void activateRestaurant(Role role) {
        int id = readInt("Restaurant ID to activate: ");
        service.activateRestaurant(role, id);
        System.out.println("Restaurant activated.");
    }

    private static void changeRestaurantStatus(Role role) {
        int id = readInt("Restaurant ID: ");
        boolean open = readYesNo("Mark as Open? (y/n): ");
        service.setRestaurantOpen(role, id, open);
        System.out.println("Restaurant status updated.");
    }

    private static void updateMenuPrice(Role role) {
        int restaurantId = readInt("Restaurant ID: ");
        int itemId = readInt("Menu item ID: ");
        double price = readDouble("New price: ");
        service.updateMenuPrice(role, restaurantId, itemId, price);
        System.out.println("Menu price updated.");
    }

    private static void updateMenuAvailability(Role role) {
        int restaurantId = readInt("Restaurant ID: ");
        int itemId = readInt("Menu item ID: ");
        boolean available = readYesNo("Is item available? (y/n): ");
        service.updateMenuAvailability(role, restaurantId, itemId, available);
        System.out.println("Menu availability updated.");
    }

    private static void updateTimings(Role role) {
        int id = readInt("Restaurant ID: ");
        LocalTime opening = readTime("New opening time (HH:mm): ");
        LocalTime closing = readTime("New closing time (HH:mm): ");
        service.updateTimings(role, id, opening, closing);
        System.out.println("Restaurant timings updated.");
    }

    private static void deleteRestaurant(Role role) {
        int id = readInt("Restaurant ID to delete: ");
        service.deleteRestaurant(role, id);
        System.out.println("Restaurant deleted.");
    }

    private static void setActiveOrders(Role role) {
        int id = readInt("Restaurant ID: ");
        int count = readInt("Number of active orders: ");
        service.setActiveOrderCount(role, id, count);
        System.out.println("Active order count updated.");
    }

    private static CuisineType chooseCuisine() {
        System.out.println("Cuisine: 1.North Indian  2.South Indian  3.Chinese  4.Italian  5.Fast Food");
        int choice = readInt("Choose cuisine: ");
        return switch (choice) {
            case 1 -> CuisineType.NORTH_INDIAN;
            case 2 -> CuisineType.SOUTH_INDIAN;
            case 3 -> CuisineType.CHINESE;
            case 4 -> CuisineType.ITALIAN;
            case 5 -> CuisineType.FAST_FOOD;
            default -> throw new IllegalArgumentException("Invalid cuisine choice.");
        };
    }

    private static FoodCategory chooseCategory() {
        System.out.println("Category: 1.Veg  2.Non-Veg");
        int choice = readInt("Choose category: ");
        return switch (choice) {
            case 1 -> FoodCategory.VEG;
            case 2 -> FoodCategory.NON_VEG;
            default -> throw new IllegalArgumentException("Invalid category choice.");
        };
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        String value = sc.nextLine().trim();
        if (value.isEmpty()) throw new IllegalArgumentException("This field is required.");
        return value;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(sc.nextLine().trim());
            } catch (DateTimeParseException ex) {
                System.out.println("Use 24-hour format, for example 09:30 or 22:00.");
            }
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = sc.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) return true;
            if (answer.equals("n") || answer.equals("no")) return false;
            System.out.println("Enter y or n.");
        }
    }
}
