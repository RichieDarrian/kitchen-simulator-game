package models;

public class CasualCustomer extends Customer{
	public String getName() { 
		return name;		
	}
	
    public int calculateReward(String recipe) {
    	int numberOfIngredients = getNumberOfIngredients(recipe);
        int baseReward = 5 + (numberOfIngredients * 2);
        return baseReward;
    }
}
