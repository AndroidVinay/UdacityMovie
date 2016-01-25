package example.com.udacitymovie;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by VPPL-10132 on 1/25/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // Create an InitializerBuilder
        super.onCreate();
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getBaseContext())
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

    }
}
