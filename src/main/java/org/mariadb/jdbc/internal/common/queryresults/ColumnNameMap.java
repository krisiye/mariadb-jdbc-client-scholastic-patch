package org.mariadb.jdbc.internal.common.queryresults;

import org.mariadb.jdbc.internal.mysql.MySQLColumnInformation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author krisiyer - patched column index per jdbc 4.1 - 15.2.3 Retrieving Values (CONJ-84) 
 * @see  http://download.oracle.com/otn-pub/jcp/jdbc-4_1-mrel-spec/jdbc4.1-fr-spec.pdf
 *
 */

public class ColumnNameMap {
    Map<String, Integer> map;
    Map<String, Integer> labelMap;
    MySQLColumnInformation[] columnInfo;

    public ColumnNameMap(MySQLColumnInformation[] columnInformations) {
       this.columnInfo = columnInformations;
    }

    public int getIndex(String name) throws SQLException {
        if (columnInfo == null) {
           throw new SQLException("No such column :" + name);
        }
        // The specs in JDBC 4.0 specify that ResultSet.findColumn and
        // ResultSet.getXXX(String name) should use column alias (AS in the query). If label is not found, we use 
        // original table name.
    	Integer res = getLabelIndex(name);

    	
    	if (res != null) {
    		return res;
    	}
        if (map == null) {
            map = new HashMap<String, Integer>();
            int i=0;
            for(MySQLColumnInformation ci : columnInfo) {
                String columnName = ci.getOriginalName().toLowerCase();
                if (columnName.equals("")) {
                    // for name-less columns (there CAN be some), use their alias
                    columnName = ci.getName().toLowerCase();
                }
                map.put(columnName, i);
                String tableName = ci.getTable().toLowerCase();
                if (!tableName.equals("")) {
                    map.put(tableName + "." + columnName, i);
                }
                i++;
            }
        }
        res = map.get(name.toLowerCase());
       
        if (res == null) {
            throw new SQLException("No such column :" + name);
        }
        return res;
    }

    private int getLabelIndex(String name) throws SQLException {
        if (labelMap == null) {
            labelMap = new HashMap<String, Integer>();
            int i=0;
            for(MySQLColumnInformation ci : columnInfo) {
                String columnAlias = ci.getName().toLowerCase();
                
                // jdbc 4.1 - 15.2.3 Retrieving Values 
                // The columns are numbered from left to right,
                // as they appear in the select list of the query, starting at 1.
                // Column labels supplied to getter methods are case insensitive. 
                // If a select list contains the same column more than once, 
                // the first instance of the column will be returned.
                
                // preserve the earliest column/alias index.
                if(!labelMap.containsKey(columnAlias))
                	labelMap.put(columnAlias, i);
                
                if (ci.getTable() != null) {
                    String tableName = ci.getTable().toLowerCase();
                    if (!tableName.equals("")) {
                    	// preserve the earliest column/alias index.
                    	if(!labelMap.containsKey(tableName + "." + columnAlias))
                    		labelMap.put(tableName + "." + columnAlias, i);
                    }
                }
                i++;
            }
        }
        Integer res = labelMap.get(name.toLowerCase());
        if (res == null) {
            throw new SQLException("No such column :" + name);
        }
        return res;
    }
}
