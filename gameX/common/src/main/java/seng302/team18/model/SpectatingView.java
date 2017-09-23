package seng302.team18.model;

import seng302.team18.message.RequestType;

/**
 * Enum that tells which mode is being spectated.
 */
public enum SpectatingView {
    RACE(RequestType.RACING.getCode()),
    ARCADE(RequestType.ARCADE.getCode()),
    BUMPER_BOATS(RequestType.BUMPER_BOATS.getCode()),
    CHALLENGE_MODE(RequestType.CHALLENGE_MODE.getCode());

    private int code;

    SpectatingView(int code) {this.code = code;}


    /**
     * @return the code associated with the viewed race mode
     */
    public int getViewType() {return code;}
}
