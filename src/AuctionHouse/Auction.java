package AuctionHouse;
import Helper.Bid;
import Helper.Item;

/**Auction object, which is a thread that keeps track of time and price*/
public class Auction implements Runnable{
    /**Keeps track of item*/
    private Item item;
    /**Keep track of highest bid*/
    private Bid maxBid;
    /**Keeps track of the max bid amount*/
    private double maxBidAmount;
    /**Keeps track of the most recent bidder*/
    /**Times the bid*/
    private int waitCount;
    /**Variable to represent the auction status
     * 0 - auction going
     * 1 - item sold
     * -1 - item recalled because of no bidder*/
    private int auctionStatus;

    /**Constructs an Auction with a given item object
     * Set item price using rarity
     * set default auction status
     * start time count*/
    public Auction(Item item){
        this.item = item;
        this.maxBid = new Bid(item.getID(),item.getBasePrice());
        maxBidAmount = item.getBasePrice();
        waitCount = 0;
        auctionStatus = 0;
    }

    /**Get the current highest bid amount
     * @return double of a that amount
     * */
    public double getMaxBidAmount(){
        return maxBidAmount;
    }

    /**Get the max bid object
     * @return bid object*/
    public Bid getMaxBid(){
        return maxBid;
    }

    /**Gets the item on sale
     * @return item object*/
    public Item getItem(){
        return item;
    }

    /**Checks if anyone have bid on this stage
     * @return true if there is, else false*/
    public boolean bidGoingOn(){
        if(maxBidAmount == item.getBasePrice()){
            return false;
        }
        return true;
    }

    /**Update replace the current max bid with a new bid,
     *  and set highest bid amount*/
    public void updateBid(Bid bid){
        maxBid = bid;
        maxBidAmount = bid.getBidAmount();
        item.updateMax(maxBidAmount);
    }

    /**Get the stage info as String
     * @return String*/
    public String toString(){
        String s = "Item on Stage: "+item.getNAME()+" $"+maxBidAmount;
        return s;
    }

    /**Tells the auction house the auction status
     * @return integer representing the status
     * */
    protected int getAuctionStatus(){
        return auctionStatus;
    }

    /**Called when some one bids to reset count down*/
    protected void resetCount(){
        waitCount = 0;
    }

    /**Runs this thread*/
    public synchronized void run(){
        while(!Thread.interrupted()){
            try{
                if(waitCount == 60){
                    /**If some one bid on the item*/
                    if(maxBid.getAgentIP() != null){
                        auctionStatus = 1;
                        /**Tell auction house house winning bidder/item/price*/
                    }else{
                        auctionStatus = -1;
                        //System.out.println("    No one bids on "+item.getNAME());
                        /**No one bid on this item, tell auction house to
                         * replace it with a new item*/
                    }
                    Thread.currentThread().interrupt();
                    break;
                }else {
                    waitCount++;
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}