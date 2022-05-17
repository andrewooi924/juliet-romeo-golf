package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.items.*;
import game.reset.Resettable;
import game.Status;
import game.reset.ResetAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Class representing the Player.
 */
public class Player extends Actor implements WalletKeeper, WoodKeeper, Resettable, IntrinsicFighter {

	private final Menu menu = new Menu();
	private int balance = 1000;
	private int woodBalance = 0;
	private boolean resetTimes = true;

	private int attackDamage = 5;



	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
		this.addCapability(Status.HOSTILE_TO_ENEMY);
		this.addCapability(Status.CAN_JUMP);
		this.addCapability(Status.WALKABLE_FOR_PLAYER);
		this.addCapability(Status.CAN_MANAGE_MONEY);
		this.addCapability(Status.CAN_MANAGE_WOOD);
		this.addCapability(Status.CAN_TELEPORT);
		this.addCapability(Status.CAN_INTRINSIC_ATTACK);
		Bottle b = new Bottle();
		this.addItemToInventory(b);
		registerInstance();
	}

	/**
	 * The player's actions to play
	 * @param actions    collection of possible Actions for this Actor
	 * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
	 * @param map        the map containing the Actor
	 * @param display    the I/O object to which messages may be written
	 * @return an Action to be played
	 */
	@Override
	public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		Location playerPos = map.locationOf(this);
		display.println("Mario" + this.printHp() + " at " + "(" + playerPos.x() + ", " + playerPos.y() + ")");
		display.println("Wallet: $" + this.getWalletBalance());
		display.println("Wood: " + this.getWoodAmount());
		// return/print the console menu
		if (this.hasCapability(Status.POWER_STAR)) {
			display.println("Mario is INVINCIBLE!");
		}
		if (resetTimes) {
			actions.add(new ResetAction());
		}
		return menu.showMenu(this, actions, display);
	}

	/**
	 * Returns a character whether it be 'm' or 'M' if player have a status of TALL
	 * @return a character either 'm' or 'M'
	 */
	@Override
	public char getDisplayChar(){
		return this.hasCapability(Status.TALL) ? Character.toUpperCase(super.getDisplayChar()): super.getDisplayChar();
	}

	/**
	 * Returns the balance of the player
	 * @return an integer representing the balance of the player
	 */
	@Override
	public int getWalletBalance() {
		return this.balance;
	}

	/**
	 * Adds the amount to the balance of the player
	 * @param amount - an integer representing the amount wanted to be added to the player
	 */
	@Override
	public void addToWallet(int amount) {
		this.balance += amount;
	}

	/**
	 * deducts the amount from the wallet
	 * @param amount - an integer representing the amount that should be deducted from the player's balance
	 */
	@Override
	public void deductFromWallet(int amount) {
		this.balance -= amount;
	}

	/**
	 * Resets the statuses on the player and heals it to maximum health
	 */
	@Override
	public void resetInstance() {
		resetTimes = false;
		heal(getMaxHp());
		this.removeCapability(Status.POWER_STAR);
		this.removeCapability(Status.TALL);
	}

	@Override
	public void setIntrinsicDamage(int damage) {
		this.attackDamage = damage;
	}

	@Override
	public int getIntrinsicDamage() {
		return this.attackDamage;
	}

	@Override
	public IntrinsicWeapon getIntrinsicWeapon() {
		return new IntrinsicWeapon(this.attackDamage, "punches");
	}

	@Override
	public Weapon getWeapon() {
		//chooses weapon with highest damage
		List<Weapon> weapons = new ArrayList<>();
		// Don't forget the base weapon as well.
		weapons.add(this.getIntrinsicWeapon());
		for (Item item : this.getInventory()){
			if (item.asWeapon() != null){
				weapons.add((Weapon)item);
			}
		}
		Collections.sort(weapons, new SortByDamage());
		return weapons.get(0);
	}

	@Override
	public int getWoodAmount() {
		return this.woodBalance;
	}

	@Override
	public void addToWood(int amount) {
		this.woodBalance += amount;
	}

	@Override
	public void deductFromWood(int amount) {
		this.woodBalance -= amount;
	}

	/**
	 * A sorting class that sorts the list of weapons by their damage in descending order.
	 */
	public class SortByDamage implements Comparator<Weapon>{

		public int compare(Weapon x, Weapon y){
			return y.damage() - x.damage();
		}
	}
}
