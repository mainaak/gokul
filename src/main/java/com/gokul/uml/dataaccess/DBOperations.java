package com.gokul.uml.dataaccess;

import com.gokul.uml.model.Customer;
import com.gokul.uml.model.Item;
import com.gokul.uml.model.Order;

import java.util.List;

public interface DBOperations {

    void saveCustomer(String name, String email, long number);

    Customer getCustomer(long number);

    void createItem(String itemName, double itemCost);

    Item getItemByName(String itemName);

    void createOrder(Customer customer, List<Item> items);

    List<Order> getAllOrders();
}
