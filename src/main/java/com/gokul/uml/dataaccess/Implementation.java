package com.gokul.uml.dataaccess;

import com.gokul.uml.model.Customer;
import com.gokul.uml.model.Item;
import com.gokul.uml.model.Order;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Implementation implements DBOperations {

    static int customerId = 0;
    static int itemId = 0;
    static int orderId = 0;

    private final List<Customer> CUSTOMER_DB = new ArrayList<>();
    private final List<Item> ITEM_DB = new ArrayList<>();
    private final List<Order> ORDERS = new ArrayList<>();

    @Override
    public void saveCustomer(String name, String email, long number) {
        for (Customer customer : CUSTOMER_DB) {
            if (customer.getNumber() == number) {
                customer.setName(name);
                customer.setEmail(email);
                return;
            }
        }
        Customer newCustomer = new Customer(name, email, number);
        newCustomer.setId(customerId++);
        CUSTOMER_DB.add(newCustomer);
    }

    @Override
    public Customer getCustomer(long number) {
        for (Customer customer : CUSTOMER_DB) {
            if (customer.getNumber() == number) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void createItem(String itemName, double itemCost) {
        for (Item item : ITEM_DB) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.setCost(itemCost);
                return;
            }
        }
        Item item = new Item(itemId++, itemName.toLowerCase(), itemCost);
        ITEM_DB.add(item);
    }

    @Override
    public Item getItemByName(String itemName) {
        for (Item item : ITEM_DB) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void createOrder(Customer customer, List<Item> items) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(items);
        order.setId(orderId++);
        ORDERS.add(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return ORDERS;
    }

    @PostConstruct
    private void persist() {
        System.out.println("Creating customers and inventory.");
        saveCustomer("Gokul Menon", "gmenon@gmail.com", 9119119119L);
        saveCustomer("Manisha Menon", "mmenon@gmail.com", 9119119110L);
        createItem("Margherita", 100D);
        createItem("Farmhouse", 250D);
        createItem("Chicken", 250D);
        createItem("Onion", 100D);
    }
}
