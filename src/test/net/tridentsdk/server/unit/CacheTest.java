package net.tridentsdk.server.unit;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import net.tridentsdk.concurrent.ConcurrentCache;
import net.tridentsdk.config.JsonConfig;
import net.tridentsdk.factory.CollectFactory;
import net.tridentsdk.factory.ConfigFactory;
import net.tridentsdk.factory.Factories;
import net.tridentsdk.server.TridentScheduler;
import net.tridentsdk.server.threads.ThreadsManager;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

public class CacheTest extends AbstractTest {
    static {
        Factories.init(new ConfigFactory() {
            @Override
            public JsonConfig serverConfig() {
                return new JsonConfig(Paths.get("/topkek"));
            }
        });
        Factories.init(new CollectFactory() {
            @Override
            public <K, V> ConcurrentMap<K, V> createMap() {
                return new ConcurrentHashMapV8<>();
            }
        });
        Factories.init(new TridentScheduler());
        Factories.init(new ThreadsManager());
    }

    private final ConcurrentCache<Object, Object> cache = new ConcurrentCache<>();

    @Test
    public void insertNullValue() {
        final Object object = new Object();
        cache.retrieve(object, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        assertNull(cache.remove(object));
    }

    @Test
    public void insertNullKey() {
        cache.retrieve(null, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }

    @Test
    public void insertRemove() {
        final Object object = new Object();
        cache.retrieve(object, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return object;
            }
        });

        assertEquals(cache.remove(object), object);
        assertNull(cache.remove(object));
    }
}
