package sample;

import java.util.Random;

public class BanditEncounter extends Encounter {

    private int desiredPayment;
    private Player player;

    public BanditEncounter(Player player) {
        this.setResponses(new String[] {"Pay", "Flee", "Fight" });
        this.player = player;
        this.desiredPayment = randomPayment();
        this.setTravelSuccess(false);

        this.setDescription(String.format("You encounter some space bandits. "
                + "They demand that you hand over ©%d. "
                + "What will you do?", desiredPayment));
    }

    @Override
    public void resolve(String response) {
        if ("Pay".equals(response)) {
            resolvePay();
        } else if ("Flee".equals(response)) {
            resolveFlee();
        } else if ("Fight".equals(response)) {
            resolveFight();
        } else if ("Continue".equals(response)) {
            setResolved(true);
        }
    }

    private int randomPayment() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public void resolvePay() {
        if (player.getCredits() > desiredPayment) {
            player.loseCredits(desiredPayment);
            setDescription("You begrudgingly transfer the "
                    + "credits to the bandits and continue on your way.");
        } else if (!player.getCurrentShip().getInventory().isEmpty()) {
            player.getCurrentShip().loseAllItems();
            setDescription("After showing the bandits how broke you are, "
                + "their leader takes pity on you and simply takes all your cargo.");
        } else {
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("Annoyed at the futility of robbing you, "
                    + "the bandits deal %d points of damage your ship before leaving.", damage));
        }
        setTravelSuccess(true);
        setResponses(new String[] {"Continue"});
    }

    public void resolveFlee() {
        if (D20.skillCheck(15, player.getPilotPoints())) {
            setDescription("You manage to escape the bandits by fleeing back the way you came.");
        } else {
            player.loseCredits(player.getCredits());
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("You attempt to escape the bandits by "
                    + "fleeing back the way you came, "
                    + "but they catch you and steal all your credits. "
                    + "In the chase, your ship took %d points of damage.", damage));
        }
        setResponses(new String[] {"Continue"});
    }

    public void resolveFight() {
        if (D20.skillCheck(15, player.getFighterPoints())) {
            setTravelSuccess(true);
            int credits = 10 * D20.roll();
            player.addCredits(credits);
            setDescription(String.format("You fight the bandits and win. You gain ©%d.", credits));
            setResponses(new String[] {"Continue"});
        } else {
            player.loseCredits(player.getCredits());
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("You fight the bandits and lose. "
                    + "Your ship takes %d points of damage.", damage));
            setResponses(new String[] {"Continue"});
        }
    }
}
