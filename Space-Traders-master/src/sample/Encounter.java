package sample;


public abstract class Encounter {
    private String[] responses;
    private boolean travelSuccess;
    private boolean resolved;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getTravelSuccess() {
        return travelSuccess;
    }

    public void setTravelSuccess(boolean newValue) {
        travelSuccess = newValue;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isResolved() {
        return resolved;
    }

    public abstract void resolve(String response);

    public String[] getResponses() {
        return responses;
    }

    public void setResponses(String[] responses) {
        this.responses = responses;
    }
}
