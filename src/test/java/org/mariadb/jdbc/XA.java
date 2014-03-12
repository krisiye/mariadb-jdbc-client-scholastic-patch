package org.mariadb.jdbc;


import org.junit.Before;
import org.junit.Test;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static junit.framework.Assert.*;

public class XA extends BaseTest {

    MySQLDataSource dataSource;

    @Before
    public void checkSupported() throws SQLException {
        requireMinimumVersion(5,0);
    }
    public XA()  {
        dataSource = new MySQLDataSource();
        dataSource.setUser("root");
        dataSource.setDatabaseName("test");
    }
    Xid newXid() {
        return new MySQLXid(1, UUID.randomUUID().toString().getBytes(),UUID.randomUUID().toString().getBytes());
    }
    Xid newXid(Xid branchFrom) {
        return new MySQLXid(1,branchFrom.getGlobalTransactionId(),UUID.randomUUID().toString().getBytes());
    }

    /**
     * 2 phase commit , with either commit or rollback at the end
     * @param doCommit
     * @throws Exception
     */
    void test2PC(boolean doCommit) throws Exception{


        MySQLDataSource dataSource = new MySQLDataSource();
        dataSource.setUser("root");
        dataSource.setDatabaseName("test");

        connection.createStatement().execute("DROP TABLE IF EXISTS xatable");
        connection.createStatement().execute("CREATE TABLE xatable (i int) ENGINE=InnoDB");

        int N=1;

        Xid parentXid = newXid();
        Connection [] connections = new Connection[N];
        XAConnection [] xaConnections = new XAConnection[N];
        XAResource[] xaResources = new XAResource[N];
        Xid xids[] = new Xid[N];

        try {

            for (int i=0; i < N ; i++) {
                xaConnections[i] = dataSource.getXAConnection();
                connections[i] = xaConnections[i].getConnection();
                xaResources[i] = xaConnections[i].getXAResource();
                xids[i] = newXid(parentXid);
            }

            for (int i=0; i < N; i++) {
                xaResources[i].start(xids[i],XAResource.TMNOFLAGS);
            }

            for (int i=0; i < N ; i++) {
                connections[i].createStatement().executeUpdate("INSERT INTO xatable VALUES (" + i + ")");
            }

            for (int i=0; i < N; i++) {
                xaResources[i].end(xids[i], XAResource.TMSUCCESS);
            }

            for (int i=0; i < N; i++) {
                xaResources[i].prepare(xids[i]);
            }

            for (int i=0; i < N; i++) {
                if (doCommit)
                    xaResources[i].commit(xids[i], false);
                else
                    xaResources[i].rollback(xids[i]);
            }


            // check the completion
            ResultSet rs = connection.createStatement().executeQuery("SELECT * from xatable order by i");
            if(doCommit) {
                for(int  i=0; i < N; i++) {
                    rs.next();
                    assertEquals(rs.getInt(1), i);
                }
            }
            else {

                assertFalse(rs.next());
            }
            rs.close();
        } finally {
            for(int i=0; i < N; i++) {
                try {
                    if (xaConnections[i] != null) {
                        xaConnections[i].close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    @Test
    public void testCommit() throws Exception {
        test2PC(true);
    }

    @Test
    public void testRollback() throws Exception {
        test2PC(false);
    }

    @Test
    public void testRecover() throws Exception {
        XAConnection xaConnection = dataSource.getXAConnection();
        try {
            Connection c = xaConnection.getConnection();
            Xid xid = newXid();
            XAResource xaResource = xaConnection.getXAResource();
            xaResource.start(xid, XAResource.TMNOFLAGS);
            c.createStatement().executeQuery("SELECT 1");
            xaResource.end(xid, XAResource.TMSUCCESS);
            xaResource.prepare(xid);
            Xid[] recoveredXids = xaResource.recover(XAResource.TMSTARTRSCAN | XAResource.TMENDRSCAN);
            assertTrue(recoveredXids != null);
            assertTrue(recoveredXids.length > 0);
            boolean found = false;

            for (Xid x:recoveredXids) {
                if (x != null && x.equals(xid)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } finally {
            xaConnection.close();
        }
    }


}
