package nablarch.common.authorization.role.session;

import nablarch.common.authorization.role.UserRoleResolver;
import nablarch.common.web.session.SessionUtil;
import nablarch.fw.ExecutionContext;

import java.util.Collection;
import java.util.Collections;

/**
 * ユーザに紐づくロールをセッションストアから解決する{@link UserRoleResolver}実装。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserRoleResolver implements UserRoleResolver {
    private String sessionStoreKey = SessionStoreUserRoleResolver.class.getName() + ".user_roles";

    @Override
    public Collection<String> resolve(String userId, ExecutionContext context) {
        return SessionUtil.or(context, sessionStoreKey, Collections.<String>emptyList());
    }

    /**
     * セッションストアにロール一覧を保存する。
     * @param roles ロール一覧
     * @param context 実行コンテキスト
     */
    public void save(Collection<String> roles, ExecutionContext context) {
        SessionUtil.put(context, sessionStoreKey, roles);
    }

    /**
     * セッションストアにロールを保存するときに使用するキーを設定する。
     * @param sessionStoreKey セッションストアにロールを保存するときに使用するキー
     */
    public void setSessionStoreKey(String sessionStoreKey) {
        this.sessionStoreKey = sessionStoreKey;
    }

    /**
     * セッションストアにロールを保存するときに使用するキーを取得する。
     * @return セッションストアにロールを保存するときに使用するキー
     */
    public String getSessionStoreKey() {
        return sessionStoreKey;
    }
}
