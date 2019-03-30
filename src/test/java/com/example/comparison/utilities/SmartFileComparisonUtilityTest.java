package com.example.comparison.utilities;

import com.example.comparison.repository.ComparisonResultRepository;
import com.example.comparison.repository.impl.inmemory.InMemoryRecordRepository;
import com.example.comparison.repository.impl.inmemory.InMemoryComparisonResultRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SmartFileComparisonUtilityTest {

    private SmartFileComparisonUtility smartFileComparisonUtility = initializeComparisonUtility();


    @BeforeClass
    private static SmartFileComparisonUtility initializeComparisonUtility() {
        InMemoryRecordRepository recordRepository = new InMemoryRecordRepository();
        ComparisonResultRepository comparisonResultRepository = new InMemoryComparisonResultRepository(recordRepository);

        return new SmartFileComparisonUtility(recordRepository, comparisonResultRepository);
    }

    @Test
    public void testComparisonUtility() throws IOException {
        //given
        String actualFilePath = getClass().getResource("/actual.txt").getPath();
        String expectedFilePath = getClass().getResource("/expected.txt").getPath();
        String output = Files.createTempFile("test", "report").toString();

        //when
        smartFileComparisonUtility.compareAndGenerateReport(expectedFilePath, actualFilePath, output);

        //then
        List<String> reportLines = Files.readAllLines(Paths.get(output));
        Assert.assertEquals(reportLines.size(), 5);
        Assert.assertTrue(reportLines.contains("mismatch for MSFT (Expected Quantity: -1000, Actual Quantity: -1001)"));
        Assert.assertTrue(reportLines.contains("missing line for IBM"));
        Assert.assertTrue(reportLines.contains("unexpected line for FB"));
        Assert.assertTrue(reportLines.contains("mismatch for AMZN (Expected Price: 00141.2500, Actual Price: 00141.2580)"));
        Assert.assertTrue(reportLines.contains("mismatch for TROW (Expected Comments: Just bought., Actual Comments: Just adding up.)"));
    }
}
