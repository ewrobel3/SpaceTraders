package sample;

import java.util.Random;

public class TraderEncounter extends Encounter {

    private Player player;
    private int desiredPayment;
    private Good offeredItem;
    private boolean negotiated;
    private String previousDescription;

    public TraderEncounter(Player player) {
        this.setResponses(new String[] {"Buy", "Ignore", "Rob", "Negotiate"});
        this.player = player;
        this.desiredPayment = randomPayment();
        this.offeredItem = Good.getRandomGood();
        this.negotiated = false;
        this.setTravelSuccess(true);

        this.setDescription(String.format("As you travel, you meet another trader "
                + "who offers wares to you for purchase. "
                + "They are selling %s for ©%d. "
                + "What will you do?", offeredItem.getName(), desiredPayment));
    }

    @Override
    public void resolve(String response) {
        if ("Buy".equals(response)) {
            resolveBuy();
        } else if ("Rob".equals(response)) {
            resolveRob();
        } else if ("Negotiate".equals(response)) {
            resolveNegotiate();
        } else if ("Back".equals(response)) {
            setDescription(previousDescription);
            setResponses(negotiated ? new String[] {"Buy", "Ignore", "Rob"}
                    : new String[] {"Buy", "Ignore", "Rob", "Negotiate"});
        } else if ("Continue".equals(response) || "Ignore".equals(response)) {
            setResolved(true);
        }
    }

    private int randomPayment() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public void resolveBuy() {
        if (!(player.getCredits() >= desiredPayment)) {
            previousDescription = getDescription();
            setDescription("You don't have enough credits.");
            setResponses(new String[] {"Back"});
        } else if (!(player.getCurrentShip().getCargoSpace() > 0)) {
            previousDescription = getDescription();
            setDescription("Your cargo hold is full.");
            setResponses(new String[] {"Back"});
        } else {
            player.loseCredits(desiredPayment);
            player.getCurrentShip().addItem(offeredItem);
            setDescription("You make your purchase and continue on your way.");
            setResponses(new String[] {"Continue"});
        }
    }

    public void resolveRob() {
        if (D20.skillCheck(12, player.getFighterPoints())) {
            if (player.getCurrentShip().getCargoSpace() > 0) {
                Good randomGood = Good.getRandomGood();
                player.getCurrentShip().addItem(randomGood);
                setDescription(String.format("You successfully rob the rival trader. "
                    + "You add the %s you stole to your ship's cargo.", randomGood.getName()));
                if (D20.roll() > 15 && player.getCurrentShip().getCargoSpace() > 0) {
                    Good secondGood = Good.getRandomGood();
                    player.getCurrentShip().addItem(secondGood);
                    setDescription(String.format("You successfully rob the rival trader. "
                            + "You add the %s and %s you stole to your ship's cargo.",
                            randomGood.getName(), secondGood.getName()));
                }
            } else {
                setDescription("You successfully robbed the rival trader, "
                    + "but you don't have room in your cargo hold for the goods you stole "
                    + "You leave embarrassed.");
            }
            //chance of getting two items - exciting
        } else {
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("You fail to rob the trader. "
                    + "They deal %d points of damage to your ship.", damage));
        }
        setResponses(new String[] {"Continue"});
    }

    public void resolveNegotiate() {
        negotiated = true;
        setResponses(new String[] {"Buy", "Ignore", "Rob"});
        if (D20.skillCheck(16, player.getMerchantPoints())) {
            desiredPayment /= 2;
            setDescription("The trader seems to be taking well to your haggling.");
        } else {
            desiredPayment *= 2;
            setDescription("You have angered the trader while attempting to negotiate.");
        }
        previousDescription = String.format("The trader is offering %s for ©%d.",
                offeredItem.getName(), desiredPayment);
        setResponses(new String[] {"Back"});
    }
}
