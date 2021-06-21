package net.arathain.malazan.common.util.interfaces;

import net.arathain.malazan.common.util.Warren;

import java.util.Optional;


public interface Talent {
    static Optional<Talent> of(Object context) {
        if (context instanceof Talent) {
            return Optional.of(((Talent) context));
        }
        return Optional.empty();
    }

    Warren getWarren();
    void setWarren(Warren warren);
    void setTelas(int level);
    int getTelas();
}
