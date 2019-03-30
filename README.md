## Smart File Compare Utility

### Solution
The [initial version](https://github.com/josseyj/SmartFileCompareUtility/tree/v1) was to parse the files into records and then compare them one by one. But this approach would not scale with large files.
So the utility implementation was updated to use some repository abstractions to store and retrieve records.

There is an in-memory repository implementation that stores the records and results in local hashmaps is included in this solution. 
But we can switch it with repository implementation backed by a DB and the compare-utility need not be changed.

Usage:
```
SmartFileComparisonUtility utility = new SmartFileComparisonUtility(recordRepository, comparisonResultRepository);
smartFileComparisonUtility.compareAndGenerateReport(expectedFilePath, actualFilePath, outputPath);
```
