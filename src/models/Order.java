package models;

import java.util.Random;

public class Order {
	private Random rnd = new Random();
	
	private String name;
    private String recipe;
    private int reward;
    private long timeToFulfill;
    private static final String[] RECIPELIST = {"Cheeseburger", "Bacon Burger", "Veggie Burger", "Deluxe Burger"};

    public Order(String name, String recipe, Integer reward, Integer timeToFulfill) {
        this.name = name;
    	this.recipe = recipe;
        this.reward = reward;
        this.timeToFulfill = timeToFulfill;
    }
    
    public Order(Customer customer) {
    	this.name = customer.getName();
        this.recipe = RECIPELIST[rnd.nextInt(RECIPELIST.length)];
        this.reward = customer.calculateReward(recipe);
        this.timeToFulfill = customer.getPatience();
    }
  

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecipe() {
        return recipe;
    }

    public int getReward() {
        return reward;
    }

    public long getTimeToFulfill() {
        return timeToFulfill;
    }

    public void timeTicks() {
        this.timeToFulfill--;
        return;
    }

    public boolean isExpired() {
        return this.timeToFulfill <= 0;
    }
}
