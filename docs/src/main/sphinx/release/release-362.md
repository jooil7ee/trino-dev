# Release 362 (20 Sep 2021)

## General

* Add support for {func}`listagg`. ({issue}`4835`)
* Improve join performance. ({issue}`8974`)
* Improve performance of queries that contain a `DISTINCT` clause. ({issue}`8967`, {issue}`9194`)
* Improve query performance by reducing CPU overhead of repartitioning data across worker nodes. ({issue}`9102`)
* Split metrics that are reported in operator stats into `connectorMetrics` and `metrics`. ({issue}`9156`)
* Prevent failure when querying `system.materialized_views` and a materialized view is dropped concurrently. ({issue}`9050`) 
* Fix incorrect results for aggregations that contain `DISTINCT` and `FILTER` clauses. ({issue}`9265`)
* Fix incorrect query results when row pattern contains back references. ({issue}`9109`)
* Fix `ALTER SCHEMA ... SET AUTHORIZATION` to resolve roles using the catalog of the schema instead of the session catalog. ({issue}`9066`)
* Fix query failure when query contains a cast from `varchar` to a shorter `char`. ({issue}`9036`)
* Fix planning failure of `INSERT` statement when source table has hidden columns. ({issue}`9150`)
* Fix planning of recursive queries when the recursion, the base plan, or the recursion step plan produce duplicate outputs. ({issue}`9153`)
* Fix failure when querying the [optimizer_rule_stats](optimizer-rule-stats) system table. ({issue}`8700`)
* Fix failure for queries that push projections into connectors. ({issue}`6200`)
* Fix planning timeout for queries containing `IS NULL`, `AND`, and `OR` predicates in the `WHERE` clause. ({issue}`9250`)
* Fix failure for queries containing `ORDER BY ... LIMIT` when columns in the subquery are known to be constant. ({issue}`9171`)

## Security

* Add `IN catalog` clause to `CREATE ROLE`, `DROP ROLE`, `GRANT ROLE`, `REVOKE ROLE`, and `SET ROLE` to specify 
  the target catalog of the statement instead of using the current session catalog. This change is necessary to 
  support system roles in the future. The old behavior of these statements can be restored by setting the 
  `deprecated.legacy-catalog-roles` config property. ({issue}`9087`)

## Web UI

* Add query error info to cluster overview page. ({issue}`8762`)

## JDBC driver

* Improve performance of listing table columns via `java.sql.DatabaseMetaData` API when filtering on schema name. ({issue}`9214`)

## Server RPM

* Change RPM architecture to `noarch` to allow installing on any machine type. ({issue}`9187`)

## BigQuery connector

* Support case insensitive name matching for BigQuery views. ({issue}`9164`)
* Change type mapping of BigQuery `datetime` from `timestamp(3)` to `timestamp(6)` in Trino. ({issue}`9052`)
* Change type mapping of BigQuery `time` from `time with time zone` to `time(6)` in Trino. ({issue}`9052`)
* Change type mapping of BigQuery `timestamp` from `timestamp(3) with time zone` to `timestamp(6) with time zone` in Trino. ({issue}`9052`)
* Fix failure for queries where predicate on `datetime` column is pushed down to BigQuery. ({issue}`9005`)
* Fix incorrect results when using parameterized `numeric` type with non-default precision and scale. ({issue}`9060`)
* Fix query failure when accessing tables with unsupported data type columns. ({issue}`9086`)
* Fix failure for queries where predicate on `float64` column involving `+infinity` or 
  `-infinity` values is pushed down to BigQuery. ({issue}`9122`)

## Cassandra connector

* Change minimum number of speculative executions from 2 to 1. ({issue}`9096`)

## Hive connector

* Support reading Parquet timestamp stored with millisecond or microsecond precision as `INT64` with 
  logical type annotations when Hive timestamp precision is `NANOSECONDS`. ({issue}`9139`)
* Support reading Parquet timestamp stored as `INT64` with nanosecond precision. ({issue}`9188`)
* Support writing Parquet timestamp stored as `INT64` with nanosecond precision when experimental Parquet writer is enabled.
  To use this, the Hive timestamp precision should be set to `NANOSECONDS`. ({issue}`9188`)
* Support loading of S3 mappings via HTTP(S) url. The `hive.s3.security-mapping.config-file property` can now 
  either point to a local file or a URL. ({issue}`6210`) 
* Allow reading from tables bucketed on a column that uses type for which bucketing is not natively 
  supported by Trino. Writing to such tables is still not allowed. ({issue}`8986`)
* Extend set of statistics reported by JMX to include metrics for calls made to the Glue statistics API. ({issue}`9100`) 
* Limit maximum file size generated by write operations to 1 GB by default. The limit is not exact and is applied on a best-effort basis. 
  The limit can be set with the `hive.target-max-file-size` configuration property or the `target_max_file_size` session property. ({issue}`7991`) 
* Allow specifying the list of schemas for which Trino will enforce that queries use a filter on partition keys for source tables.
  The list can be specified using the `hive.query-partition-filter-required-schemas`, or the `query_partition_filter_required_schemas` session property.
  The list is taken into consideration only if the `hive.query-partition-filter-required` configuration property or the `query_partition_filter_required` 
  session property is set to `true`. ({issue}`9106`)
* Fix failure when writing Parquet files with Snappy compression on ARM64. ({issue}`9148`)
* Fix performance regression when reading Parquet files that contain dictionaries. ({issue}`9161`)
* Fix incorrect table locking in Hive metastore when querying unpartitioned non-transactional tables. ({issue}`9070`)
* Fix `ArrayIndexOutOfBoundsException` when using the experimental Parquet writer. ({issue}`5518`)
* Fix reading Avro tables written with older Avro versions that had certain semi-invalid schemas. ({issue}`9114`)
* Fix possible `INSERT`/`UPDATE`/`ANALYZE` query failure when Glue metastore is in use and table statistics collection is enabled. ({issue}`9297`)

## Iceberg connector

* Add support for Iceberg `uuid` type. ({issue}`6663`)
* Fix the mapping of nested fields between table metadata and Parquet file metadata. This
  enables evolution of `row` typed columns for Iceberg tables stored in Parquet. ({issue}`9124`)
* Fix failure for queries filtering on columns with array, map, or row type. ({issue}`8822`)
* Fix `ArrayIndexOutOfBoundsException` when using the experimental Parquet writer. ({issue}`5518`)
* Fix query failures caused by errors reading certain Avro metadata files. ({issue}`9114`)

## Pinot connector

* Support pushdown of filters on `varbinary` columns to Pinot. ({issue}`9180`)
* Fix incorrect results for queries that contain aggregations and `IN` and `NOT IN` filters over `varchar` columns. ({issue}`9133`)
* Fix failure for queries with filters on `real` or `double` columns having `+Infinity` or `-Infinity` values. ({issue}`9180`)

## TPC-H connector

* Add support for switching the mapping of floating point values between SQL `double` and `decimal` types. The mapping 
  can be set via the `tpch.double-type-mapping` configuration property. ({issue}`7990`)

## SPI

* Change `Connector.isSingleStatementWritesOnly()` to return `true` by default. ({issue}`8872`)
