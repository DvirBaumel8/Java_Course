package Engine.ValidationManager;

public class Validator {
    private StringBuilder menuErrorMessage;

    RequestValidator requestValidator;
    SuggestValidator suggestValidator;

    public Validator() {
        this.menuErrorMessage = new StringBuilder();
        this.requestValidator = new RequestValidator();
        this.suggestValidator = new SuggestValidator();
    }

    public boolean validateMenuInput(String choice) {
        short input = 0;
        try {
            input = Short.parseShort(choice);
        }
        catch(Exception e) {
            try {
                Double.parseDouble(choice);
                this.menuErrorMessage.append("Your choice was fraction (double) please choose Integer, try again");
                return false;
            }
            catch(Exception ex) {

            }
            this.menuErrorMessage.append("Your choice isn't a number, please try again\n");
            return false;
        }
        if(input > 6 || input < 0 ) {
            this.menuErrorMessage.append("Your choice isn't a number between 1-6, please try again\n");
            return false;
        }
        return true;
    }

    public StringBuilder getMenuErrorMessage() {
        return menuErrorMessage;
    }

    public void setMenuErrorMessage(StringBuilder menuErrorMessage) {
        this.menuErrorMessage = menuErrorMessage;
    }

    public RequestValidator getRequestValidator() {
        return requestValidator;
    }

    public void setRequestValidator(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    public SuggestValidator getSuggestValidator() {
        return suggestValidator;
    }

    public void setSuggestValidator(SuggestValidator suggestValidator) {
        this.suggestValidator = suggestValidator;
    }
}
