package org.teiid.test.language;

import java.io.File;

public class ListAllFile {

	public static void main(String[] args) {
		File file = new File("/home/kylin/project/teiid/api/src/main/java/org/teiid/language");
		for(File f : file.listFiles()) {
			String name = f.getName();
			System.out.println(name.substring(0, name.length() - 5));
		}
	}
/*
SearchedCase
WindowSpecification
Expression
WithItem
Limit
Delete
LanguageFactory
SearchedWhenClause
Parameter
Literal
GroupBy
LanguageUtil
NamedTable
LanguageObject
ExpressionValueSource
SetClause
Argument
SubqueryIn
ColumnReference
DerivedColumn
InsertValueSource
AndOr
BatchedUpdates
Like
Select
Array
SubqueryComparison
BatchedCommand
Call
MetadataReference
SetQuery
With
SQLConstants
BaseInCondition
SubqueryContainer
QueryExpression
Comparison
vi
SortSpecification
AggregateFunction
ScalarSubquery
DerivedTable
TableReference
BaseLanguageObject
OrderBy
Update
Function
Join
Predicate
Command
Exists
WindowFunction
Condition
IsNull
In
Not
Insert
 
 
 
 
 
 */
}
