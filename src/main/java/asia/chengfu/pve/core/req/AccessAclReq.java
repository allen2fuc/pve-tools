package asia.chengfu.pve.core.req;

public class AccessAclReq {
    private String path;
    private String users;
    private String roles;

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
        return "AccessAclReq{" +
                "path='" + path + '\'' +
                ", users='" + users + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
