package asia.chengfu.pve.service.req;

public class AddAccessAclReq {
    private String path;
    private String users;
    private String roles;

    public static AddAccessAclReq of(String users,String vmid) {
        return of(users, "PVEVMUser", "/vms/" + vmid);
    }

    public static AddAccessAclReq of(String users, String roles, String path) {
        AddAccessAclReq addAccessAclReq = new AddAccessAclReq();
        addAccessAclReq.setPath(path);
        addAccessAclReq.setUsers(users);
        addAccessAclReq.setRoles(roles);
        return addAccessAclReq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AddAccessAclReq{" +
                "path='" + path + '\'' +
                ", users='" + users + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
