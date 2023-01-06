package nablarch.common.authorization.session;

import nablarch.common.web.session.SessionEntry;
import nablarch.common.web.session.SessionStore;
import nablarch.fw.ExecutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メモリ上にデータを保存する{@link SessionStore}実装（テスト用）。
 * @author Tanaka Tomoyuki
 */
public class InMemorySessionStore extends SessionStore {
    private final Map<String, List<SessionEntry>> store = new HashMap<String, List<SessionEntry>>();

    /**
     * コンストラクタ。
     */
    public InMemorySessionStore() {
        super("inMemory");
    }

    @Override
    public List<SessionEntry> load(String sessionId, ExecutionContext executionContext) {
        return store.get(sessionId);
    }

    @Override
    public void save(String sessionId, List<SessionEntry> entries, ExecutionContext executionContext) {
        store.put(sessionId, entries);
    }

    @Override
    public void delete(String sessionId, ExecutionContext executionContext) {
        store.remove(sessionId);
    }

    @Override
    public void invalidate(String sessionId, ExecutionContext executionContext) {
        store.clear();
    }
}
