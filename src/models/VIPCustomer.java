package models;

public class VIPCustomer extends Customer {
	public String getName() { 
		return name.concat(" VIP");		
	}
	
    public int calculateReward(String recipe) {
        int numberOfIngredients = getNumberOfIngredients(recipe);
        int baseReward = 5 + (numberOfIngredients * 2);
        return baseReward + 10;
    }
}
