package models;

import java.util.Random;

public abstract class Customer {
	private Random rnd = new Random();
	
	protected String name;
	private Integer patience;
	private static final String[] NAMES = {"Camellia", "Garry", "Rachel", "Ocram", "Nanashi", "Sunny", "Mogeko", "Aqua"};
	protected static final String[] RECIPES = {"Cheeseburger", "Bacon Burger", "Veggie Burger", "Deluxe Burger"};
	protected static final int[] INGREDIENT_COUNTS = {4, 6, 5, 7};
	
	public Customer() {
		super();
		this.name = NAMES[rnd.nextInt(NAMES.length)];
		this.patience = rnd.nextInt(21) + 15;
	}                
	
	public abstract String getName();

	public Integer getPatience() {
		return patience;
	}

	public void setPatience(Integer patience) {
		this.patience = patience;
	}
	
    protected int getNumberOfIngredients(String recipe) {
        for (int i = 0; i < RECIPES.length; i++) {
            if (RECIPES[i].equals(recipe)) {
                return INGREDIENT_COUNTS[i];
            }
        }
        return 0;
    }

	public abstract int calculateReward(String recipe);
}
