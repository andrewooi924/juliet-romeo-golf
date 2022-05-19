package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropItemAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.items.PickUpItemAction;
import edu.monash.fit2099.engine.positions.Location;
import game.Status;
import game.actions.PickUpStackableAction;
import game.reset.Resettable;

/**
 * Literally just wood, can be used to craft items at a crafting bench
 * or sold to Toad for money
 */
//TODO, should also be sellable by modifying the Tradable interface
public class Wood extends Item implements Tradable, Resettable, Stackable{

    /**
     * The price of the wood if sold to Toad
     */
    private int price = 20;//sell price, not the buy price
    private int woodAmount;

    /**
     * A constructor for the Wood class
     */
    public Wood(int woodAmount){
        super("Wood (" + woodAmount + ")", 'W', false);
        this.woodAmount = woodAmount;
        registerInstance();
    }

    /**
     * Gets the amount of wood in this item. (It can have multiple wood in a single item)
     * @return int - represents the wood amount
     */
    @Override
    public int getAmount() {
        return woodAmount;
    }

    @Override
    public Storable getStorableType() {
        return Storable.WOOD;
    }

    /**
     * Getter for the price of the Wood
     * @return the price of the Wood
     */
    @Override
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for the Wood
     * @return the Wood
     */
    @Override
    public Item getItem() {
        return this;
    }

    /**
     * If the status is Resettable, then remove the coin from the ground
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        if (this.hasCapability(Status.RESETTABLE)) {
            currentLocation.removeItem(this);
            this.removeCapability(Status.RESETTABLE);
        }
    }

    @Override
    public DropItemAction getDropAction(Actor actor){
        return null;
    }

    @Override
    public PickUpItemAction getPickUpAction(final Actor actor) {
        return new PickUpStackableAction(this, this);
    }

    /**
     * Adds the capability RESETTABLE to this class
     */
    @Override
    public void resetInstance() {
        this.addCapability(Status.RESETTABLE);
    }
}
