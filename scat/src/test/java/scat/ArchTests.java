package scat;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

/**
 * @author levry
 */
@AnalyzeClasses(packagesOf = ScatApplication.class, importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchTests {

    @ArchTest
    static ArchRule architecture_is_respected = Architectures.onionArchitecture()
            .adapter("web", "scat.adapter.web..")
            .adapter("persistence", "scat.adapter.persistence..")
            .domainModels("scat.domain.model..")
            .domainServices(
                    "scat.domain.batch..",
                    "scat.domain.repo..",
                    "scat.domain.service.."
            )
            .withOptionalLayers(true);

}
