package com.sdust.crawler.service;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;

import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.remote.http.HttpMethod.POST;

/**
 * A {@link org.openqa.selenium.WebDriver} implementation that controls a PhantomJS running in Remote WebDriver mode.
 * This class is provided as a convenience for easily testing PhantomJS.
 * The control server which each instance communicates with will live and die with the instance.
 *
 * The Driver requires to optionally set some Capabilities or Environment Variables:
 * <ul>
 * <li>{@link PhantomJSDriverService#PHANTOMJS_EXECUTABLE_PATH_PROPERTY}</li>
 * <li>{@link PhantomJSDriverService#PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY}</li>
 * </ul>
 *
 * {@link PhantomJSDriverService#PHANTOMJS_EXECUTABLE_PATH_PROPERTY} is required only if the executable
 * <code>phantomjs</code> is not available through the <code>$PATH</code> environment variable:
 * you can provide it either via the {@link Capabilities} construction parameter object,
 * or via {@link System} Property.
 *
 * {@link PhantomJSDriverService#PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY} is optional in case you want to use a specific
 * version of GhostDriver (i.e. during development of GhostDriver).
 * You can provide it either via the {@link Capabilities} construction parameter object,
 * or via {@link System} Property.
 *
 * Instead, if you have a PhantomJS WebDriver process already running, you can instead use {@link
 * RemoteWebDriver#RemoteWebDriver(java.net.URL, Capabilities)} to delegate the
 * execution of your WebDriver/Selenium scripts to it. Of course, in that case you will than be in
 * charge to control the life-cycle of the PhantomJS process.
 *
 * NOTE: PhantomJS Remote WebDriver mode is implemented via
 * <a href="https://github.com/detro/ghostdriver">GhostDriver</a>.
 * It's a separate project that, at every stable release, is merged into PhantomJS.
 * If interested in developing (contributing to) GhostDriver, it's possible to run PhantomJS and pass GhostDriver as
 * a script.
 *
 * NOTE: The design of this class is heavily inspired by {@link org.openqa.selenium.chrome.ChromeDriver}.
 *
 * @author Ivan De Marino <http://ivandemarino.me>
 * @see PhantomJSDriverService#createDefaultService()
 */
public class PhantomJSDriverNew extends RemoteWebDriver implements TakesScreenshot {

    private final PhantomJSDriverService service;

    /**
     * Creates a new PhantomJSDriver instance. The instance will have a
     * default set of desired capabilities.
     *
     * @see PhantomJSDriverService#createDefaultService() for configuration details.
     */
    public PhantomJSDriverNew() {
        this(DesiredCapabilities.phantomjs());
    }

    /**
     * Creates a new PhantomJSDriver instance.
     *
     * @param desiredCapabilities The capabilities required from PhantomJS/GhostDriver.
     * @see PhantomJSDriverService#createDefaultService() for configuration details.
     */
    public PhantomJSDriverNew(Capabilities desiredCapabilities) {
        this(PhantomJSDriverService.createDefaultService(desiredCapabilities), desiredCapabilities);
    }

    /**
     * Creates a new PhantomJSDriver instance. The {@code service} will be started along with the
     * driver, and shutdown upon calling {@link #quit()}.
     *
     * @param service             The service to use.
     * @param desiredCapabilities The capabilities required from PhantomJS/GhostDriver.
     */
    public PhantomJSDriverNew(PhantomJSDriverService service, Capabilities desiredCapabilities) {
        super(new PhantomJSCommandExecutorNew(service), desiredCapabilities);
        this.service = service;
    }

    /**
     * Take screenshot of the current window.
     *
     * @param target The target type/format of the Screenshot
     * @return Screenshot of current window, in the requested format
     * @see TakesScreenshot#getScreenshotAs(OutputType)
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        // Get the screenshot as base64 and convert it to the requested type (i.e. OutputType<T>)
        String base64 = (String) execute(DriverCommand.SCREENSHOT).getValue();
        return target.convertFromBase64Png(base64);
    }

    /**
     * Execute a PhantomJS fragment.  Provides extra functionality not found in WebDriver
     * but available in PhantomJS.
     *
     * See the <a href="http://phantomjs.org/api/">PhantomJS API</a>
     * for details on what is available.
     *
     * A 'page' variable pointing to currently selected page is available for use.
     * If there is no page yet, one is created.
     *
     * When overriding any callbacks be sure to wrap in a try/catch block, as failures
     * may cause future WebDriver calls to fail.
     *
     * Certain callbacks are used by GhostDriver (the PhantomJS WebDriver implementation)
     * already.  Overriding these may cause the script to fail.  It's a good idea to check
     * for existing callbacks before overriding.
     *
     * @param script The fragment of PhantomJS JavaScript to execute.
     * @param args List of arguments to pass to the function that the script is wrapped in.
     *             These can accessed in the script as 'arguments[0]', 'arguments[1]',
     *             'arguments[2]', etc
     * @return The result of the evaluation.
     */
    public Object executePhantomJS(String script, Object... args) {
        script = script.replaceAll("\"", "\\\"");

        Iterable<Object> convertedArgs = Iterables.transform(
                Lists.newArrayList(args), new WebElementToJsonConverter());
        Map<String, ?> params = ImmutableMap.of(
                "script", script, "args", Lists.newArrayList(convertedArgs));

        return execute(COMMAND_EXECUTE_PHANTOM_SCRIPT, params).getValue();
    }

    private static final String COMMAND_EXECUTE_PHANTOM_SCRIPT = "executePhantomScript";

    public static Map<String, CommandInfo> getCustomCommands() {
        Map<String, CommandInfo> customCommands = new HashMap<String, CommandInfo>();

        customCommands.put(COMMAND_EXECUTE_PHANTOM_SCRIPT,
                new CommandInfo("/session/:sessionId/phantom/execute", POST));

        return customCommands;
    }
    public PhantomJSDriverService getService(){
        return service;
    }
}

