package asia.chengfu.pve.core.resp;

import cn.hutool.core.annotation.Alias;

public class PVENodesQemuStatusCurrentResp {
    //"name":"win10-test04","freemem":5658095616,"ballooninfo":{"major_page_faults":312033,"minor_page_faults":38617787,"max_mem":8489271296,"total_mem":8488681472,"last_update":1728723001,"mem_swapped_out":83365888,"free_mem":5658095616,"mem_swapped_in":10970226688,"actual":8489271296},"balloon":8489271296,"ha":{"managed":0},"uptime":26174,"netin":1333756942,"pid":4909,"maxdisk":85899345920,"qmpstatus":"running","cpus":4,"running-machine":"pc-q35-9.0+pve0","proxmox-support":{"pbs-dirty-bitmap-migration":true,"pbs-dirty-bitmap-savevm":true,"query-bitmap-info":true,"pbs-masterkey":true,"backup-fleecing":true,"pbs-dirty-bitmap":true,"backup-max-workers":true,"pbs-library-version":"1.4.1 (UNKNOWN)"},"cpu":0.278455018357746,"diskread":11440111616,"agent":1,"blockstat":{"ide2":{"flush_operations":0,"invalid_wr_operations":0,"zone_append_bytes":0,"invalid_zone_append_operations":0,"flush_total_time_ns":0,"wr_merged":0,"rd_operations":34,"wr_bytes":0,"unmap_merged":0,"failed_unmap_operations":0,"rd_bytes":172032,"wr_highest_offset":0,"invalid_flush_operations":0,"wr_operations":0,"failed_rd_operations":0,"rd_merged":0,"failed_flush_operations":0,"rd_total_time_ns":2002264,"zone_append_operations":0,"unmap_bytes":0,"wr_total_time_ns":0,"zone_append_merged":0,"idle_time_ns":26006632258028,"account_failed":true,"failed_wr_operations":0,"invalid_rd_operations":0,"unmap_operations":0,"account_invalid":true,"invalid_unmap_operations":0,"timed_stats":[],"unmap_total_time_ns":0,"failed_zone_append_operations":0,"zone_append_total_time_ns":0},"scsi1":{"zone_append_total_time_ns":0,"failed_zone_append_operations":0,"unmap_total_time_ns":0,"timed_stats":[],"invalid_unmap_operations":0,"invalid_rd_operations":0,"unmap_operations":0,"account_invalid":true,"failed_wr_operations":0,"idle_time_ns":24778467533719,"account_failed":true,"zone_append_merged":0,"unmap_bytes":0,"wr_total_time_ns":0,"zone_append_operations":0,"rd_total_time_ns":1324461,"failed_flush_operations":0,"failed_rd_operations":0,"invalid_flush_operations":0,"wr_operations":0,"rd_merged":0,"rd_bytes":27648,"wr_highest_offset":0,"unmap_merged":0,"failed_unmap_operations":0,"wr_bytes":0,"wr_merged":0,"rd_operations":9,"invalid_zone_append_operations":0,"flush_total_time_ns":39199,"zone_append_bytes":0,"invalid_wr_operations":0,"flush_operations":4},"scsi0":{"unmap_bytes":105793310720,"wr_total_time_ns":205852150529,"zone_append_merged":0,"idle_time_ns":257345900,"failed_wr_operations":0,"account_failed":true,"account_invalid":true,"unmap_operations":75792,"invalid_rd_operations":0,"invalid_unmap_operations":0,"timed_stats":[],"unmap_total_time_ns":191342260,"failed_zone_append_operations":0,"zone_append_total_time_ns":0,"failed_flush_operations":0,"rd_total_time_ns":480215028753,"zone_append_operations":0,"failed_unmap_operations":0,"unmap_merged":0,"rd_bytes":11439911936,"wr_highest_offset":85342945280,"rd_merged":0,"wr_operations":513943,"invalid_flush_operations":0,"failed_rd_operations":0,"flush_operations":8356,"invalid_wr_operations":0,"zone_append_bytes":0,"flush_total_time_ns":405283418978,"invalid_zone_append_operations":0,"rd_operations":322030,"wr_merged":0,"wr_bytes":17347429888}},"running-qemu":"9.0.2","status":"running","nics":{"tap108i0":{"netin":1333756942,"netout":49802139}},"vmid":108,"clipboard":null,"disk":0,"mem":2830585856,"diskwrite":17347429888,"maxmem":8489271296,"netout":49802139
    private int vmid;

    private String name;

    private String lock;   //clone

    private String status;

    private long disk;

    private long mem;

    private long diskwrite;

    private long maxmem;

    private long netout;

    private long freemem;

    private Ballooninfo ballooninfo;

    private long balloon;

    private HA ha;

    private long uptime;

    private long netin;

    private long pid;

    private long maxdisk;

    private String qmpstatus;

    @Alias("running-qemu")
    private String runningQemu;

