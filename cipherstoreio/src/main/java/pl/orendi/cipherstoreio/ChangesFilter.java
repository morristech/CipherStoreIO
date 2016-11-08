package pl.orendi.cipherstoreio;

/**
 * Created by Pawel Bochenski on 08.11.2016.
 */

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.Changes;

import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

import static com.pushtorefresh.storio.internal.Checks.checkNotNull;

/**
 * FOR INTERNAL USAGE ONLY.
 * <p>
 * Hides RxJava from ClassLoader via separate class.
 */
final class ChangesFilter implements Func1<Changes, Boolean> {

    @NonNull
    private final Set<String> tables;

    private ChangesFilter(@NonNull Set<String> tables) {
        this.tables = tables;
    }

    @NonNull
    static Observable<Changes> apply(@NonNull Observable<Changes> rxBus, @NonNull Set<String> tables) {
        checkNotNull(tables, "Set of tables can not be null");
        return rxBus
                .filter(new ChangesFilter(tables));
    }

    @Override
    public Boolean call(Changes changes) {
        // if one of changed tables found in tables for subscription -> notify observer
        for (String affectedTable : changes.affectedTables()) {
            if (tables.contains(affectedTable)) {
                return true;
            }
        }

        return false;
    }
}
