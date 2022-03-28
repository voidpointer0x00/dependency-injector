package voidpointer.spigot.framework.di;

import java.util.Objects;

final class Pair<F, S> {
    F first;
    S second;

    Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }

    static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
