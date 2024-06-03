package application.server.domain;

import java.util.Optional;

public enum DocumentState {
    FREE(1, "FREE"),
    RESERVED(2, "RESERVED"),
    BORROWED(3, "BORROWED");

    private final int stateId;
    private final String name;

    DocumentState(int stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

    public int getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }

    public static Optional<DocumentState> fromInt(int stateId) {
        for (DocumentState state : DocumentState.values()) {
            if (state.getStateId() == stateId) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}