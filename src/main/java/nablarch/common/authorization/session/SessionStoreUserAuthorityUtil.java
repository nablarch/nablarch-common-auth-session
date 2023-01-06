package nablarch.common.authorization.session;

import nablarch.core.repository.SystemRepository;
import nablarch.core.util.annotation.Published;
import nablarch.fw.ExecutionContext;

import java.util.Collection;

/**
 * ユーザに紐づく権限をセッションストアに保存するAPIを提供するクラス。
 * @author Tanaka Tomoyuki
 */
@Published
public class SessionStoreUserAuthorityUtil {

    /**
     * 現在のセッションに紐づくユーザが持つ権限をセッションストアに保存する。
     * @param authorities ユーザが持つ権限の一覧
     * @param context 実行コンテキスト
     * @throws IllegalStateException
     *     {@code "userAuthorityResolver"}という名前で{@link SessionStoreUserAuthorityResolver}のコンポーネントがシステムリポジトリから取得できない場合
     */
    public static void save(Collection<String> authorities, ExecutionContext context) {
        SessionStoreUserAuthorityResolver resolver = SystemRepository.get("userAuthorityResolver");
        if (resolver == null) {
            throw new IllegalStateException("The component of SessionStoreUserAuthorityResolver named as 'userAuthorityResolver' is not found.");
        }
        resolver.save(authorities, context);
    }

    /**
     * 本クラスはインスタンスを生成しない。
     */
    private SessionStoreUserAuthorityUtil() {}
}
