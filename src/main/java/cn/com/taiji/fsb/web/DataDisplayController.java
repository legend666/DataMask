package cn.com.taiji.fsb.web;

import cn.com.taiji.fsb.domain.Connect;
import cn.com.taiji.fsb.service.impl.ConnectSerivceImpl;
import net.minidev.json.JSONArray;
//import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import schemacrawler.schema.*;
import schemacrawler.schemacrawler.*;
import schemacrawler.utility.SchemaCrawlerUtility;
import com.alibaba.fastjson.JSONObject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import static us.fatehi.commandlineparser.CommandLineUtility.applyApplicationLogLevel;
import static us.fatehi.commandlineparser.CommandLineUtility.logSystemClasspath;
import static us.fatehi.commandlineparser.CommandLineUtility.logSystemProperties;

@Controller
@RequestMapping("/display")
public class DataDisplayController {
    @Autowired
    ConnectSerivceImpl connectSerivceImpl;

    @RequestMapping(value = "/getAllConnectName", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList<String> getAllConnect() {
        List<Connect> lsConnects = connectSerivceImpl.getAllConnect();
        ArrayList<String> connectname = new ArrayList<>();
        for (Connect u : lsConnects) {
            connectname.add(u.getConnectionname());
        }
        return connectname;
    }

    private String getConinfo(String ConName) {
        List<Connect> lsConnects = connectSerivceImpl.getAllConnect();
        Map<String, String> conmap = new HashMap<>();
//        {连接名：id},找到对应的id，然后取到整条值
        for (Connect u : lsConnects) {
            conmap.put(u.getConnectionname(), u.getConnectionid());
        }
        return conmap.get(ConName);
    }

    @RequestMapping(value = "/DbMetadata", method = RequestMethod.POST)
    @ResponseBody
//    传过来的是连接名，不是id，所以得再查一遍
    public String getDbMetadata(@RequestBody String ConName) throws Exception {
        String connectionId = getConinfo(ConName);
        Connect coninfo = connectSerivceImpl.getConnectById(connectionId);
//        开始获取元数据
        // Turn application logging on by applying the correct log level
        applyApplicationLogLevel(Level.OFF);
        // Log system properties and classpath
        logSystemClasspath();
        logSystemProperties();

        // Create the options
        final SchemaCrawlerOptionsBuilder optionsBuilder = SchemaCrawlerOptionsBuilder
                .builder()
                // Set what details are required in the schema - this affects the
                // time taken to crawl the schema
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard())
                .includeSchemas(new RegularExpressionInclusionRule(coninfo.getDbname() + ".dbo"))
                .includeTables(tableFullName -> !tableFullName.contains("ΒΙΒΛΊΑ"));
        final SchemaCrawlerOptions options = optionsBuilder.toOptions();

        // Get the schema definition
        final Catalog catalog = SchemaCrawlerUtility.getCatalog(getConnection(coninfo), options);
        JSONArray tablejoRows = new JSONArray();
        for (final Schema schema : catalog.getSchemas()) {
//            Collection<Table> colTables = catalog.getTables(schema);
//            System.out.println(colTables);
            for (final Table table : catalog.getTables(schema)) {
                if (table instanceof View) {
                    continue;
                } else {
                    JSONObject joTable = new JSONObject();
                    int rowCount = getTableRowCount(table.getName(), coninfo);
                    joTable.put("tableName", table.getName());
                    joTable.put("rowCount", rowCount);
                    joTable.put("masking", 0);
                    tablejoRows.add(joTable);
//                    System.out.println(getTableData(table.getName(), coninfo));
                }
            }
        }
        return tablejoRows.toString();

    }

    //    根据连接信息，拼出连接地址
    private static Connection getConnection(Connect coninfo)
            throws SQLException {
        String serverip = coninfo.getServerip();
        String dbname = coninfo.getDbname();
        String dbtypeid = coninfo.getDbtypeid();
        String dbtype = "";
//        "1:SQL Server;2:Oracle;3:MySQL"
        switch (dbtypeid) {
            case "1":
                dbtype = "sqlserver";
                break;
            case "2":
                dbtype = "oracle";
                break;
            case "3":
                dbtype = "oracle";
                break;
        }
//        "jdbc:sqlserver://localhost:1433;DatabaseName=address_model";
        final String connectionUrl = "jdbc:" + dbtype + "://" + serverip + ";DatabaseName=" + dbname;
        final DataSource dataSource = new DatabaseConnectionOptions(connectionUrl);
        return dataSource.getConnection(coninfo.getDbuser(), coninfo.getUserpassword());
    }

    private static int getTableRowCount(String tableName, Connect coninfo) throws Exception {
        Connection con = getConnection(coninfo);
        String sql = String.format("select count(*) from [%s]", tableName);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int rowCount = rs.getInt(1);
        con.close();
//        System.out.println(rowCount);
        return rowCount;
    }

    @RequestMapping(value = "/getTableData", method = RequestMethod.POST)
    @ResponseBody
//    ConnectionName + rowData
    public Map<String, String> getTableData(String ConName, String tableName) throws Exception {
        String connectionId = getConinfo(ConName);
        Connect coninfo = connectSerivceImpl.getConnectById(connectionId);
        Connection conn = getConnection(coninfo);
        String sql = String.format("select top(100) * from [%s]", tableName);
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery(sql);
//        ResultSetMetaData meta = rs.getMetaData();
        JSONArray tableMeta = new JSONArray();
        List<String> columnList = new ArrayList<>();
        JSONArray tableData = new JSONArray();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columeCount = meta.getColumnCount();
            for (int i = 1; i < columeCount + 1; i++) {
                JSONObject Metadata = new JSONObject();
                String columnName = meta.getColumnName(i);
                columnList.add(columnName);
                Metadata.put("id", i);
                Metadata.put("column", columnName);
                Metadata.put("type", meta.getColumnTypeName(i));
                Metadata.put("masking","否");
                tableMeta.add(Metadata);
            }
            while (rs.next()) {
                JSONObject jocolumn = new JSONObject(true);
                for (String column : columnList) {
                    String columnData = rs.getString(column);
                    jocolumn.put(column, columnData);
                }
                tableData.add(jocolumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        Map<String, String> map = new HashMap<>();
        map.put("tableMeta",tableMeta.toString());
        map.put("tableData",tableData.toString());

        return map;
    }
}
