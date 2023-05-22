package com.gokul.uml.controllers;

import com.gokul.uml.dataaccess.DBOperations;
import com.gokul.uml.model.Customer;
import com.gokul.uml.model.Item;
import com.gokul.uml.model.Order;
import com.gokul.uml.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = {"*", "null"})
public class RequestController {

    private final DBOperations dbOperations;

    @Autowired
    public RequestController(DBOperations dbOperations) {
        this.dbOperations = dbOperations;
    }

    @PostMapping("inventory")
    public int getTotal(@RequestBody List<Item> items) {
        for (Item item : items) {
            dbOperations.createItem(item.getName(), item.getCost());
        }
        int itemsCreated = 0;
        for (Item item : items) {
            if (dbOperations.getItemByName(item.getName()) != null) {
                itemsCreated++;
            }
        }
        return itemsCreated;
    }

    @PostMapping("order")
    public List<Order> createOrder(@RequestBody OrderRequest orderRequest) {

        Customer customer = orderRequest.getCustomer();
        dbOperations.saveCustomer(customer.getName(), customer.getEmail(), customer.getNumber());

        String[] items = orderRequest.getItems().split(",");

        List<Item> itemList = new ArrayList<>();

        for (String itemName : items) {
            Item item = dbOperations.getItemByName(itemName);
            if (item != null) itemList.add(item);
        }

        if (itemList.size() != items.length) throw new RuntimeException("Items mentioned do not exist in the inventory");
        dbOperations.createOrder(dbOperations.getCustomer(customer.getNumber()), itemList);
        return dbOperations.getAllOrders();
    }

    @GetMapping("total/{customerNumber}")
    public double calculateTotalForCustomer(@PathVariable long customerNumber) {
        List<Order> orders = dbOperations.getAllOrders();
        Optional<Order> first = orders.stream().filter(o -> o.getCustomer().getNumber() == customerNumber).findFirst();
        if (!first.isPresent()) throw new RuntimeException("No orders exist for this customer");
        Order order = first.get();
        return order.getItems().stream().mapToDouble(Item::getCost).sum();
    }
}
