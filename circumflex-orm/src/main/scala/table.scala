package ru.circumflex.orm


import collection.mutable.Buffer

/**
 * Provides base functionality for generating domain model schema,
 * as well as validating, quering, inserting, deleting and updating
 * domain model objects (a.k.a. <code>records</code>).
 * In general there should be only one table instance per record class
 * (a singleton object, or, more conveniantly, the companion object).
 */
abstract class Table[R <: Record](val schemaName: String,
                                  val tableName: String) {

  //TODO make em private
  var columns: Seq[Column[_, R]] = Nil
  var constraints: Seq[Constraint[_]] = Nil     // TODO ???????

  /**
   * Configuration object is used for all persistence-related stuff.
   * Override it if you want to use your own configuration implementation.
   * @return DefaultConfiguration by default
   */
  def configuration: Configuration = DefaultConfiguration

  /**
   * Proxy to fast-retrieve dialect.
   */
  def dialect: Dialect = configuration.dialect

  /**
   *  Returns dialect-specific qualified name of a table.
   */
  def qualifiedName: String = configuration.dialect.tableQualifiedName(this)

  /**
   * Returns a mandatory primary key constraint for this table.
   */
  def primaryKey: PrimaryKey[R];




  /* HELPERS */

  /**
   * Helper method to create primary key constraint.
   */
  def pk(columns: Column[_, R]*) =
    new PrimaryKey(this, columns.toList)

  /**
   * Helper method to create unique constraint (return table instance for chaining).
   */
  def unique(columns: Column[_, R]*): Table[R] = {
    val constr = new UniqueKey(this, columns.toList)
    constraints ++= List(constr)
    return this
  }

  /**
   * Helper method to create a bigint column.
   */
  def longColumn(name: String): LongColumn[R] = {
    val col = new LongColumn(this, name)
    columns ++= List(col)
    return col
  }

  /**
   * Helper method to create a string column.
   */
  def stringColumn(name: String): StringColumn[R] = {
    val col = new StringColumn(this, name)
    columns ++= List(col)
    return col
  }

}