    private int cpus;

    @Alias("running-machine")
    private String runningMachine;

    @Alias("proxmox-support")
    private ProxmoxSupport proxmoxSupport;

    private double cpu;

    private long diskread;

    private int agent;

//    private Blockstat blockstat;
//
//    public static class Blockstat{
//        private Ide2 ide2;
//    }
//
//    public static class Ide2{
//
//    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVmid() {
        return vmid;
    }

    public void setVmid(int vmid) {
        this.vmid = vmid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDisk() {
        return disk;
    }

    public void setDisk(long disk) {
        this.disk = disk;
    }

    public long getMem() {
        return mem;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }

    public long getDiskwrite() {
        return diskwrite;
    }

    public void setDiskwrite(long diskwrite) {
        this.diskwrite = diskwrite;
    }

    public long getMaxmem() {
        return maxmem;
    }

    public void setMaxmem(long maxmem) {
        this.maxmem = maxmem;
    }

    public long getNetout() {
        return netout;
    }

    public void setNetout(long netout) {
        this.netout = netout;
    }

    public long getFreemem() {
        return freemem;
    }

    public void setFreemem(long freemem) {
        this.freemem = freemem;
    }

    public Ballooninfo getBallooninfo() {
        return ballooninfo;
    }

    public void setBallooninfo(Ballooninfo ballooninfo) {
        this.ballooninfo = ballooninfo;
    }

    public long getBalloon() {
        return balloon;
    }

    public void setBalloon(long balloon) {
        this.balloon = balloon;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getRunningQemu() {
        return runningQemu;
    }

    public void setRunningQemu(String runningQemu) {
        this.runningQemu = runningQemu;
    }

    public HA getHa() {
        return ha;
    }

    public void setHa(HA ha) {
        this.ha = ha;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public long getNetin() {
        return netin;
    }

    public void setNetin(long netin) {
        this.netin = netin;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getMaxdisk() {
        return maxdisk;
    }

    public void setMaxdisk(long maxdisk) {
        this.maxdisk = maxdisk;
    }

    public String getQmpstatus() {
        return qmpstatus;
    }

    public void setQmpstatus(String qmpstatus) {
        this.qmpstatus = qmpstatus;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public String getRunningMachine() {
        return runningMachine;
    }

    public void setRunningMachine(String runningMachine) {
        this.runningMachine = runningMachine;
    }

    public ProxmoxSupport getProxmoxSupport() {
        return proxmoxSupport;
    }

    public void setProxmoxSupport(ProxmoxSupport proxmoxSupport) {
        this.proxmoxSupport = proxmoxSupport;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public long getDiskread() {
        return diskread;
    }

    public void setDiskread(long diskread) {
        this.diskread = diskread;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "PVENodesQemuStatusCurrentResp{" +
                "vmid=" + vmid +
                ", name='" + name + '\'' +
                ", disk=" + disk +
                ", mem=" + mem +
                ", diskwrite=" + diskwrite +
                ", maxmem=" + maxmem +
                ", netout=" + netout +
                ", freemem=" + freemem +
                ", ballooninfo=" + ballooninfo +
                ", balloon=" + balloon +
                ", ha=" + ha +
                ", uptime=" + uptime +
                ", netin=" + netin +
                ", pid=" + pid +
                ", maxdisk=" + maxdisk +
                ", qmpstatus='" + qmpstatus + '\'' +
                ", cpus=" + cpus +
                ", runningMachine='" + runningMachine + '\'' +
                ", proxmoxSupport=" + proxmoxSupport +
                ", cpu=" + cpu +
                ", diskread=" + diskread +
                ", agent=" + agent +
                '}';
    }

    public static class ProxmoxSupport{
        @Alias("pbs-dirty-bitmap-migration")
        private boolean pbsDirtyBitmapMigration;

        @Alias("pbs-dirty-bitmap-savevm")
        private boolean pbsDirtyBitmapSavevm;

        @Alias("query-bitmap-info")
        private boolean queryBitmapInfo;

        @Alias("pbs-masterkey")
        private boolean pbsMasterkey;

        @Alias("backup-fleecing")
        private boolean backupFleecing;

        @Alias("pbs-dirty-bitmap")
        private boolean pbsDirtyBitmap;

        @Alias("backup-max-workers")
        private boolean backupMaxWorkers;

        @Alias("pbs-library-version")
        private String pbsLibraryVersion;


        public boolean isPbsDirtyBitmapMigration() {
            return pbsDirtyBitmapMigration;
        }

        public void setPbsDirtyBitmapMigration(boolean pbsDirtyBitmapMigration) {
            this.pbsDirtyBitmapMigration = pbsDirtyBitmapMigration;
        }

        public boolean isPbsDirtyBitmapSavevm() {
            return pbsDirtyBitmapSavevm;
        }

        public void setPbsDirtyBitmapSavevm(boolean pbsDirtyBitmapSavevm) {
            this.pbsDirtyBitmapSavevm = pbsDirtyBitmapSavevm;
        }

        public boolean isQueryBitmapInfo() {
            return queryBitmapInfo;
        }

        public void setQueryBitmapInfo(boolean queryBitmapInfo) {
            this.queryBitmapInfo = queryBitmapInfo;
        }

        public boolean isPbsMasterkey() {
            return pbsMasterkey;
        }

        public void setPbsMasterkey(boolean pbsMasterkey) {
            this.pbsMasterkey = pbsMasterkey;
        }

        public boolean isBackupFleecing() {
            return backupFleecing;
        }

        public void setBackupFleecing(boolean backupFleecing) {
            this.backupFleecing = backupFleecing;
        }

        public boolean isPbsDirtyBitmap() {
            return pbsDirtyBitmap;
        }

        public void setPbsDirtyBitmap(boolean pbsDirtyBitmap) {
            this.pbsDirtyBitmap = pbsDirtyBitmap;
        }

        public boolean isBackupMaxWorkers() {
            return backupMaxWorkers;
        }

        public void setBackupMaxWorkers(boolean backupMaxWorkers) {
            this.backupMaxWorkers = backupMaxWorkers;
        }

        public String getPbsLibraryVersion() {
            return pbsLibraryVersion;
        }

        public void setPbsLibraryVersion(String pbsLibraryVersion) {
            this.pbsLibraryVersion = pbsLibraryVersion;
        }

        @Override
        public String toString() {
            return "ProxmoxSupport{" +
                    "pbsDirtyBitmapMigration=" + pbsDirtyBitmapMigration +
                    ", pbsDirtyBitmapSavevm=" + pbsDirtyBitmapSavevm +
                    ", queryBitmapInfo=" + queryBitmapInfo +
                    ", pbsMasterkey=" + pbsMasterkey +
                    ", backupFleecing=" + backupFleecing +
                    ", pbsDirtyBitmap=" + pbsDirtyBitmap +
                    ", backupMaxWorkers=" + backupMaxWorkers +
                    ", pbsLibraryVersion='" + pbsLibraryVersion + '\'' +
                    '}';
        }
    }

    public static class HA{
        private int managed;

        public int getManaged() {
            return managed;
        }

        public void setManaged(int managed) {
            this.managed = managed;
        }

        @Override
        public String toString() {
            return "HA{" +
                    "managed=" + managed +
                    '}';
        }
    }

    public static class Ballooninfo{
        @Alias("major_page_faults")
        private long majorPageFaults;

        @Alias("minor_page_faults")
        private long minorPageFaults;

        @Alias("max_mem")
        private long maxMem;

        @Alias("total_mem")
        private long totalMem;

        @Alias("last_update")
        private long lastUpdate;

        @Alias("mem_swapped_out")
        private long memSwappedOut;

        @Alias("free_mem")
        private long freeMem;

        @Alias("mem_swapped_in")
        private long memSwappedIn;

        private long actual;

        public long getMajorPageFaults() {
            return majorPageFaults;
        }

        public void setMajorPageFaults(long majorPageFaults) {
            this.majorPageFaults = majorPageFaults;
        }

        public long getMinorPageFaults() {
            return minorPageFaults;
        }

        public void setMinorPageFaults(long minorPageFaults) {
            this.minorPageFaults = minorPageFaults;
        }

        public long getMaxMem() {
            return maxMem;
        }

        public void setMaxMem(long maxMem) {
            this.maxMem = maxMem;
        }

        public long getTotalMem() {
            return totalMem;
        }

        public void setTotalMem(long totalMem) {
            this.totalMem = totalMem;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public long getMemSwappedOut() {
            return memSwappedOut;
        }

        public void setMemSwappedOut(long memSwappedOut) {
            this.memSwappedOut = memSwappedOut;
        }

        public long getFreeMem() {
            return freeMem;
        }

        public void setFreeMem(long freeMem) {
            this.freeMem = freeMem;
        }

        public long getMemSwappedIn() {
            return memSwappedIn;
        }

        public void setMemSwappedIn(long memSwappedIn) {
            this.memSwappedIn = memSwappedIn;
        }

        public long getActual() {
            return actual;
        }

        public void setActual(long actual) {
            this.actual = actual;
        }

        @Override
        public String toString() {
            return "Ballooninfo{" +
                    "majorPageFaults=" + majorPageFaults +
                    ", minorPageFaults=" + minorPageFaults +
                    ", maxMem=" + maxMem +
                    ", totalMem=" + totalMem +
                    ", lastUpdate=" + lastUpdate +
                    ", memSwappedOut=" + memSwappedOut +
                    ", freeMem=" + freeMem +
                    ", memSwappedIn=" + memSwappedIn +
                    ", actual=" + actual +
                    '}';
        }
    }
}
