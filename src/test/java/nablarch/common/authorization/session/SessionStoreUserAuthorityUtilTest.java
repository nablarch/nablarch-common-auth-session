package nablarch.common.authorization.session;

import nablarch.core.repository.ObjectLoader;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.ExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

/**
 * {@link SessionStoreUserAuthorityUtil}の単体テスト。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserAuthorityUtilTest {
    private final MockSessionStoreUserAuthorityResolver mockSessionStoreUserAuthorityResolver = new MockSessionStoreUserAuthorityResolver();
    private final ExecutionContext context = new ExecutionContext();

    @Before
    public void setUp() {
        SystemRepository.clear();
        SystemRepository.load(new ObjectLoader() {
            @Override
            public Map<String, Object> load() {
                final Map<String, Object> objects = new HashMap<String, Object>();
                objects.put("userAuthorityResolver", mockSessionStoreUserAuthorityResolver);
                return objects;
            }
        });
    }

    /**
     * saveメソッドのテスト。
     */
    @Test
    public void testSave() {
        Collection<String> authorities = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");

        SessionStoreUserAuthorityUtil.save(authorities, context);

        assertThat(mockSessionStoreUserAuthorityResolver.authorities, is(sameInstance(authorities)));
        assertThat(mockSessionStoreUserAuthorityResolver.context, is(sameInstance(context)));
    }

    /**
     * saveメソッドのテスト(UserAuthorityResolverのコンポーネントが見つからなかった場合は例外をスローする)。
     */
    @Test
    public void testSaveThrowsExceptionIfUserAuthorityResolverIsNotFound() {
        SystemRepository.clear();

        final IllegalStateException exception = assertThrows(IllegalStateException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                Collection<String> authorities = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
                SessionStoreUserAuthorityUtil.save(authorities, context);
            }
        });

        assertThat(exception.getMessage(),
            is("The component of SessionStoreUserAuthorityResolver named as 'userAuthorityResolver' is not found."));
    }

    /**
     * {@link SessionStoreUserAuthorityResolver}のモック。
     */
    private static class MockSessionStoreUserAuthorityResolver extends SessionStoreUserAuthorityResolver {
        /**
         * saveメソッドに渡された権限一覧。
         */
        private Collection<String> authorities;
        /**
         * saveメソッドに渡された実行コンテキスト。
         */
        private ExecutionContext context;

        @Override
        public void save(Collection<String> authorities, ExecutionContext context) {
            this.authorities = authorities;
            this.context = context;
        }
    }
}