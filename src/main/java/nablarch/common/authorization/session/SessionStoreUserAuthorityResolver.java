package nablarch.common.authorization.session;

import nablarch.common.authorization.UserAuthorityResolver;
import nablarch.common.web.session.SessionUtil;
import nablarch.fw.ExecutionContext;

import java.util.Collection;
import java.util.Collections;

/**
 * ユーザに紐づく権限をセッションストアから解決する{@link UserAuthorityResolver}実装。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserAuthorityResolver implements UserAuthorityResolver {
    private String sessionStoreKey = SessionStoreUserAuthorityResolver.class.getName() + ".user_authorities";

    @Override
    public Collection<String> resolve(String userId, ExecutionContext context) {
        return SessionUtil.or(context, sessionStoreKey, Collections.<String>emptyList());
    }

    /**
     * セッションストアに権限一覧を保存する。
     * @param authorities 権限一覧
     * @param context 実行コンテキスト
     */
    public void save(Collection<String> authorities, ExecutionContext context) {
        SessionUtil.put(context, sessionStoreKey, authorities);
    }

    /**
     * セッションストアに権限を保存するときに使用するキーを設定する。
     * @param sessionStoreKey セッションストアに権限を保存するときに使用するキー
     */
    public void setSessionStoreKey(String sessionStoreKey) {
        this.sessionStoreKey = sessionStoreKey;
    }

    /**
     * セッションストアに権限を保存するときに使用するキーを取得する。
     * @return セッションストアに権限を保存するときに使用するキー
     */
    public String getSessionStoreKey() {
        return sessionStoreKey;
    }
}
