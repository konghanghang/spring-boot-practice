package com.test.snapshot;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStats;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotStatus;
import org.elasticsearch.action.admin.cluster.snapshots.status.SnapshotsStatusResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.snapshots.Snapshot;
import org.elasticsearch.snapshots.SnapshotId;
import org.elasticsearch.snapshots.SnapshotInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 参考地址：https://dzone.com/articles/how-use-elasticsearch-snapshot
 * https://blog.csdn.net/yangshangwei/article/details/104072859?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SnapshotTest {

    @Resource
    private TransportClient client;

    /**
     * 获取所有仓库
     */
    @Test
    public void getRepository() {
        ClusterAdminClient cluster = client.admin().cluster();
        List<RepositoryMetaData> repositories = cluster.prepareGetRepositories()
                // 可以指定需要查看的仓库的名称
                .addRepositories("security_repository1")
                .get().repositories();
        for (RepositoryMetaData repository : repositories) {
            System.out.println(repository.name());
            //hdfs
            System.out.println(repository.type());
            // {"path":"/es","security.principal":"hdfs/localhost@EXAMPLE.COM","uri":"hdfs://127.0.0.1:9000"}
            System.out.println(repository.settings().toString());
            System.out.println("----------------------");
        }
    }

    /**
     * 创建仓库
     */
    @Test
    public void createRepository() {
        /**
         * doc地址: https://www.elastic.co/guide/en/elasticsearch/plugins/7.7/repository-hdfs-config.html
         * uri: The uri address for hdfs. ex: "hdfs://<host>:<port>/". (Required)
         * path: The file path within the filesystem where data is stored/loaded. ex: "path/to/file". (Required)
         * load_defaults: Whether to load the default Hadoop configuration or not. (Enabled by default)
         * conf.<key>: Inlined configuration parameter to be added to Hadoop configuration. (Optional) Only client oriented properties from the hadoop core and hdfs configuration files will be recognized by the plugin.
         * compress: Whether to compress the metadata or not. (Disabled by default)
         * max_restore_bytes_per_sec: Throttles per node restore rate. Defaults to 40mb per second.
         * max_snapshot_bytes_per_sec: Throttles per node snapshot rate. Defaults to 40mb per second.
         * readonly: Makes repository read-only. Defaults to false.
         * chunk_size: Override the chunk size. (Disabled by default)
         * security.principal: Kerberos principal to use when connecting to a secured HDFS cluster.
         */
        Settings settings = Settings.builder().put("path", "/es")
                .put("uri", "hdfs://127.0.0.1:9000")
                .put("security.principal", "hdfs/localhost@EXAMPLE.COM")
                .put("compress", true)
                .build();
        AcknowledgedResponse hdfs = client.admin().cluster()
                .preparePutRepository("test-api")
                .setType("hdfs")
                .setSettings(settings).get();
        System.out.println(hdfs.isAcknowledged());
    }

    @Test
    public void verifyRepository() {
        VerifyRepositoryResponse verifyRepositoryResponse = client.admin().cluster()
                .prepareVerifyRepository("security_repository").execute().actionGet();
        List<VerifyRepositoryResponse.NodeView> nodes = verifyRepositoryResponse.getNodes();
        for (VerifyRepositoryResponse.NodeView node : nodes) {
            System.out.println(node.getName());
        }
    }

    /**
     * 删除仓库
     */
    @Test
    public void deleteRepository() {
        AcknowledgedResponse acknowledgedResponse = client.admin().cluster()
                .prepareDeleteRepository("test-api").execute().actionGet();
        System.out.println(acknowledgedResponse.isAcknowledged());
        getRepository();
    }

    @Test
    public void getSnapshot() {
        List<SnapshotInfo> snapshots = client.admin().cluster()
                .prepareGetSnapshots("security_repository").get().getSnapshots();
        for (SnapshotInfo snapshot : snapshots) {
            RestStatus status = snapshot.status();
            System.out.println(status.getStatus());
            List<String> indices = snapshot.indices();
            System.out.println(StringUtils.join(indices, ","));
            SnapshotId snapshotId = snapshot.snapshotId();
            System.out.println(snapshotId.getName() + "---" + snapshotId.getUUID());
            // 判断快照当前的状态
            System.out.println(snapshot.state().value());
            System.out.println("---------------");
        }
        GetSnapshotsResponse actionGet = client.admin().cluster()
                .prepareGetSnapshots("security_repository")
                // 可以指定需要查看的快照的名称
                .addSnapshots("snapshot_1")
                .execute().actionGet();
        snapshots = actionGet.getSnapshots();
        for (SnapshotInfo snapshotInfo : snapshots) {
            RestStatus status = snapshotInfo.status();
            System.out.println(status.getStatus());
            List<String> indices = snapshotInfo.indices();
            System.out.println(StringUtils.join(indices, ","));
            SnapshotId snapshotId = snapshotInfo.snapshotId();
            System.out.println(snapshotId.getName() + "---" + snapshotId.getUUID());
            System.out.println(snapshotInfo.state().completed());
            System.out.println("---------------");
        }
        SnapshotsStatusResponse snapshotsStatusResponse = client.admin().cluster()
                .prepareSnapshotStatus().setRepository("security_repository").setSnapshots("snapshot")
                .execute().actionGet();
        List<SnapshotStatus> status = snapshotsStatusResponse.getSnapshots();
        for (SnapshotStatus snapshotStatus : status) {
            Snapshot snapshot = snapshotStatus.getSnapshot();
            System.out.println(snapshot.getSnapshotId());
            SnapshotStats stats = snapshotStatus.getStats();
            stats.getStartTime();
        }
    }

    /**
     * ignore_unavailable如果设置为true的话，那么那些不存在的index就会被忽略掉
     * include_global_state为false，可以阻止cluster的全局state也作为snapshot的一部分被备份
     * 创建快照
     * 200
     * people
     * test-api---uoCI87x7Tdah4-fFZ1yO3w
     * true
     * 200
     */
    @Test
    public void createSnapshot() {
        CreateSnapshotResponse createSnapshotResponse = client.admin().cluster()
                .prepareCreateSnapshot("security_repository", "test-api")
                .setWaitForCompletion(true)
                // 为false，可以阻止cluster的全局state也作为snapshot的一部分被备份
                // .setIncludeGlobalState(false)
                // 如果某个索引的部分primary shard不可用，那么会导致备份过程失败，那么此时可以将partial设置为true。
                // .setPartial(true)
                // .setIndicesOptions(IndicesOptions.STRICT_EXPAND_OPEN_FORBID_CLOSED_IGNORE_THROTTLED)
                .setIndices("people_2").get();
        SnapshotInfo snapshot = createSnapshotResponse.getSnapshotInfo();
        RestStatus status = snapshot.status();
        System.out.println(status.getStatus());
        List<String> indices = snapshot.indices();
        System.out.println(StringUtils.join(indices, ","));
        SnapshotId snapshotId = snapshot.snapshotId();
        System.out.println(snapshotId.getName() + "---" + snapshotId.getUUID());
        System.out.println(snapshot.state().toString());
        System.out.println(createSnapshotResponse.status().getStatus());
    }

    /**
     * 删除快照
     */
    @Test
    public void deleteSnapshot() {
        AcknowledgedResponse acknowledgedResponse = client.admin().cluster()
                .prepareDeleteSnapshot("security_repository", "test-api").execute().actionGet();
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    /**
     * 恢复索引，要被恢复的索引在es中应该已经被删除或者已经关闭，不然报错。
     */
    @Test
    public void restoreSnapshot() {
        RestoreSnapshotResponse restoreSnapshotResponse = client.admin().cluster()
                .prepareRestoreSnapshot("security_repository", "snapshot_1")
                // 可以指定需要恢复的索引
                .setIndices("people")
                .execute()
                .actionGet();
        // RestoreInfo restoreInfo = restoreSnapshotResponse.getRestoreInfo();
        // System.out.println(restoreInfo.status().getStatus());
    }

}
