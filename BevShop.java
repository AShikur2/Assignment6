import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BevShop implements BevShopInterface {

    private int numAlcoholDrinksOrdered;
    private List<Order> orders;
    private Order currentOrder;
    
    private double monthlySales;
    private final int MIN_AGE = 21;

    public BevShop() {
        this.numAlcoholDrinksOrdered = 0;
        this.orders = new ArrayList<>();
        this.monthlySales = 0.0;
    }

    @Override
    public void startNewOrder(int customerAge, DAY day, String customerName, int time) {
    	Customer customer = new Customer(customerName, customerAge);
    	currentOrder = new Order(time, day, customer);
        orders.add(currentOrder);
    }

    @Override
    public void processAlcoholOrder(String bevName, SIZE size) {
        if (numAlcoholDrinksOrdered >= 3) {
            throw new IllegalArgumentException("You cannot order more than 3 alcoholic drinks in one order.");
        }

        if (size == SIZE.SMALL) {
            throw new IllegalArgumentException("Alcohol drinks cannot be ordered in small size.");
        }

        int age = orders.get(orders.size() - 1).getCustomer().getAge();
        if (age < 21) {
            throw new IllegalArgumentException("You must be at least 21 years old to order alcohol drinks.");
        }

        orders.get(orders.size() - 1).addNewBeverage(bevName, size);
        numAlcoholDrinksOrdered++;
    }

    @Override
    public void processCoffeeOrder(String bevName, SIZE size, boolean extraShot, boolean extraSyrup) {
        orders.get(orders.size() - 1).addNewBeverage(bevName, size, extraShot, extraSyrup);
    }

    @Override
    public void processSmoothieOrder(String bevName, SIZE size, int numOfFruits, boolean addProtein) {
        orders.get(orders.size() - 1).addNewBeverage(bevName, size, numOfFruits, addProtein);
    }

    @Override
    public int findOrder(int orderNo) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == orderNo) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double totalOrderPrice(int orderNo) {
        return orders.get(orderNo).calcOrderTotal();
    }

    @Override
    public void sortOrders() {
        Collections.sort(orders);
    }

    @Override
    public Order getOrderAtIndex(int index) {
        return orders.get(index);
    }

    @Override
    public boolean validTime(int time) {
        return (time >= MIN_TIME && time <= MAX_TIME);
    }

    @Override
    public double totalMonthlySale() {
        double total = 0.0;
        for (Order order : orders) {
            total += order.calcOrderTotal();
        }
        monthlySales = total;
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append(order.toString());
            sb.append("\n");
        }
        sb.append(String.format("Total Monthly Sale: $%.2f", monthlySales));
        return sb.toString();
    }

	@Override
	public boolean eligibleForMore() {
	    // Check if the order already has 3 alcoholic drinks
	    if (numAlcoholDrinksOrdered == 3) {
	        return false;
	    }
	    // Check if the customer is under 21 years old
	    if (customer.getAge() < 21) {
	        return false;
	    }
	    // Check if the order already has at least one coffee
	    boolean hasCoffee = false;
	    for (Beverage beverage : beverages) {
	        if (beverage instanceof Coffee) {
	            hasCoffee = true;
	            break;
	        }
	    }
	    if (!hasCoffee) {
	        return false;
	    }
	    // Check if the order already has at least one food item
	    boolean hasFood = false;
	    for (Beverage beverage : orderList) {
	        if (beverage instanceof Food) {
	            hasFood = true;
	            break;
	        }
	    }
	    if (!hasFood) {
	        return false;
	    }
	    // If all checks pass, the order is eligible for more alcoholic drinks
	    return true;
	}

	@Override
	public boolean validAge(int age) {
	    return age > MIN_AGE;
	}

	public Order getCurrentOrder() {
	    if (currentOrder == null) {
	        return null;
	    }
	    return currentOrder;
	}

	public int getNumOfAlcoholDrink() {
	    return numAlcoholDrinksOrdered;
	}

	public int totalNumOfMonthlyOrders() {
	    return orders.size();
	}

	public boolean isMaxFruit(int numFruit) {
	    if (currentOrder == null) {
	        return false;
	    }
	    for (int i = 0; i < currentOrder.getTotalItems(); i++) {
	        Beverage beverage = currentOrder.getBeverage(i);
	        if (beverage instanceof Smoothie) {
	            Smoothie smoothie = (Smoothie) beverage;
	            numFruit += smoothie.getNumOfFruits();
	        }
	    }
	    return numFruit >= MAX_FRUIT;
	}

}
