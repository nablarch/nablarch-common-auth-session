package nablarch.common.authorization.role.session;

import nablarch.core.repository.SystemRepository;
import nablarch.core.util.annotation.Published;
import nablarch.fw.ExecutionContext;

import java.util.Collection;

/**
 * ユーザに紐づくロールをセッションストアに保存するAPIを提供するクラス。
 * @author Tanaka Tomoyuki
 */
@Published
public class SessionStoreUserRoleUtil {

    /**
     * 現在のセッションに紐づくユーザが持つロールをセッションストアに保存する。
     * @param roles ユーザが持つロールの一覧
     * @param context 実行コンテキスト
     * @throws IllegalStateException
     *     {@code "userRoleResolver"}という名前で{@link SessionStoreUserRoleResolver}のコンポーネントがシステムリポジトリから取得できない場合
     */
    public static void save(Collection<String> roles, ExecutionContext context) {
        SessionStoreUserRoleResolver resolver = SystemRepository.get("userRoleResolver");
        if (resolver == null) {
            throw new IllegalStateException("The component of SessionStoreUserRoleResolver named as 'userRoleResolver' is not found.");
        }
        resolver.save(roles, context);
    }

    /**
     * 本クラスはインスタンスを生成しない。
     */
    private SessionStoreUserRoleUtil() {}
}
