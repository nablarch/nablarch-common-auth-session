package nablarch.common.authorization.role.session;

import nablarch.common.authorization.role.session.SessionStoreUserRoleResolver;
import nablarch.common.authorization.role.session.SessionStoreUserRoleUtil;
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
 * {@link SessionStoreUserRoleUtil}の単体テスト。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserRoleUtilTest {
    private final MockSessionStoreUserRoleResolver mockSessionStoreUserRoleResolver = new MockSessionStoreUserRoleResolver();
    private final ExecutionContext context = new ExecutionContext();

    @Before
    public void setUp() {
        SystemRepository.clear();
        SystemRepository.load(new ObjectLoader() {
            @Override
            public Map<String, Object> load() {
                final Map<String, Object> objects = new HashMap<String, Object>();
                objects.put("userRoleResolver", mockSessionStoreUserRoleResolver);
                return objects;
            }
        });
    }

    /**
     * saveメソッドのテスト。
     */
    @Test
    public void testSave() {
        Collection<String> roles = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");

        SessionStoreUserRoleUtil.save(roles, context);

        assertThat(mockSessionStoreUserRoleResolver.roles, is(sameInstance(roles)));
        assertThat(mockSessionStoreUserRoleResolver.context, is(sameInstance(context)));
    }

    /**
     * saveメソッドのテスト(UserRoleResolverのコンポーネントが見つからなかった場合は例外をスローする)。
     */
    @Test
    public void testSaveThrowsExceptionIfUserRoleResolverIsNotFound() {
        SystemRepository.clear();

        final IllegalStateException exception = assertThrows(IllegalStateException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                Collection<String> roles = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
                SessionStoreUserRoleUtil.save(roles, context);
            }
        });

        assertThat(exception.getMessage(),
            is("The component of SessionStoreUserRoleResolver named as 'userRoleResolver' is not found."));
    }

    /**
     * {@link SessionStoreUserRoleResolver}のモック。
     */
    private static class MockSessionStoreUserRoleResolver extends SessionStoreUserRoleResolver {
        /**
         * saveメソッドに渡されたロール一覧。
         */
        private Collection<String> roles;
        /**
         * saveメソッドに渡された実行コンテキスト。
         */
        private ExecutionContext context;

        @Override
        public void save(Collection<String> roles, ExecutionContext context) {
            this.roles = roles;
            this.context = context;
        }
    }
}