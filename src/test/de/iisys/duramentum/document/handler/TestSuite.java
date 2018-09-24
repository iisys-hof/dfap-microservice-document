/*
 * *
 *  * Created: ${DATE} – ${TIME}
 *  * Project: ${PROJECT_NAME}
 *  * Package: ${PACKAGE_NAME}
 *  * @author Thomas Winkler <thomas.winkler@hiisys.de>
 *
 */

package de.iisys.duramentum.document.handler;

/**
 * Created: 18.09.17 – 12:01
 * Project: duramentum-document
 * Package: de.iisys.duramentum.document.handler
 *
 * @author Thomas Winkler <thomas.winkler@iisys.de>
 */
import de.iisys.duramentum.document.handler.strategy.SearchWithMachineAndOrderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileHandlerForSMBTest.class,
        FolderHandlerForSMBTest.class,
        SearchWithMachineAndOrderTest.class,
})

public class TestSuite {
}