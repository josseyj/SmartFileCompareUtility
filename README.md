## Smart File Compare Utility

### Solution
The [initial version](https://github.com/josseyj/SmartFileCompareUtility/tree/v1) was to parse the files into records and then compare them one by one. But this approach would not scale with large files.
So the utility implementation was updated to use two repository abstractions to store/retrieve records and comparison results.

The solution includes in-memory repository implementations that stores the records and results within memory. 
But we can switch it with repository implementation backed by a DB and the compare-utility need not be changed.

Usage:
```
SmartFileComparisonUtility utility = new SmartFileComparisonUtility(recordRepository, comparisonResultRepository);
smartFileComparisonUtility.compareAndGenerateReport(expectedFilePath, actualFilePath, outputPath);
```
Now that the comparison and report generation is separated, we could still improve the comparison by splitting the files  and running in parallel and generate the report in the end.
