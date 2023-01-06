package nablarch.common.authorization.session;

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
 * {@link SessionStoreUserAuthorityResolver}の単体テスト。
 * @author Tanaka Tomoyuki
 */
public class SessionStoreUserAuthorityResolverTest {
    private final SessionStoreUserAuthorityResolver sut = new SessionStoreUserAuthorityResolver();
    private final ExecutionContext context = new ExecutionContext();

    @Before
    public void setUp() {
        SystemRepository.clear();
        final DiContainer diContainer =
            new DiContainer(new XmlComponentDefinitionLoader("nablarch/common/authorization/session/component-configuration.xml"));
        SystemRepository.load(diContainer);
    }

    /**
     * saveメソッドのテスト。
     */
    @Test
    public void testSave() {
        final Object authoritiesBeforeTest = SessionUtil.orNull(context, sut.getSessionStoreKey());
        assertThat(authoritiesBeforeTest, is(nullValue()));

        final List<String> authorities = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        sut.save(authorities, context);

        final List<String> authoritiesAfterTest = SessionUtil.get(context, sut.getSessionStoreKey());
        assertThat(authoritiesAfterTest, is(sameInstance(authorities)));
    }

    /**
     * setSessionStoreKeyメソッドのテスト。
     */
    @Test
    public void testSetSessionStoreKey() {
        final String key = "test-key";

        sut.setSessionStoreKey(key);

        final Object authoritiesBeforeTest = SessionUtil.orNull(context, key);
        assertThat(authoritiesBeforeTest, is(nullValue()));

        final List<String> authorities = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        sut.save(authorities, context);

        final List<String> authoritiesAfterTest = SessionUtil.get(context, key);
        assertThat(authoritiesAfterTest, is(sameInstance(authorities)));
    }

    /**
     * resolveメソッドのテスト。
     */
    @Test
    public void testResolve() {
        final Collection<String> authorities = Arrays.asList("FOO", "BAR", "FIZZ", "BUZZ");
        SessionUtil.put(context, sut.getSessionStoreKey(), authorities);

        final Collection<String> actual = sut.resolve("userId", context);

        assertThat(actual, is(sameInstance(authorities)));
    }

    /**
     * resolveメソッドのテスト(セッションから権限一覧が取得できない場合は空のコレクションを返す)。
     */
    @Test
    public void testResolveReturnEmptyCollectionIfAuthoritiesIsNotFoundInSession() {
        final Collection<String> actual = sut.resolve("userId", context);

        assertThat(actual, is(empty()));
    }
}