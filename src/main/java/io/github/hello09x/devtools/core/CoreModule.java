package io.github.hello09x.devtools.core;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public class CoreModule extends AbstractModule {


    @Provides
    @Singleton
    public Gson gson() {
        return new Gson();
    }

}
