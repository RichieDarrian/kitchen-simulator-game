package main;

import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.Random;
import java.util.Scanner;

import models.CasualCustomer;
import models.Ingredient;
import models.Order;
import models.VIPCustomer;

public class Main {
	
	Scanner sc = new Scanner(System.in);
	Random rnd = new Random();
	boolean playing;
	boolean running;
	boolean output;
	
	Vector<Order> orders = new Vector<>();
	Vector<Ingredient> inventory = new Vector<>();

	String username;
	int currentScore = 0;
    int tick = 0;
    
    Thread gameLoopThread;
    
    void initializeRestaurant() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("restaurant.txt"));
			String line;
			try {
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("#");
					String customerName = parts[1];
					String recipeName = parts[2];
					Integer totalReward = Integer.parseInt(parts[3]);
					Integer time = Integer.parseInt(parts[4]);
					orders.add(new Order(customerName, recipeName, totalReward, time));
				}
			} catch (NumberFormatException | IOException e) {
			}
		} catch (FileNotFoundException e) {
		}
    }
    
    void initializeInventory() {
        boolean isFirstTime = false;

        try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
            if (br.readLine() == null) {
                isFirstTime = true;
            }
        } catch (FileNotFoundException e) {
            isFirstTime = true;
        } catch (IOException e) {
        }
        
        if (isFirstTime) {
            inventory.add(new Ingredient("Onions", 10));
            inventory.add(new Ingredient("Bun", 10));
            inventory.add(new Ingredient("Cheese", 10));
            inventory.add(new Ingredient("Tomato", 10));
            inventory.add(new Ingredient("Bacon", 10));
            inventory.add(new Ingredient("Patty", 10));
            inventory.add(new Ingredient("Pickles", 10));

            inventorySaver();
        }
        
        try {
			BufferedReader br = new BufferedReader(new FileReader("inventory.txt"));
            String line;
			while ((line = br.readLine()) != null) {
                String[] parts = line.split("#");
                String ingredientName = parts[0]; 
                Integer quantity = Integer.parseInt(parts[1]);  // The quantity as an integer
                inventory.add(new Ingredient(ingredientName, quantity)); 
            }
			br.close();
        } catch (FileNotFoundException e) {
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
    }
    
    void initializeScore() {
    	BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("score.txt"));
			currentScore = Integer.parseInt(br.readLine());
			br.close();
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
    }
    
    void initializeThread() {
    	initializeRestaurant();
    	initializeInventory();
    	initializeScore();
    }
    
    void restaurantSaver() {
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("restaurant.txt"));
			int num = 1;
			for (Order order : orders) {
				bw.write(String.format("%d#%s#%s#%d#%d\n", num, order.getName(), order.getRecipe(), order.getReward(), order.getTimeToFulfill()));				
				num++;
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}    		
	}
    
    void inventorySaver() {
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("inventory.txt"));
			for (Ingredient ingredient : inventory) {
				bw.write(String.format("%s#%d\n", ingredient.getName(), ingredient.getQuantity()));				
			}
			bw.close();
        } catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    void scoreSaver() {
        try {
        	BufferedWriter bw = new BufferedWriter(new FileWriter("score.txt"));
        	bw.write(String.format("%s\n", currentScore));
        	bw.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    void threadSaver() {
    	restaurantSaver();
    	inventorySaver();
    	scoreSaver();
    }
	
	void displayRestaurant() {
		for (int i = 0; i < 100; i++) {
            System.out.println();
        }
		System.out.printf("%s's Restaurant\n", username);
		System.out.println(" ╔═══════╦═════════════════╦══════════════════════╦════════╦═══════╗");
		System.out.println(" ║ No.   ║ Customer        ║ Order                ║ Reward ║ Time  ║");
		int num = 1;
		for (Order order : orders) {
	        System.out.printf(" ║ %-6d║ %-16s║ %-21s║ $%-6d║ %5ds║\n", num, order.getName(), order.getRecipe(), order.getReward(), order.getTimeToFulfill());
	        num++;
	    }
		restaurantSaver();
		System.out.println(" ╚═══════╩═════════════════╩══════════════════════╩════════╩═══════╝");
		System.out.println(" ╔══════ Current Inventory ═════╗");
		System.out.println(" ║ Ingredient      │ Quantity   ║");
		System.out.println(" ╠═════════════════╪════════════╣");
        for (Ingredient ingredient : inventory) {
            System.out.printf(" ║ %-16s│ %-11d║\n", ingredient.getName(), ingredient.getQuantity());
        }
        inventorySaver();
        System.out.println(" ╚═════════════════╧════════════╝");
        System.out.println();
        System.out.printf(" ╔══════════════════ Game Stats ══════════════════╗\n");
        System.out.printf(" ║ Current Score: $%-31d║\n", currentScore);
        scoreSaver();
        System.out.println(" ╚════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(" ╔══════════════════════════════════════════════════════════════╗");
        System.out.println(" ║ Select an order to process or press enter to return to menu  ║");
        System.out.println(" ╚══════════════════════════════════════════════════════════════╝");
        

	}
	
	void newOrder() {
		tick++;
		if (tick >= 4) {
			int chance = rnd.nextInt(2);
			if (chance == 1 && orders.size() < 5 || orders.isEmpty()) {
				int chance2 = rnd.nextInt(6);
				if (chance2 == 5) {
					orders.add(new Order(new VIPCustomer()));					
				} else {
					orders.add(new Order(new CasualCustomer()));					
				}
			}
			tick = 0;
		}
	}
	
void gameLoop() {
    running = true;    
    new Thread(() -> {
        while (running) {
        	if (output) {
        		displayRestaurant();        		
        	}
            for (int i = orders.size() - 1; i >= 0; i--) {
                Order order = orders.get(i);
                order.timeTicks();
                if (order.isExpired()) {
                    orders.remove(i);
                }
            }
            newOrder();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                running = false;
            }
            threadSaver();
        }
    }).start();
}
	
    void startGameLoopInBackground() {
        gameLoopThread = new Thread(() -> gameLoop());
        gameLoopThread.start();
    }
	
    void stopGameLoop() {
        running = false;
        try {
            if (gameLoopThread != null) {
                gameLoopThread.join(); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    void outputMode() {
        do {
    		for (int i = 0; i < 100; i++) {
                System.out.println();
            }
            System.out.print("Please enter your name (Cannot be empty): ");
            username = sc.nextLine();
        } while (username.isEmpty());

        startGameLoopInBackground();

        while (true) {
            displayRestaurant();
            String input = sc.nextLine();
            
            if (input.isEmpty()) {
                stopGameLoop();
                break;
            }
        }
        
		for (int i = 0; i < 100; i++) {
            System.out.println();
        }
        System.out.printf("╔══════════════════════════════════════════════════════════════════════════════╗\n");
        System.out.printf("║                            YOUR FINAL SCORE IS : %-28d║\n", currentScore);
        System.out.printf("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        sc.nextLine();
    }
    
    String[] getIngredientsForRecipe(String recipe) {
        switch (recipe) {
            case "Cheeseburger":
                return new String[]{"Bun", "Cheese", "Patty", "Bun"};
            case "Bacon Burger":
                return new String[]{"Bun", "Bacon", "Patty", "Lettuce", "Tomato", "Bun"};
            case "Veggie Burger":
                return new String[]{"Bun", "Lettuce", "Tomato", "Onions", "Pickles", "Bun"};
            case "Deluxe Burger":
                return new String[]{"Bun", "Cheese", "Bacon", "Patty", "Lettuce", "Tomato", "Onions", "Bun"};
            default:
                return new String[]{};
        }
    }
	
void inputMode() {
	startGameLoopInBackground();
	for (int i = 0; i < 100; i++) {
        System.out.println();
    }
	
    while (true) {
    	String action;
    	System.out.print("Enter command (e.g., 'process 1' to process order 1, 'restock' to restock inventory, 'exit' to quit): ");
    	action = sc.nextLine().trim();
    	
    	if (action.equals("exit")) {
    		stopGameLoop();
    		return;
    	} else if (action.equals("restock")) {
    		if (currentScore >= 30) {
    			System.out.println("Inventory restocked by 10 for each ingredient.");
    			currentScore -= 30;
    			for (Ingredient ingredient : inventory) {
    				ingredient.addQuantity(10);
    			}
    			scoreSaver();
    			inventorySaver();
    			continue;
    		} else {
    			System.out.println("Not enough money.");
    			continue;
    		}
    	}
    	
    	String[] parts = action.split(" ");
    	if (parts[0].equals("process")) {
    		Integer tempOrderNum;
            try {
                tempOrderNum = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                continue;
            }
            
            try {
            	BufferedReader br;
            	br = new BufferedReader(new FileReader("restaurant.txt"));
            	String line;
				while ((line = br.readLine()) != null) {
				    String[] parts2 = line.split("#");
				    Integer orderNum = Integer.parseInt(parts2[0]);
				    String recipe = parts2[2];

				    if (tempOrderNum == orderNum) {
				        String[] requiredIngredients = getIngredientsForRecipe(recipe);
				        System.out.printf("Ingredients needed: %s\n", String.join(", ", requiredIngredients));
				        System.out.print("Enter the ingredients to complete the order (comma-separated): ");
				        String completingOrder = sc.nextLine();

				        if (completingOrder.equals(String.join(", ", requiredIngredients))) {
				        	boolean canFulfillOrder = true;

				        	for (Ingredient ingredient : inventory) {
				        	    int needed = 0;
				        	    for (String name : requiredIngredients) {
				        	        if (name.equals(ingredient.getName())) {
				        	            needed++;
				        	        }
				        	    }
				        	    if (ingredient.getQuantity() < needed) {
				        	        canFulfillOrder = false;
				        	    }
				        	}

				            if (canFulfillOrder) {
				                for (String ingredientName : requiredIngredients) {
				                    for (Ingredient ingredient : inventory) {
				                        if (ingredient.getName().equals(ingredientName)) {
				                            ingredient.removeQuantity(1);
				                        }
				                    }
				                }
				                inventorySaver();
                                int gain = Integer.parseInt(parts2[3]);
                                currentScore += gain;
                                for (int num = 1; num <= orders.size(); num++) {
                                	if (tempOrderNum == num) {
                                		orders.remove(num-1);
                                	}
                                }
                                restaurantSaver();
				                System.out.printf("Order complete! You earned $%d\n", gain);
				                br.close();
				                break;
				            } else {
				                System.out.println("Not enough ingredients in inventory to complete the order.");
				                br.close();
				                break;
				            }
				        } else {
				        	System.out.println("Incorrect ingredients or order has expired");
				        	break;
				        }
				    }
				}
			} catch (Exception e) {
				System.out.println("Incorrect ingredients or order has expired");
			}	
    	}
    }
}
	
	void gameGuide() {
        while (true) {
    		for (int i = 0; i < 100; i++) {
                System.out.println();
            }
    		System.out.println(" ╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println(" ║                                HOW TO PLAY                                   ║");
            System.out.println(" ║                               Krazy Kitchen                                  ║");
            System.out.println(" ╠══════════════════════════════════════════════════════════════════════════════╣");
            System.out.println(" ║ Welcome, Master Chef! Your mission: Manage a burger joint, complete orders   ║");
            System.out.println(" ║ accurately and quickly. You'll switch between Output and Input modes.        ║");
            System.out.println(" ╟──────────────────────────────────────────────────────────────────────────────╢");
            System.out.println(" ║ 1. Output Mode (View orders and inventory):                                  ║");
            System.out.println(" ║    ? View active orders, inventory, and current score                        ║");
            System.out.println(" ║    ? Monitor customer names, burger requests, timers, and potential rewards  ║");
            System.out.println(" ║    ? Watch out for VIP orders - higher rewards!                              ║");
            System.out.println(" ║    ? Keep an eye on your inventory                                           ║");
            System.out.println(" ╟──────────────────────────────────────────────────────────────────────────────╢");
            System.out.println(" ║ 2. Input Mode (Process orders):                                              ║");
            System.out.println(" ║    ? Assemble burgers based on customer requests                             ║");
            System.out.println(" ║    ? Follow recipes exactly (e.g., 'Cheeseburger' = Bun + Patty + Cheese)    ║");
            System.out.println(" ║    ? Correct assembly = Points and rewards                                   ║");
            System.out.println(" ╟──────────────────────────────────────────────────────────────────────────────╢");
            System.out.println(" ║ 3. Scoring and Gameplay Tips:                                                ║");
            System.out.println(" ║    ? Base reward for each completed order                                    ║");
            System.out.println(" ║    ? Bonus rewards for VIP orders                                            ║");
            System.out.println(" ║    ? Avoid expired orders                                                    ║");
            System.out.println(" ║    ? Manage your time and inventory wisely                                   ║");
            System.out.println(" ╟──────────────────────────────────────────────────────────────────────────────╢");
            System.out.println(" ║ 4. Exiting the Game:                                                         ║");
            System.out.println(" ║    ? Type 'exit' during Output Mode to leave                                 ║");
            System.out.println(" ║    ? Return to main menu or switch modes anytime                             ║");
            System.out.println(" ╚══════════════════════════════════════════════════════════════════════════════╝");
            System.out.println(" Good luck, Chef! Keep those burgers coming!");
            System.out.println("");
            System.out.println(" Press \'enter\' to continue");
            
            sc.nextLine();
            return;
        }
	}
	
	void quit() {
		for (int i = 0; i < 100; i++) {
            System.out.println();
        }
        System.out.println("                           Exiting the game. Goodbye!");
        System.out.println("                                 ....:::::::.....");
        System.out.println("                           ..:-=+*###%%%%%%##*+=-:..");
        System.out.println("                       ..:=+#%%%%%%%%%%%%%%%%%%##%%%#*=-:.");
        System.out.println("                     .:=*%%%%%%%%%%%%%%%%%%%%%%#+%%%%%%%#+-:.");
        System.out.println("                  .:-%%%%%%%%%%%%%%%%%%%%%%%%%#*+%%%%%%%%#=:.");
        System.out.println("                .:=#%%%%%%%%%%%%%%%%%%%%#%%%%%#+--#%%%%%%%%%%#+:.");
        System.out.println("               :-%%%%%%%%%%%%%%%%%%%%%#%%%#+::=##%%%%%%%%%%%%#=:.");
        System.out.println("             .:+#%%%%%%%%%%%%%%%%%%%%#++=-::=###%%%%%%%%%%%%%%%*:.");
        System.out.println("            .:%%%%%%%%%%%%%%%%##+=-:::::--+#%**%%%%%%%%%%%%%%%%%#-:");
        System.out.println("           .:%%%%%%#%%##%#+=-::::::::--+####+*%%%%%%%%%%%%%%%%%%%#-:");
        System.out.println("          .:%%%%%%####%#=::::..::-=+#########%%%%%%%%%%%%%%%%%%%%%#-:");
        System.out.println("          :=%%%%%%%==#%:::...::=+##%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%:.");
        System.out.println("         ::#%%%%%%#-%#:::...:-=##%%%%%%%%%%%%%%#%%%%%#%%%%%%%%%%%%%%=:");
        System.out.println("         :+%%%%%%%#-#%+::.. .:-#%%%%%%%#*==-----===-=++#%%%%%%%%%%%%%%#:.");
        System.out.println("        .:%%%%%%%%+#%+::.. .:-#%%%##=:::::::....:::::=#%%%%%%%%%%%%%%-.");
        System.out.println("        .:#%%%%%%%%##%::.. .::=++###*+++===::.. .:::-###%%%%%%%%%%%-:");
        System.out.println("        .:%%%%%%%%%%##%-::.. ..:-==+**######+==::. ..::=%##%%%%%%%%%%=:");
        System.out.println("        .:#%%%%%%%%%%%%#=::::.....::::::::+#%%#+::. .::-%%*#%%%%%%%%%-:");
        System.out.println("         :%%%%%%%%%%%%%#==---=-::::::-=+#%%%%%%%#::...::-%%+%%%%%%%%#:.");
        System.out.println("         :-%%%%%%%%%%%%##%%##%%####%%%%%%%%###*=::..:::+%%=+%%%%%%%%+:");
        System.out.println("         .:%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#=::..::::%+-%%%%%%%#-.");
        System.out.println("          :-#%%%%%%%%%%%%%%%%%%%%%#%%%##%%#+=-::...:::=#####%%%%%%%=:");
        System.out.println("           :-#%%%%%%%%%%%%%%%%%%%*####+=-:::::::::-+#%##%%#%%%%%%%+:.");
        System.out.println("            :=#%%%%%%%%%%%%%%%%#+##=-:::::--=+**#%%%%%%%%%%%%%%%%+:.");
        System.out.println("             :-#%%%%%%%%%%%%%%###=:::-=+#%%%%%%%%%%%%%%%%%%%%%%%+:.");
        System.out.println("              .:+%%%%%%%%%%%%###-::+#%%%%%%%%%%%%%%%%%%%%%%%%%%%#-:");
        System.out.println("                :-#%%%%%%%%%%%*:-+#%%%%%%%%%%%%%%%%%%%%%%%%%%%#=:.");
        System.out.println("                 .:=%%%%%%%%=**#%%%%%%%%%%%%%%%%%%%%%%%%%%#+:.");
        System.out.println("                   .:-+#%%%%%#%*%%%%%%%%%%%%%%%%%%%%%%%%#=:.");
        System.out.println("                      .:-+#%%%%%#%%%%%%%%%%%%%%%%%%%%%#*=:.");
        System.out.println("                         ..:=+#%%%%%%%%%%%%%%%%%#+=-:.");
        System.out.println("                            ...:--==+++++++==--:...");
        System.out.println("                                       .");
        System.out.println("             24-1 Relentlessly move forward and achieve our dreams.");
        System.exit(0);
		return;
	}

	void displayMenu() {
        int option;
        
		while (true) {
    		for (int i = 0; i < 100; i++) {
                System.out.println();
            }
    		System.out.println(
    				"██╗░░██╗██████╗░░█████╗░███████╗██╗░░░██╗░░░░██╗░░██╗██╗████████╗░█████╗░██╗░░██╗███████╗███╗░░██╗\n" +
    				"██║░██╔╝██╔══██╗██╔══██╗╚════██║╚██╗░██╔╝░░░░██║░██╔╝██║╚══██╔══╝██╔══██╗██║░░██║██╔════╝████╗░██║\n" +
    				"█████═╝░██████╔╝███████║░░███╔═╝░╚████╔╝░░░░░█████═╝░██║░░░██║░░░██║░░╚═╝███████║█████╗░░██╔██╗██║\n" +
    				"██╔═██╗░██╔══██╗██╔══██║██╔══╝░░░░╚██╔╝░░░░░░██╔═██╗░██║░░░██║░░░██║░░██╗██╔══██║██╔══╝░░██║╚████║\n" +
    				"██║░╚██╗██║░░██║██║░░██║███████╗░░░██║░░░░░░░██║░╚██╗██║░░░██║░░░╚█████╔╝██║░░██║███████╗██║░╚███║\n" +
    				"╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░░░░░╚═╝░░╚═╝╚═╝░░░╚═╝░░░░╚════╝░╚═╝░░╚═╝╚══════╝╚═╝░░╚══╝"
    		);
    		System.out.println("Main Menu:");
    		System.out.println("1. Output Mode (View orders and inventory)");
    		System.out.println("2. Input Mode (Process orders)");
    		System.out.println("3. How to Play");
    		System.out.println("4. Exit");
    		System.out.print(">> ");
    		option = sc.nextInt(); sc.nextLine();
    		switch (option) {
    		case 1:
    			output = true;
    			outputMode();
    			output = false;
    			break;
    		case 2:
    			inputMode();
    			break;
    		case 3:
    			gameGuide();
    			break;
    		case 4:
    			quit();
    			break;
    		default:
    			System.out.println("Invalid option. Please select again.");
    			continue;
    		}	
        }
	}
	
	public Main() {
		playing = true;
	    initializeThread();
		
		while (playing) {
			displayMenu();
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
