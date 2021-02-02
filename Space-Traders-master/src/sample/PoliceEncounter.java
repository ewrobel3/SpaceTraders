package sample;

public class PoliceEncounter extends Encounter {

    private Good demandedItem;
    private Player player;

    public PoliceEncounter(Player player) {
        this.setResponses(new String[] {"Forfeit", "Flee", "Fight" });
        this.player = player;
        this.demandedItem = player.getCurrentShip().getRandomInventoryItem();
        this.setTravelSuccess(false);
        setDescription(String.format("The space police hail your ship. "
                + "They tell you that your cargo of %s is contraband and must be confiscated. "
                + "What will you do?", demandedItem.getName()));
    }

    @Override
    public void resolve(String response) {
        if ("Forfeit".equals(response)) {
            resolveForfeit();
        } else if ("Flee".equals(response)) {
            resolveFlee();
        } else if ("Fight".equals(response)) {
            resolveFight();
        } else if ("Continue".equals(response)) {
            setResolved(true);
        }
    }

    public void resolveForfeit() {
        player.getCurrentShip().removeItem(demandedItem);
        setTravelSuccess(true);
        setDescription(String.format("You show the police your cargo of %1$s. "
            + "After a brief inspection they load the %1$s onto their cruiser. "
            + "\"Thank you for your cooperation, you are free to go\"", demandedItem.getName()));
        setResponses(new String[] {"Continue"});
    }

    public void resolveFlee() {
        if (D20.skillCheck(17, player.getPilotPoints())) {
            setDescription("You manage to escape the police by fleeing back the way you came.");
        } else {
            player.getCurrentShip().removeItem(demandedItem);
            player.loseCredits(player.getCredits() / 4);
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("You attempt to flee the police, but fail. "
                + "They impose a fine totalling 25%% of your credits and take your shipment of %s. "
                + "During the chase, your ship took %d points of damage.",
                    demandedItem.getName(), damage));
        }
        setResponses(new String[] {"Continue"});
    }

    public void resolveFight() {
        if (D20.skillCheck(17, player.getFighterPoints())) {
            setTravelSuccess(true);
            setDescription("You successfully fight off the police "
                    + "and continue to your destination.");
        } else {
            player.getCurrentShip().getInventory().clear();
            int damage = D20.roll();
            player.getCurrentShip().loseHealth(damage);
            setDescription(String.format("You attempt to fight off the police, but fail. "
                + "Your ship takes %d points of damage and the police take your entire cargo.",
                    damage));
            //maybe other bad things happen
        }
        setResponses(new String[] {"Continue"});
    }
}
