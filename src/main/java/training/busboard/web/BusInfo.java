package training.busboard.web;

import training.busboard.Main;

public class BusInfo {
    private final String postcode;
    private final String boardString;

    public BusInfo(String postcode) {
        this.postcode = postcode;
        this.boardString = Main.busBoard(postcode);
    }

    public String getPostcode() {
        return postcode;
    }
    public String getBoardString() {
        return boardString;
    }
}
