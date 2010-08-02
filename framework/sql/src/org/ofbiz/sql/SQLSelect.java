/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ofbiz.sql;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.StringUtil;

public final class SQLSelect extends SQLStatement<SQLSelect> implements InsertSource {
    private final boolean isDistinct;
    private final List<FieldAll> fieldAlls;
    private final Map<String, FieldDef> fieldDefs;
    private final Table table;
    private final Map<String, Relation> relations;
    private final Condition whereCondition;
    private final Condition havingCondition;
    private final int offset;
    private final int limit;
    private final List<OrderByItem> orderBy;
    private final List<String> groupBy;

    public SQLSelect(boolean isDistinct, List<FieldAll> fieldAlls, Map<String, FieldDef> fieldDefs, Table table, Map<String, Relation> relations, Condition whereCondition, Condition havingCondition, List<String> groupBy, List<OrderByItem> orderBy, int offset, int limit) {
        this.isDistinct = isDistinct;
        this.fieldAlls = checkEmpty(fieldAlls);
        this.fieldDefs = checkEmpty(fieldDefs);
        this.table = table;
        this.relations = checkEmpty(relations);
        this.whereCondition = whereCondition;
        this.havingCondition = havingCondition;
        this.groupBy = groupBy;
        this.orderBy = orderBy;
        this.offset = offset;
        this.limit = limit;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean getIsDistinct() {
        return isDistinct;
    }

    public Collection<FieldAll> getFieldAlls() {
        return fieldAlls;
    }

    public Collection<FieldDef> getFieldDefs() {
        return fieldDefs.values();
    }

    public Table getTable() {
        return table;
    }

    public Map<String, Relation> getRelations() {
        return relations;
    }

    public Condition getWhereCondition() {
        return whereCondition;
    }

    public Condition getHavingCondition() {
        return havingCondition;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public List<OrderByItem> getOrderBy() {
        return orderBy;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public boolean equals(Object o) {
        if (!(o instanceof SQLSelect)) {
            return false;
        }

        SQLSelect other = (SQLSelect) o;
        return isDistinct == other.isDistinct
            && equalsHelper(fieldAlls, other.fieldAlls)
            && equalsHelper(fieldDefs, other.fieldDefs)
            && table.equals(other.table)
            && equalsHelper(relations, other.relations)
            && equalsHelper(whereCondition, other.whereCondition)
            && equalsHelper(havingCondition, other.havingCondition)
            && offset == other.offset
            && limit == other.limit
            && equalsHelper(groupBy, other.groupBy)
            && equalsHelper(orderBy, other.orderBy)
        ;
    }

    public StringBuilder appendTo(StringBuilder sb) {
        sb.append("SELECT");
        if (isDistinct) {
            sb.append(" DISTINCT");
        }
        if (fieldAlls != null) {
            StringUtil.appendTo(sb, fieldAlls, " ", null, ",");
        }
        if (fieldAlls != null && fieldDefs != null) {
            sb.append(',');
        }
        if (fieldDefs != null) {
            StringUtil.appendTo(sb, fieldDefs.values(), " ", null, ",");
        }
        sb.append(" FROM ");
        table.appendTo(sb);
        if (relations != null) {
            StringUtil.appendTo(sb, relations.values(), " ", null, null);
        }
        if (whereCondition != null) {
            sb.append(" WHERE ");
            whereCondition.appendTo(sb);
        }
        if (havingCondition != null) {
            sb.append(" HAVING ");
            havingCondition.appendTo(sb);
        }
        if (groupBy != null) {
            sb.append(" GROUP BY ");
            StringUtil.append(sb, groupBy, null, null, ", ");
        }
        if (orderBy != null) {
            sb.append(" ORDER BY ");
            StringUtil.append(sb, orderBy, null, null, ", ");
        }
        if (offset != -1) {
            sb.append(" OFFSET ").append(offset);
        }
        if (limit != -1) {
            sb.append(" LIMIT ").append(limit);
        }
        sb.append(';');
        return sb;
    }
}
