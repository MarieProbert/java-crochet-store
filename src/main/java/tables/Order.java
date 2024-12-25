package tables;

import java.util.HashMap;
import java.util.Map;

public class Order {
	private int orderID; //Primary key
	private int clientID; // Foreign key
	private String status; //enum : (en cours -> panier; validé -> payé; livrée -> livrée)
	private Map<Integer, Integer> map = new HashMap<>(); //ref du produit, quantite
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Map<Integer, Integer> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}
	
	

}
