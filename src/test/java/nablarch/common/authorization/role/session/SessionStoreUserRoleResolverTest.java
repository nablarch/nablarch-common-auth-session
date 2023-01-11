package nablarch.common.authorization.role.session;

import nablarch.common.web.session.SessionUtil;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.fw.ExecutionContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * {@link SessionStoreUserRoleResolver}の単体テスト。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserRoleResolverTest {
    private final SessionStoreUserRoleResolver sut = new SessionStoreUserRoleResolver();
    private final ExecutionContext context = new ExecutionContext();

    @Before
    public void setUp() {
        SystemRepository.clear();
        final DiContainer diContainer =
            new DiContainer(new XmlComponentDefinitionLoader("nablarch/common/authorization/role/session/component-configuration.xml"));
        SystemRepository.load(diContainer);
    }

    /**
     * saveメソッドのテスト。
     */
    @Test
    public void testSave() {
        final Object rolesBeforeTest = SessionUtil.orNull(context, sut.getSessionStoreKey());
        assertThat(rolesBeforeTest, is(nullValue()));

        final List<String> roles = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        sut.save(roles, context);

        final List<String> rolesAfterTest = SessionUtil.get(context, sut.getSessionStoreKey());
        assertThat(rolesAfterTest, is(sameInstance(roles)));
    }

    /**
     * setSessionStoreKeyメソッドのテスト。
     */
    @Test
    public void testSetSessionStoreKey() {
        final String key = "test-key";

        sut.setSessionStoreKey(key);

        final Object rolesBeforeTest = SessionUtil.orNull(context, key);
        assertThat(rolesBeforeTest, is(nullValue()));

        final List<String> roles = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        sut.save(roles, context);

        final List<String> rolesAfterTest = SessionUtil.get(context, key);
        assertThat(rolesAfterTest, is(sameInstance(roles)));
    }

    /**
     * resolveメソッドのテスト。
     */
    @Test
    public void testResolve() {
        final Collection<String> roles = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        SessionUtil.put(context, sut.getSessionStoreKey(), roles);

        final Collection<String> actual = sut.resolve("userId", context);

        assertThat(actual, is(sameInstance(roles)));
    }

    /**
     * resolveメソッドのテスト(セッションからロール一覧が取得できない場合は空のコレクションを返す)。
     */
    @Test
    public void testResolveReturnEmptyCollectionIfRolesIsNotFoundInSession() {
        final Collection<String> actual = sut.resolve("userId", context);

        assertThat(actual, is(empty()));
    }
}